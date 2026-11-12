package newDayCoffee.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.bus.DSDoUongBUS;
import newDayCoffee.bus.DSTaiKhoanBUS;
import newDayCoffee.bus.ChiTietHoaDonBUS;
import newDayCoffee.bus.HoaDonBUS;
import newDayCoffee.bus.DSNhanVienBUS;
import newDayCoffee.gui.panels.BanHangPOSPanel;
import newDayCoffee.gui.panels.CaTrucCuaToiPanel;
import newDayCoffee.gui.panels.CaTrucPanel;
import newDayCoffee.gui.panels.HoaDonPanel;
import newDayCoffee.gui.panels.QuanLyNhanVienPanel;
import newDayCoffee.gui.panels.QuanLyThucDonPanel;
import newDayCoffee.gui.panels.TaiKhoanPanel;
import newDayCoffee.gui.panels.ThanhToanPOSPanel;
import newDayCoffee.gui.panels.TongQuanPanel;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.UIUtils;

public class MainFrame extends JFrame implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbSysName;
	private JLabel lbTenNguoiDung;
	private JLabel lbChucVu;
	private JButton btnDangXuat;
	private JPanel pnContainerCard;
	private CardLayout cardLayout;

	// Header dùng chung - chỉ đổi chữ tiêu đề khi chuyển màn hình
	private JLabel lbHeaderTieuDe;

	// Khai báo các nút menu độc lập
	private JButton btnTongQuan;
	private JButton btnBanHang;
	private JButton btnThucDon;
	private JButton btnNhanVien;
	private JButton btnHoaDon;
	private JButton btnCaTruc;
	private JButton btnCaTrucCuaToi;
	private JButton btnTaiKhoan;

	// Các panel chức năng
	private TongQuanPanel tongQuanPanel;
	private BanHangPOSPanel banHangPOSPanel;
	private QuanLyThucDonPanel quanLyThucDonPanel;
	private QuanLyNhanVienPanel quanLyNhanVienPanel;
	private ThanhToanPOSPanel thanhToanPanel;
	private CaTrucPanel CaTrucPanel;
	private CaTrucCuaToiPanel CaTrucCuaToiPanel;
	private HoaDonPanel HoaDonPanel;
	private TaiKhoanPanel TaiKhoanPanel;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSTaiKhoanBUS taiKhoanBUS;
	private final DSNhanVienBUS DSNhanVienBUS;
	private final DSDoUongBUS dsDoUongBUS;
	private final ChiTietHoaDonBUS chiTietHoaDonBUS;
	private final DSHoaDonBUS dsHoaDonBUS;
	private final HoaDonBUS hoaDonBUS;

	public MainFrame(DSTaiKhoanBUS taiKhoanBUS, DSNhanVienBUS DSNhanVienBUS) {
		this.taiKhoanBUS = taiKhoanBUS;
		this.DSNhanVienBUS = DSNhanVienBUS;
		this.dsDoUongBUS = new DSDoUongBUS();
		this.chiTietHoaDonBUS = new ChiTietHoaDonBUS();
		this.dsHoaDonBUS = new DSHoaDonBUS(chiTietHoaDonBUS);
		this.hoaDonBUS = new HoaDonBUS(dsHoaDonBUS, chiTietHoaDonBUS);
		khoiTaoGiaoDien();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setTitle("New Day Coffee");
		setSize(1280, 740);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);

		Font fontMenu = new Font(UIUtils.FONT_FAMILY, Font.BOLD, 15);
		Font fontThuong = new Font(UIUtils.FONT_FAMILY, Font.PLAIN, 13);

		setLayout(new BorderLayout());

		// 1. THANH MENU BÊN TRÁI (SIDEBAR)
		JPanel pnSidebar = new JPanel(new BorderLayout());
		pnSidebar.setPreferredSize(new Dimension(235, 0));
		pnSidebar.setBackground(UIUtils.COLOR_SIDEBAR);
		pnSidebar.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(110, 72, 44)),
			new EmptyBorder(22, 18, 20, 18)
		));

		// LOGO VÀ TÊN HỆ THỐNG
		JPanel pnLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pnLogo.setOpaque(false);
		pnLogo.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(110, 72, 44)),
			new EmptyBorder(0, 0, 18, 0)
		));

		JPanel circleLogo = UIUtils.taoLogoTron(36, UIUtils.COLOR_HEADER);

		lbSysName = new JLabel("Hệ thống quản lý CAFE");
		lbSysName.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 13));
		lbSysName.setForeground(UIUtils.COLOR_ACCENT_GOLD);

		pnLogo.add(circleLogo);
		pnLogo.add(lbSysName);
		pnSidebar.add(pnLogo, BorderLayout.NORTH);

		// DANH SÁCH MENU
		Box pnMenu = Box.createVerticalBox();
		pnMenu.setBorder(new EmptyBorder(22, 0, 0, 0));

		btnTongQuan = UIUtils.taoNutMenu("Tổng quan", fontMenu, this);
		btnTongQuan.setForeground(Color.BLACK);
		if (Auth.coQuyenTruyCapManHinh("Tổng quan")) {
			pnMenu.add(btnTongQuan);
			pnMenu.add(Box.createVerticalStrut(8));
		}

		btnBanHang = UIUtils.taoNutMenu("Bán hàng", fontMenu, this);
		if (Auth.coQuyenTruyCapManHinh("Bán hàng")) {
			pnMenu.add(btnBanHang);
			pnMenu.add(Box.createVerticalStrut(8));
		}

		btnThucDon = UIUtils.taoNutMenu("Thực đơn", fontMenu, this);
		if (Auth.coQuyenTruyCapManHinh("Thực đơn")) {
			pnMenu.add(btnThucDon);
			pnMenu.add(Box.createVerticalStrut(8));
		}

		btnNhanVien = UIUtils.taoNutMenu("Nhân viên", fontMenu, this);
		if (Auth.coQuyenTruyCapManHinh("Nhân viên")) {
			pnMenu.add(btnNhanVien);
			pnMenu.add(Box.createVerticalStrut(8));
		}

		btnCaTruc = UIUtils.taoNutMenu("Ca trực", fontMenu, this);
		if (Auth.coQuyenTruyCapManHinh("Ca trực")) {
			pnMenu.add(btnCaTruc);
			pnMenu.add(Box.createVerticalStrut(8));
		}

		btnCaTrucCuaToi = UIUtils.taoNutMenu("Ca trực của tôi", fontMenu, this);
		if (Auth.coQuyenTruyCapManHinh("Ca trực của tôi")) {
			pnMenu.add(btnCaTrucCuaToi);
			pnMenu.add(Box.createVerticalStrut(8));
		}

		btnHoaDon = UIUtils.taoNutMenu("Hóa đơn", fontMenu, this);
		if (Auth.coQuyenTruyCapManHinh("Danh sách hóa đơn")) {
			pnMenu.add(btnHoaDon);
			pnMenu.add(Box.createVerticalStrut(8));
		}

		btnTaiKhoan = UIUtils.taoNutMenu("Tài khoản", fontMenu, this);
		if (Auth.coQuyenTruyCapManHinh("Tài khoản")) {
			pnMenu.add(btnTaiKhoan);
			pnMenu.add(Box.createVerticalStrut(8));
		}

		pnSidebar.add(pnMenu, BorderLayout.CENTER);

		// THÔNG TIN NGƯỜI DÙNG + ĐĂNG XUẤT
		JPanel pnNguoiDung = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 14, new Insets(12, 14, 12, 14));
		pnNguoiDung.setLayout(new GridLayout(0, 1, 0, 4));

		lbTenNguoiDung = new JLabel(Auth.getTenHienThi(), JLabel.CENTER);
		lbTenNguoiDung.setFont(UIUtils.FONT_LABEL_BOLD);
		lbTenNguoiDung.setForeground(UIUtils.COLOR_ACCENT_GOLD);

		String chucVu = "";
		if (Auth.getNguoiDung() != null) {
			chucVu = "Chức vụ: " + Auth.getNguoiDung().getChucVu();
		}
		lbChucVu = new JLabel(chucVu, JLabel.CENTER);
		lbChucVu.setFont(fontThuong);
		lbChucVu.setForeground(new Color(220, 200, 182));

		btnDangXuat = UIUtils.createButton("Đăng xuất", new Color(168, 42, 42), new Font(UIUtils.FONT_FAMILY, Font.BOLD, 13), 0, 34);
		btnDangXuat.addActionListener(this);

		pnNguoiDung.add(lbTenNguoiDung);
		pnNguoiDung.add(lbChucVu);
		pnNguoiDung.add(Box.createVerticalStrut(4));
		pnNguoiDung.add(btnDangXuat);
		pnSidebar.add(pnNguoiDung, BorderLayout.SOUTH);

		add(pnSidebar, BorderLayout.WEST);

		// 2. KHU VỰC BÊN PHẢI (HEADER CHUNG + CONTENT CARDLAYOUT)
		JPanel pnRightArea = new JPanel(new BorderLayout());
		pnRightArea.setOpaque(false);

		// --- HEADER DÙNG CHUNG ---
		JPanel pnHeaderChung = new JPanel(new BorderLayout());
		pnHeaderChung.setBackground(UIUtils.COLOR_HEADER);
		pnHeaderChung.setPreferredSize(new Dimension(0, 72));
		pnHeaderChung.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(110, 72, 44)),
			new EmptyBorder(0, 30, 0, 30)
		));

		lbHeaderTieuDe = new JLabel("CHI NHÁNH GÒ VẤP");
		lbHeaderTieuDe.setFont(UIUtils.titleFont);
		lbHeaderTieuDe.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pnHeaderChung.add(lbHeaderTieuDe, BorderLayout.WEST);

		JPanel pnBadges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 17));
		pnBadges.setOpaque(false);

		JPanel badgeCaKet = UIUtils.taoBadge(155);
		JLabel lbCaKet = new JLabel("Ca đang mở");
		lbCaKet.setFont(UIUtils.FONT_LABEL_BOLD);
		lbCaKet.setForeground(new Color(142, 240, 172));
		badgeCaKet.add(lbCaKet);

		JPanel badgeNguoiDung = UIUtils.taoBadge(240);
		JLabel lbNguoiDungHeader = new JLabel(UIUtils.taoNhanNguoiDung());
		lbNguoiDungHeader.setFont(UIUtils.FONT_LABEL_BOLD);
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

		if (Auth.coQuyenTruyCapManHinh("Tổng quan")) {
			tongQuanPanel = new TongQuanPanel(dsHoaDonBUS);
			pnContainerCard.add(tongQuanPanel, "Tổng quan");
		}
		if (Auth.coQuyenTruyCapManHinh("Bán hàng")) {
			banHangPOSPanel = new BanHangPOSPanel(this, dsDoUongBUS, chiTietHoaDonBUS);
			pnContainerCard.add(banHangPOSPanel, "Bán hàng");
		}
		if (Auth.coQuyenTruyCapManHinh("Thực đơn")) {
			quanLyThucDonPanel = new QuanLyThucDonPanel(dsDoUongBUS);
			pnContainerCard.add(quanLyThucDonPanel, "Thực đơn");
		}
		if (Auth.coQuyenTruyCapManHinh("Nhân viên")) {
			quanLyNhanVienPanel = new QuanLyNhanVienPanel(DSNhanVienBUS);
			pnContainerCard.add(quanLyNhanVienPanel, "Nhân viên");
		}
		if (Auth.coQuyenTruyCapManHinh("Thanh toán")) {
			thanhToanPanel = new ThanhToanPOSPanel(this, chiTietHoaDonBUS, hoaDonBUS, dsHoaDonBUS);
			pnContainerCard.add(thanhToanPanel, "Thanh toán");
		}
		if (Auth.coQuyenTruyCapManHinh("Ca trực")) {
			CaTrucPanel = new CaTrucPanel();
			pnContainerCard.add(CaTrucPanel, "Ca trực");
		}
		if (Auth.coQuyenTruyCapManHinh("Ca trực của tôi")) {
			CaTrucCuaToiPanel = new CaTrucCuaToiPanel();
			pnContainerCard.add(CaTrucCuaToiPanel, "Ca trực của tôi");
		}
		if (Auth.coQuyenTruyCapManHinh("Danh sách hóa đơn")) {
			HoaDonPanel = new HoaDonPanel(dsHoaDonBUS);
			pnContainerCard.add(HoaDonPanel, "Danh sách hóa đơn");
		}
		if (Auth.coQuyenTruyCapManHinh("Tài khoản")) {
			TaiKhoanPanel = new TaiKhoanPanel(taiKhoanBUS);
			pnContainerCard.add(TaiKhoanPanel, "Tài khoản");
		}

		cardLayout.show(pnContainerCard, "Tổng quan");

		pnRightArea.add(pnContainerCard, BorderLayout.CENTER);
		add(pnRightArea, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTongQuan)) {
			switchCard("Tổng quan");
		} else if (o.equals(btnBanHang)) {
			switchCard("Bán hàng");
		} else if (o.equals(btnThucDon)) {
			switchCard("Thực đơn");
		} else if (o.equals(btnNhanVien)) {
			switchCard("Nhân viên");
		} else if (o.equals(btnCaTruc)) {
			switchCard("Ca trực");
		} else if (o.equals(btnCaTrucCuaToi)) {
			switchCard("Ca trực của tôi");
		} else if (o.equals(btnHoaDon)) {
			switchCard("Danh sách hóa đơn");
		} else if (o.equals(btnTaiKhoan)) {
			switchCard("Tài khoản");
		} else if (o.equals(btnDangXuat)) {
			DSNhanVienBUS.dangXuat();
			new LoginFrame(taiKhoanBUS, DSNhanVienBUS).setVisible(true);
			this.dispose();
		}
	}

	/** Mở màn hình thanh toán kèm ghi chú của đơn hàng đang bán. */
	public void moThanhToan(String ghiChu) {
		if (!Auth.coQuyenTruyCapManHinh("Thanh toán")) {
			return;
		}
		thanhToanPanel.datGhiChu(ghiChu);
		switchCard("Thanh toán");
	}

	/** Xóa ghi chú bán hàng sau khi thanh toán thành công. */
	public void xoaGhiChuBanHang() {
		banHangPOSPanel.xoaGhiChu();
	}

	/** Chuyển màn hình, cập nhật tiêu đề, menu và dữ liệu hiển thị. */
	public void switchCard(String tenManHinh) {
		if (!Auth.coQuyenTruyCapManHinh(tenManHinh)) {
			return;
		}
		cardLayout.show(pnContainerCard, tenManHinh);
		UIUtils.resetMauMenu(btnTongQuan, btnBanHang, btnThucDon, btnNhanVien,
				btnCaTruc, btnCaTrucCuaToi, btnHoaDon, btnTaiKhoan);

		if (tenManHinh.equals("Tổng quan")) {
			btnTongQuan.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("CHI NHÁNH GÒ VẤP");
			tongQuanPanel.capNhatDuLieu();
		} else if (tenManHinh.equals("Bán hàng")) {
			btnBanHang.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("BÁN HÀNG");
			banHangPOSPanel.capNhatDanhSachMon();
			banHangPOSPanel.capNhatGioHang();
		} else if (tenManHinh.equals("Thực đơn")) {
			btnThucDon.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("THỰC ĐƠN QUÁN");
			quanLyThucDonPanel.capNhatBang();
		} else if (tenManHinh.equals("Nhân viên")) {
			btnNhanVien.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("NHÂN VIÊN");
			quanLyNhanVienPanel.capNhatBang();
		} else if (tenManHinh.equals("Ca trực")) {
			btnCaTruc.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("CA TRỰC");
		} else if (tenManHinh.equals("Ca trực của tôi")) {
			btnCaTrucCuaToi.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("CA TRỰC CỦA TÔI");
		} else if (tenManHinh.equals("Danh sách hóa đơn")) {
			btnHoaDon.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("HÓA ĐƠN");
			HoaDonPanel.capNhatDuLieu();
		} else if (tenManHinh.equals("Thanh toán")) {
			btnBanHang.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("THANH TOÁN");
			thanhToanPanel.capNhatDuLieu();
		} else if (tenManHinh.equals("Tài khoản")) {
			btnTaiKhoan.setForeground(Color.BLACK);
			lbHeaderTieuDe.setText("TÀI KHOẢN");
		}
	}
}
