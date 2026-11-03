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
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import newDayCoffee.bus.DSDoUongBUS;
import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.bus.DSTaiKhoanBUS;
import newDayCoffee.bus.HoaDonBUS;
import newDayCoffee.bus.NhanVienBUS;
import newDayCoffee.gui.panels.BanHangPOSPanel;
import newDayCoffee.gui.panels.QuanLyNhanVienPanel;
import newDayCoffee.gui.panels.QuanLyThucDonPanel;
import newDayCoffee.gui.panels.ThanhToanPOSPanel;
import newDayCoffee.gui.panels.TongQuanPanel;
import newDayCoffee.utils.Auth;

/**
 * Màn hình chính của ứng dụng.
 *
 * MainFrame là nơi tạo các BUS dùng chung và truyền vào từng panel
 * (không dùng singleton). Điều hướng menu và đăng xuất đều dùng
 * ActionListener.
 */
public class MainFrame extends JFrame implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbSysName;
	private JLabel lbTenNguoiDung;
	private JLabel lbChucVu;
	private JButton btnDangXuat;
	private JPanel pnContainerCard;
	private CardLayout cardLayout;
	private final List<JButton> listMenuButtons = new ArrayList<>();
	private final List<String> listMenuTen = new ArrayList<>();

	// 5 panel chức năng
	private TongQuanPanel tongQuanPanel;
	private BanHangPOSPanel banHangPOSPanel;
	private QuanLyThucDonPanel quanLyThucDonPanel;
	private QuanLyNhanVienPanel quanLyNhanVienPanel;
	private ThanhToanPOSPanel thanhToanPanel;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSTaiKhoanBUS taiKhoanBUS;
	private final NhanVienBUS nhanVienBUS;

	public MainFrame(DSTaiKhoanBUS taiKhoanBUS, NhanVienBUS nhanVienBUS) {
		this.taiKhoanBUS = taiKhoanBUS;
		this.nhanVienBUS = nhanVienBUS;

		khoiTaoGiaoDien();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setTitle("New Day Coffee");
		setSize(1280, 720);
		setLocationRelativeTo(null);
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

		String[] dsMenu;
		if (Auth.laQuanLy()) {
			dsMenu = new String[]{"Tổng quan", "Bán hàng", "Thực đơn", "Nhân viên"};
		} else {
			dsMenu = new String[]{"Tổng quan", "Bán hàng"};
		}

		for (int i = 0; i < dsMenu.length; i++) {
			final String tenMenu = dsMenu[i];
			JButton btnMenu = new JButton(tenMenu);
			btnMenu.setFont(fontMenu);
			if (i == 0) {
				btnMenu.setForeground(Color.BLACK);
			} else {
				btnMenu.setForeground(Color.WHITE);
			}
			btnMenu.setHorizontalAlignment(SwingConstants.LEFT);
			btnMenu.setOpaque(false);
			btnMenu.setContentAreaFilled(false);
			btnMenu.setBorderPainted(false);
			btnMenu.setFocusPainted(false);
			btnMenu.setRolloverEnabled(false);
			btnMenu.setBorder(new EmptyBorder(0, 0, 0, 0));
			btnMenu.setMargin(new Insets(0, 0, 0, 0));
			btnMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
			btnMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnMenu.addActionListener(this);
			listMenuButtons.add(btnMenu);
			listMenuTen.add(tenMenu);
			pnMenu.add(btnMenu);
			pnMenu.add(Box.createVerticalStrut(22));
		}
		pnSidebar.add(pnMenu, BorderLayout.CENTER);

		// THÔNG TIN NGƯỜI DÙNG + ĐĂNG XUẤT
		JPanel pnNguoiDung = new JPanel();
		pnNguoiDung.setLayout(new java.awt.GridLayout(0, 1, 0, 4));
		pnNguoiDung.setOpaque(false);

		lbTenNguoiDung = new JLabel(Auth.getTenHienThi());
		lbTenNguoiDung.setFont(new Font("Arial", Font.BOLD, 13));
		lbTenNguoiDung.setForeground(Color.BLACK);

		String chucVu = "";
		if (Auth.getNguoiDung() != null) {
			chucVu = "Chức vụ: " + Auth.getNguoiDung().getChucVu();
		}
		lbChucVu = new JLabel(chucVu);
		lbChucVu.setFont(fontThuong);
		lbChucVu.setForeground(new Color(80, 40, 10));

		btnDangXuat = new JButton("Đăng xuất");
		btnDangXuat.setFont(fontThuong);
		btnDangXuat.setBackground(new Color(140, 30, 30));
		btnDangXuat.setForeground(Color.WHITE);
		btnDangXuat.setFocusPainted(false);
		btnDangXuat.setBorderPainted(false);
		btnDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDangXuat.setPreferredSize(new Dimension(0, 32));

		pnNguoiDung.add(lbTenNguoiDung);
		pnNguoiDung.add(lbChucVu);
		pnNguoiDung.add(Box.createVerticalStrut(6));
		pnNguoiDung.add(btnDangXuat);
		pnSidebar.add(pnNguoiDung, BorderLayout.SOUTH);

		add(pnSidebar, BorderLayout.WEST);

		// 2. KHU VỰC CONTAINER CHUYỂN ĐỔI BẰNG CARDLAYOUT
		cardLayout = new CardLayout();
		pnContainerCard = new JPanel(cardLayout);
		pnContainerCard.setOpaque(false);

		// Tạo các BUS dùng chung cho mọi panel
		DSDoUongBUS dsDoUongBUS = new DSDoUongBUS();
		DSHoaDonBUS dsHoaDonBUS = new DSHoaDonBUS();
		HoaDonBUS hoaDonBUS = new HoaDonBUS(dsHoaDonBUS, nhanVienBUS);

		// Khởi tạo đủ 5 panel (Truyền MainFrame vào BanHangPOSPanel và ThanhToanPOSPanel để điều hướng)
		tongQuanPanel = new TongQuanPanel(dsHoaDonBUS);
		banHangPOSPanel = new BanHangPOSPanel(this, dsDoUongBUS, hoaDonBUS);
		quanLyThucDonPanel = new QuanLyThucDonPanel(dsDoUongBUS);
		quanLyNhanVienPanel = new QuanLyNhanVienPanel(nhanVienBUS);
		thanhToanPanel = new ThanhToanPOSPanel(this, hoaDonBUS, dsHoaDonBUS);

		pnContainerCard.add(tongQuanPanel, "Tổng quan");
		pnContainerCard.add(banHangPOSPanel, "Bán hàng");
		pnContainerCard.add(quanLyThucDonPanel, "Thực đơn");
		pnContainerCard.add(quanLyNhanVienPanel, "Nhân viên");
		pnContainerCard.add(thanhToanPanel, "Thanh toán");

		add(pnContainerCard, BorderLayout.CENTER);

		// Gắn sự kiện đăng xuất
		btnDangXuat.addActionListener(this);
	}

	// ==================== KHU VỰC SỰ KIỆN ====================
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnDangXuat) {
			dangXuat();
			return;
		}

		for (int i = 0; i < listMenuButtons.size(); i++) {
			if (e.getSource() == listMenuButtons.get(i)) {
				switchCard(listMenuTen.get(i));
				return;
			}
		}
	}

	// ==================== KHU VỰC ĐIỀU HƯỚNG ====================
	/**
	 * Chuyển đổi giữa các màn hình trong CardLayout.
	 * Để public để BanHangPOSPanel và ThanhToanPOSPanel có thể gọi trực tiếp.
	 */
	public void switchCard(String item) {
		if (item.equals("Tổng quan")) {
			tongQuanPanel.capNhatDuLieu();
		} else if (item.equals("Bán hàng")) {
			banHangPOSPanel.capNhatDanhSachMon();
			banHangPOSPanel.capNhatGioHang();
		} else if (item.equals("Thực đơn")) {
			quanLyThucDonPanel.capNhatBang();
		} else if (item.equals("Nhân viên")) {
			quanLyNhanVienPanel.capNhatBang();
		} else if (item.equals("Thanh toán")) {
			thanhToanPanel.capNhatDuLieu();
		}

		// Nếu đang ở màn hình "Thanh toán" thì trên menu vẫn sáng ở mục "Bán hàng"
		String menuDangChon = item.equals("Thanh toán") ? "Bán hàng" : item;
		for (int i = 0; i < listMenuButtons.size(); i++) {
			JButton btn = listMenuButtons.get(i);
			if (btn.getText().equals(menuDangChon)) {
				btn.setForeground(Color.BLACK);
			} else {
				btn.setForeground(Color.WHITE);
			}
		}
		cardLayout.show(pnContainerCard, item);
	}

	// ==================== KHU VỰC ĐĂNG XUẤT ====================
	private void dangXuat() {
		int luaChon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?",
				"Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}

		nhanVienBUS.dangXuat();
		LoginFrame loginFrame = new LoginFrame(taiKhoanBUS, nhanVienBUS);
		loginFrame.setVisible(true);
		this.dispose();
	}

	// ==================== KHU VỰC KHỞI CHẠY ====================
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}
}
