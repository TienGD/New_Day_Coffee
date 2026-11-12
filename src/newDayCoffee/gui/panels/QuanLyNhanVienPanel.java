package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSNhanVienBUS;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.dto.TaiKhoanDTO;
import newDayCoffee.utils.UIUtils;
import newDayCoffee.utils.Validator;

public class QuanLyNhanVienPanel extends JPanel implements ActionListener, ListSelectionListener {

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

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSNhanVienBUS DSNhanVienBUS;

	public QuanLyNhanVienPanel(DSNhanVienBUS DSNhanVienBUS) {
		this.DSNhanVienBUS = DSNhanVienBUS;
		khoiTaoGiaoDien();
		ganSuKien();
		capNhatBang();
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
		radNam.setSelected(true);

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

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnSua.addActionListener(this);
		btnLuu.addActionListener(this);
		btnTimKiem.addActionListener(this);
		btnLamMoi.addActionListener(this);
		txtTimMa.addActionListener(this);
		txtTimTen.addActionListener(this);

		tblNhanVien.getSelectionModel().addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object nguon = e.getSource();

		if (nguon == btnThem) {
			themNhanVien();
		} else if (nguon == btnSua) {
			suaNhanVien();
		} else if (nguon == btnXoa) {
			xoaNhanVien();
		} else if (nguon == btnLuu) {
			luuDuLieu();
		} else if (nguon == btnLamMoi) {
			lamMoi();
		} else if (nguon == btnTimKiem || nguon == txtTimMa || nguon == txtTimTen) {
			capNhatBang();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		int dong = tblNhanVien.getSelectedRow();
		if (dong < 0) {
			return;
		}

		String maNV = String.valueOf(modelNhanVien.getValueAt(dong, 0));
		NhanVienDTO nhanVien = DSNhanVienBUS.timTheoMa(maNV);
		if (nhanVien == null) {
			return;
		}

		txtMaNV.setText(nhanVien.getMaNV());
		txtMaNV.setEditable(false);
		txtTenNV.setText(nhanVien.getTenNV());
		txtNgaySinh.setText(UIUtils.dinhDangNgay(nhanVien.getNgaySinh()));
		txtSDT.setText(nhanVien.getSdt());
		txtEmail.setText(nhanVien.getEmail());
		cboChucVu.setSelectedItem(nhanVien.getChucVu());
		chonGioiTinh(nhanVien.getGioiTinh());
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp lại bảng theo mã / tên đang nhập. */
	public void capNhatBang() {
		List<NhanVienDTO> dsNhanVien = DSNhanVienBUS.timKiem(txtTimMa.getText(), txtTimTen.getText());

		modelNhanVien.setRowCount(0);
		for (int i = 0; i < dsNhanVien.size(); i++) {
			NhanVienDTO nhanVien = dsNhanVien.get(i);
			modelNhanVien.addRow(new Object[]{
				nhanVien.getMaNV(),
				nhanVien.getTenNV(),
				nhanVien.getGioiTinh(),
				nhanVien.getChucVu(),
				UIUtils.dinhDangNgay(nhanVien.getNgaySinh()),
				nhanVien.getSdt(),
				nhanVien.getEmail()
			});
		}

		if (dsNhanVien.isEmpty()
				&& !(txtTimMa.getText().trim().isEmpty() && txtTimTen.getText().trim().isEmpty())) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên.",
					"Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void themNhanVien() {
		String loi = kiemTraDuLieuNhanVien();
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể thêm nhân viên", JOptionPane.ERROR_MESSAGE);
			return;
		}

		NhanVienDTO nhanVien = taoNhanVienTuForm();
		TaiKhoanDTO taiKhoan = new TaiKhoanDTO(nhanVien, "123456");
		String ketQua = DSNhanVienBUS.themNhanVien(nhanVien, taiKhoan);
		if (ketQua != null) {
			JOptionPane.showMessageDialog(this, ketQua, "Không thể thêm nhân viên", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã thêm nhân viên. Mật khẩu mặc định: 123456",
				"Thành công", JOptionPane.INFORMATION_MESSAGE);
		lamMoi();
	}

	private void suaNhanVien() {
		String loi = kiemTraDuLieuNhanVien();
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể sửa nhân viên", JOptionPane.ERROR_MESSAGE);
			return;
		}

		NhanVienDTO nhanVien = taoNhanVienTuForm();
		String ketQua = DSNhanVienBUS.suaNhanVien(nhanVien);
		if (ketQua != null) {
			JOptionPane.showMessageDialog(this, ketQua, "Không thể sửa nhân viên", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã cập nhật nhân viên.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		capNhatBang();
	}

	/** Kiểm tra dữ liệu form nhân viên trước khi thêm hoặc sửa. */
	private String kiemTraDuLieuNhanVien() {
		String maNV = txtMaNV.getText().trim();
		if (maNV.isEmpty()) {
			return "Vui lòng nhập mã nhân viên.";
		}
		if (!maNV.matches(Validator.MA_NHAN_VIEN)) {
			return "Mã nhân viên phải có đúng 4 ký tự, dạng nvXX hoặc qlXX (ví dụ nv01).";
		}

		String tenNV = txtTenNV.getText().trim();
		if (tenNV.isEmpty()) {
			return "Vui lòng nhập tên nhân viên.";
		}
		if (tenNV.length() < 2 || tenNV.length() > 50) {
			return "Tên nhân viên phải có từ 2 đến 50 ký tự.";
		}
		if (tenNV.matches(".*\\d.*")) {
			return "Tên nhân viên không được chứa chữ số.";
		}
		String[] dsTu = tenNV.split(" ");
		for (int i = 0; i < dsTu.length; i++) {
			if (dsTu[i].isEmpty() || !Character.isUpperCase(dsTu[i].charAt(0))) {
				return "Tên nhân viên phải viết hoa chữ cái đầu mỗi từ và cách nhau bằng một khoảng trắng.";
			}
		}

		String ngaySinhText = txtNgaySinh.getText().trim();
		if (!ngaySinhText.matches(Validator.NGAY_SINH)) {
			return "Ngày sinh không hợp lệ (định dạng dd-MM-yyyy).";
		}
		LocalDate ngaySinh = docNgay(ngaySinhText);
		if (ngaySinh == null) {
			return "Ngày sinh không hợp lệ (định dạng dd-MM-yyyy).";
		}
		if (ngaySinh.isAfter(LocalDate.now())) {
			return "Ngày sinh không được lớn hơn ngày hiện tại.";
		}
		if (ngaySinh.getYear() < 1900) {
			return "Năm sinh phải từ 1900 trở lại đây.";
		}

		String sdt = txtSDT.getText().trim().replace(" ", "").replace("-", "");
		if (sdt.isEmpty()) {
			return "Vui lòng nhập số điện thoại.";
		}
		if (!sdt.matches(Validator.SO_DIEN_THOAI)) {
			return "Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0.";
		}

		String email = txtEmail.getText().trim();
		if (!email.isEmpty() && !email.matches(Validator.EMAIL)) {
			return "Email không đúng định dạng (ví dụ ten@gmail.com).";
		}

		String ma = maNV.toLowerCase();
		String chucVu = layChucVu();
		if (ma.startsWith("ql") && !chucVu.equals("Quản lí")) {
			return "Mã nhân viên bắt đầu bằng ql phải có chức vụ Quản lí.";
		}
		if (ma.startsWith("nv") && chucVu.equals("Quản lí")) {
			return "Mã nhân viên bắt đầu bằng nv không được có chức vụ Quản lí.";
		}
		return null;
	}

	/** Đọc ngày sinh với các định dạng dd-MM-yyyy, dd/MM/yyyy, yyyy-MM-dd. */
	private LocalDate docNgay(String chuoiNgay) {
		String s = chuoiNgay == null ? "" : chuoiNgay.trim();
		if (s.isEmpty()) {
			return null;
		}
		String[] dsDinhDang = {"d-M-uuuu", "d/M/uuuu", "uuuu-M-d"};
		for (int i = 0; i < dsDinhDang.length; i++) {
			try {
				DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern(dsDinhDang[i])
						.withResolverStyle(ResolverStyle.STRICT);
				return LocalDate.parse(s, dinhDang);
			} catch (DateTimeParseException e) {
				// Thử định dạng tiếp theo
			}
		}
		return null;
	}

	private void xoaNhanVien() {
		String maNV = txtMaNV.getText().trim();

		int dong = tblNhanVien.getSelectedRow();
		if (maNV.isEmpty() && dong >= 0) {
			maNV = String.valueOf(modelNhanVien.getValueAt(dong, 0));
		}
		if (maNV.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập mã nhân viên cần xóa.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		NhanVienDTO nhanVien = DSNhanVienBUS.timTheoMa(maNV);
		String tenNV = (nhanVien == null) ? maNV : nhanVien.getTenNV();

		int luaChon = JOptionPane.showConfirmDialog(this, "Xóa nhân viên \"" + tenNV + "\"?",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}

		String loi = DSNhanVienBUS.xoaNhanVien(maNV);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể xóa nhân viên", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã xóa nhân viên.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		lamMoi();
	}

	private void luuDuLieu() {
		if (DSNhanVienBUS.luuVaoCSDL()) {
			JOptionPane.showMessageDialog(this, "Đã lưu danh sách nhân viên.",
					"Thành công", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Lưu dữ liệu thất bại.",
					"Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void lamMoi() {
		txtTimMa.setText("");
		txtTimTen.setText("");
		xoaForm();
		capNhatBang();
	}

	private NhanVienDTO taoNhanVienTuForm() {
		LocalDate ngaySinh = docNgay(txtNgaySinh.getText());
		return new NhanVienDTO(txtMaNV.getText().trim(), txtTenNV.getText().trim(),
				ngaySinh, txtSDT.getText().trim(), txtEmail.getText().trim(),
				layChucVu(), layGioiTinh(), "");
	}

	private String layChucVu() {
		Object chucVu = cboChucVu.getSelectedItem();
		return chucVu == null ? "" : chucVu.toString();
	}

	private String layGioiTinh() {
		if (radNu.isSelected()) {
			return "Nữ";
		}
		if (radKhac.isSelected()) {
			return "Khác";
		}
		return "Nam";
	}

	private void chonGioiTinh(String gioiTinh) {
		if ("Nữ".equalsIgnoreCase(gioiTinh)) {
			radNu.setSelected(true);
		} else if ("Khác".equalsIgnoreCase(gioiTinh)) {
			radKhac.setSelected(true);
		} else {
			radNam.setSelected(true);
		}
	}

	private void xoaForm() {
		txtMaNV.setEditable(true);
		txtMaNV.setText("");
		txtTenNV.setText("");
		txtNgaySinh.setText("");
		txtSDT.setText("");
		txtEmail.setText("");
		cboChucVu.setSelectedIndex(0);
		radNam.setSelected(true);
		tblNhanVien.clearSelection();
		txtMaNV.requestFocusInWindow();
	}
}
