package newDayCoffee.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

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

	

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	public LoginFrame() {
		khoiTaoGiaoDien();
		themSuKienChoCacNut();
	}

	
	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setTitle("Đăng nhập");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.decode("#AC7F5E"));

		// Sử dụng BoxLayout theo trục dọc (VerticalBox) cho toàn bộ nội dung
		Box mainBox = Box.createVerticalBox();
		mainBox.setOpaque(false);
		
		mainBox.add(Box.createVerticalGlue());

		// TIÊU ĐỀ CHÍNH
		Box pnHeader = Box.createHorizontalBox();
		lbHeader = new JLabel("Đăng nhập", JLabel.CENTER);
		lbHeader.setFont(new Font("Arial", Font.BOLD, 26));
		lbHeader.setForeground(Color.decode("#682D03"));
		
		pnHeader.add(Box.createHorizontalGlue());
		pnHeader.add(lbHeader);
		pnHeader.add(Box.createHorizontalGlue());
		mainBox.add(pnHeader);

		mainBox.add(Box.createRigidArea(new Dimension(0, 25)));

		Font fontNhap = new Font("Arial", Font.PLAIN, 14);

		// 1. Ô Tài khoản (Sử dụng RoundedJTextField với độ bo tròn 15 pixel)
		Box pnTaiKhoan = Box.createHorizontalBox();
		txtMa = new RoundedJTextField(15) {
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
		txtMa.setMaximumSize(new Dimension(320, 40));
		txtMa.setPreferredSize(new Dimension(320, 40));
		txtMa.setFont(fontNhap);
		txtMa.setBackground(Color.WHITE);
		
		pnTaiKhoan.add(Box.createHorizontalGlue());
		pnTaiKhoan.add(txtMa);
		pnTaiKhoan.add(Box.createHorizontalGlue());
		mainBox.add(pnTaiKhoan);

		mainBox.add(Box.createRigidArea(new Dimension(0, 15)));

		// 2. Ô Mật khẩu kèm Icon ẩn/hiện (mắt) đặt trong Container bo tròn
		Box pnMatKhau = Box.createHorizontalBox();
		
		// Panel chứa mật khẩu và icon mắt cũng được tùy biến vẽ bo tròn góc
		JPanel panelPassContainer = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
				g2.dispose();
				super.paintComponent(g);
			}
		};
		panelPassContainer.setOpaque(false);
		panelPassContainer.setMaximumSize(new Dimension(320, 40));
		panelPassContainer.setPreferredSize(new Dimension(320, 40));
		panelPassContainer.setBackground(Color.WHITE);
		panelPassContainer.setBorder(new EmptyBorder(0, 5, 0, 5));

		passMatKhau = new RoundedJPasswordField(15) {
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
		passMatKhau.setFont(fontNhap);
		passMatKhau.setBorder(null);
		passMatKhau.setOpaque(false);

		// Icon mắt ẩn/hiện mật khẩu
		lblShowHidePass = new JLabel(" 👁 ");
		lblShowHidePass.setForeground(Color.GRAY);
		lblShowHidePass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		

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
		btnDangNhap = new JButton("Đăng nhập");
		btnDangNhap.setBackground(Color.decode("#682D03"));
		btnDangNhap.setForeground(Color.white);
		btnDangNhap.setMaximumSize(new Dimension(320, 40));
		btnDangNhap.setPreferredSize(new Dimension(320, 40));
		btnDangNhap.setFocusPainted(false);
		btnDangNhap.setFont(new Font("Arial", Font.BOLD, 14));
		
		pnBtnDangNhap.add(Box.createHorizontalGlue());
		pnBtnDangNhap.add(btnDangNhap);
		pnBtnDangNhap.add(Box.createHorizontalGlue());
		mainBox.add(pnBtnDangNhap);

		mainBox.add(Box.createRigidArea(new Dimension(0, 15)));

		// 4. Dòng "Quên mật khẩu?" dạng JLabel có sự kiện chuột
		Box pnQuenMK = Box.createHorizontalBox();
		lblQuenMatKhau = new JLabel("Quên mật khẩu?", JLabel.CENTER);
		lblQuenMatKhau.setFont(new Font("Arial", Font.PLAIN, 13));
		lblQuenMatKhau.setForeground(Color.WHITE);
		lblQuenMatKhau.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		pnQuenMK.add(Box.createHorizontalGlue());
		pnQuenMK.add(lblQuenMatKhau);
		pnQuenMK.add(Box.createHorizontalGlue());
		mainBox.add(pnQuenMK);

		mainBox.add(Box.createVerticalGlue());

		this.add(mainBox, BorderLayout.CENTER);
	}
	private void themSuKienChoCacNut() {
		btnDangNhap.addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o.equals(btnDangNhap)) {
			MainFrame mainFrame = new MainFrame();
			mainFrame.setVisible(true);
			
			// Đóng cửa sổ Login hiện tại
			this.dispose();
		}
		
	}

	
	// ==================== LỚP TÙY CHỈNH: Ô NHẬP LIỆU BO TRÒN GÓC ====================
		private static class RoundedJTextField extends JTextField {
			private int cornerRadius;

			public RoundedJTextField(int radius) {
				super();
				this.cornerRadius = radius;
				setOpaque(false); // Để vẽ nền bo tròn trong suốt phần thừa
				setBorder(new EmptyBorder(0, 10, 0, 10)); // Khoảng đệm lề trong cho chữ
			}

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Vẽ nền trắng bo tròn
				g2.setColor(getBackground());
				g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
				
				g2.dispose();
				super.paintComponent(g);
			}

			@Override
			protected void paintBorder(Graphics g) {
				// Không vẽ viền mặc định, có thể tùy biến vẽ viền tại đây nếu muốn
			}
		}

		private static class RoundedJPasswordField extends JPasswordField {
			private int cornerRadius;

			public RoundedJPasswordField(int radius) {
				super();
				this.cornerRadius = radius;
				setOpaque(false);
				setBorder(new EmptyBorder(0, 10, 0, 10));
			}

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setColor(getBackground());
				g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
				
				g2.dispose();
				super.paintComponent(g);
			}

			@Override
			protected void paintBorder(Graphics g) {
			}
		}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new LoginFrame().setVisible(true);
		});
	}


	
}