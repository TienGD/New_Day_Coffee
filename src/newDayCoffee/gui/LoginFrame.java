package newDayCoffee.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import newDayCoffee.bus.NhanVienBUS;
import newDayCoffee.bus.DSTaiKhoanBUS;

/**
 * Màn hình đăng nhập - cửa ngõ đầu tiên của hệ thống.
 *
 * LoginFrame tạo TaiKhoanBUS và NhanVienBUS, sau đó truyền lại cho MainFrame
 * để toàn bộ ứng dụng dùng chung một nguồn dữ liệu (không dùng singleton).
 */
public class LoginFrame extends JFrame implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbHeader;
	private JTextField txtMa;
	private JPasswordField passMatKhau;
	private JButton btnDangNhap;
	private String TaiKhoanNhap = "Mã Nhân Viên (vd:nv00)";
	private String MatKhauNhap = "Mật khẩu";

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSTaiKhoanBUS taiKhoanBUS;
	private final NhanVienBUS nhanVienBUS;

	/** Khởi động ứng dụng: tạo dữ liệu dùng chung rồi mở màn hình đăng nhập. */
	public LoginFrame() {
		this(new DSTaiKhoanBUS(), null);
	}

	public LoginFrame(DSTaiKhoanBUS taiKhoanBUS, NhanVienBUS nhanVienBUS) {
		this.taiKhoanBUS = taiKhoanBUS;
		this.nhanVienBUS = (nhanVienBUS == null) ? new NhanVienBUS(taiKhoanBUS) : nhanVienBUS;

		// XÓA DỮ LIỆU MẪU: xóa dòng dưới đây và phương thức taoDuLieuMau() trong NhanVienBUS.
		nhanVienBUS.taoDuLieuMau();

		khoiTaoGiaoDien();
		ganSuKien();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setTitle("Đăng nhập");
		setSize(500, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setBackground(new Color(245, 235, 224));

		// ĐIỀN THÔNG TIN
		JPanel pnMain = new JPanel(new GridBagLayout());
		pnMain.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 0, 10, 0);

		// TIÊU ĐỀ CHÍNH
		lbHeader = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
		Font font = new Font("Arial", Font.BOLD, 25);
		lbHeader.setFont(font);
		lbHeader.setForeground(new Color(139, 69, 19));
		gbc.gridx = 0;
		gbc.gridy = 0;
		pnMain.add(lbHeader, gbc);
		Font fontNhap = new Font("Arial", Font.BOLD, 13);

		// 1. Ô Mã
		txtMa = new JTextField() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (getText().isEmpty()) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					g2.setColor(Color.GRAY);
					g2.setFont(fontNhap);

					Insets insets = getInsets();
					int textY = (getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent();
					g2.drawString(TaiKhoanNhap, insets.left, textY);
					g2.dispose();
				}
			}
		};
		txtMa.setPreferredSize(new Dimension(300, 36));
		gbc.gridx = 0;
		gbc.gridy = 1;
		pnMain.add(txtMa, gbc);

		// 2. Ô Mật khẩu
		passMatKhau = new JPasswordField() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (getPassword().length == 0) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					g2.setColor(Color.GRAY);
					g2.setFont(fontNhap);

					Insets insets = getInsets();
					int textY = (getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent();
					g2.drawString(MatKhauNhap, insets.left, textY);
					g2.dispose();
				}
			}
		};
		passMatKhau.setPreferredSize(new Dimension(300, 36));
		gbc.gridx = 0;
		gbc.gridy = 2;
		pnMain.add(passMatKhau, gbc);

		// 3. Nút Đăng nhập
		btnDangNhap = new JButton("Đăng nhập");
		btnDangNhap.setBackground(new Color(139, 69, 19));
		btnDangNhap.setForeground(Color.white);
		btnDangNhap.setPreferredSize(new Dimension(300, 36));
		btnDangNhap.setFocusPainted(false);
		gbc.gridx = 0;
		gbc.gridy = 3;
		pnMain.add(btnDangNhap, gbc);

		this.add(pnMain, BorderLayout.CENTER);
	}

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnDangNhap.addActionListener(this);
		// Nhấn Enter ở ô mã hoặc ô mật khẩu cũng đăng nhập
		txtMa.addActionListener(this);
		passMatKhau.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		xuLyDangNhap();
	}

	// ==================== KHU VỰC XỬ LÝ ĐĂNG NHẬP ====================
	private void xuLyDangNhap() {
		String maNV = txtMa.getText();
		String matKhau = new String(passMatKhau.getPassword());

		// NhanVienBUS kiểm tra qua Validator, TaiKhoanBUS và lưu phiên vào Auth
		String loi = nhanVienBUS.dangNhap(maNV, matKhau);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
			passMatKhau.setText("");
			passMatKhau.requestFocusInWindow();
			return;
		}

		// Mở màn hình chính và đóng màn hình đăng nhập
		MainFrame mainFrame = new MainFrame(taiKhoanBUS, nhanVienBUS);
		mainFrame.setVisible(true);
		this.dispose();
	}

	// ==================== KHU VỰC KHỞI CHẠY ====================
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}
}
