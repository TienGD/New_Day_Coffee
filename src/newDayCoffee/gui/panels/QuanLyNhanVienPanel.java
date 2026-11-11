package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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

import newDayCoffee.utils.UIUtils;

public class QuanLyNhanVienPanel extends JPanel {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
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

	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnLuu;

	private JTextField txtTimMa;
	private JTextField txtTimTen;
	private JButton btnTimKiem;
	private JButton btnLamMoi;

	private JTable tblNhanVien;
	private DefaultTableModel modelNhanVien;

	public QuanLyNhanVienPanel() {
		khoiTaoGiaoDien();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new BorderLayout());

		Font fontLabel = UIUtils.FONT_LABEL_BOLD;

		// ==================== PHẦN THÂN CHÍNH (CHỨA FORM VÀ BẢNG) ====================
		JPanel pnContent = new JPanel(new BorderLayout(0, 18));
		pnContent.setBackground(UIUtils.COLOR_BG_MAIN);
		pnContent.setBorder(new EmptyBorder(18, 22, 22, 22));

		// --- KHỐI PHÍA TRÊN: BÊN TRÁI LÀ THÔNG TIN NV, BÊN PHẢI LÀ TÌM KIẾM ---
		JPanel pnTopSection = new JPanel(new GridBagLayout());
		pnTopSection.setOpaque(false);
		GridBagConstraints gbcTop = new GridBagConstraints();
		gbcTop.fill = GridBagConstraints.BOTH;
		gbcTop.gridy = 0;
		gbcTop.weighty = 1.0;

		// A. Khu vực Thông tin nhân viên (bên trái)
		JPanel pnThongTinNV = UIUtils.createCardPanel(UIUtils.COLOR_CARD_SURFACE, 16, new Insets(16, 18, 16, 18));
		pnThongTinNV.setLayout(new BoxLayout(pnThongTinNV, BoxLayout.Y_AXIS));

		JPanel pnTitleLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnTitleLeft.setOpaque(false);
		JLabel lbTitleThongTin = UIUtils.createTitlePanel("THÔNG TIN NHÂN VIÊN");
		pnTitleLeft.add(lbTitleThongTin);
		pnThongTinNV.add(pnTitleLeft);
		pnThongTinNV.add(Box.createVerticalStrut(14));

		// Hai cột nhập liệu song song
		JPanel pnHaiCotInput = new JPanel();
		pnHaiCotInput.setLayout(new BoxLayout(pnHaiCotInput, BoxLayout.X_AXIS));
		pnHaiCotInput.setOpaque(false);

		// Cột 1: Mã nhân viên, Tên nhân viên, Ngày sinh
		JPanel pnCot1 = new JPanel();
		pnCot1.setLayout(new BoxLayout(pnCot1, BoxLayout.Y_AXIS));
		pnCot1.setOpaque(false);

		txtMaNV = UIUtils.createTextField(172, 34);
		txtTenNV = UIUtils.createTextField(172, 34);
		txtNgaySinh = UIUtils.createTextField(172, 34);
		txtNgaySinh.setToolTipText("Định dạng dd-MM-yyyy");

		pnCot1.add(UIUtils.createInputRow("Mã nhân viên:", txtMaNV, fontLabel, 96));
		pnCot1.add(Box.createVerticalStrut(14));
		pnCot1.add(UIUtils.createInputRow("Tên nhân viên:", txtTenNV, fontLabel, 96));
		pnCot1.add(Box.createVerticalStrut(14));
		pnCot1.add(UIUtils.createInputRow("Ngày sinh:", txtNgaySinh, fontLabel, 96));

		// Cột 2: Số điện thoại, Email, Chức vụ
		JPanel pnCot2 = new JPanel();
		pnCot2.setLayout(new BoxLayout(pnCot2, BoxLayout.Y_AXIS));
		pnCot2.setOpaque(false);

		txtSDT = UIUtils.createTextField(172, 34);
		txtEmail = UIUtils.createTextField(172, 34);

		String[] dsChucVu = {"Thu ngân", "Pha chế", "Quản lí"};
		cboChucVu = UIUtils.createComboBox(dsChucVu, 172, 34);

		pnCot2.add(UIUtils.createInputRow("Số điện thoại:", txtSDT, fontLabel, 92));
		pnCot2.add(Box.createVerticalStrut(14));
		pnCot2.add(UIUtils.createInputRow("Email:", txtEmail, fontLabel, 92));
		pnCot2.add(Box.createVerticalStrut(14));
		pnCot2.add(UIUtils.createInputRow("Chức vụ:", cboChucVu, fontLabel, 92));
		pnCot2.add(Box.createVerticalStrut(3));

		pnHaiCotInput.add(pnCot1);
		pnHaiCotInput.add(Box.createHorizontalStrut(20));
		pnHaiCotInput.add(pnCot2);
		pnThongTinNV.add(pnHaiCotInput);
		pnThongTinNV.add(Box.createVerticalStrut(16));

		// Hàng Giới tính & Các nút Thêm, Xóa, Sửa, Lưu
		JPanel pnHangCuoiForm = new JPanel();
		pnHangCuoiForm.setLayout(new BoxLayout(pnHangCuoiForm, BoxLayout.X_AXIS));
		pnHangCuoiForm.setOpaque(false);

		JLabel lbGioiTinh = new JLabel("Giới tính:");
		lbGioiTinh.setFont(fontLabel);
		lbGioiTinh.setForeground(new Color(248, 240, 232));
		lbGioiTinh.setPreferredSize(new Dimension(65, 32));

		radNam = UIUtils.createRadioButton("Nam", fontLabel);
		radNu = UIUtils.createRadioButton("Nữ", fontLabel);
		radKhac = UIUtils.createRadioButton("Khác", fontLabel);

		grpGioiTinh = new ButtonGroup();
		grpGioiTinh.add(radNam);
		grpGioiTinh.add(radNu);
		grpGioiTinh.add(radKhac);

		Dimension btnActionSize = new Dimension(72, 34);
		btnThem = UIUtils.createButton("Thêm", UIUtils.COLOR_BTN_THEM, 72, 34);
		btnThem.setMaximumSize(btnActionSize);
		btnXoa  = UIUtils.createButton("Xóa", UIUtils.COLOR_BTN_XOA, 72, 34);
		btnXoa.setMaximumSize(btnActionSize);
		btnSua  = UIUtils.createButton("Sửa", UIUtils.COLOR_BTN_SUA, 72, 34);
		btnSua.setMaximumSize(btnActionSize);
		btnLuu  = UIUtils.createButton("Lưu", UIUtils.COLOR_BTN_LUU, 72, 34);
		btnLuu.setMaximumSize(btnActionSize);

		pnHangCuoiForm.add(lbGioiTinh);
		pnHangCuoiForm.add(radNam);
		pnHangCuoiForm.add(Box.createHorizontalStrut(8));
		pnHangCuoiForm.add(radNu);
		pnHangCuoiForm.add(Box.createHorizontalStrut(8));
		pnHangCuoiForm.add(radKhac);
		pnHangCuoiForm.add(Box.createHorizontalStrut(24));
		pnHangCuoiForm.add(btnThem);
		pnHangCuoiForm.add(Box.createHorizontalStrut(10));
		pnHangCuoiForm.add(btnXoa);
		pnHangCuoiForm.add(Box.createHorizontalStrut(10));
		pnHangCuoiForm.add(btnSua);
		pnHangCuoiForm.add(Box.createHorizontalStrut(10));
		pnHangCuoiForm.add(btnLuu);
		pnHangCuoiForm.add(Box.createHorizontalGlue());

		pnThongTinNV.add(pnHangCuoiForm);

		// B. Khu vực Tìm kiếm nhân viên bên phải
		JPanel pnTimKiemNV = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 16, new Insets(16, 20, 18, 20));
		pnTimKiemNV.setLayout(new BoxLayout(pnTimKiemNV, BoxLayout.Y_AXIS));

		JPanel pnTitleSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnTitleSearch.setOpaque(false);
		JLabel lbTitleTimKiem = UIUtils.createSearchTitle("TÌM KIẾM NHÂN VIÊN");
		pnTitleSearch.add(lbTitleTimKiem);

		txtTimMa = UIUtils.createTextField(190, 34);
		txtTimTen = UIUtils.createTextField(190, 34);

		// Đặt 2 nút trên cùng 1 hàng ngang cố định, không bị rớt dòng hay cắt chữ
		JPanel pnBtnSearchWrap = new JPanel();
		pnBtnSearchWrap.setLayout(new BoxLayout(pnBtnSearchWrap, BoxLayout.X_AXIS));
		pnBtnSearchWrap.setOpaque(false);

		Dimension sizeTim = new Dimension(100, 36);
		Dimension sizeLamMoi = new Dimension(175, 36);
		btnTimKiem = UIUtils.createButton("Tìm kiếm", UIUtils.COLOR_BTN_TIM, 100, 36);
		btnTimKiem.setMinimumSize(sizeTim);
		btnTimKiem.setMaximumSize(sizeTim);

		btnLamMoi = UIUtils.createButton("Làm mới danh sách", UIUtils.COLOR_BTN_LAM_MOI, 175, 36);
		btnLamMoi.setMinimumSize(sizeLamMoi);
		btnLamMoi.setMaximumSize(sizeLamMoi);

		pnBtnSearchWrap.add(Box.createHorizontalGlue());
		pnBtnSearchWrap.add(btnTimKiem);
		pnBtnSearchWrap.add(Box.createHorizontalStrut(12));
		pnBtnSearchWrap.add(btnLamMoi);
		pnBtnSearchWrap.add(Box.createHorizontalGlue());

		pnTimKiemNV.add(pnTitleSearch);
		pnTimKiemNV.add(Box.createVerticalStrut(18));
		pnTimKiemNV.add(UIUtils.createInputRow("Nhập mã cần tìm:", txtTimMa, fontLabel, 118));
		pnTimKiemNV.add(Box.createVerticalStrut(14));
		pnTimKiemNV.add(UIUtils.createInputRow("Nhập tên cần tìm:", txtTimTen, fontLabel, 118));
		pnTimKiemNV.add(Box.createVerticalStrut(20));
		pnTimKiemNV.add(pnBtnSearchWrap);

		gbcTop.gridx = 0;
		gbcTop.weightx = 0.60;
		gbcTop.insets = new Insets(0, 0, 0, 16);
		pnTopSection.add(pnThongTinNV, gbcTop);

		gbcTop.gridx = 1;
		gbcTop.weightx = 0.40;
		gbcTop.insets = new Insets(0, 0, 0, 0);
		pnTopSection.add(pnTimKiemNV, gbcTop);

		pnContent.add(pnTopSection, BorderLayout.NORTH);

		// --- KHỐI PHÍA DƯỚI: BẢNG DANH SÁCH NHÂN VIÊN ---
		String[] tenCot = {"Mã NV", "Tên nhân viên", "Giới tính", "Chức vụ", "Ngày sinh", "Số điện thoại", "Email"};
		modelNhanVien = new DefaultTableModel(tenCot, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblNhanVien = new JTable(modelNhanVien);
		JScrollPane scrollBang = UIUtils.createTableScrollPane(tblNhanVien);
		UIUtils.centerTableCells(tblNhanVien, 0, 2, 3, 4, 5);

		pnContent.add(scrollBang, BorderLayout.CENTER);
		add(pnContent, BorderLayout.CENTER);
	}
}
