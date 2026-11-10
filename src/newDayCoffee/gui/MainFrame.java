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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import newDayCoffee.gui.panels.BanHangPOSPanel;
import newDayCoffee.gui.panels.CaTrucPanel;
import newDayCoffee.gui.panels.QuanLyNhanVienPanel;
import newDayCoffee.gui.panels.QuanLyThucDonPanel;
import newDayCoffee.gui.panels.ThanhToanPOSPanel;
import newDayCoffee.gui.panels.TongQuanPanel;
import newDayCoffee.utils.Auth;

public class MainFrame extends JFrame implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbSysName;
	private JLabel lbTenNguoiDung;
	private JLabel lbChucVu;
	private JButton btnDangXuat;
	private JPanel pnContainerCard;
	private CardLayout cardLayout;

	// Header dùng chung - chỉ đổi chữ tiêu đề khi chuyển màn hình
	private JLabel lbHeaderTieuDe; // Nhãn tiêu đề trên header (VD: "BÁN HÀNG", "NHÂN VIÊN", ...)

	// Khai báo các nút menu độc lập để dễ quản lý sự kiện không dùng vòng lặp
	private JButton btnTongQuan;
	private JButton btnBanHang;
	private JButton btnThucDon;
	private JButton btnNhanVien;

	// 5 panel chức năng (chỉ chứa phần nội dung, không còn header riêng)
	private TongQuanPanel tongQuanPanel;
	private BanHangPOSPanel banHangPOSPanel;
	private QuanLyThucDonPanel quanLyThucDonPanel;
	private QuanLyNhanVienPanel quanLyNhanVienPanel;
	private ThanhToanPOSPanel thanhToanPanel;
	private JButton btnCaTruc;
	private CaTrucPanel CaTrucPanel;

	// ==================== KHU VỰC TẦNG XỬ LÝ ===================
	public MainFrame() {
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

		// DANH SÁCH MENU (Khởi tạo trực tiếp từng nút không dùng vòng lặp)
		Box pnMenu = Box.createVerticalBox();
		pnMenu.setBorder(new EmptyBorder(40, 5, 0, 0));

		// Nút Tổng quan (Mặc định chọn sẵn nên màu đen)
		btnTongQuan = taoNutMenu("Tổng quan", fontMenu);
		btnTongQuan.setForeground(Color.BLACK);
		pnMenu.add(btnTongQuan);
		pnMenu.add(Box.createVerticalStrut(22));

		// Nút Bán hàng
		btnBanHang = taoNutMenu("Bán hàng", fontMenu);
		pnMenu.add(btnBanHang);
		pnMenu.add(Box.createVerticalStrut(22));

		// Các nút chỉ dành cho Quản lý
		btnThucDon = taoNutMenu("Thực đơn", fontMenu);
		pnMenu.add(btnThucDon);
		pnMenu.add(Box.createVerticalStrut(22));

		btnNhanVien = taoNutMenu("Nhân viên", fontMenu);
		pnMenu.add(btnNhanVien);
		pnMenu.add(Box.createVerticalStrut(22));
		
		btnCaTruc = taoNutMenu("Ca trực", fontMenu);
		pnMenu.add(btnCaTruc);
		pnMenu.add(Box.createVerticalStrut(22));

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

		btnDangXuat.addActionListener(this); // Đăng ký sự kiện trực tiếp

		pnNguoiDung.add(lbTenNguoiDung);
		pnNguoiDung.add(lbChucVu);
		pnNguoiDung.add(Box.createVerticalStrut(6));
		pnNguoiDung.add(btnDangXuat);
		pnSidebar.add(pnNguoiDung, BorderLayout.SOUTH);

		add(pnSidebar, BorderLayout.WEST);

		JPanel pnRightArea = new JPanel(new BorderLayout());
		pnRightArea.setOpaque(false);

		// --- HEADER DÙNG CHUNG (nằm ngoài CardLayout, không bao giờ bị swap) ---
		JPanel pnHeaderChung = new JPanel(new BorderLayout());
		pnHeaderChung.setBackground(new Color(139, 69, 19)); // Màu nâu cố định, không đổi
		pnHeaderChung.setPreferredSize(new Dimension(0, 75));
		pnHeaderChung.setBorder(new EmptyBorder(0, 30, 0, 30));

		// Tiêu đề bên trái - sẽ thay đổi CHỮ khi chuyển màn hình (không đổi màu nền)
		lbHeaderTieuDe = new JLabel("CHI NHÁNH GÒ VẤP"); // Mặc định màn hình Tổng quan
		lbHeaderTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
		lbHeaderTieuDe.setForeground(Color.WHITE);
		pnHeaderChung.add(lbHeaderTieuDe, BorderLayout.WEST);

		// Các badge bên phải (Ca kết + Người dùng) - luôn hiển thị, không đổi
		JPanel pnBadges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 18));
		pnBadges.setOpaque(false);

		JPanel badgeCaKet = taoBadge(155);
		JLabel lbCaKet = new JLabel("Ca đang mở");
		lbCaKet.setFont(fontThuong);
		lbCaKet.setForeground(Color.WHITE);
		badgeCaKet.add(lbCaKet);

		JPanel badgeNguoiDung = taoBadge(240);
		JLabel lbNguoiDungHeader = new JLabel(taoNhanNguoiDung());
		lbNguoiDungHeader.setFont(fontThuong);
		lbNguoiDungHeader.setForeground(Color.WHITE);
		badgeNguoiDung.add(lbNguoiDungHeader);

		pnBadges.add(badgeCaKet);
		pnBadges.add(badgeNguoiDung);
		pnHeaderChung.add(pnBadges, BorderLayout.EAST);

		pnRightArea.add(pnHeaderChung, BorderLayout.NORTH);

		// --- KHU VỰC NỘI DUNG CHUYỂN ĐỔI BẰNG CARDLAYOUT ---
		cardLayout = new CardLayout();
		pnContainerCard = new JPanel(cardLayout);
		pnContainerCard.setOpaque(false);

		// Khởi tạo các panel chức năng (không còn header riêng bên trong)
		tongQuanPanel = new TongQuanPanel();
		banHangPOSPanel = new BanHangPOSPanel();
		quanLyThucDonPanel = new QuanLyThucDonPanel();
		quanLyNhanVienPanel = new QuanLyNhanVienPanel();
		CaTrucPanel = new CaTrucPanel();
//		thanhToanPanel = new ThanhToanPOSPanel();

		// Thêm vào CardLayout kèm theo tên định danh tương ứng
		pnContainerCard.add(tongQuanPanel, "Tổng quan");
		pnContainerCard.add(banHangPOSPanel, "Bán hàng");
		pnContainerCard.add(quanLyThucDonPanel, "Thực đơn");
		pnContainerCard.add(quanLyNhanVienPanel, "Nhân viên");
		pnContainerCard.add(CaTrucPanel,"Ca trực");
//		pnContainerCard.add(thanhToanPanel, "Thanh toán");

		// Hiển thị panel "Tổng quan" làm mặc định
		cardLayout.show(pnContainerCard, "Tổng quan");

		pnRightArea.add(pnContainerCard, BorderLayout.CENTER);
		add(pnRightArea, BorderLayout.CENTER);
	}

	// Phương thức hỗ trợ tạo nhanh nút menu để tránh lặp code
	private JButton taoNutMenu(String text, Font font) {
		JButton btn = new JButton(text);
		btn.setFont(font);
		btn.setForeground(Color.WHITE);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setRolloverEnabled(false);
		btn.setBorder(new EmptyBorder(0, 0, 0, 0));
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.addActionListener(this); // Đăng ký sự kiện trực tiếp không dùng vòng lặp
		return btn;
	}

	/**
	 * Tạo badge bo góc dùng trên header (nền nâu nhạt hơn).
	 * Dùng paintComponent để vẽ hình bo góc tùy chỉnh.
	 */
	private JPanel taoBadge(int width) {
		JPanel badge = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(172, 118, 78)); // Màu badge cố định
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
				g2.dispose();
			}
		};
		badge.setOpaque(false);
		badge.setPreferredSize(new Dimension(width, 38));
		return badge;
	}

	// Hàm chuyển màu tất cả các nút menu về trắng
	private void resetMauMenu() {
		btnTongQuan.setForeground(Color.WHITE);
		btnBanHang.setForeground(Color.WHITE);
		if (btnThucDon != null) btnThucDon.setForeground(Color.WHITE);
		if (btnNhanVien != null) btnNhanVien.setForeground(Color.WHITE);
		btnCaTruc.setForeground(Color.white);
	}

	/** Trả về nhãn hiển thị người đang đăng nhập trên header */
	private String taoNhanNguoiDung() {
		if (Auth.laQuanLy()) {
			return "Quản lý: " + Auth.getTenHienThi();
		}
		return "Nhân viên: " + Auth.getTenHienThi();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		// Xử lý chuyển đổi qua lại giữa các màn hình bằng điều kiện rẽ nhánh (Không dùng vòng lặp)
		// Mỗi nhánh: (1) chuyển CardLayout, (2) reset màu menu,
		//            (3) tô đen nút đang chọn, (4) đổi CHỮ tiêu đề header (không đổi màu nền!)
		if (o.equals(btnTongQuan)) {
			cardLayout.show(pnContainerCard, "Tổng quan");
			resetMauMenu();
			btnTongQuan.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("CHI NHÁNH GÒ VẤP");
		}
		else if (o.equals(btnBanHang)) {
			cardLayout.show(pnContainerCard, "Bán hàng");
			resetMauMenu();
			btnBanHang.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("BÁN HÀNG");
		}
		else if (btnThucDon != null && o.equals(btnThucDon)) {
			cardLayout.show(pnContainerCard, "Thực đơn");
			resetMauMenu();
			btnThucDon.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("THỰC ĐƠN QUÁN");
		}
		else if (btnNhanVien != null && o.equals(btnNhanVien)) {
			cardLayout.show(pnContainerCard, "Nhân viên");
			resetMauMenu();
			btnNhanVien.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("NHÂN VIÊN");
		}
		else if(btnCaTruc !=null && o.equals(btnCaTruc)) {
			cardLayout.show(pnContainerCard, "Ca trực");
			resetMauMenu();
			btnCaTruc.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("CA TRỰC");
		}
//		else if (o.equals(btnDangXuat)) {
//			int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//			if (choice == JOptionPane.YES_OPTION) {
//				new LoginFrame().setVisible(true);
//				this.dispose();
//			}
//		}
	}
}
