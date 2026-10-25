package com.coffeeshop.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class QuanLyNhanVienPanel extends JPanel {
	// Các thành phần nhập liệu Thông tin nhân viên
	private JTextField txtMaNV;
	private JTextField txtTenNV;
	private JTextField txtNgaySinh;
	private JTextField txtSDT;
	private JTextField txtEmail;
	private JComboBox<String> cboChucVu;
	private JRadioButton radNam;
	private JRadioButton radNu;
	private JRadioButton radKhac;
	private ButtonGroup grpGioiTinh;

	// Các nút chức năng
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnSua;

	// Các thành phần Tìm kiếm nhân viên
	private JTextField txtTimMa;
	private JTextField txtTimTen;
	private JButton btnLamMoi;

	// Bảng danh sách nhân viên
	private JTable tblNhanVien;
	private DefaultTableModel modelNhanVien;

	public QuanLyNhanVienPanel() {
		setLayout(new BorderLayout());

		// Bảng màu chủ đạo theo hình mẫu
		Color bgHeader = new Color(138, 82, 36);
		Color bgMain = new Color(166, 123, 91);
		Color bgInput = new Color(235, 232, 228);
		Color bgHeaderBtn = new Color(179, 134, 96);

		Font fontTitle = new Font("Arial", Font.BOLD, 16);
		Font fontLabel = new Font("Arial", Font.PLAIN, 13);

		// ==================== 1. THANH HEADER PHÍA TRÊN ====================
		JPanel pnHeader = new JPanel(new BorderLayout());
		pnHeader.setBackground(bgHeader);
		pnHeader.setPreferredSize(new Dimension(0, 68));
		pnHeader.setBorder(new EmptyBorder(0, 30, 0, 25));

		JLabel lbHeaderTitle = new JLabel("NHÂN VIÊN");
		lbHeaderTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lbHeaderTitle.setForeground(Color.WHITE);
		pnHeader.add(lbHeaderTitle, BorderLayout.WEST);

		JPanel pnHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 16));
		pnHeaderRight.setOpaque(false);

		JButton btnCaKet = new JButton("Ca kết đang mở");
		btnCaKet.setBackground(bgHeaderBtn);
		btnCaKet.setForeground(Color.WHITE);
		btnCaKet.setFont(fontLabel);
		btnCaKet.setFocusPainted(false);
		btnCaKet.setBorderPainted(false);
		btnCaKet.setPreferredSize(new Dimension(145, 36));

		JButton btnQuanLy = new JButton("Quản lý: Lâm Nguyễn Như Ý");
		btnQuanLy.setBackground(bgHeaderBtn);
		btnQuanLy.setForeground(Color.WHITE);
		btnQuanLy.setFont(fontLabel);
		btnQuanLy.setFocusPainted(false);
		btnQuanLy.setBorderPainted(false);
		btnQuanLy.setPreferredSize(new Dimension(210, 36));

		pnHeaderRight.add(btnCaKet);
		pnHeaderRight.add(btnQuanLy);
		pnHeader.add(pnHeaderRight, BorderLayout.EAST);

		add(pnHeader, BorderLayout.NORTH);

		// ==================== 2. PHẦN THÂN CHÍNH (CHỨA FORM VÀ BẢNG) ====================
		JPanel pnContent = new JPanel(new BorderLayout(0, 20));
		pnContent.setBackground(bgMain);
		pnContent.setBorder(new EmptyBorder(15, 25, 25, 25));

		// --- KHỐI PHÍA TRÊN: BÊN TRÁI LÀ THÔNG TIN NV, BÊN PHẢI LÀ TÌM KIẾM ---
		JPanel pnTopSection = new JPanel();
		pnTopSection.setLayout(new BoxLayout(pnTopSection, BoxLayout.X_AXIS));
		pnTopSection.setOpaque(false);

		// A. Khu vực Thông tin nhân viên
		JPanel pnThongTinNV = new JPanel();
		pnThongTinNV.setLayout(new BoxLayout(pnThongTinNV, BoxLayout.Y_AXIS));
		pnThongTinNV.setOpaque(false);
		pnThongTinNV.setBorder(new EmptyBorder(0, 0, 0, 20));

		JPanel pnTitleLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnTitleLeft.setOpaque(false);
		JLabel lbTitleThongTin = new JLabel("THÔNG TIN NHÂN VIÊN");
		lbTitleThongTin.setFont(fontTitle);
		lbTitleThongTin.setForeground(Color.WHITE);
		pnTitleLeft.add(lbTitleThongTin);
		pnThongTinNV.add(pnTitleLeft);
		pnThongTinNV.add(Box.createVerticalStrut(15));

		// Hai cột nhập liệu song song
		JPanel pnHaiCotInput = new JPanel();
		pnHaiCotInput.setLayout(new BoxLayout(pnHaiCotInput, BoxLayout.X_AXIS));
		pnHaiCotInput.setOpaque(false);

		// Cột 1: Mã nhân viên, Tên nhân viên, Ngày sinh
		JPanel pnCot1 = new JPanel();
		pnCot1.setLayout(new BoxLayout(pnCot1, BoxLayout.Y_AXIS));
		pnCot1.setOpaque(false);

		txtMaNV = taoOChu(bgInput, 190, 34);
		txtTenNV = taoOChu(bgInput, 190, 34);
		txtNgaySinh = taoOChu(bgInput, 190, 34);

		pnCot1.add(taoDongNhap("Mã nhân viên:", txtMaNV, fontLabel, 95));
		pnCot1.add(Box.createVerticalStrut(16));
		pnCot1.add(taoDongNhap("Tên nhân viên:", txtTenNV, fontLabel, 95));
		pnCot1.add(Box.createVerticalStrut(16));
		pnCot1.add(taoDongNhap("Ngày sinh:", txtNgaySinh, fontLabel, 95));

		// Cột 2: Số điện thoại, Email, Chức vụ
		JPanel pnCot2 = new JPanel();
		pnCot2.setLayout(new BoxLayout(pnCot2, BoxLayout.Y_AXIS));
		pnCot2.setOpaque(false);

		txtSDT = taoOChu(bgInput, 195, 34);
		txtEmail = taoOChu(bgInput, 195, 34);

		String[] dsChucVu = {"Thu ngân", "Pha chế", "Quản lí"};
		cboChucVu = new JComboBox<>(dsChucVu);
		cboChucVu.setBackground(bgInput);
		cboChucVu.setFont(fontLabel);
		cboChucVu.setPreferredSize(new Dimension(195, 34));
		cboChucVu.setMaximumSize(new Dimension(195, 34));

		pnCot2.add(taoDongNhap("Số điện thoại:", txtSDT, fontLabel, 90));
		pnCot2.add(Box.createVerticalStrut(16));
		pnCot2.add(taoDongNhap("Email:", txtEmail, fontLabel, 90));
		pnCot2.add(Box.createVerticalStrut(16));
		pnCot2.add(taoDongNhap("Chức vụ:", cboChucVu, fontLabel, 90));

		pnHaiCotInput.add(pnCot1);
		pnHaiCotInput.add(Box.createHorizontalStrut(30));
		pnHaiCotInput.add(pnCot2);
		pnThongTinNV.add(pnHaiCotInput);
		pnThongTinNV.add(Box.createVerticalStrut(18));

		// Hàng Giới tính & Các nút Thêm, Xóa, Sửa
		JPanel pnHangCuoiForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnHangCuoiForm.setOpaque(false);

		JLabel lbGioiTinh = new JLabel("Giới tính:");
		lbGioiTinh.setFont(fontLabel);
		lbGioiTinh.setForeground(Color.WHITE);
		lbGioiTinh.setPreferredSize(new Dimension(95, 30));

		radNam = new JRadioButton("Nam");
		radNu = new JRadioButton("Nữ");
		radKhac = new JRadioButton("Khác");

		JRadioButton[] dsRad = {radNam, radNu, radKhac};
		grpGioiTinh = new ButtonGroup();
		for (int i = 0; i < dsRad.length; i++) {
			dsRad[i].setOpaque(false);
			dsRad[i].setForeground(Color.WHITE);
			dsRad[i].setFont(fontLabel);
			dsRad[i].setFocusPainted(false);
			grpGioiTinh.add(dsRad[i]);
		}

		btnThem = taoNutBam("Thêm", new Color(46, 184, 72), 68, 28);
		btnXoa = taoNutBam("Xóa", new Color(198, 40, 40), 68, 28);
		btnSua = taoNutBam("Sửa", new Color(158, 145, 135), 68, 28);

		pnHangCuoiForm.add(lbGioiTinh);
		pnHangCuoiForm.add(radNam);
		pnHangCuoiForm.add(Box.createHorizontalStrut(12));
		pnHangCuoiForm.add(radNu);
		pnHangCuoiForm.add(Box.createHorizontalStrut(12));
		pnHangCuoiForm.add(radKhac);
		pnHangCuoiForm.add(Box.createHorizontalStrut(75));
		pnHangCuoiForm.add(btnThem);
		pnHangCuoiForm.add(Box.createHorizontalStrut(20));
		pnHangCuoiForm.add(btnXoa);
		pnHangCuoiForm.add(Box.createHorizontalStrut(20));
		pnHangCuoiForm.add(btnSua);

		pnThongTinNV.add(pnHangCuoiForm);

		// B. Khu vực Tìm kiếm nhân viên bên phải
		JPanel pnTimKiemNV = new JPanel();
		pnTimKiemNV.setLayout(new BoxLayout(pnTimKiemNV, BoxLayout.Y_AXIS));
		pnTimKiemNV.setOpaque(false);
		pnTimKiemNV.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(110, 75, 45)),
			new EmptyBorder(5, 20, 0, 0)
		));

		JPanel pnTitleSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnTitleSearch.setOpaque(false);
		JLabel lbTitleTimKiem = new JLabel("TÌM KIẾM NHÂN VIÊN");
		lbTitleTimKiem.setFont(new Font("Arial", Font.BOLD, 14));
		lbTitleTimKiem.setForeground(Color.WHITE);
		pnTitleSearch.add(lbTitleTimKiem);

		txtTimMa = taoOChu(bgInput, 175, 34);
		txtTimTen = taoOChu(bgInput, 175, 34);

		JPanel pnBtnLamMoiWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnBtnLamMoiWrap.setOpaque(false);
		btnLamMoi = taoNutBam("Làm mới danh sách", new Color(230, 126, 50), 185, 34);
		btnLamMoi.setBorder(BorderFactory.createLineBorder(new Color(120, 60, 20), 1));
		btnLamMoi.setBorderPainted(true);
		pnBtnLamMoiWrap.add(btnLamMoi);

		pnTimKiemNV.add(pnTitleSearch);
		pnTimKiemNV.add(Box.createVerticalStrut(18));
		pnTimKiemNV.add(taoDongNhap("Nhập mã cần tìm:", txtTimMa, fontLabel, 115));
		pnTimKiemNV.add(Box.createVerticalStrut(16));
		pnTimKiemNV.add(taoDongNhap("Nhập tên cần tìm:", txtTimTen, fontLabel, 115));
		pnTimKiemNV.add(Box.createVerticalStrut(22));
		pnTimKiemNV.add(pnBtnLamMoiWrap);

		pnTopSection.add(pnThongTinNV);
		pnTopSection.add(pnTimKiemNV);
		pnContent.add(pnTopSection, BorderLayout.NORTH);

		// --- KHỐI PHÍA DƯỚI: BẢNG DANH SÁCH NHÂN VIÊN ---
		String[] tenCot = {"Mã NV", "Tên nhân viên", "Giới tính", "Chức vụ", "Ngày sinh", "Số điện thoại", "Email"};
		modelNhanVien = new DefaultTableModel(tenCot, 0);
		modelNhanVien.addRow(new Object[]{"A01", "Lâm Nguyễn Như Ý", "Nữ", "Quản lí", "01-01-2006", "782730129", "ls@gmail.com"});
		modelNhanVien.addRow(new Object[]{"A02", "Nguyễn Quang Toàn", "Nam", "Thu ngân", "01-01-2006", "37218997", "hs@gmail.com"});
		modelNhanVien.addRow(new Object[]{"A03", "Trần Xuân Tiền", "Nam", "Giám sát ca", "01-01-2006", "232100008", "dj@gmail.com"});
		modelNhanVien.addRow(new Object[]{"A04", "Trần Đăng Khoa", "Nam", "Pha chế", "01-01-2006", "-", "-"});

		tblNhanVien = new JTable(modelNhanVien);
		tblNhanVien.setRowHeight(42);
		tblNhanVien.setBackground(new Color(242, 235, 228));
		tblNhanVien.setGridColor(new Color(225, 215, 205));
		tblNhanVien.setFont(new Font("Arial", Font.PLAIN, 12));

		tblNhanVien.getTableHeader().setBackground(new Color(245, 245, 245));
		tblNhanVien.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
		tblNhanVien.getTableHeader().setPreferredSize(new Dimension(0, 38));

		JScrollPane scrollBang = new JScrollPane(tblNhanVien);
		scrollBang.getViewport().setBackground(new Color(242, 235, 228));
		scrollBang.setBorder(BorderFactory.createLineBorder(new Color(45, 155, 240), 2));

		pnContent.add(scrollBang, BorderLayout.CENTER);
		add(pnContent, BorderLayout.CENTER);
	}

	private JPanel taoDongNhap(String tenNhan, Component comp, Font font, int doRongNhan) {
		JPanel pnDong = new JPanel();
		pnDong.setLayout(new BoxLayout(pnDong, BoxLayout.X_AXIS));
		pnDong.setOpaque(false);

		JLabel lb = new JLabel(tenNhan);
		lb.setFont(font);
		lb.setForeground(Color.WHITE);
		lb.setPreferredSize(new Dimension(doRongNhan, 34));
		lb.setMinimumSize(new Dimension(doRongNhan, 34));
		lb.setMaximumSize(new Dimension(doRongNhan, 34));

		pnDong.add(lb);
		pnDong.add(comp);
		return pnDong;
	}

	private JTextField taoOChu(Color bg, int width, int height) {
		JTextField txt = new JTextField();
		txt.setBackground(bg);
		txt.setFont(new Font("Arial", Font.PLAIN, 13));
		txt.setBorder(new EmptyBorder(5, 10, 5, 10));
		txt.setPreferredSize(new Dimension(width, height));
		txt.setMaximumSize(new Dimension(width, height));
		return txt;
	}

	private JButton taoNutBam(String text, Color bg, int width, int height) {
		JButton btn = new JButton(text);
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.BOLD, 12));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setPreferredSize(new Dimension(width, height));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}
}
