package newDayCoffee.bus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.utils.Auth;

public class HoaDonBUS {

	private HoaDonDTO hoaDon;
	private DoUongDTO doUong;
	private DSChiTietHoaDonBus dsChiTietHoaDon;

	private DSHoaDonBUS dsHoaDonBUS;
	private ChiTietHoaDonBUS chiTietHoaDonBUS;
	private HoaDonDTO hoaDonVuaTao;

	public HoaDonBUS() {
	}

	public HoaDonBUS(HoaDonDTO hoaDon, DoUongDTO doUong, DSChiTietHoaDonBus dsChiTietHoaDon) {
		super();
		this.hoaDon = hoaDon;
		this.doUong = doUong;
		this.dsChiTietHoaDon = dsChiTietHoaDon;
	}

	public HoaDonBUS(DSHoaDonBUS dsHoaDonBUS, ChiTietHoaDonBUS chiTietHoaDonBUS) {
		this.dsHoaDonBUS = dsHoaDonBUS;
		this.chiTietHoaDonBUS = chiTietHoaDonBUS;
		this.dsChiTietHoaDon = chiTietHoaDonBUS != null ? chiTietHoaDonBUS.getDsChiTietHoaDonBus() : null;
		this.hoaDonVuaTao = null;
	}

	public HoaDonDTO getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDonDTO hoaDon) {
		this.hoaDon = hoaDon;
	}

	public DoUongDTO getDoUong() {
		return doUong;
	}

	public void setDoUong(DoUongDTO doUong) {
		this.doUong = doUong;
	}

	public DSChiTietHoaDonBus getDsChiTietHoaDon() {
		return dsChiTietHoaDon;
	}

	public void setDsChiTietHoaDon(DSChiTietHoaDonBus dsChiTietHoaDon) {
		this.dsChiTietHoaDon = dsChiTietHoaDon;
	}

	public double tinhTongTien(HoaDonDTO hd) {
		if (hd == null || hd.getMaHD() == null || hd.getMaHD().trim().isEmpty()) {
			return 0;
		}

		List<ChiTietHoaDonDTO> dsChiTiet = null;
		if (dsChiTietHoaDon != null) {
			dsChiTiet = dsChiTietHoaDon.layTheoHoaDon(hd.getMaHD());
		} else if (dsHoaDonBUS != null) {
			dsChiTiet = dsHoaDonBUS.layChiTiet(hd.getMaHD());
		} else if (chiTietHoaDonBUS != null) {
			dsChiTiet = chiTietHoaDonBUS.layTheoHoaDon(hd.getMaHD());
		}

		if (dsChiTiet == null || dsChiTiet.isEmpty()) {
			return 0;
		}

		double tong = 0;
		for (int i = 0; i < dsChiTiet.size(); i++) {
			ChiTietHoaDonDTO ct = dsChiTiet.get(i);
			if (ct != null) {
				ChiTietHoaDonBUS busChiTiet = new ChiTietHoaDonBUS(ct, ct.getDoUong());
				tong = tong + busChiTiet.tinhThanhTien();
			}
		}
		return tong;
	}

	public double tinhTongTien() {
		return tinhTongTien(this.hoaDon);
	}

	public double tienThua(HoaDonDTO hd) {
		if (hd == null) {
			return 0;
		}
		double tongTien = tinhTongTien(hd);
		double khachTra = hd.getTienKhachDua();
		return khachTra - tongTien;
	}

	public double tinhTienThua(HoaDonDTO hd) {
		return tienThua(hd);
	}

	public double tinhTienThua() {
		return tienThua(this.hoaDon);
	}

	public double tinhTienThua(double tienKhachDua, double tongTien) {
		return tienKhachDua - tongTien;
	}

	public String thanhToan(HoaDonDTO hoaDonMoi) {
		hoaDonVuaTao = null;

		if (chiTietHoaDonBUS == null || chiTietHoaDonBUS.gioHangRong()) {
			return "Đơn hàng chưa có món nào để thanh toán.";
		}
		if (!Auth.isDangNhap()) {
			return "Vui lòng đăng nhập trước khi thanh toán.";
		}
		if (hoaDonMoi == null) {
			return "Thông tin hóa đơn không hợp lệ.";
		}
		if (dsHoaDonBUS == null) {
			return "Hệ thống hóa đơn chưa được khởi tạo.";
		}

		hoaDonMoi.setMaHD(dsHoaDonBUS.taoMaHoaDon());
		hoaDonMoi.setNgayLap(LocalDate.now());
		hoaDonMoi.setMaNV(Auth.getNguoiDung());
		hoaDonMoi.setGhiChu(chuanHoa(hoaDonMoi.getGhiChu()));
		hoaDonMoi.setTrangThai("Đã thanh toán");

		if (!dsHoaDonBUS.themHoaDon(hoaDonMoi)) {
			return "Không thể lưu hóa đơn. Vui lòng thử lại.";
		}

		this.hoaDon = hoaDonMoi;
		hoaDonVuaTao = hoaDonMoi;
		return null;
	}

	public HoaDonDTO layHoaDonVuaTao() {
		return hoaDonVuaTao;
	}

	public String xuatHoaDonRaFile(HoaDonDTO hoaDonXuat, String noiDung) {
		if (hoaDonXuat == null || noiDung == null) {
			return null;
		}

		try {
			Path thuMuc = Paths.get("hoadon");
			Files.createDirectories(thuMuc);
			Path file = thuMuc.resolve(hoaDonXuat.getMaHD() + ".txt");
			Files.write(file, noiDung.getBytes(StandardCharsets.UTF_8));
			return file.toAbsolutePath().toString();
		} catch (IOException e) {
			return null;
		}
	}

	private static String chuanHoa(String giaTri) {
		return giaTri == null ? "" : giaTri.trim();
	}
}
