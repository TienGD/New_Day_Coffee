-- ============================================================================
-- New Day Coffee - Database Setup Script (Idempotent / Chay 1 lan duoc ngay)
-- Database: QuanLyDoUong | SQL Login: newday_app (khop resources/database.properties)
-- Luu y: Ket noi bang Windows Auth / sa (sysadmin) trong SSMS de chay script nay.
--        De app Java ket noi bang SQL Login, SQL Server can bat Mixed Mode & TCP/IP 1433.
-- ============================================================================

USE [master];
GO

IF SERVERPROPERTY('IsIntegratedSecurityOnly') = 1
BEGIN
    PRINT N'CANH BAO: SQL Server dang o che do Windows Authentication only.';
    PRINT N'Hay bat "SQL Server and Windows Authentication mode" trong Server Properties -> Security roi Restart SQL Server de Java JDBC dang nhap duoc bang newday_app.';
END
GO

-- 1. Tao hoac cap nhat SQL Server Login [newday_app] o [master] truoc khi tao DB/User
IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = N'newday_app')
BEGIN
    CREATE LOGIN [newday_app]
        WITH PASSWORD = N'Trankhoa09122006',
        CHECK_POLICY = OFF,
        CHECK_EXPIRATION = OFF;
END
ELSE
BEGIN
    ALTER LOGIN [newday_app]
        WITH PASSWORD = N'Trankhoa09122006',
        CHECK_POLICY = OFF,
        CHECK_EXPIRATION = OFF;
END
GO

-- 2. Xoa sach DB cu neu da ton tai (tranh Msg 1801 / Msg 2714 / Msg 2627) roi tao moi
IF DB_ID(N'QuanLyDoUong') IS NOT NULL
BEGIN
    ALTER DATABASE [QuanLyDoUong] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [QuanLyDoUong];
END
GO

CREATE DATABASE [QuanLyDoUong];
GO

ALTER LOGIN [newday_app] WITH DEFAULT_DATABASE = [QuanLyDoUong];
GO

ALTER DATABASE [QuanLyDoUong] SET READ_WRITE;
ALTER DATABASE [QuanLyDoUong] SET MULTI_USER;
GO

USE [QuanLyDoUong];
GO

-- 3. Tao User & cap quyen cho [newday_app] trong DB QuanLyDoUong
CREATE USER [newday_app] FOR LOGIN [newday_app] WITH DEFAULT_SCHEMA = [dbo];
GO
ALTER ROLE [db_datareader] ADD MEMBER [newday_app];
GO
ALTER ROLE [db_datawriter] ADD MEMBER [newday_app];
GO
GRANT EXECUTE ON SCHEMA::[dbo] TO [newday_app];
GO

-- 4. Tao cac bang (Tables)
SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE [dbo].[DoUong](
    [maDoUong] [nvarchar](40) NOT NULL,
    [tenDoUong] [nvarchar](200) NOT NULL,
    [moTa] [nvarchar](1000) NULL,
    [giaBan] [decimal](12, 2) NOT NULL,
    CONSTRAINT [PK_DoUong] PRIMARY KEY CLUSTERED ([maDoUong] ASC)
);
GO

CREATE TABLE [dbo].[NhanVien](
    [maNV] [nvarchar](40) NOT NULL,
    [tenNV] [nvarchar](200) NOT NULL,
    [gioiTinh] [nvarchar](10) NOT NULL,
    [soDienThoai] [nvarchar](10) NOT NULL,
    [chucVu] [nvarchar](100) NOT NULL,
    [ngaySinh] [date] NOT NULL,
    [email] [nvarchar](254) NOT NULL,
    CONSTRAINT [PK_NhanVien] PRIMARY KEY CLUSTERED ([maNV] ASC)
);
GO

CREATE TABLE [dbo].[TaiKhoan](
    [maDangNhap] [nvarchar](40) NOT NULL,
    [matKhau] [nvarchar](510) NOT NULL,
    [maNV] [nvarchar](40) NOT NULL,
    CONSTRAINT [PK_TaiKhoan] PRIMARY KEY CLUSTERED ([maDangNhap] ASC)
);
GO

CREATE TABLE [dbo].[HoaDon](
    [maHoaDon] [nvarchar](40) NOT NULL,
    [ngayLap] [datetime2](0) NOT NULL,
    [tienKhachTra] [decimal](12, 2) NOT NULL,
    [ghiChu] [nvarchar](500) NULL,
    [maNV] [nvarchar](40) NOT NULL,
    [trangThai] [nvarchar](30) NOT NULL,
    CONSTRAINT [PK_HoaDon] PRIMARY KEY CLUSTERED ([maHoaDon] ASC)
);
GO

CREATE TABLE [dbo].[ChiTietHoaDon](
    [maHoaDon] [nvarchar](40) NOT NULL,
    [maDoUong] [nvarchar](40) NOT NULL,
    [soLuong] [int] NOT NULL,
    CONSTRAINT [PK_ChiTietHoaDon] PRIMARY KEY CLUSTERED ([maHoaDon] ASC, [maDoUong] ASC)
);
GO

-- 5. Tao cac View
SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE VIEW [dbo].[ThanhTien]
AS
SELECT
    ct.maHoaDon,
    ct.maDoUong,
    du.tenDoUong,
    ct.soLuong,
    du.giaBan,
    CAST(ct.soLuong * du.giaBan AS DECIMAL(18,2)) AS thanhTien
FROM dbo.ChiTietHoaDon AS ct
INNER JOIN dbo.DoUong AS du
    ON ct.maDoUong = du.maDoUong;
GO

CREATE VIEW [dbo].[TongTien]
AS
SELECT
    maHoaDon,
    CAST(SUM(thanhTien) AS DECIMAL(18,2)) AS tongTien
FROM dbo.ThanhTien
GROUP BY maHoaDon;
GO

CREATE VIEW [dbo].[TienThua]
AS
SELECT
    hd.maHoaDon,
    hd.ngayLap,
    hd.maNV,
    nv.tenNV,
    hd.tienKhachTra,
    COALESCE(tt.tongTien, 0) AS tongTien,
    CAST(hd.tienKhachTra - COALESCE(tt.tongTien, 0) AS DECIMAL(18,2)) AS tienThua
FROM dbo.HoaDon AS hd
INNER JOIN dbo.NhanVien AS nv
    ON hd.maNV = nv.maNV
LEFT JOIN dbo.TongTien AS tt
    ON hd.maHoaDon = tt.maHoaDon;
GO

-- 6. Tao cac Function (da fix loi Msg 207 o fn_TinhTongTienHoaDon)
CREATE FUNCTION [dbo].[fn_TinhThanhTien]
(
    @soLuong INT,
    @donGiaBan DECIMAL(12,2)
)
RETURNS DECIMAL(18,2)
AS
BEGIN
    IF @soLuong IS NULL OR @donGiaBan IS NULL
        RETURN NULL;

    RETURN CAST(@soLuong * @donGiaBan AS DECIMAL(18,2));
END;
GO

CREATE FUNCTION [dbo].[fn_TinhTongTienHoaDon]
(
    @maHoaDon NVARCHAR(40)
)
RETURNS DECIMAL(18,2)
AS
BEGIN
    DECLARE @tongTien DECIMAL(18,2);

    SELECT
        @tongTien = SUM(CAST(ct.soLuong AS DECIMAL(18,2)) * du.giaBan)
    FROM dbo.ChiTietHoaDon AS ct
    INNER JOIN dbo.DoUong AS du
        ON ct.maDoUong = du.maDoUong
    WHERE ct.maHoaDon = @maHoaDon;

    RETURN COALESCE(@tongTien, 0);
END;
GO

CREATE FUNCTION [dbo].[fn_TinhTienThua]
(
    @maHoaDon NVARCHAR(40)
)
RETURNS DECIMAL(18,2)
AS
BEGIN
    DECLARE @tienKhachTra DECIMAL(12,2);
    DECLARE @tongTien DECIMAL(18,2);

    SELECT @tienKhachTra = tienKhachTra
    FROM dbo.HoaDon
    WHERE maHoaDon = @maHoaDon;

    IF @tienKhachTra IS NULL
        RETURN NULL;

    SET @tongTien = dbo.fn_TinhTongTienHoaDon(@maHoaDon);

    RETURN CAST(@tienKhachTra - @tongTien AS DECIMAL(18,2));
END;
GO

-- 7. Rang buoc (Unique, Default, Foreign Keys, Check Constraints)
SET ANSI_PADDING ON;
GO

ALTER TABLE [dbo].[TaiKhoan] ADD CONSTRAINT [UQ_TaiKhoan_MaNV] UNIQUE NONCLUSTERED ([maNV] ASC);
GO
ALTER TABLE [dbo].[HoaDon] ADD DEFAULT (sysdatetime()) FOR [ngayLap];
GO
ALTER TABLE [dbo].[HoaDon] ADD CONSTRAINT [DF_HoaDon_trangThai] DEFAULT (N'Đã thanh toán') FOR [trangThai];
GO

ALTER TABLE [dbo].[ChiTietHoaDon] WITH CHECK ADD CONSTRAINT [FK_CTHD_DoUong] FOREIGN KEY([maDoUong])
    REFERENCES [dbo].[DoUong] ([maDoUong]);
GO
ALTER TABLE [dbo].[ChiTietHoaDon] CHECK CONSTRAINT [FK_CTHD_DoUong];
GO

ALTER TABLE [dbo].[ChiTietHoaDon] WITH CHECK ADD CONSTRAINT [FK_CTHD_HoaDon] FOREIGN KEY([maHoaDon])
    REFERENCES [dbo].[HoaDon] ([maHoaDon]);
GO
ALTER TABLE [dbo].[ChiTietHoaDon] CHECK CONSTRAINT [FK_CTHD_HoaDon];
GO

ALTER TABLE [dbo].[HoaDon] WITH CHECK ADD CONSTRAINT [FK_HoaDon_NhanVien] FOREIGN KEY([maNV])
    REFERENCES [dbo].[NhanVien] ([maNV]);
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK_HoaDon_NhanVien];
GO

ALTER TABLE [dbo].[TaiKhoan] WITH CHECK ADD CONSTRAINT [FK_TaiKhoan_NhanVien] FOREIGN KEY([maNV])
    REFERENCES [dbo].[NhanVien] ([maNV]);
GO
ALTER TABLE [dbo].[TaiKhoan] CHECK CONSTRAINT [FK_TaiKhoan_NhanVien];
GO

ALTER TABLE [dbo].[ChiTietHoaDon] WITH CHECK ADD CONSTRAINT [CK_CTHD_SoLuong] CHECK (([soLuong]>(0)));
GO
ALTER TABLE [dbo].[DoUong] WITH CHECK ADD CONSTRAINT [CK_DoUong_GiaBan] CHECK (([giaBan]>(0)));
GO
ALTER TABLE [dbo].[DoUong] WITH CHECK ADD CONSTRAINT [CK_DoUong_MaDoUong] CHECK ((len([maDoUong])>=(4) AND (([maDoUong]) collate Latin1_General_100_BIN2 like N'cof[0-9]%' OR ([maDoUong]) collate Latin1_General_100_BIN2 like N'tea[0-9]%' OR ([maDoUong]) collate Latin1_General_100_BIN2 like N'mil[0-9]%') AND NOT substring([maDoUong],(4),(40)) like N'%[^0-9]%'));
GO
ALTER TABLE [dbo].[DoUong] WITH CHECK ADD CONSTRAINT [CK_DoUong_Ten] CHECK ((len(ltrim(rtrim([tenDoUong])))>(0)));
GO
ALTER TABLE [dbo].[HoaDon] WITH CHECK ADD CONSTRAINT [CK_HoaDon_TienKhachTra] CHECK (([tienKhachTra]>=(0)));
GO
ALTER TABLE [dbo].[NhanVien] WITH CHECK ADD CONSTRAINT [CK_NhanVien_ChucVu] CHECK ((left([maNV],(2))=N'ql' AND [chucVu]=N'Quản lí' OR left([maNV],(2))=N'nv' AND ([chucVu]=N'Pha chế' OR [chucVu]=N'Thu ngân')));
GO
ALTER TABLE [dbo].[NhanVien] WITH CHECK ADD CONSTRAINT [CK_NhanVien_Email] CHECK (([email] like N'%_@_%._%' AND NOT [email] like N'% %'));
GO
ALTER TABLE [dbo].[NhanVien] WITH CHECK ADD CONSTRAINT [CK_NhanVien_GioiTinh] CHECK (([gioiTinh]=N'Khác' OR [gioiTinh]=N'Nữ' OR [gioiTinh]=N'Nam'));
GO
ALTER TABLE [dbo].[NhanVien] WITH CHECK ADD CONSTRAINT [CK_NhanVien_MaNV] CHECK ((len([maNV])=(4) AND datalength([maNV])=(8) AND (([maNV]) collate Latin1_General_100_BIN2 like N'nv[0-9][0-9]' OR ([maNV]) collate Latin1_General_100_BIN2 like N'ql[0-9][0-9]')));
GO
ALTER TABLE [dbo].[NhanVien] WITH CHECK ADD CONSTRAINT [CK_NhanVien_SDT] CHECK (([soDienThoai] like N'0%' AND len([soDienThoai])=(10) AND NOT [soDienThoai] like N'%[^0-9]%'));
GO
ALTER TABLE [dbo].[TaiKhoan] WITH CHECK ADD CONSTRAINT [CK_TaiKhoan_MaDangNhap] CHECK ((len([maDangNhap])>(0) AND NOT ([maDangNhap]) collate Latin1_General_100_BIN2 like N'%[^a-z0-9]%'));
GO
ALTER TABLE [dbo].[TaiKhoan] WITH CHECK ADD CONSTRAINT [CK_TaiKhoan_MatKhau] CHECK ((len([matKhau])>(0)));
GO

-- 8. Nap du lieu mau theo dung thu tu cha -> con
INSERT [dbo].[DoUong] ([maDoUong], [tenDoUong], [moTa], [giaBan]) VALUES (N'cof01', N'Cà phê đen', N'Cà phê phin truyền thống.', CAST(25000.00 AS Decimal(12, 2)));
INSERT [dbo].[DoUong] ([maDoUong], [tenDoUong], [moTa], [giaBan]) VALUES (N'cof02', N'Cà phê sữa', N'Cà phê phin kết hợp sữa đặc.', CAST(30000.00 AS Decimal(12, 2)));
INSERT [dbo].[DoUong] ([maDoUong], [tenDoUong], [moTa], [giaBan]) VALUES (N'cof03', N'Bạc xỉu', N'Sữa nhiều, cà phê nhẹ.', CAST(35000.00 AS Decimal(12, 2)));
INSERT [dbo].[DoUong] ([maDoUong], [tenDoUong], [moTa], [giaBan]) VALUES (N'mil01', N'Trà sữa truyền thống', N'Trà sữa vị truyền thống.', CAST(35000.00 AS Decimal(12, 2)));
INSERT [dbo].[DoUong] ([maDoUong], [tenDoUong], [moTa], [giaBan]) VALUES (N'mil02', N'Trà sữa trân châu đường đen', N'Trà sữa và trân châu đường đen.', CAST(42000.00 AS Decimal(12, 2)));
INSERT [dbo].[DoUong] ([maDoUong], [tenDoUong], [moTa], [giaBan]) VALUES (N'tea01', N'Trà đào', N'Trà đào thanh mát.', CAST(30000.00 AS Decimal(12, 2)));
INSERT [dbo].[DoUong] ([maDoUong], [tenDoUong], [moTa], [giaBan]) VALUES (N'tea02', N'Trà tắc', N'Trà tắc chua ngọt.', CAST(25000.00 AS Decimal(12, 2)));
INSERT [dbo].[DoUong] ([maDoUong], [tenDoUong], [moTa], [giaBan]) VALUES (N'tea03', N'Trà vải', N'Trà kết hợp hương vị vải.', CAST(32000.00 AS Decimal(12, 2)));
GO

INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'nv01', N'Nguyễn Minh Anh', N'Nam', N'0901234501', N'Thu ngân', CAST(N'1999-03-15' AS Date), N'minhanh@newdaycoffee.vn');
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'nv02', N'Trần Quốc Bảo', N'Nam', N'0901234502', N'Pha chế', CAST(N'1998-07-21' AS Date), N'quocbao@newdaycoffee.vn');
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'nv03', N'Lê Hoàng Phúc', N'Nam', N'0901234503', N'Pha chế', CAST(N'2000-01-12' AS Date), N'hoangphuc@newdaycoffee.vn');
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'nv04', N'Phạm Thị Mai', N'Nữ', N'0901234504', N'Thu ngân', CAST(N'1999-09-10' AS Date), N'thimai@newdaycoffee.vn');
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'nv05', N'Võ Thanh Tùng', N'Nam', N'0901234505', N'Pha chế', CAST(N'1997-11-25' AS Date), N'thanhtung@newdaycoffee.vn');
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'nv06', N'Đặng Ngọc Hà', N'Nữ', N'0901234506', N'Thu ngân', CAST(N'2001-04-18' AS Date), N'ngochà@newdaycoffee.vn');
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'nv07', N'Bùi Gia Huy', N'Nam', N'0901234507', N'Pha chế', CAST(N'2000-06-20' AS Date), N'giahuy@newdaycoffee.vn');
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'nv08', N'Huỳnh Thảo Vy', N'Nữ', N'0901234508', N'Thu ngân', CAST(N'2001-12-05' AS Date), N'thaovy@newdaycoffee.vn');
INSERT [dbo].[NhanVien] ([maNV], [tenNV], [gioiTinh], [soDienThoai], [chucVu], [ngaySinh], [email]) VALUES (N'ql01', N'Nguyễn Thành Đạt', N'Nam', N'0901234509', N'Quản lí', CAST(N'1992-05-16' AS Date), N'thanhdat@newdaycoffee.vn');
GO

INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'nv01', N'123456', N'nv01');
INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'nv02', N'123456', N'nv02');
INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'nv03', N'123456', N'nv03');
INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'nv04', N'123456', N'nv04');
INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'nv05', N'123456', N'nv05');
INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'nv06', N'123456', N'nv06');
INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'nv07', N'123456', N'nv07');
INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'nv08', N'123456', N'nv08');
INSERT [dbo].[TaiKhoan] ([maDangNhap], [matKhau], [maNV]) VALUES (N'ql01', N'123456', N'ql01');
GO

INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026001', CAST(N'2026-11-01T08:15:00.0000000' AS DateTime2), CAST(110000.00 AS Decimal(12, 2)), N'Mang đi', N'nv01', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026002', CAST(N'2026-11-01T09:20:00.0000000' AS DateTime2), CAST(120000.00 AS Decimal(12, 2)), N'Uống tại quán', N'nv02', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026003', CAST(N'2026-11-02T10:10:00.0000000' AS DateTime2), CAST(150000.00 AS Decimal(12, 2)), NULL, N'nv03', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026004', CAST(N'2026-11-02T14:30:00.0000000' AS DateTime2), CAST(80000.00 AS Decimal(12, 2)), N'Mang đi', N'nv04', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026005', CAST(N'2026-11-03T08:45:00.0000000' AS DateTime2), CAST(150000.00 AS Decimal(12, 2)), NULL, N'nv05', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026006', CAST(N'2026-11-03T15:20:00.0000000' AS DateTime2), CAST(140000.00 AS Decimal(12, 2)), N'Uống tại quán', N'nv06', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026007', CAST(N'2026-11-04T09:10:00.0000000' AS DateTime2), CAST(100000.00 AS Decimal(12, 2)), NULL, N'nv07', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026008', CAST(N'2026-11-04T16:25:00.0000000' AS DateTime2), CAST(130000.00 AS Decimal(12, 2)), N'Mang đi', N'nv08', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026009', CAST(N'2026-11-05T08:30:00.0000000' AS DateTime2), CAST(140000.00 AS Decimal(12, 2)), NULL, N'ql01', N'Đã thanh toán');
INSERT [dbo].[HoaDon] ([maHoaDon], [ngayLap], [tienKhachTra], [ghiChu], [maNV], [trangThai]) VALUES (N'HD2026010', CAST(N'2026-11-05T17:15:00.0000000' AS DateTime2), CAST(90000.00 AS Decimal(12, 2)), N'Uống tại quán', N'ql01', N'Đã thanh toán');
GO

INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026001', N'cof01', 2);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026001', N'cof02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026001', N'tea01', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026002', N'cof03', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026002', N'mil01', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026002', N'tea02', 2);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026003', N'cof03', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026003', N'mil02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026003', N'tea03', 2);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026004', N'cof01', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026004', N'cof02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026004', N'tea02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026005', N'cof03', 2);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026005', N'mil02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026005', N'tea01', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026006', N'cof02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026006', N'mil01', 2);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026006', N'tea03', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026007', N'cof01', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026007', N'mil02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026007', N'tea02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026008', N'cof03', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026008', N'mil01', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026008', N'tea01', 2);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026009', N'cof02', 2);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026009', N'mil02', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026009', N'tea03', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026010', N'cof01', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026010', N'mil01', 1);
INSERT [dbo].[ChiTietHoaDon] ([maHoaDon], [maDoUong], [soLuong]) VALUES (N'HD2026010', N'tea02', 1);
GO

-- 9. Kiem tra nhanh so ban ghi, View va Function
SELECT
    (SELECT COUNT(*) FROM dbo.DoUong) AS SoDoUong,
    (SELECT COUNT(*) FROM dbo.NhanVien) AS SoNhanVien,
    (SELECT COUNT(*) FROM dbo.TaiKhoan) AS SoTaiKhoan,
    (SELECT COUNT(*) FROM dbo.HoaDon) AS SoHoaDon,
    (SELECT COUNT(*) FROM dbo.ChiTietHoaDon) AS SoChiTietHoaDon,
    dbo.fn_TinhThanhTien(2, 25000) AS TestTinhThanhTien,
    dbo.fn_TinhTongTienHoaDon(N'HD001') AS TestTongTienHD001,
    dbo.fn_TinhTienThua(N'HD001') AS TestTienThuaHD001;
GO
