package newDayCoffee.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import newDayCoffee.bus.NhanVienBUS;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.gui.components.CustomButton;
import newDayCoffee.gui.panels.BanHangPOSPanel;
import newDayCoffee.gui.panels.QuanLyNhanVienPanel;
import newDayCoffee.gui.panels.QuanLyThucDonPanel;
import newDayCoffee.gui.panels.TongQuanPanel;
import newDayCoffee.utils.Auth;

public class MainFrame extends JFrame {
	private JLabel lbSysName;
	private JLabel lbShift;
	private JLabel lbNguoiDung;
	private JButton btnDangXuat;
	private JPanel pnContainerCard;
	private CardLayout cardLayout;
	private List<JLabel> listMenuLabels = new ArrayList<>();
	private final NhanVienBUS nhanVienBUS = NhanVienBUS.getInstance();

	// 4 Panel chức năng độc lập
	private TongQuanPanel tongQuanPanel;
	private BanHangPOSPanel banHangPOSPanel;
	private QuanLyThucDonPanel quanLyThucDonPanel;
	private QuanLyNhanVienPanel quanLyNhanVienPanel;

	public MainFrame() {
		NhanVienDTO nguoiDangNhap = Auth.getCurrentUser();
		String tenNguoiDung = nguoiDangNhap == null ? "Khách" : nguoiDangNhap.getTenNV();
		setTitle("New Day Coffee - " + tenNguoiDung);
		// Giới hạn kích thước theo màn hình để không bị tràn khi Windows dùng tỉ lệ hiển thị > 100%
		java.awt.Dimension kichThuocManHinh = Toolkit.getDefaultToolkit().getScreenSize();
		int chieuRong = Math.min(1200, (int) (kichThuocManHinh.width * 0.9));
		int chieuCao = Math.min(650, (int) (kichThuocManHinh.height * 0.85));
		setSize(chieuRong, chieuCao);
		setLocationRelativeTo(null);
		// Mở rộng tối đa để tránh bị cắt khi Windows dùng tỉ lệ hiển thị > 100%
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(new Color(245, 235, 224));

		Font fontMenu = new Font("Arial", Font.BOLD, 19);
		Font fontThuong = new Font("Arial", Font.PLAIN, 14);

		setLayout(new BorderLayout());

		// 1. THANH MENU BÊN TRÁI (SIDEBAR)
		JPanel pnSidebar = new JPanel(new BorderLayout());
		pnSidebar.setPreferredSize(new Dimension(220, 0));
		pnSidebar.setBackground(new Color(193, 148, 112));
		pnSidebar.setBorder(new EmptyBorder(25, 20, 20, 20));

		// LOGO VÀ TÊN HỆ THỐNG
		JPanel pnLogo = new JPanel();
		pnLogo.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pnLogo.setOpaque(false);

		JPanel circleLogo = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(139, 69, 19));
				g2.fillOval(0, 0, 32, 32);
				g2.dispose();
			}
		};
		circleLogo.setPreferredSize(new Dimension(32, 32));
		circleLogo.setOpaque(false);

		lbSysName = new JLabel("Hệ thống quản lý CAFE");
		lbSysName.setFont(new Font("Arial", Font.BOLD, 13));
		lbSysName.setForeground(new Color(140, 30, 30));

		pnLogo.add(circleLogo);
		pnLogo.add(lbSysName);
		pnSidebar.add(pnLogo, BorderLayout.NORTH);

		// DANH SÁCH MENU
		Box pnMenu = Box.createVerticalBox();
		pnMenu.setBorder(new EmptyBorder(40, 5, 0, 0));

		String[] dsMenu = {
			"Tổng quan", "Bán hàng", "Thực đơn", "Nhân viên"
		};

		for (int i = 0; i < dsMenu.length; i++) {
			JLabel lbMenu = new JLabel(dsMenu[i]);
			lbMenu.setFont(fontMenu);
			if (i == 0) {
				lbMenu.setForeground(Color.BLACK);
			} else {
				lbMenu.setForeground(Color.WHITE);
			}
			lbMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
			listMenuLabels.add(lbMenu);
			final String menuItem = dsMenu[i];
			lbMenu.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					switchCard(menuItem);
				}
			});
			pnMenu.add(lbMenu);
			pnMenu.add(Box.createVerticalStrut(22));
		}
		pnSidebar.add(pnMenu, BorderLayout.CENTER);

		// KHU VỰC NGƯỜI DÙNG ĐANG ĐĂNG NHẬP + NÚT ĐĂNG XUẤT
		JPanel pnNguoiDung = new JPanel();
		pnNguoiDung.setLayout(new javax.swing.BoxLayout(pnNguoiDung, javax.swing.BoxLayout.Y_AXIS));
		pnNguoiDung.setOpaque(false);
		pnNguoiDung.setBorder(new EmptyBorder(10, 0, 0, 0));

		String chucVu = nguoiDangNhap == null ? "" : nguoiDangNhap.getChucVu();
		lbNguoiDung = new JLabel("<html>Xin chào,<br><b>" + tenNguoiDung + "</b><br>" + chucVu + "</html>");
		lbNguoiDung.setFont(new Font("Arial", Font.PLAIN, 12));
		lbNguoiDung.setForeground(Color.WHITE);
		lbNguoiDung.setAlignmentX(Component.LEFT_ALIGNMENT);

		btnDangXuat = new CustomButton("Đăng xuất", new Color(120, 60, 20), 160, 34);
		btnDangXuat.setFont(new Font("Arial", Font.BOLD, 13));
		btnDangXuat.setAlignmentX(Component.LEFT_ALIGNMENT);

		pnNguoiDung.add(lbNguoiDung);
		pnNguoiDung.add(Box.createVerticalStrut(10));
		pnNguoiDung.add(btnDangXuat);
		pnSidebar.add(pnNguoiDung, BorderLayout.SOUTH);

		add(pnSidebar, BorderLayout.WEST);

		// 2. KHU VỰC CONTAINER CHUYỂN ĐỔI BẰNG CARDLAYOUT
		cardLayout = new CardLayout();
		pnContainerCard = new JPanel(cardLayout);
		pnContainerCard.setOpaque(false);

		tongQuanPanel = new TongQuanPanel();
		banHangPOSPanel = new BanHangPOSPanel();
		quanLyThucDonPanel = new QuanLyThucDonPanel();
		quanLyNhanVienPanel = new QuanLyNhanVienPanel();

		pnContainerCard.add(tongQuanPanel, "Tổng quan");
		pnContainerCard.add(banHangPOSPanel, "Bán hàng");
		pnContainerCard.add(quanLyThucDonPanel, "Thực đơn");
		pnContainerCard.add(quanLyNhanVienPanel, "Nhân viên");

		add(pnContainerCard, BorderLayout.CENTER);

		// ==================== KHU VỰC GẮN SỰ KIỆN ====================
		btnDangXuat.addActionListener(e -> xuLyDangXuat());

		// Nạp dữ liệu ban đầu cho tất cả panel
		capNhatTatCaPanel();
	}

	/** Làm mới dữ liệu của toàn bộ panel chức năng. */
	private void capNhatTatCaPanel() {
		tongQuanPanel.capNhatDuLieu();
		banHangPOSPanel.capNhatDuLieu();
		quanLyThucDonPanel.capNhatDuLieu();
		quanLyNhanVienPanel.capNhatDuLieu();
	}

	/** Xử lý đăng xuất: xóa phiên đăng nhập và quay lại màn hình đăng nhập. */
	private void xuLyDangXuat() {
		int luaChon = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn đăng xuất khỏi hệ thống?",
				"Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}

		nhanVienBUS.dangXuat();
		LoginFrame loginFrame = new LoginFrame();
		loginFrame.setVisible(true);
		this.dispose();
	}

	private void switchCard(String item) {
		// Phân quyền: chỉ quản lí mới được vào chức năng quản lý nhân viên
		if (item.equals("Nhân viên") && !Auth.isManager()) {
			JOptionPane.showMessageDialog(this,
					"Chức năng quản lý nhân viên chỉ dành cho Quản lí.",
					"Không đủ quyền", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (item.equals("Tổng quan") || item.equals("Bán hàng") || item.equals("Thực đơn") || item.equals("Nhân viên")) {
			for (JLabel lb : listMenuLabels) {
				if (lb.getText().equals(item)) {
					lb.setForeground(Color.BLACK);
				} else {
					lb.setForeground(Color.WHITE);
				}
			}
			cardLayout.show(pnContainerCard, item);

			// Làm mới panel vừa mở để dữ liệu luôn đồng bộ giữa các chức năng
			if (item.equals("Tổng quan")) {
				tongQuanPanel.capNhatDuLieu();
			} else if (item.equals("Bán hàng")) {
				banHangPOSPanel.capNhatDuLieu();
			} else if (item.equals("Thực đơn")) {
				quanLyThucDonPanel.capNhatDuLieu();
			} else if (item.equals("Nhân viên")) {
				quanLyNhanVienPanel.capNhatDuLieu();
			}
		} else {
			JOptionPane.showMessageDialog(this, "Chức năng \"" + item + "\" đang trong quá trình phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Điểm khởi chạy của ứng dụng.
	 * Luôn mở màn hình đăng nhập trước, MainFrame chỉ được mở sau khi đăng nhập thành công.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}
}
