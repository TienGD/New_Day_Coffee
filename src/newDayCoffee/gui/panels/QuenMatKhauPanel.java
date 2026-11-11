package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import newDayCoffee.utils.UIUtils;
import newDayCoffee.utils.Validator;

/**
 * Panel Quên mật khẩu hiển thị ngay trên LoginFrame khi người dùng chọn "Quên mật khẩu?".
 * Cho phép nhân viên nhập Email và Mật khẩu mới với tông màu đồng bộ toàn hệ thống.
 */
public class QuenMatKhauPanel extends JPanel implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbHeader;
	private JTextField txtEmail;
	private JPasswordField passMatKhauMoi;
	private JLabel lblShowHidePass;
	private JButton btnXacNhan;
	private JButton btnQuayLai;

	private String EmailNhap = "Nhập email nhân viên";
	private String MatKhauMoiNhap = "Nhập mật khẩu mới";
	private ActionListener suKienQuayLai;

	public QuenMatKhauPanel(ActionListener suKienQuayLai) {
		this.suKienQuayLai = suKienQuayLai;
		khoiTaoGiaoDien();
		themSuKienChoCacNut();
	}

	public QuenMatKhauPanel() {
		this(null);
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new BorderLayout());
		setOpaque(false);

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

		// TIÊU ĐỀ CHÍNH
		Box pnHeader = Box.createHorizontalBox();
		lbHeader = new JLabel("Quên mật khẩu", JLabel.CENTER);
		lbHeader.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 25));
		lbHeader.setForeground(UIUtils.COLOR_ACCENT_GOLD);

		pnHeader.add(Box.createHorizontalGlue());
		pnHeader.add(lbHeader);
		pnHeader.add(Box.createHorizontalGlue());
		mainBox.add(pnHeader);

		mainBox.add(Box.createRigidArea(new Dimension(0, 20)));

		Font fontNhap = new Font(UIUtils.FONT_FAMILY, Font.PLAIN, 14);
		Dimension inputSize = new Dimension(330, 42);

		// 1. Ô Nhập Email nhân viên (bo tròn 15px, tái sử dụng từ UIUtils)
		Box pnEmail = Box.createHorizontalBox();
		txtEmail = UIUtils.createRoundedTextFieldWithHint(15, EmailNhap, fontNhap, inputSize);

		pnEmail.add(Box.createHorizontalGlue());
		pnEmail.add(txtEmail);
		pnEmail.add(Box.createHorizontalGlue());
		mainBox.add(pnEmail);

		mainBox.add(Box.createRigidArea(new Dimension(0, 14)));

		// 2. Ô Nhập Mật khẩu mới kèm Icon ẩn/hiện đặt trong Container bo tròn
		Box pnMatKhauMoi = Box.createHorizontalBox();
		JPanel panelPassContainer = UIUtils.createRoundedContainer(15, UIUtils.COLOR_BG_INPUT, inputSize);

		passMatKhauMoi = UIUtils.createRoundedPasswordFieldWithHint(15, MatKhauMoiNhap, fontNhap);
		passMatKhauMoi.setBorder(new EmptyBorder(0, 8, 0, 8));

		lblShowHidePass = new JLabel(" 👁 ");
		lblShowHidePass.setForeground(new Color(120, 95, 75));
		lblShowHidePass.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPanel panelRightIcon = new JPanel(new BorderLayout());
		panelRightIcon.setOpaque(false);
		panelRightIcon.add(lblShowHidePass, BorderLayout.CENTER);

		panelPassContainer.add(passMatKhauMoi, BorderLayout.CENTER);
		panelPassContainer.add(panelRightIcon, BorderLayout.EAST);

		pnMatKhauMoi.add(Box.createHorizontalGlue());
		pnMatKhauMoi.add(panelPassContainer);
		pnMatKhauMoi.add(Box.createHorizontalGlue());
		mainBox.add(pnMatKhauMoi);

		mainBox.add(Box.createRigidArea(new Dimension(0, 20)));

		// 3. Nút Cập nhật mật khẩu mới
		Box pnBtnXacNhan = Box.createHorizontalBox();
		btnXacNhan = UIUtils.taoNutPhang(
			"Đặt lại mật khẩu",
			new Color(186, 118, 58),
			Color.WHITE,
			new Font(UIUtils.FONT_FAMILY, Font.BOLD, 15),
			inputSize
		);
		btnXacNhan.setMaximumSize(inputSize);

		pnBtnXacNhan.add(Box.createHorizontalGlue());
		pnBtnXacNhan.add(btnXacNhan);
		pnBtnXacNhan.add(Box.createHorizontalGlue());
		mainBox.add(pnBtnXacNhan);

		mainBox.add(Box.createRigidArea(new Dimension(0, 12)));

		// 4. Nút Quay lại đăng nhập
		Box pnQuayLai = Box.createHorizontalBox();
		btnQuayLai = new JButton("← Quay lại đăng nhập");
		btnQuayLai.setFont(UIUtils.FONT_LABEL_BOLD);
		btnQuayLai.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		btnQuayLai.setOpaque(false);
		btnQuayLai.setContentAreaFilled(false);
		btnQuayLai.setBorderPainted(false);
		btnQuayLai.setFocusPainted(false);
		btnQuayLai.setCursor(new Cursor(Cursor.HAND_CURSOR));

		pnQuayLai.add(Box.createHorizontalGlue());
		pnQuayLai.add(btnQuayLai);
		pnQuayLai.add(Box.createHorizontalGlue());
		mainBox.add(pnQuayLai);

		mainBox.add(Box.createVerticalGlue());

		cardQuenMK.add(mainBox, BorderLayout.CENTER);
		add(cardQuenMK, BorderLayout.CENTER);
	}

	private void themSuKienChoCacNut() {
		btnXacNhan.addActionListener(this);
		btnQuayLai.addActionListener(this);
	}

	public void lamMoiForm() {
		txtEmail.setText("");
		passMatKhauMoi.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnXacNhan)) {
			String email = Validator.chuanHoa(txtEmail.getText());
			String matKhauMoi = new String(passMatKhauMoi.getPassword()).trim();

			if (email.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập email nhân viên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
				txtEmail.requestFocus();
				return;
			}
			String loiEmail = Validator.kiemTraEmail(email);
			if (loiEmail != null) {
				JOptionPane.showMessageDialog(this, loiEmail, "Thông báo", JOptionPane.WARNING_MESSAGE);
				txtEmail.requestFocus();
				return;
			}
			if (matKhauMoi.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới.", "Thông báo", JOptionPane.WARNING_MESSAGE);
				passMatKhauMoi.requestFocus();
				return;
			}
			if (matKhauMoi.length() < 6) {
				JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 6 ký tự.", "Thông báo", JOptionPane.WARNING_MESSAGE);
				passMatKhauMoi.requestFocus();
				return;
			}

			JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công cho email: " + email, "Thành công", JOptionPane.INFORMATION_MESSAGE);
			lamMoiForm();
			if (suKienQuayLai != null) {
				suKienQuayLai.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "QUAY_LAI"));
			}
		} else if (o.equals(btnQuayLai)) {
			lamMoiForm();
			if (suKienQuayLai != null) {
				suKienQuayLai.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "QUAY_LAI"));
			}
		}
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public JPasswordField getPassMatKhauMoi() {
		return passMatKhauMoi;
	}

	public JButton getBtnXacNhan() {
		return btnXacNhan;
	}

	public JButton getBtnQuayLai() {
		return btnQuayLai;
	}
}
