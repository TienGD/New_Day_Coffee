package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.utils.UIUtils;

/**
 * Màn hình thanh toán đơn hàng (POS).
 *
 * Panel nhận MainFrame để quay lại màn hình bán hàng và HoaDonBUS để hiển thị
 * giỏ hàng, tính tiền thừa, lập hóa đơn. Mọi sự kiện đều dùng ActionListener.
 */
public class ThanhToanPOSPanel extends JPanel {

	private static final Color MAU_XAM_NHAT = UIUtils.COLOR_BG_INPUT;
	private static final Color MAU_DO_DAM = new Color(158, 42, 42);
	private static final double BUOC_TIEN = 10000;
	private static final String[] NHAN_BAN_PHIM = {
		"7", "8", "9", "X",
		"4", "5", "6", "+",
		"1", "2", "3", "-",
		"0", "000", ".", "C"
	};

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	// Cột trái: Thông tin đơn hàng & Danh sách sản phẩm
	private JLabel lbMaHoaDonGiaTri;
	private JLabel lbNgayLapGiaTri;
	private JLabel lbNhanVienGiaTri;
	private JTable tblSanPham;
	private DefaultTableModel modelSanPham;
	private JButton btnQuayLai;

	// Cột phải - Hàng trên: Các thẻ tổng kết tiền
	private JLabel lbTongTienHangGiaTri;
	private JLabel lbThanhTienGiaTri;

	// Cột phải - Nhập liệu & Phương thức thanh toán
	private JTextField txtKhachDua;
	private JTextField txtTienThua;
	private JButton btnTienMat;
	private JTextArea txtGhiChu;

	// Cột phải - Bàn phím số & Nút tác vụ
	private JButton[] dsNutBanPhim;
	private JButton btnInHoaDon;
	private JButton btnThanhToan;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private String phuongThucDangChon = "Tiền mặt";

	public ThanhToanPOSPanel() {
		khoiTaoGiaoDien();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new GridBagLayout());
		setBackground(UIUtils.COLOR_BG_MAIN);
		setBorder(new EmptyBorder(18, 22, 20, 22));

		Font fontTieuDe = UIUtils.FONT_TITLE_SEARCH;
		Font fontDam = UIUtils.FONT_LABEL_BOLD;
		Font fontThuong = UIUtils.FONT_LABEL;

		Color mauXamNhat = MAU_XAM_NHAT;
		Color mauDoDam = MAU_DO_DAM;
		Color mauDoTuoi = new Color(196, 56, 56);
		Color mauXanhLaDam = UIUtils.COLOR_BTN_LUU;

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;

		// ==================== CỘT TRÁI: THÔNG TIN & DANH SÁCH SẢN PHẨM ====================
		JPanel pnLeftCol = new JPanel(new GridBagLayout());
		pnLeftCol.setOpaque(false);

		GridBagConstraints gbcLeft = new GridBagConstraints();
		gbcLeft.gridx = 0;
		gbcLeft.fill = GridBagConstraints.BOTH;
		gbcLeft.weightx = 1.0;

		// 1. Khung Thông tin đơn hàng
		JPanel pnThongTin = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 16, new Insets(14, 16, 16, 16));
		pnThongTin.setLayout(new BorderLayout(0, 12));

		JLabel lbTitleThongTin = new JLabel("Thông tin đơn hàng");
		lbTitleThongTin.setFont(fontTieuDe);
		lbTitleThongTin.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		lbTitleThongTin.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(228, 176, 114, 55)),
			new EmptyBorder(0, 0, 8, 0)
		));
		pnThongTin.add(lbTitleThongTin, BorderLayout.NORTH);

		JPanel pnChiTietThongTin = new JPanel(new GridLayout(3, 2, 10, 10));
		pnChiTietThongTin.setOpaque(false);
		pnChiTietThongTin.setBorder(new EmptyBorder(0, 6, 0, 6));

		JLabel lbMaHD = new JLabel("Mã hóa đơn:");
		lbMaHD.setFont(fontDam);
		lbMaHD.setForeground(new Color(235, 220, 205));
		lbMaHoaDonGiaTri = new JLabel("");
		lbMaHoaDonGiaTri.setFont(fontDam);
		lbMaHoaDonGiaTri.setForeground(Color.WHITE);

		JLabel lbNgayLap = new JLabel("Ngày lập:");
		lbNgayLap.setFont(fontDam);
		lbNgayLap.setForeground(new Color(235, 220, 205));
		lbNgayLapGiaTri = new JLabel("");
		lbNgayLapGiaTri.setFont(fontDam);
		lbNgayLapGiaTri.setForeground(Color.WHITE);

		JLabel lbNV = new JLabel("Nhân viên:");
		lbNV.setFont(fontDam);
		lbNV.setForeground(new Color(235, 220, 205));
		lbNhanVienGiaTri = new JLabel("");
		lbNhanVienGiaTri.setFont(fontDam);
		lbNhanVienGiaTri.setForeground(Color.WHITE);

		pnChiTietThongTin.add(lbMaHD);
		pnChiTietThongTin.add(lbMaHoaDonGiaTri);
		pnChiTietThongTin.add(lbNgayLap);
		pnChiTietThongTin.add(lbNgayLapGiaTri);
		pnChiTietThongTin.add(lbNV);
		pnChiTietThongTin.add(lbNhanVienGiaTri);

		pnThongTin.add(pnChiTietThongTin, BorderLayout.CENTER);

		gbcLeft.gridy = 0;
		gbcLeft.weighty = 0.25;
		gbcLeft.insets = new Insets(0, 0, 16, 0);
		pnLeftCol.add(pnThongTin, gbcLeft);

		// 2. Khung Danh sách sản phẩm
		JPanel pnDanhSachSP = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 16, new Insets(14, 16, 16, 16));
		pnDanhSachSP.setLayout(new BorderLayout(0, 10));

		JPanel pnHeaderSP = new JPanel(new BorderLayout());
		pnHeaderSP.setOpaque(false);
		pnHeaderSP.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(228, 176, 114, 55)),
			new EmptyBorder(0, 0, 8, 0)
		));
		JLabel lbTitleSP = new JLabel("Danh sách sản phẩm");
		lbTitleSP.setFont(fontTieuDe);
		lbTitleSP.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pnHeaderSP.add(lbTitleSP, BorderLayout.WEST);
		pnDanhSachSP.add(pnHeaderSP, BorderLayout.NORTH);

		String[] cotBang = {"STT", "Tên món", "SL", "Đơn giá", "Thành tiền"};
		modelSanPham = new DefaultTableModel(cotBang, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblSanPham = new JTable(modelSanPham);
		JScrollPane scrollBang = UIUtils.createTableScrollPane(tblSanPham);
		UIUtils.centerTableCells(tblSanPham);
		tblSanPham.getColumnModel().getColumn(0).setPreferredWidth(40);
		tblSanPham.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblSanPham.getColumnModel().getColumn(2).setPreferredWidth(35);

		pnDanhSachSP.add(scrollBang, BorderLayout.CENTER);

		gbcLeft.gridy = 1;
		gbcLeft.weighty = 0.65;
		gbcLeft.insets = new Insets(0, 0, 16, 0);
		pnLeftCol.add(pnDanhSachSP, gbcLeft);

		// 3. Nút Quay lại đơn hàng
		JPanel pnBottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnBottomLeft.setOpaque(false);

		btnQuayLai = UIUtils.taoNutPhang("←  Quay lại đơn hàng", mauDoDam, Color.WHITE, fontDam, new Dimension(180, 42));
		pnBottomLeft.add(btnQuayLai);

		gbcLeft.gridy = 2;
		gbcLeft.weighty = 0.10;
		gbcLeft.insets = new Insets(0, 0, 0, 0);
		pnLeftCol.add(pnBottomLeft, gbcLeft);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.32;
		gbc.insets = new Insets(0, 0, 0, 18);
		add(pnLeftCol, gbc);

		// ==================== CỘT PHẢI: KHU VỰC THANH TOÁN ====================
		JPanel pnRightCol = UIUtils.createCardPanel(UIUtils.COLOR_CARD_SURFACE, 18, new Insets(18, 28, 22, 28));
		pnRightCol.setLayout(new BorderLayout(0, 22));

		// 1. Hàng trên: các ô thông tin tổng tiền
		JPanel pnTopSummary = new JPanel(new GridLayout(1, 4, 22, 0));
		pnTopSummary.setOpaque(false);
		pnTopSummary.setPreferredSize(new Dimension(0, 86));

		lbTongTienHangGiaTri = new JLabel("0Đ", SwingConstants.CENTER);
		lbThanhTienGiaTri = new JLabel("0Đ", SwingConstants.CENTER);

		pnTopSummary.add(UIUtils.taoTheThongKeThanhToan("Tổng tiền hàng", lbTongTienHangGiaTri, mauXamNhat, new Color(60, 35, 20), mauDoTuoi));
		pnTopSummary.add(UIUtils.taoTheThongKeThanhToan("Thành tiền", lbThanhTienGiaTri, mauDoTuoi, Color.WHITE, Color.WHITE));

		pnRightCol.add(pnTopSummary, BorderLayout.NORTH);

		// 2. Phần thân: Chia 2 cột (Cột form bên trái & Cột bàn phím bên phải)
		JPanel pnCenterPayment = new JPanel(new GridBagLayout());
		pnCenterPayment.setOpaque(false);

		GridBagConstraints gbcPay = new GridBagConstraints();
		gbcPay.fill = GridBagConstraints.BOTH;
		gbcPay.weighty = 1.0;

		// --- Nửa trái của khung phải: Khách đưa, Tiền thừa, Phương thức, Ghi chú ---
		JPanel pnFormLeft = new JPanel(new GridBagLayout());
		pnFormLeft.setOpaque(false);

		GridBagConstraints gbcForm = new GridBagConstraints();
		gbcForm.gridx = 0;
		gbcForm.fill = GridBagConstraints.HORIZONTAL;
		gbcForm.weightx = 1.0;
		gbcForm.anchor = GridBagConstraints.NORTHWEST;

		// Khách đưa
		JLabel lbKhachDua = new JLabel("Khách đưa:");
		lbKhachDua.setFont(fontDam);
		lbKhachDua.setForeground(Color.WHITE);
		gbcForm.gridy = 0;
		gbcForm.insets = new Insets(0, 0, 6, 0);
		pnFormLeft.add(lbKhachDua, gbcForm);

		txtKhachDua = UIUtils.createTextField(0, 38);
		txtKhachDua.setText("0");
		txtKhachDua.setForeground(new Color(60, 40, 25));
		gbcForm.gridy = 1;
		gbcForm.insets = new Insets(0, 0, 18, 0);
		pnFormLeft.add(txtKhachDua, gbcForm);

		// Tiền thừa trả lại
		JLabel lbTienThua = new JLabel("Tiền thừa trả lại:");
		lbTienThua.setFont(fontDam);
		lbTienThua.setForeground(Color.WHITE);
		gbcForm.gridy = 2;
		gbcForm.insets = new Insets(0, 0, 6, 0);
		pnFormLeft.add(lbTienThua, gbcForm);

		txtTienThua = UIUtils.createTextField(0, 38);
		txtTienThua.setText("Thiếu: 0");
		txtTienThua.setEditable(false);
		txtTienThua.setForeground(new Color(40, 24, 14));
		txtTienThua.setFont(fontDam);
		gbcForm.gridy = 3;
		gbcForm.insets = new Insets(0, 0, 18, 0);
		pnFormLeft.add(txtTienThua, gbcForm);

		// Phương thức thanh toán
		JLabel lbPhuongThuc = new JLabel("Phương thức thanh toán:");
		lbPhuongThuc.setFont(fontDam);
		lbPhuongThuc.setForeground(Color.WHITE);
		gbcForm.gridy = 4;
		gbcForm.insets = new Insets(0, 0, 8, 0);
		pnFormLeft.add(lbPhuongThuc, gbcForm);

		JPanel pnPhuongThucGrid = new JPanel(new GridLayout(2, 2, 16, 14));
		pnPhuongThucGrid.setOpaque(false);

		btnTienMat = UIUtils.taoNutPhang("Tiền mặt", mauDoDam, Color.WHITE, fontDam, new Dimension(0, 40));
		pnPhuongThucGrid.add(btnTienMat);

		gbcForm.gridy = 5;
		gbcForm.insets = new Insets(0, 0, 18, 0);
		pnFormLeft.add(pnPhuongThucGrid, gbcForm);

		// Ghi chú
		JLabel lbGhiChu = new JLabel("Ghi chú:");
		lbGhiChu.setFont(fontDam);
		lbGhiChu.setForeground(Color.WHITE);
		gbcForm.gridy = 6;
		gbcForm.insets = new Insets(0, 0, 8, 0);
		pnFormLeft.add(lbGhiChu, gbcForm);

		txtGhiChu = new JTextArea();
		txtGhiChu.setBackground(mauXamNhat);
		txtGhiChu.setForeground(new Color(40, 24, 14));
		txtGhiChu.setFont(fontThuong);
		txtGhiChu.setLineWrap(true);
		txtGhiChu.setWrapStyleWord(true);
		txtGhiChu.setBorder(BorderFactory.createCompoundBorder(
			new LineBorder(new Color(185, 162, 140), 1, true),
			new EmptyBorder(8, 10, 8, 10)
		));
		txtGhiChu.setPreferredSize(new Dimension(220, 85));

		JPanel pnGhiChuHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnGhiChuHolder.setOpaque(false);
		pnGhiChuHolder.add(txtGhiChu);

		gbcForm.gridy = 7;
		gbcForm.weighty = 1.0;
		gbcForm.insets = new Insets(0, 0, 0, 0);
		pnFormLeft.add(pnGhiChuHolder, gbcForm);

		gbcPay.gridx = 0;
		gbcPay.gridy = 0;
		gbcPay.weightx = 0.48;
		gbcPay.insets = new Insets(0, 0, 0, 35);
		pnCenterPayment.add(pnFormLeft, gbcPay);

		// --- Nửa phải của khung phải: Bàn phím nhập số & Nút In/Thanh toán ---
		JPanel pnKeypadRight = new JPanel(new GridBagLayout());
		pnKeypadRight.setOpaque(false);

		GridBagConstraints gbcKey = new GridBagConstraints();
		gbcKey.gridx = 0;
		gbcKey.fill = GridBagConstraints.BOTH;
		gbcKey.weightx = 1.0;

		JLabel lbNhapSo = new JLabel("Nhập số tiền khách đưa:");
		lbNhapSo.setFont(fontDam);
		lbNhapSo.setForeground(Color.WHITE);
		gbcKey.gridy = 0;
		gbcKey.weighty = 0.0;
		gbcKey.insets = new Insets(0, 0, 12, 0);
		pnKeypadRight.add(lbNhapSo, gbcKey);

		// Lưới bàn phím 4x4
		JPanel pnGridPhim = new JPanel(new GridLayout(4, 4, 14, 14));
		pnGridPhim.setOpaque(false);
		pnGridPhim.setBorder(new EmptyBorder(0, 8, 0, 20));

		dsNutBanPhim = new JButton[NHAN_BAN_PHIM.length];
		for (int i = 0; i < NHAN_BAN_PHIM.length; i++) {
			String labelPhim = NHAN_BAN_PHIM[i];
			Color bgPhim = ("X".equals(labelPhim) || "C".equals(labelPhim))
				? new Color(235, 205, 195)
				: mauXamNhat;
			dsNutBanPhim[i] = UIUtils.taoNutPhang(labelPhim, bgPhim, new Color(42, 24, 14), new Font(UIUtils.FONT_FAMILY, Font.BOLD, 15), new Dimension(48, 50));
			pnGridPhim.add(dsNutBanPhim[i]);
		}

		gbcKey.gridy = 1;
		gbcKey.weighty = 0.85;
		gbcKey.insets = new Insets(0, 0, 22, 0);
		pnKeypadRight.add(pnGridPhim, gbcKey);

		// Hàng nút In hóa đơn & Thanh toán
		JPanel pnActionButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
		pnActionButtons.setOpaque(false);

		btnInHoaDon = UIUtils.taoNutPhang("In hóa đơn", mauXamNhat, new Color(42, 24, 14), fontDam, new Dimension(145, 48));
		btnThanhToan = UIUtils.taoNutPhang("Thanh toán", mauXanhLaDam, Color.WHITE, fontDam, new Dimension(160, 48));

		pnActionButtons.add(btnInHoaDon);
		pnActionButtons.add(btnThanhToan);

		gbcKey.gridy = 2;
		gbcKey.weighty = 0.15;
		gbcKey.insets = new Insets(0, 0, 0, 0);
		pnKeypadRight.add(pnActionButtons, gbcKey);

		gbcPay.gridx = 1;
		gbcPay.gridy = 0;
		gbcPay.weightx = 0.52;
		gbcPay.insets = new Insets(0, 0, 0, 0);
		pnCenterPayment.add(pnKeypadRight, gbcPay);

		pnRightCol.add(pnCenterPayment, BorderLayout.CENTER);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.68;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(pnRightCol, gbc);
	}
}
