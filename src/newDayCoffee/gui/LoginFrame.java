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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import newDayCoffee.bus.NhanVienBUS;

public class LoginFrame extends JFrame {
	private JLabel lbHeader;
	private JTextField txtMa;
	private JPasswordField passMatKhau;
	private JButton btnDangNhap;
	private JLabel lbGoiY;
	private String TaiKhoanNhap = "Mã Nhân Viên (vd:nv00)";
	private String MatKhauNhap = "Mật khẩu";

	// BUS xử lý đăng nhập (kiểm tra dữ liệu + lưu phiên vào Auth)
	private final NhanVienBUS nhanVienBUS = NhanVienBUS.getInstance();

	public LoginFrame() {
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

		// 4. Gợi ý tài khoản mẫu (dùng khi chưa có CSDL)
		lbGoiY = new JLabel("Tài khoản mẫu: NV01 / 123456", JLabel.CENTER);
		lbGoiY.setFont(new Font("Arial", Font.ITALIC, 12));
		lbGoiY.setForeground(new Color(120, 90, 70));
		gbc.gridx = 0;
		gbc.gridy = 4;
		pnMain.add(lbGoiY, gbc);

		this.add(pnMain, BorderLayout.CENTER);

		// ==================== KHU VỰC GẮN SỰ KIỆN ====================
		btnDangNhap.addActionListener(e -> xuLyDangNhap());
		passMatKhau.addActionListener(e -> xuLyDangNhap());
		txtMa.addActionListener(e -> passMatKhau.requestFocusInWindow());
		getRootPane().setDefaultButton(btnDangNhap);
	}

	/**
	 * Xử lý sự kiện đăng nhập:
	 * 1. Lấy dữ liệu người dùng nhập.
	 * 2. Giao cho NhanVienBUS kiểm tra và đăng nhập.
	 * 3. Nếu thành công thì mở MainFrame, ngược lại hiển thị lỗi.
	 */
	private void xuLyDangNhap() {
		String maNV = txtMa.getText();
		String matKhau = new String(passMatKhau.getPassword());

		String loi = nhanVienBUS.dangNhap(maNV, matKhau);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Đăng nhập thất bại",
					JOptionPane.WARNING_MESSAGE);
			passMatKhau.requestFocusInWindow();
			passMatKhau.selectAll();
			return;
		}

		// Đăng nhập thành công: mở màn hình chính và đóng màn hình đăng nhập
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
		this.dispose();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}
}
