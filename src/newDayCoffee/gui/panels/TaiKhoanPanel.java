package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSTaiKhoanBUS;
import newDayCoffee.dto.TaiKhoanDTO;
import newDayCoffee.utils.UIUtils;
import newDayCoffee.utils.Validator;

/**
 * Màn hình Quản lý tài khoản.
 *
 * Giao diện gồm 2 phần chính:
 * 1. Khối phía trên:
 *    - Bên trái: Khung thông tin tài khoản với 2 thuộc tính (Mã nhân viên, Mật khẩu)
 *                và 4 nút Thêm, Xóa, Sửa, Lưu nằm ngang hàng.
 *    - Bên phải: Khung tìm kiếm tài khoản theo mã nhân viên.
 * 2. Khối phía dưới:
 *    - Bảng danh sách tài khoản (Mã nhân viên, Mật khẩu).
 *
 * Toàn bộ các cấu hình UI dùng chung đều được gọi từ class UIUtils.
 */
public class TaiKhoanPanel extends JPanel implements ActionListener, ListSelectionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	// Nhập liệu thông tin tài khoản
	private JTextField txtMaNV;
	private JPasswordField txtMatKhau;

	// Các nút thao tác dữ liệu
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnLuu;

	// Tìm kiếm bên phải (chỉ tìm theo mã)
	private JTextField txtTimMa;
	private JButton btnTimKiem;
	private JButton btnLamMoi;

	// Bảng hiển thị danh sách tài khoản
	private JTable tblTaiKhoan;
	private DefaultTableModel modelTaiKhoan;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSTaiKhoanBUS dsTaiKhoanBUS;

	public TaiKhoanPanel(DSTaiKhoanBUS dsTaiKhoanBUS) {
		this.dsTaiKhoanBUS = dsTaiKhoanBUS;
		khoiTaoGiaoDien();
		ganSuKien();
		capNhatBang();
	}

	// ==================== KHU VỰC KHỞI TẠO GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new BorderLayout());

		// Panel nội dung chính bọc ngoài
		JPanel pnContent = new JPanel(new BorderLayout(0, 18));
		pnContent.setBackground(UIUtils.COLOR_BACKGROUND);
		pnContent.setBorder(new EmptyBorder(18, 22, 22, 22));

		// -------------------------------------------------------------
		// 1. KHỐI PHÍA TRÊN: CHIA 2 PHẦN (THÔNG TIN BÊN TRÁI, TÌM KIẾM BÊN PHẢI)
		// -------------------------------------------------------------
		JPanel pnTopSection = new JPanel(new GridBagLayout());
		pnTopSection.setOpaque(false);
		GridBagConstraints gbcTop = new GridBagConstraints();
		gbcTop.fill = GridBagConstraints.BOTH;
		gbcTop.gridy = 0;
		gbcTop.weighty = 1.0;

		// ==================== A. KHU VỰC THÔNG TIN TÀI KHOẢN (BÊN TRÁI) ====================
		JPanel pnThongTin = UIUtils.createCardPanel(UIUtils.COLOR_CARD_SURFACE, 16, new Insets(16, 20, 18, 20));
		pnThongTin.setLayout(new BoxLayout(pnThongTin, BoxLayout.Y_AXIS));

		// Tiêu đề phần thông tin
		JPanel pnTitleThongTin = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnTitleThongTin.setOpaque(false);
		JLabel lbTitleThongTin = UIUtils.createTitlePanel("THÔNG TIN TÀI KHOẢN");
		pnTitleThongTin.add(lbTitleThongTin);

		// 2 ô nhập liệu: Mã nhân viên và Mật khẩu (đặt ngang song song nhau)
		JPanel pnHaiCotInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnHaiCotInput.setOpaque(false);

		txtMaNV = UIUtils.createTextField(175, 34);
		txtMatKhau = UIUtils.createPasswordField(175, 34);

		JPanel dongMaNV = UIUtils.createInputRow("Mã nhân viên:", txtMaNV, UIUtils.FONT_LABEL_BOLD, 98);
		JPanel dongMatKhau = UIUtils.createInputRow("Mật khẩu:", txtMatKhau, UIUtils.FONT_LABEL_BOLD, 76);

		pnHaiCotInput.add(dongMaNV);
		pnHaiCotInput.add(Box.createHorizontalStrut(20));
		pnHaiCotInput.add(dongMatKhau);

		// Hàng chứa 4 nút bấm ngang hàng: Thêm, Xóa, Sửa, Lưu
		JPanel pnHangNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnHangNut.setOpaque(false);

		btnThem = UIUtils.createButton("Thêm", UIUtils.COLOR_BTN_THEM, 78, 34);
		btnXoa  = UIUtils.createButton("Xóa", UIUtils.COLOR_BTN_XOA, 78, 34);
		btnSua  = UIUtils.createButton("Sửa", UIUtils.COLOR_BTN_SUA, 78, 34);
		btnLuu  = UIUtils.createButton("Lưu", UIUtils.COLOR_BTN_LUU, 78, 34);

		pnHangNut.add(btnThem);
		pnHangNut.add(Box.createHorizontalStrut(14));
		pnHangNut.add(btnXoa);
		pnHangNut.add(Box.createHorizontalStrut(14));
		pnHangNut.add(btnSua);
		pnHangNut.add(Box.createHorizontalStrut(14));
		pnHangNut.add(btnLuu);

		pnThongTin.add(pnTitleThongTin);
		pnThongTin.add(Box.createVerticalStrut(16));
		pnThongTin.add(pnHaiCotInput);
		pnThongTin.add(Box.createVerticalStrut(20));
		pnThongTin.add(pnHangNut);

		// ==================== B. KHU VỰC TÌM KIẾM (BÊN PHẢI) ====================
		JPanel pnTimKiem = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 16, new Insets(16, 20, 18, 20));
		pnTimKiem.setLayout(new BoxLayout(pnTimKiem, BoxLayout.Y_AXIS));

		// Tiêu đề phần tìm kiếm
		JPanel pnTitleSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnTitleSearch.setOpaque(false);
		JLabel lbTitleTimKiem = UIUtils.createSearchTitle("TÌM KIẾM TÀI KHOẢN");
		pnTitleSearch.add(lbTitleTimKiem);

		// Ô nhập mã cần tìm
		txtTimMa = UIUtils.createTextField(190, 34);

		// Nhóm nút tìm kiếm và làm mới trên cùng 1 hàng ngang
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

		pnTimKiem.add(pnTitleSearch);
		pnTimKiem.add(Box.createVerticalStrut(16));
		pnTimKiem.add(UIUtils.createInputRow("Nhập mã cần tìm:", txtTimMa, UIUtils.FONT_LABEL_BOLD, 118));
		pnTimKiem.add(Box.createVerticalStrut(20));
		pnTimKiem.add(pnBtnSearchWrap);

		gbcTop.gridx = 0;
		gbcTop.weightx = 0.58;
		gbcTop.insets = new Insets(0, 0, 0, 16);
		pnTopSection.add(pnThongTin, gbcTop);

		gbcTop.gridx = 1;
		gbcTop.weightx = 0.42;
		gbcTop.insets = new Insets(0, 0, 0, 0);
		pnTopSection.add(pnTimKiem, gbcTop);

		pnContent.add(pnTopSection, BorderLayout.NORTH);

		// -------------------------------------------------------------
		// 2. KHỐI PHÍA DƯỚI: BẢNG DANH SÁCH TÀI KHOẢN
		// -------------------------------------------------------------
		String[] tenCot = {"Mã đăng nhập", "Mật khẩu"};
		modelTaiKhoan = new DefaultTableModel(tenCot, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblTaiKhoan = new JTable(modelTaiKhoan);
		JScrollPane scrollBang = UIUtils.createTableScrollPane(tblTaiKhoan);
		UIUtils.centerTableCells(tblTaiKhoan, 0, 1);

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

		tblTaiKhoan.getSelectionModel().addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object nguon = e.getSource();

		if (nguon == btnThem) {
			themTaiKhoan();
		} else if (nguon == btnSua) {
			suaTaiKhoan();
		} else if (nguon == btnXoa) {
			xoaTaiKhoan();
		} else if (nguon == btnLuu) {
			luuDuLieu();
		} else if (nguon == btnTimKiem || nguon == txtTimMa) {
			capNhatBang();
		} else if (nguon == btnLamMoi) {
			lamMoi();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		int dong = tblTaiKhoan.getSelectedRow();
		if (dong < 0) {
			return;
		}

		txtMaNV.setText(String.valueOf(modelTaiKhoan.getValueAt(dong, 0)));
		txtMaNV.setEditable(false);
		txtMatKhau.setText(String.valueOf(modelTaiKhoan.getValueAt(dong, 1)));
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp lại bảng tài khoản theo mã đang tìm. */
	public void capNhatBang() {
		modelTaiKhoan.setRowCount(0);

		String maTim = txtTimMa.getText().trim();
		if (maTim.isEmpty()) {
			for (TaiKhoanDTO taiKhoan : dsTaiKhoanBUS.layDanhSach()) {
				modelTaiKhoan.addRow(new Object[]{
					taiKhoan.getMaNV().getMaNV(),
					taiKhoan.getMatKhau()
				});
			}
		} else {
			TaiKhoanDTO taiKhoan = dsTaiKhoanBUS.timTheoMa(maTim);
			if (taiKhoan == null) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản.",
						"Thông báo", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			modelTaiKhoan.addRow(new Object[]{
				taiKhoan.getMaNV().getMaNV(),
				taiKhoan.getMatKhau()
			});
		}
	}

	private void themTaiKhoan() {
		String maNV = txtMaNV.getText().trim();
		if (maNV.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên.",
					"Không thể thêm tài khoản", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!maNV.matches(Validator.MA_NHAN_VIEN)) {
			JOptionPane.showMessageDialog(this,
					"Mã nhân viên phải có đúng 4 ký tự, dạng nvXX hoặc qlXX (ví dụ nv01).",
					"Không thể thêm tài khoản", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String matKhau = new String(txtMatKhau.getPassword()).trim();
		if (!matKhau.isEmpty() && !matKhau.matches(Validator.MAT_KHAU)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự.",
					"Không thể thêm tài khoản", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String loi = dsTaiKhoanBUS.themTaiKhoan(maNV, matKhau);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể thêm tài khoản", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã thêm tài khoản.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		xoaForm();
		capNhatBang();
	}

	private void suaTaiKhoan() {
		String maNV = layMaDangChon();
		if (maNV.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa.",
					"Không thể sửa tài khoản", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String matKhau = new String(txtMatKhau.getPassword()).trim();
		if (!matKhau.matches(Validator.MAT_KHAU)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự.",
					"Không thể sửa tài khoản", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String loi = dsTaiKhoanBUS.doiMatKhau(maNV, matKhau);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể sửa tài khoản", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã cập nhật mật khẩu.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		capNhatBang();
	}

	private void xoaTaiKhoan() {
		String maNV = layMaDangChon();
		if (maNV.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập mã tài khoản cần xóa.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int luaChon = JOptionPane.showConfirmDialog(this, "Xóa tài khoản \"" + maNV + "\"?",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}

		if (!dsTaiKhoanBUS.xoaTaiKhoan(maNV)) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản cần xóa.",
					"Không thể xóa tài khoản", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã xóa tài khoản.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		xoaForm();
		capNhatBang();
	}

	private void luuDuLieu() {
		if (dsTaiKhoanBUS.luuVaoCSDL()) {
			JOptionPane.showMessageDialog(this, "Đã lưu danh sách tài khoản.",
					"Thành công", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Lưu dữ liệu thất bại.",
					"Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String layMaDangChon() {
		String maNV = txtMaNV.getText().trim();
		if (!maNV.isEmpty()) {
			return maNV;
		}

		int dong = tblTaiKhoan.getSelectedRow();
		if (dong < 0) {
			return "";
		}
		return String.valueOf(modelTaiKhoan.getValueAt(dong, 0));
	}

	private void lamMoi() {
		txtTimMa.setText("");
		xoaForm();
		capNhatBang();
	}

	private void xoaForm() {
		txtMaNV.setEditable(true);
		txtMaNV.setText("");
		txtMatKhau.setText("");
		tblTaiKhoan.clearSelection();
		txtMaNV.requestFocusInWindow();
	}
}
