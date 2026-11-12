package newDayCoffee.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import newDayCoffee.bus.DSTaiKhoanBUS;
import newDayCoffee.bus.DSNhanVienBUS;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.dto.TaiKhoanDTO;
import newDayCoffee.utils.UIUtils;
import newDayCoffee.utils.Validator;

public class LoginFrame extends JFrame implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbHeader;
	private JTextField txtMa;
	private JPasswordField passMatKhau;
	private JButton btnDangNhap;
	private JLabel lblQuenMatKhau;
	private JLabel lblShowHidePass;
	private boolean isPasswordVisible = false;
	private String TaiKhoanNhap = "Tài khoản";
	private String MatKhauNhap = "Mật khẩu";

	// Thành phần giao diện Panel Quên mật khẩu
	private CardLayout cardLayout;
	private JPanel pnCardContainer;
	private JPanel quenMatKhauPanel;
	private JTextField txtEmailQuenMK;
	private JPasswordField passMatKhauMoi;
	private JButton btnDatLaiMatKhau;
	private JButton btnQuayLaiDangNhap;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSTaiKhoanBUS taiKhoanBUS;
	private final DSNhanVienBUS DSNhanVienBUS;

	public LoginFrame() {
		this(new DSTaiKhoanBUS(), null);
	}

	public LoginFrame(DSTaiKhoanBUS taiKhoanBUS, DSNhanVienBUS DSNhanVienBUS) {
		this.taiKhoanBUS = taiKhoanBUS;
		this.DSNhanVienBUS = (DSNhanVienBUS == null) ? new DSNhanVienBUS(taiKhoanBUS) : DSNhanVienBUS;

		// Tạo tài khoản mẫu ql01 / nv01 để đăng nhập thử
		taoDuLieuMau();

		khoiTaoGiaoDien();
		themSuKienChoCacNut();
	}

	/** Tạo DTO mẫu tại giao diện rồi chuyển cho BUS lưu trữ. */
	private void taoDuLieuMau() {
		NhanVienDTO quanLy = new NhanVienDTO("ql01", "Nguyễn Quản Lý",
				LocalDate.of(1990, 1, 1), "0901234567", "quanly@newdaycoffee.vn",
				"Quản lí", "Nam", "");
		NhanVienDTO thuNgan = new NhanVienDTO("nv01", "Trần Thu Ngân",
				LocalDate.of(2000, 1, 1), "0901234568", "thungan@newdaycoffee.vn",
				"Thu ngân", "Nữ", "");

		TaiKhoanDTO taiKhoanQuanLy = new TaiKhoanDTO(quanLy, "123456");
		TaiKhoanDTO taiKhoanThuNgan = new TaiKhoanDTO(thuNgan, "123456");
		DSNhanVienBUS.napDuLieuMau(quanLy, thuNgan, taiKhoanQuanLy, taiKhoanThuNgan);
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setTitle("Đăng nhập");
		setSize(540, 460);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel pnRoot = new JPanel(new GridBagLayout());
		pnRoot.setBackground(UIUtils.COLOR_SIDEBAR);
		setContentPane(pnRoot);

		// Thẻ Card Đăng nhập nổi bật ở trung tâm (tái sử dụng từ UIUtils)
		JPanel cardLogin = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 24, new Insets(26, 32, 24, 32));
		cardLogin.setLayout(new BorderLayout());
		cardLogin.setPreferredSize(new Dimension(410, 365));

		Box mainBox = Box.createVerticalBox();
		mainBox.setOpaque(false);
		mainBox.add(Box.createVerticalGlue());

		// Biểu tượng tách cà phê phía trên tiêu đề
		Box pnIcon = Box.createHorizontalBox();
		JPanel logoEmblem = UIUtils.taoLogoTron(46, UIUtils.COLOR_HEADER);
		logoEmblem.setMaximumSize(new Dimension(46, 46));
		pnIcon.add(Box.createHorizontalGlue());
		pnIcon.add(logoEmblem);
		pnIcon.add(Box.createHorizontalGlue());
		mainBox.add(pnIcon);
		mainBox.add(Box.createRigidArea(new Dimension(0, 10)));

		// TIÊU ĐỀ CHÍNH
		Box pnHeader = Box.createHorizontalBox();
		lbHeader = new JLabel("Đăng nhập", JLabel.CENTER);
		lbHeader.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 26));
		lbHeader.setForeground(UIUtils.COLOR_ACCENT_GOLD);

		pnHeader.add(Box.createHorizontalGlue());
		pnHeader.add(lbHeader);
		pnHeader.add(Box.createHorizontalGlue());
		mainBox.add(pnHeader);

		mainBox.add(Box.createRigidArea(new Dimension(0, 22)));

		Font fontNhap = new Font(UIUtils.FONT_FAMILY, Font.PLAIN, 14);
		Dimension inputSize = new Dimension(330, 42);

		// 1. Ô Tài khoản (bo tròn 15px, tái sử dụng từ UIUtils)
		Box pnTaiKhoan = Box.createHorizontalBox();
		txtMa = UIUtils.createRoundedTextFieldWithHint(15, TaiKhoanNhap, fontNhap, inputSize);

		pnTaiKhoan.add(Box.createHorizontalGlue());
		pnTaiKhoan.add(txtMa);
		pnTaiKhoan.add(Box.createHorizontalGlue());
		mainBox.add(pnTaiKhoan);

		mainBox.add(Box.createRigidArea(new Dimension(0, 14)));

		// 2. Ô Mật khẩu kèm Icon ẩn/hiện đặt trong Container bo tròn (tái sử dụng từ UIUtils)
		Box pnMatKhau = Box.createHorizontalBox();
		JPanel panelPassContainer = UIUtils.createRoundedContainer(15, UIUtils.COLOR_BG_INPUT, inputSize);

		passMatKhau = UIUtils.createRoundedPasswordFieldWithHint(15, MatKhauNhap, fontNhap);
		passMatKhau.setBorder(new EmptyBorder(0, 8, 0, 8));

		lblShowHidePass = new JLabel(" 👁 ");
		lblShowHidePass.setForeground(new Color(120, 95, 75));
		lblShowHidePass.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPanel panelRightIcon = new JPanel(new BorderLayout());
		panelRightIcon.setOpaque(false);
		panelRightIcon.add(lblShowHidePass, BorderLayout.CENTER);

		panelPassContainer.add(passMatKhau, BorderLayout.CENTER);
		panelPassContainer.add(panelRightIcon, BorderLayout.EAST);

		pnMatKhau.add(Box.createHorizontalGlue());
		pnMatKhau.add(panelPassContainer);
		pnMatKhau.add(Box.createHorizontalGlue());
		mainBox.add(pnMatKhau);

		mainBox.add(Box.createRigidArea(new Dimension(0, 20)));

		// 3. Nút Đăng nhập
		Box pnBtnDangNhap = Box.createHorizontalBox();
		btnDangNhap = UIUtils.taoNutPhang(
			"Đăng nhập",
			new Color(186, 118, 58),
			Color.WHITE,
			new Font(UIUtils.FONT_FAMILY, Font.BOLD, 15),
			inputSize
		);
		btnDangNhap.setMaximumSize(inputSize);

		pnBtnDangNhap.add(Box.createHorizontalGlue());
		pnBtnDangNhap.add(btnDangNhap);
		pnBtnDangNhap.add(Box.createHorizontalGlue());
		mainBox.add(pnBtnDangNhap);

		mainBox.add(Box.createRigidArea(new Dimension(0, 14)));

		// 4. Dòng "Quên mật khẩu?"
		Box pnQuenMK = Box.createHorizontalBox();
		lblQuenMatKhau = new JLabel("Quên mật khẩu?", JLabel.CENTER);
		lblQuenMatKhau.setFont(UIUtils.FONT_LABEL);
		lblQuenMatKhau.setForeground(new Color(235, 215, 195));
		lblQuenMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));

		pnQuenMK.add(Box.createHorizontalGlue());
		pnQuenMK.add(lblQuenMatKhau);
		pnQuenMK.add(Box.createHorizontalGlue());
		mainBox.add(pnQuenMK);

		mainBox.add(Box.createVerticalGlue());

		cardLogin.add(mainBox, BorderLayout.CENTER);

		// Khởi tạo CardLayout chuyển đổi giữa Đăng nhập và Panel Quên mật khẩu
		cardLayout = new CardLayout();
		pnCardContainer = new JPanel(cardLayout);
		pnCardContainer.setOpaque(false);

		quenMatKhauPanel = taoPanelQuenMatKhau();

		pnCardContainer.add(cardLogin, "DANG_NHAP");
		pnCardContainer.add(quenMatKhauPanel, "QUEN_MAT_KHAU");

		pnRoot.add(pnCardContainer);
	}

	private JPanel taoPanelQuenMatKhau() {
		JPanel cardQuenMK = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 24, new Insets(24, 32, 22, 32));
		cardQuenMK.setLayout(new BorderLayout());
		cardQuenMK.setPreferredSize(new Dimension(410, 365));

		Box mainBox = Box.createVerticalBox();
		mainBox.setOpaque(false);
		mainBox.add(Box.createVerticalGlue());

		// Biểu tượng tách cà phê đồng bộ với màn hình Đăng nhập
		Box pnIcon = Box.createHorizontalBox();
		JPanel logoEmblem = UIUtils.taoLogoTron(46, UIUtils.COLOR_HEADER);
		logoEmblem.setMaximumSize(new Dimension(46, 46));
		pnIcon.add(Box.createHorizontalGlue());
		pnIcon.add(logoEmblem);
		pnIcon.add(Box.createHorizontalGlue());
		mainBox.add(pnIcon);
		mainBox.add(Box.createRigidArea(new Dimension(0, 10)));

		// TIÊU ĐỀ QUÊN MẬT KHẨU
		Box pnHeader = Box.createHorizontalBox();
		JLabel lbTitleQuenMK = new JLabel("Quên mật khẩu", JLabel.CENTER);
		lbTitleQuenMK.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 25));
		lbTitleQuenMK.setForeground(UIUtils.COLOR_ACCENT_GOLD);

		pnHeader.add(Box.createHorizontalGlue());
		pnHeader.add(lbTitleQuenMK);
		pnHeader.add(Box.createHorizontalGlue());
		mainBox.add(pnHeader);

		mainBox.add(Box.createRigidArea(new Dimension(0, 20)));

		Font fontNhap = new Font(UIUtils.FONT_FAMILY, Font.PLAIN, 14);
		Dimension inputSize = new Dimension(330, 42);

		// 1. Ô Nhập Email nhân viên
		Box pnEmail = Box.createHorizontalBox();
		txtEmailQuenMK = UIUtils.createRoundedTextFieldWithHint(15, "Nhập email nhân viên", fontNhap, inputSize);

		pnEmail.add(Box.createHorizontalGlue());
		pnEmail.add(txtEmailQuenMK);
		pnEmail.add(Box.createHorizontalGlue());
		mainBox.add(pnEmail);

		mainBox.add(Box.createRigidArea(new Dimension(0, 14)));

		// 2. Ô Nhập Mật khẩu mới
		Box pnMatKhauMoi = Box.createHorizontalBox();
		JPanel panelPassContainer = UIUtils.createRoundedContainer(15, UIUtils.COLOR_BG_INPUT, inputSize);

		passMatKhauMoi = UIUtils.createRoundedPasswordFieldWithHint(15, "Nhập mật khẩu mới", fontNhap);
		passMatKhauMoi.setBorder(new EmptyBorder(0, 8, 0, 8));

		JLabel lblEyeQuenMK = new JLabel(" 👁 ");
		lblEyeQuenMK.setForeground(new Color(120, 95, 75));
		lblEyeQuenMK.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPanel panelRightIcon = new JPanel(new BorderLayout());
		panelRightIcon.setOpaque(false);
		panelRightIcon.add(lblEyeQuenMK, BorderLayout.CENTER);

		panelPassContainer.add(passMatKhauMoi, BorderLayout.CENTER);
		panelPassContainer.add(panelRightIcon, BorderLayout.EAST);

		pnMatKhauMoi.add(Box.createHorizontalGlue());
		pnMatKhauMoi.add(panelPassContainer);
		pnMatKhauMoi.add(Box.createHorizontalGlue());
		mainBox.add(pnMatKhauMoi);

		mainBox.add(Box.createRigidArea(new Dimension(0, 20)));

		// 3. Nút Đặt lại mật khẩu
		Box pnBtnXacNhan = Box.createHorizontalBox();
		btnDatLaiMatKhau = UIUtils.taoNutPhang(
			"Đặt lại mật khẩu",
			new Color(186, 118, 58),
			Color.WHITE,
			new Font(UIUtils.FONT_FAMILY, Font.BOLD, 15),
			inputSize
		);
		btnDatLaiMatKhau.setMaximumSize(inputSize);

		pnBtnXacNhan.add(Box.createHorizontalGlue());
		pnBtnXacNhan.add(btnDatLaiMatKhau);
		pnBtnXacNhan.add(Box.createHorizontalGlue());
		mainBox.add(pnBtnXacNhan);

		mainBox.add(Box.createRigidArea(new Dimension(0, 12)));

		// 4. Nút Quay lại đăng nhập
		Box pnQuayLai = Box.createHorizontalBox();
		btnQuayLaiDangNhap = new JButton("← Quay lại đăng nhập");
		btnQuayLaiDangNhap.setFont(UIUtils.FONT_LABEL_BOLD);
		btnQuayLaiDangNhap.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		btnQuayLaiDangNhap.setOpaque(false);
		btnQuayLaiDangNhap.setContentAreaFilled(false);
		btnQuayLaiDangNhap.setBorderPainted(false);
		btnQuayLaiDangNhap.setFocusPainted(false);
		btnQuayLaiDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));

		pnQuayLai.add(Box.createHorizontalGlue());
		pnQuayLai.add(btnQuayLaiDangNhap);
		pnQuayLai.add(Box.createHorizontalGlue());
		mainBox.add(pnQuayLai);

		mainBox.add(Box.createVerticalGlue());

		cardQuenMK.add(mainBox, BorderLayout.CENTER);
		return cardQuenMK;
	}

	private void themSuKienChoCacNut() {
		btnDangNhap.addActionListener(this);
		btnDatLaiMatKhau.addActionListener(this);
		btnQuayLaiDangNhap.addActionListener(this);
		txtMa.addActionListener(this);
		passMatKhau.addActionListener(this);

		lblQuenMatKhau.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				hienThiManHinhQuenMatKhau();
			}
		});
	}

	public void hienThiManHinhQuenMatKhau() {
		setTitle("Quên mật khẩu");
		txtEmailQuenMK.setText("");
		passMatKhauMoi.setText("");
		cardLayout.show(pnCardContainer, "QUEN_MAT_KHAU");
	}

	public void hienThiManHinhDangNhap() {
		setTitle("Đăng nhập");
		cardLayout.show(pnCardContainer, "DANG_NHAP");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnDangNhap) || o.equals(txtMa) || o.equals(passMatKhau)) {
			xuLyDangNhap();
		} else if (o.equals(btnQuayLaiDangNhap)) {
			hienThiManHinhDangNhap();
		} else if (o.equals(btnDatLaiMatKhau)) {
			xuLyDatLaiMatKhau();
		}
	}

	// ==================== KHU VỰC XỬ LÝ ĐĂNG NHẬP ====================
	private void xuLyDangNhap() {
		String maNV = txtMa.getText().trim();
		String matKhau = new String(passMatKhau.getPassword()).trim();

		if (maNV.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên.",
					"Đăng nhập thất bại", JOptionPane.WARNING_MESSAGE);
			txtMa.requestFocusInWindow();
			return;
		}
		if (!maNV.matches(Validator.MA_NHAN_VIEN)) {
			JOptionPane.showMessageDialog(this,
					"Mã nhân viên phải có đúng 4 ký tự, dạng nvXX hoặc qlXX (ví dụ nv01).",
					"Đăng nhập thất bại", JOptionPane.WARNING_MESSAGE);
			txtMa.requestFocusInWindow();
			return;
		}
		if (matKhau.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu.",
					"Đăng nhập thất bại", JOptionPane.WARNING_MESSAGE);
			passMatKhau.requestFocusInWindow();
			return;
		}
		if (!matKhau.matches(Validator.MAT_KHAU)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự.",
					"Đăng nhập thất bại", JOptionPane.WARNING_MESSAGE);
			passMatKhau.setText("");
			passMatKhau.requestFocusInWindow();
			return;
		}

		String loi = DSNhanVienBUS.dangNhap(maNV, matKhau);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
			passMatKhau.setText("");
			passMatKhau.requestFocusInWindow();
			return;
		}

		MainFrame mainFrame = new MainFrame(taiKhoanBUS, DSNhanVienBUS);
		mainFrame.setVisible(true);
		this.dispose();
	}

	private void xuLyDatLaiMatKhau() {
		String email = txtEmailQuenMK.getText().trim();
		String matKhauMoi = new String(passMatKhauMoi.getPassword()).trim();

		if (email.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập email nhân viên.",
					"Đặt lại mật khẩu thất bại", JOptionPane.WARNING_MESSAGE);
			txtEmailQuenMK.requestFocusInWindow();
			return;
		}
		if (!email.matches(Validator.EMAIL)) {
			JOptionPane.showMessageDialog(this,
					"Email không đúng định dạng (ví dụ ten@gmail.com).",
					"Đặt lại mật khẩu thất bại", JOptionPane.WARNING_MESSAGE);
			txtEmailQuenMK.requestFocusInWindow();
			return;
		}
		if (matKhauMoi.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới.",
					"Đặt lại mật khẩu thất bại", JOptionPane.WARNING_MESSAGE);
			passMatKhauMoi.requestFocusInWindow();
			return;
		}
		if (!matKhauMoi.matches(Validator.MAT_KHAU)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 6 ký tự.",
					"Đặt lại mật khẩu thất bại", JOptionPane.WARNING_MESSAGE);
			passMatKhauMoi.requestFocusInWindow();
			return;
		}

		String loi = DSNhanVienBUS.datLaiMatKhau(email, matKhauMoi);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Đặt lại mật khẩu thất bại", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công.", "Thành công",
				JOptionPane.INFORMATION_MESSAGE);
		hienThiManHinhDangNhap();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LoginFrame().setVisible(true);
			}
		});
	}
}
