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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.ChiTietHoaDonBUS;
import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.bus.HoaDonBUS;
import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.gui.MainFrame;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.UIUtils;
import newDayCoffee.utils.Validator;

/**
 * Màn hình thanh toán đơn hàng (POS).
 *
 * Panel nhận MainFrame để quay lại màn hình bán hàng và HoaDonBUS để hiển thị
 * giỏ hàng, tính tiền thừa, lập hóa đơn. Các thao tác bấm nút dùng
 * ActionListener; ô nhập tiền cập nhật thêm khi mất focus.
 */
public class ThanhToanPOSPanel extends JPanel implements ActionListener, FocusListener {

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
	private final MainFrame mainFrame;
	private final ChiTietHoaDonBUS chiTietHoaDonBUS;
	private final HoaDonBUS hoaDonBUS;
	private final DSHoaDonBUS dsHoaDonBUS;
	private String phuongThucDangChon = "Tiền mặt";

	public ThanhToanPOSPanel(MainFrame mainFrame, ChiTietHoaDonBUS chiTietHoaDonBUS,
			HoaDonBUS hoaDonBUS, DSHoaDonBUS dsHoaDonBUS) {
		this.mainFrame = mainFrame;
		this.chiTietHoaDonBUS = chiTietHoaDonBUS;
		this.hoaDonBUS = hoaDonBUS;
		this.dsHoaDonBUS = dsHoaDonBUS;

		khoiTaoGiaoDien();
		ganSuKien();
		capNhatDuLieu();
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

		txtKhachDua = UIUtils.createTextField(0, 38, "Ví dụ: 100000");
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

		txtGhiChu = UIUtils.createTextAreaWithHint("Ví dụ: khách mang về...");
		txtGhiChu.setBackground(mauXamNhat);
		txtGhiChu.setForeground(new Color(40, 24, 14));
		txtGhiChu.setFont(fontThuong);
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
		JPanel pnActionButtons = new JPanel(new GridLayout(1, 2, 14, 0));
		pnActionButtons.setOpaque(false);
		pnActionButtons.setBorder(new EmptyBorder(0, 8, 0, 20));

		btnInHoaDon = UIUtils.taoNutPhang("In hóa đơn", mauXamNhat, new Color(42, 24, 14), fontDam, new Dimension(0, 48));
		btnThanhToan = UIUtils.taoNutPhang("Thanh toán", mauXanhLaDam, Color.WHITE, fontDam, new Dimension(0, 48));

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

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnQuayLai.addActionListener(this);
		btnTienMat.addActionListener(this);
		btnInHoaDon.addActionListener(this);
		btnThanhToan.addActionListener(this);
		txtKhachDua.addActionListener(this);
		txtKhachDua.addFocusListener(this);

		for (int i = 0; i < dsNutBanPhim.length; i++) {
			dsNutBanPhim[i].addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object nguon = e.getSource();

		if (nguon == btnQuayLai) {
			if (mainFrame != null) {
				mainFrame.switchCard("Bán hàng");
			}
		} else if (nguon == btnTienMat) {
			chonPhuongThuc("Tiền mặt");
		} else if (nguon == btnInHoaDon) {
			inHoaDon();
		} else if (nguon == btnThanhToan) {
			thanhToan();
		} else if (nguon == txtKhachDua) {
			capNhatTienThua();
		} else {
			for (int i = 0; i < dsNutBanPhim.length; i++) {
				if (nguon == dsNutBanPhim[i]) {
					xuLyBanPhim(NHAN_BAN_PHIM[i]);
					return;
				}
			}
		}
	}

	/** Đổi phương thức thanh toán đang chọn và tô lại màu nút. */
	private void chonPhuongThuc(String phuongThuc) {
		phuongThucDangChon = phuongThuc;
		if (btnTienMat.getText().equals(phuongThuc)) {
			btnTienMat.setBackground(MAU_DO_DAM);
			btnTienMat.setForeground(Color.WHITE);
		}
	}

	// ==================== KHU VỰC BÀN PHÍM NHẬP TIỀN ====================
	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == txtKhachDua) {
			txtKhachDua.selectAll();
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == txtKhachDua) {
			capNhatTienThua();
		}
	}

	private void xuLyBanPhim(String nhanPhim) {
		if (nhanPhim.equals("C")) {
			txtKhachDua.setText("");
		} else if (nhanPhim.equals("X")) {
			xoaKyTuCuoi();
		} else if (nhanPhim.equals("+")) {
			dieuChinhTien(BUOC_TIEN);
		} else if (nhanPhim.equals("-")) {
			dieuChinhTien(-BUOC_TIEN);
		} else {
			themVaoOTien(nhanPhim);
		}
		capNhatTienThua();
	}

	private void themVaoOTien(String kyTu) {
		String hienTai = txtKhachDua.getText().trim();
		if (hienTai.equals("0")) {
			hienTai = "";
		}
		if (kyTu.equals(".")) {
			if (hienTai.indexOf('.') >= 0) {
				return;
			}
			if (hienTai.isEmpty()) {
				hienTai = "0";
			}
		}
		txtKhachDua.setText(hienTai + kyTu);
		txtKhachDua.setCaretPosition(txtKhachDua.getText().length());
	}

	private void xoaKyTuCuoi() {
		String hienTai = txtKhachDua.getText().trim();
		if (hienTai.isEmpty()) {
			return;
		}
		hienTai = hienTai.substring(0, hienTai.length() - 1);
		txtKhachDua.setText(hienTai);
	}

	private void dieuChinhTien(double buocTien) {
		double hienTai = docSoTien(txtKhachDua.getText());
		if (hienTai < 0) {
			hienTai = 0;
		}
		double giaTriMoi = hienTai + buocTien;
		if (giaTriMoi < 0) {
			giaTriMoi = 0;
		}
		txtKhachDua.setText(UIUtils.dinhDangTienNhap(giaTriMoi));
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp thông tin đơn hàng hiện tại từ HoaDonBUS lên màn hình. */
	public void capNhatDuLieu() {
		lbMaHoaDonGiaTri.setText(dsHoaDonBUS.taoMaHoaDon());
		lbNgayLapGiaTri.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		lbNhanVienGiaTri.setText(Auth.getTenHienThi());

		modelSanPham.setRowCount(0);
		List<ChiTietHoaDonDTO> gioHang = chiTietHoaDonBUS.layGioHang();
		for (int i = 0; i < gioHang.size(); i++) {
			ChiTietHoaDonDTO dong = gioHang.get(i);
			modelSanPham.addRow(new Object[]{
				String.valueOf(i + 1),
				dong.getTenMon(),
				String.valueOf(dong.getSoLuong()),
				UIUtils.dinhDangTien(dong.getDonGia()),
				UIUtils.dinhDangTien(dong.getThanhTien())
			});
		}

		double tongTien = chiTietHoaDonBUS.tongTienGioHang();
		lbTongTienHangGiaTri.setText(UIUtils.dinhDangTien(tongTien));
		lbThanhTienGiaTri.setText(UIUtils.dinhDangTien(tongTien));

		capNhatTienThua();
	}

	private void capNhatTienThua() {
		double tongTien = chiTietHoaDonBUS.tongTienGioHang();
		double khachDua = docSoTien(txtKhachDua.getText());
		if (khachDua < 0) {
			txtTienThua.setText("Tiền khách đưa không hợp lệ");
			return;
		}

		double chenhLech = khachDua - tongTien;
		if (chenhLech < 0) {
			txtTienThua.setText("Thiếu: " + UIUtils.dinhDangTienKhongDonVi(-chenhLech));
		} else {
			txtTienThua.setText("Thừa: " + UIUtils.dinhDangTienKhongDonVi(chenhLech));
		}
	}

	/** Đọc số tiền người dùng nhập. Chấp nhận các dạng 15000, 15.000, 15,000. */
	private double docSoTien(String chuoiTien) {
		String s = chuoiTien == null ? "" : chuoiTien.trim();
		if (s.isEmpty()) {
			return 0;
		}
		s = s.replace("đ", "").replace("Đ", "").replace(" ", "");
		if (!s.matches(Validator.SO_TIEN)) {
			return -1;
		}
		try {
			return Double.parseDouble(s.replace(".", "").replace(",", ""));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void inHoaDon() {
		HoaDonDTO hoaDon = hoaDonBUS.layHoaDonVuaTao();
		if (hoaDon == null) {
			JOptionPane.showMessageDialog(this,
					"Chưa có hóa đơn để in. Vui lòng thanh toán trước.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JTextArea noiDung = new JTextArea(taoNoiDungHoaDon(hoaDon));
		noiDung.setEditable(false);
		noiDung.setFont(new Font("Monospaced", Font.PLAIN, 13));

		JScrollPane scrollNoiDung = new JScrollPane(noiDung);
		scrollNoiDung.setPreferredSize(new Dimension(460, 420));

		int luaChon = JOptionPane.showConfirmDialog(this, scrollNoiDung,
				"Hóa đơn " + hoaDon.getMaHD(), JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (luaChon != JOptionPane.OK_OPTION) {
			return;
		}

		String duongDan = hoaDonBUS.xuatHoaDonRaFile(hoaDon, noiDung.getText());
		if (duongDan == null) {
			JOptionPane.showMessageDialog(this, "Không thể lưu hóa đơn ra file.",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn:\n" + duongDan,
					"Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/** Tạo nội dung hóa đơn dạng văn bản để hiển thị hoặc ghi file. */
	private String taoNoiDungHoaDon(HoaDonDTO hoaDon) {
		if (hoaDon == null) {
			return "";
		}

		double tongTien = dsHoaDonBUS.tongTienHoaDon(hoaDon);
		double tienThoi = hoaDon.getTienKhachDua() - tongTien;

		StringBuilder noiDung = new StringBuilder();
		noiDung.append("        NEW DAY COFFEE\n");
		noiDung.append("      HÓA ĐƠN THANH TOÁN\n");
		noiDung.append("------------------------------------------\n");
		noiDung.append("Mã hóa đơn : ").append(hoaDon.getMaHD()).append("\n");
		noiDung.append("Ngày lập   : ").append(UIUtils.dinhDangNgay(hoaDon.getNgayLap())).append("\n");
		noiDung.append("Nhân viên  : ").append(tenNhanVien(hoaDon.getMaNV())).append("\n");
		noiDung.append("------------------------------------------\n");

		List<ChiTietHoaDonDTO> dsChiTiet = dsHoaDonBUS.layChiTiet(hoaDon.getMaHD());
		for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
			noiDung.append(chiTiet.getTenMon())
					.append("  x").append(chiTiet.getSoLuong())
					.append("  ").append(UIUtils.dinhDangTien(chiTiet.getDonGia()))
					.append(" = ").append(UIUtils.dinhDangTien(chiTiet.getThanhTien()))
					.append("\n");
		}

		noiDung.append("------------------------------------------\n");
		noiDung.append("Tổng tiền  : ").append(UIUtils.dinhDangTien(tongTien)).append("\n");
		noiDung.append("Khách đưa  : ").append(UIUtils.dinhDangTien(hoaDon.getTienKhachDua())).append("\n");
		noiDung.append("Tiền thối  : ").append(UIUtils.dinhDangTien(tienThoi)).append("\n");
		noiDung.append("Trạng thái : ").append(hoaDon.getTrangThai()).append("\n");

		if (hoaDon.getGhiChu() != null && !hoaDon.getGhiChu().trim().isEmpty()) {
			noiDung.append("Ghi chú    : ").append(hoaDon.getGhiChu()).append("\n");
		}

		noiDung.append("------------------------------------------\n");
		noiDung.append("Cảm ơn quý khách! Hẹn gặp lại.\n");
		return noiDung.toString();
	}

	/** Lấy tên nhân viên lập hóa đơn để in phiếu. */
	private String tenNhanVien(NhanVienDTO nhanVien) {
		if (nhanVien == null) {
			return "";
		}
		if (nhanVien.getTenNV() != null && !nhanVien.getTenNV().trim().isEmpty()) {
			return nhanVien.getTenNV();
		}
		return nhanVien.getMaNV() == null ? "" : nhanVien.getMaNV();
	}

	private void thanhToan() {
		if (chiTietHoaDonBUS.gioHangRong()) {
			JOptionPane.showMessageDialog(this, "Đơn hàng chưa có món nào để thanh toán.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (phuongThucDangChon == null || phuongThucDangChon.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn phương thức thanh toán.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String tienKhachDuaText = txtKhachDua.getText().trim();
		if (tienKhachDuaText.isEmpty() || !tienKhachDuaText.matches(Validator.SO_TIEN)) {
			JOptionPane.showMessageDialog(this, "Tiền khách đưa không hợp lệ.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			txtKhachDua.requestFocusInWindow();
			return;
		}

		double tienKhachDua = docSoTien(tienKhachDuaText);
		if (tienKhachDua < 0) {
			JOptionPane.showMessageDialog(this, "Tiền khách đưa không hợp lệ.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			txtKhachDua.requestFocusInWindow();
			return;
		}

		double tongTien = chiTietHoaDonBUS.tongTienGioHang();
		if (tienKhachDua < tongTien) {
			JOptionPane.showMessageDialog(this, "Tiền khách đưa không đủ. Còn thiếu "
					+ UIUtils.dinhDangTien(tongTien - tienKhachDua) + ".",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			txtKhachDua.requestFocusInWindow();
			return;
		}

		HoaDonDTO hoaDonMoi = new HoaDonDTO();
		hoaDonMoi.setTienKhachDua(tienKhachDua);
		hoaDonMoi.setGhiChu(txtGhiChu.getText());

		String loi = hoaDonBUS.thanhToan(hoaDonMoi);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		HoaDonDTO hoaDon = hoaDonBUS.layHoaDonVuaTao();
		String maHoaDon = hoaDon == null ? "" : hoaDon.getMaHD();
		JOptionPane.showMessageDialog(this,
				"Thanh toán thành công!\nMã hóa đơn: " + maHoaDon,
				"Thông báo", JOptionPane.INFORMATION_MESSAGE);

		txtKhachDua.setText("");
		txtGhiChu.setText("");
		if (mainFrame != null) {
			mainFrame.xoaGhiChuBanHang();
		}
		capNhatDuLieu();
	}

	/** Nhận ghi chú đơn hàng từ màn hình bán hàng trước khi thanh toán. */
	public void datGhiChu(String ghiChu) {
		txtGhiChu.setText(ghiChu == null ? "" : ghiChu);
	}
}
