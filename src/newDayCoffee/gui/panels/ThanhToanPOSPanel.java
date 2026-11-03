package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.bus.HoaDonBUS;
import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.gui.MainFrame;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;

/**
 * Màn hình thanh toán đơn hàng (POS).
 *
 * Panel nhận MainFrame để quay lại màn hình bán hàng và HoaDonBUS để hiển thị
 * giỏ hàng, tính tiền thừa, lập hóa đơn. Mọi sự kiện đều dùng ActionListener.
 */
public class ThanhToanPOSPanel extends JPanel {

	private static final Color MAU_XAM_NHAT = new Color(218, 218, 218);
	private static final Color MAU_DO_DAM = new Color(128, 28, 28);
	private static final double BUOC_TIEN = 10000;

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
	private JLabel lbGiamGiaGiaTri;
	private JLabel lbThueGiaTri;
	private JLabel lbThanhTienGiaTri;

	// Cột phải - Nhập liệu & Phương thức thanh toán
	private JTextField txtKhachDua;
	private JTextField txtTienThua;
	private JButton btnTienMat;
	private JButton btnTheNganHang;
	private JButton btnChuyenKhoan;
	private JButton btnViMomo;
	private JTextArea txtGhiChu;

	// Cột phải - Bàn phím số & Nút tác vụ
	private JButton[] dsNutBanPhim;
	private JButton btnInHoaDon;
	private JButton btnThanhToan;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final MainFrame mainFrame;
	private final HoaDonBUS hoaDonBUS;
	private final DSHoaDonBUS dsHoaDonBUS;
	private String phuongThucDangChon = "Tiền mặt";

	public ThanhToanPOSPanel(MainFrame mainFrame, HoaDonBUS hoaDonBUS, DSHoaDonBUS dsHoaDonBUS) {
		this.mainFrame = mainFrame;
		this.hoaDonBUS = hoaDonBUS;
		this.dsHoaDonBUS = dsHoaDonBUS;

		khoiTaoGiaoDien();
		ganSuKien();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new GridBagLayout());
		setBackground(new Color(165, 122, 85));
		setBorder(new EmptyBorder(15, 15, 15, 15));

		Font fontTieuDe = new Font("Arial", Font.BOLD, 14);
		Font fontDam = new Font("Arial", Font.BOLD, 13);
		Font fontThuong = new Font("Arial", Font.PLAIN, 13);

		Color mauNenChinh = new Color(165, 122, 85);
		Color mauVienDen = new Color(45, 30, 20);
		Color mauXamNhat = MAU_XAM_NHAT;
		Color mauDoDam = MAU_DO_DAM;
		Color mauDoTuoi = new Color(242, 82, 82);
		Color mauXanhLaDam = new Color(8, 88, 52);

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
		JPanel pnThongTin = new JPanel(new BorderLayout(0, 12));
		pnThongTin.setBackground(mauNenChinh);
		pnThongTin.setBorder(BorderFactory.createCompoundBorder(
			new LineBorder(mauVienDen, 1),
			new EmptyBorder(10, 14, 15, 14)
		));

		JLabel lbTitleThongTin = new JLabel("Thông tin đơn hàng");
		lbTitleThongTin.setFont(fontTieuDe);
		lbTitleThongTin.setForeground(Color.BLACK);
		pnThongTin.add(lbTitleThongTin, BorderLayout.NORTH);

		JPanel pnChiTietThongTin = new JPanel(new GridLayout(3, 2, 10, 12));
		pnChiTietThongTin.setOpaque(false);
		pnChiTietThongTin.setBorder(new EmptyBorder(0, 10, 0, 10));

		JLabel lbMaHD = new JLabel("Mã hóa đơn:");
		lbMaHD.setFont(fontDam);
		lbMaHD.setForeground(Color.WHITE);
		lbMaHoaDonGiaTri = new JLabel("");
		lbMaHoaDonGiaTri.setFont(fontDam);
		lbMaHoaDonGiaTri.setForeground(Color.WHITE);

		JLabel lbNgayLap = new JLabel("Ngày lập:");
		lbNgayLap.setFont(fontDam);
		lbNgayLap.setForeground(Color.WHITE);
		lbNgayLapGiaTri = new JLabel("");
		lbNgayLapGiaTri.setFont(fontDam);
		lbNgayLapGiaTri.setForeground(Color.WHITE);

		JLabel lbNV = new JLabel("Nhân viên:");
		lbNV.setFont(fontDam);
		lbNV.setForeground(Color.WHITE);
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
		gbcLeft.insets = new Insets(0, 0, 20, 0);
		pnLeftCol.add(pnThongTin, gbcLeft);

		// 2. Khung Danh sách sản phẩm
		JPanel pnDanhSachSP = new JPanel(new BorderLayout());
		pnDanhSachSP.setBackground(mauNenChinh);
		pnDanhSachSP.setBorder(new LineBorder(mauVienDen, 1));

		JPanel pnHeaderSP = new JPanel(new BorderLayout());
		pnHeaderSP.setBackground(mauNenChinh);
		pnHeaderSP.setBorder(new EmptyBorder(12, 12, 12, 12));
		JLabel lbTitleSP = new JLabel("Danh sách sản phẩm");
		lbTitleSP.setFont(fontTieuDe);
		lbTitleSP.setForeground(Color.BLACK);
		pnHeaderSP.add(lbTitleSP, BorderLayout.WEST);
		pnDanhSachSP.add(pnHeaderSP, BorderLayout.NORTH);

		String[] cotBang = {"STT", "Tên món", "SL", "Đơn giá", "Thành tiền"};
		modelSanPham = new DefaultTableModel(cotBang, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		// Dữ liệu sản phẩm được nạp từ giỏ hàng khi màn hình thanh toán mở ra

		tblSanPham = new JTable(modelSanPham);
		tblSanPham.setRowHeight(48);
		tblSanPham.setBackground(mauXamNhat);
		tblSanPham.setGridColor(new Color(110, 110, 110));
		tblSanPham.setShowVerticalLines(false);
		tblSanPham.setFont(new Font("Arial", Font.PLAIN, 12));
		tblSanPham.getTableHeader().setBackground(mauXamNhat);
		tblSanPham.getTableHeader().setForeground(new Color(60, 70, 80));
		tblSanPham.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 11));
		tblSanPham.getTableHeader().setPreferredSize(new Dimension(0, 32));
		tblSanPham.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, new Color(110, 110, 110)));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < tblSanPham.getColumnCount(); i++) {
			tblSanPham.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		tblSanPham.getColumnModel().getColumn(0).setPreferredWidth(40);
		tblSanPham.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblSanPham.getColumnModel().getColumn(2).setPreferredWidth(35);

		JScrollPane scrollBang = new JScrollPane(tblSanPham);
		scrollBang.getViewport().setBackground(mauXamNhat);
		scrollBang.setBorder(new MatteBorder(1, 0, 0, 0, mauVienDen));
		pnDanhSachSP.add(scrollBang, BorderLayout.CENTER);

		gbcLeft.gridy = 1;
		gbcLeft.weighty = 0.65;
		gbcLeft.insets = new Insets(0, 0, 20, 0);
		pnLeftCol.add(pnDanhSachSP, gbcLeft);

		// 3. Nút Quay lại đơn hàng
		JPanel pnBottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnBottomLeft.setOpaque(false);

		btnQuayLai = taoNutPhang("←  Quay lại đơn hàng", mauDoDam, Color.WHITE, fontDam, new Dimension(170, 42));
		pnBottomLeft.add(btnQuayLai);

		gbcLeft.gridy = 2;
		gbcLeft.weighty = 0.10;
		gbcLeft.insets = new Insets(0, 0, 0, 0);
		pnLeftCol.add(pnBottomLeft, gbcLeft);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.30;
		gbc.insets = new Insets(0, 0, 0, 15);
		add(pnLeftCol, gbc);

		// ==================== CỘT PHẢI: KHU VỰC THANH TOÁN ====================
		JPanel pnRightCol = new JPanel(new BorderLayout(0, 25));
		pnRightCol.setBackground(mauNenChinh);
		pnRightCol.setBorder(BorderFactory.createCompoundBorder(
			new LineBorder(mauVienDen, 1),
			new EmptyBorder(15, 35, 25, 35)
		));

		// 1. Hàng trên: 4 ô thông tin tổng tiền
		JPanel pnTopSummary = new JPanel(new GridLayout(1, 4, 30, 0));
		pnTopSummary.setOpaque(false);
		pnTopSummary.setPreferredSize(new Dimension(0, 95));

		lbTongTienHangGiaTri = new JLabel("0Đ", SwingConstants.CENTER);
		lbGiamGiaGiaTri = new JLabel("0Đ", SwingConstants.CENTER);
		lbThueGiaTri = new JLabel("0Đ", SwingConstants.CENTER);
		lbThanhTienGiaTri = new JLabel("0Đ", SwingConstants.CENTER);

		pnTopSummary.add(taoTheThongKe("Tổng tiền hàng", lbTongTienHangGiaTri, mauXamNhat, Color.BLACK, mauDoTuoi));
		pnTopSummary.add(taoTheThongKe("Giảm giá", lbGiamGiaGiaTri, mauXamNhat, Color.BLACK, mauDoTuoi));
		pnTopSummary.add(taoTheThongKe("Thuế", lbThueGiaTri, mauXamNhat, Color.BLACK, mauDoTuoi));
		pnTopSummary.add(taoTheThongKe("Thành tiền", lbThanhTienGiaTri, mauDoTuoi, Color.BLACK, Color.WHITE));

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

		txtKhachDua = new JTextField("0");
		txtKhachDua.setBackground(mauXamNhat);
		txtKhachDua.setForeground(Color.GRAY);
		txtKhachDua.setFont(fontThuong);
		txtKhachDua.setPreferredSize(new Dimension(0, 38));
		txtKhachDua.setBorder(new EmptyBorder(0, 12, 0, 12));
		gbcForm.gridy = 1;
		gbcForm.insets = new Insets(0, 0, 20, 0);
		pnFormLeft.add(txtKhachDua, gbcForm);

		// Tiền thừa trả lại
		JLabel lbTienThua = new JLabel("Tiền thừa trả lại:");
		lbTienThua.setFont(fontDam);
		lbTienThua.setForeground(Color.WHITE);
		gbcForm.gridy = 2;
		gbcForm.insets = new Insets(0, 0, 6, 0);
		pnFormLeft.add(lbTienThua, gbcForm);

		txtTienThua = new JTextField("Thiếu: 0");
		txtTienThua.setEditable(false);
		txtTienThua.setBackground(mauXamNhat);
		txtTienThua.setForeground(Color.BLACK);
		txtTienThua.setFont(fontDam);
		txtTienThua.setPreferredSize(new Dimension(0, 38));
		txtTienThua.setBorder(new EmptyBorder(0, 10, 0, 10));
		gbcForm.gridy = 3;
		gbcForm.insets = new Insets(0, 0, 20, 0);
		pnFormLeft.add(txtTienThua, gbcForm);

		// Phương thức thanh toán
		JLabel lbPhuongThuc = new JLabel("Phương thức thanh toán:");
		lbPhuongThuc.setFont(fontDam);
		lbPhuongThuc.setForeground(Color.WHITE);
		gbcForm.gridy = 4;
		gbcForm.insets = new Insets(0, 0, 10, 0);
		pnFormLeft.add(lbPhuongThuc, gbcForm);

		JPanel pnPhuongThucGrid = new JPanel(new GridLayout(2, 2, 20, 18));
		pnPhuongThucGrid.setOpaque(false);

		btnTienMat = taoNutPhang("Tiền mặt", mauDoDam, Color.WHITE, fontDam, new Dimension(0, 42));
		btnTheNganHang = taoNutPhang("Thẻ ngân hàng", mauXamNhat, Color.BLACK, fontDam, new Dimension(0, 42));
		btnChuyenKhoan = taoNutPhang("Chuyển khoản", mauXamNhat, Color.BLACK, fontDam, new Dimension(0, 42));
		btnViMomo = taoNutPhang("Ví momo", mauXamNhat, Color.BLACK, fontDam, new Dimension(0, 42));

		pnPhuongThucGrid.add(btnTienMat);
		pnPhuongThucGrid.add(btnTheNganHang);
		pnPhuongThucGrid.add(btnChuyenKhoan);
		pnPhuongThucGrid.add(btnViMomo);

		gbcForm.gridy = 5;
		gbcForm.insets = new Insets(0, 0, 22, 0);
		pnFormLeft.add(pnPhuongThucGrid, gbcForm);

		// Ghi chú
		JLabel lbGhiChu = new JLabel("Ghi chú:");
		lbGhiChu.setFont(fontDam);
		lbGhiChu.setForeground(Color.WHITE);
		gbcForm.gridy = 6;
		gbcForm.insets = new Insets(0, 0, 8, 0);
		pnFormLeft.add(lbGhiChu, gbcForm);

		JPanel pnGhiChuWrapper = new JPanel(new BorderLayout());
		pnGhiChuWrapper.setOpaque(false);

		txtGhiChu = new JTextArea();
		txtGhiChu.setBackground(mauXamNhat);
		txtGhiChu.setFont(fontThuong);
		txtGhiChu.setLineWrap(true);
		txtGhiChu.setWrapStyleWord(true);
		txtGhiChu.setBorder(new EmptyBorder(8, 8, 8, 8));
		txtGhiChu.setPreferredSize(new Dimension(210, 85));

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
		gbcPay.insets = new Insets(0, 0, 0, 45);
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
		JPanel pnGridPhim = new JPanel(new GridLayout(4, 4, 22, 20));
		pnGridPhim.setOpaque(false);
		pnGridPhim.setBorder(new EmptyBorder(0, 15, 0, 35));

		String[] nhanPhim = {
			"7", "8", "9", "X",
			"4", "5", "6", "+",
			"1", "2", "3", "-",
			"0", "000", ".", "C"
		};
		dsNutBanPhim = new JButton[nhanPhim.length];
		for (int i = 0; i < nhanPhim.length; i++) {
			dsNutBanPhim[i] = taoNutPhang(nhanPhim[i], mauXamNhat, Color.BLACK, fontDam, new Dimension(48, 52));
			pnGridPhim.add(dsNutBanPhim[i]);
		}

		gbcKey.gridy = 1;
		gbcKey.weighty = 0.85;
		gbcKey.insets = new Insets(0, 0, 25, 0);
		pnKeypadRight.add(pnGridPhim, gbcKey);

		// Hàng nút In hóa đơn & Thanh toán
		JPanel pnActionButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
		pnActionButtons.setOpaque(false);

		btnInHoaDon = taoNutPhang("In hóa đơn", mauXamNhat, Color.BLACK, fontDam, new Dimension(145, 52));
		btnThanhToan = taoNutPhang("Thanh toán", mauXanhLaDam, Color.WHITE, fontDam, new Dimension(160, 52));

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
		gbc.weightx = 0.70;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(pnRightCol, gbc);
	}

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnQuayLai.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainFrame != null) {
					mainFrame.switchCard("Bán hàng");
				}
			}
		});

		btnTienMat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chonPhuongThuc("Tiền mặt");
			}
		});
		btnTheNganHang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chonPhuongThuc("Thẻ ngân hàng");
			}
		});
		btnChuyenKhoan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chonPhuongThuc("Chuyển khoản");
			}
		});
		btnViMomo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chonPhuongThuc("Ví momo");
			}
		});

		for (int i = 0; i < dsNutBanPhim.length; i++) {
			final String nhanPhim = dsNutBanPhim[i].getText();
			dsNutBanPhim[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					xuLyBanPhim(nhanPhim);
				}
			});
		}

		btnInHoaDon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inHoaDon();
			}
		});
		btnThanhToan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thanhToan();
			}
		});

		// Cập nhật tiền thiếu / thừa mỗi khi ô khách đưa thay đổi
		txtKhachDua.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				capNhatTienThua();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				capNhatTienThua();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				capNhatTienThua();
			}
		});
	}

	/** Đổi phương thức thanh toán đang chọn và tô lại màu các nút. */
	private void chonPhuongThuc(String phuongThuc) {
		phuongThucDangChon = phuongThuc;

		JButton[] dsNut = {btnTienMat, btnTheNganHang, btnChuyenKhoan, btnViMomo};
		for (int i = 0; i < dsNut.length; i++) {
			if (dsNut[i].getText().equals(phuongThuc)) {
				dsNut[i].setBackground(MAU_DO_DAM);
				dsNut[i].setForeground(Color.WHITE);
			} else {
				dsNut[i].setBackground(MAU_XAM_NHAT);
				dsNut[i].setForeground(Color.BLACK);
			}
		}
	}

	// ==================== KHU VỰC BÀN PHÍM NHẬP TIỀN ====================
	private void xuLyBanPhim(String nhanPhim) {
		if (nhanPhim.equals("C")) {
			txtKhachDua.setText("0");
		} else if (nhanPhim.equals("X")) {
			xoaKyTuCuoi();
		} else if (nhanPhim.equals("+")) {
			dieuChinhTien(BUOC_TIEN);
		} else if (nhanPhim.equals("-")) {
			dieuChinhTien(-BUOC_TIEN);
		} else {
			themVaoOTien(nhanPhim);
		}
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
		txtKhachDua.setText(hienTai.isEmpty() ? "0" : hienTai);
	}

	private void dieuChinhTien(double buocTien) {
		double hienTai = Validator.docSoTien(txtKhachDua.getText());
		if (hienTai < 0) {
			hienTai = 0;
		}
		double giaTriMoi = hienTai + buocTien;
		if (giaTriMoi < 0) {
			giaTriMoi = 0;
		}
		txtKhachDua.setText(HoaDonBUS.dinhDangTienNhap(giaTriMoi));
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp thông tin đơn hàng hiện tại từ HoaDonBUS lên màn hình. */
	public void capNhatDuLieu() {
		lbMaHoaDonGiaTri.setText(dsHoaDonBUS.taoMaHoaDon());
		lbNgayLapGiaTri.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		lbNhanVienGiaTri.setText(Auth.getTenHienThi());

		modelSanPham.setRowCount(0);
		List<ChiTietHoaDonDTO> gioHang = hoaDonBUS.layGioHang();
		for (int i = 0; i < gioHang.size(); i++) {
			ChiTietHoaDonDTO dong = gioHang.get(i);
			modelSanPham.addRow(new Object[]{
				String.valueOf(i + 1),
				dong.getTenMon(),
				String.valueOf(dong.getSoLuong()),
				HoaDonBUS.dinhDangTien(dong.getDonGia()),
				HoaDonBUS.dinhDangTien(dong.getThanhTien())
			});
		}

		double tongTien = hoaDonBUS.tongTien();
		lbTongTienHangGiaTri.setText(HoaDonBUS.dinhDangTien(tongTien));
		lbGiamGiaGiaTri.setText("0Đ");
		lbThueGiaTri.setText("0Đ");
		lbThanhTienGiaTri.setText(HoaDonBUS.dinhDangTien(tongTien));

		capNhatTienThua();
	}

	private void capNhatTienThua() {
		double tongTien = hoaDonBUS.tongTien();
		double khachDua = Validator.docSoTien(txtKhachDua.getText());
		if (khachDua < 0) {
			txtTienThua.setText("Tiền khách đưa không hợp lệ");
			return;
		}

		double chenhLech = khachDua - tongTien;
		if (chenhLech < 0) {
			txtTienThua.setText("Thiếu: " + dinhDangTienKhongDonVi(-chenhLech));
		} else {
			txtTienThua.setText("Thừa: " + dinhDangTienKhongDonVi(chenhLech));
		}
	}

	private String dinhDangTienKhongDonVi(double soTien) {
		return HoaDonBUS.dinhDangTien(soTien).replace("Đ", "");
	}

	private void inHoaDon() {
		HoaDonDTO hoaDon = hoaDonBUS.layHoaDonVuaTao();
		if (hoaDon == null) {
			JOptionPane.showMessageDialog(this,
					"Chưa có hóa đơn để in. Vui lòng thanh toán trước.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JTextArea noiDung = new JTextArea(hoaDonBUS.taoNoiDungHoaDon(hoaDon));
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

		String duongDan = hoaDonBUS.xuatHoaDonRaFile(hoaDon);
		if (duongDan == null) {
			JOptionPane.showMessageDialog(this, "Không thể lưu hóa đơn ra file.",
					"Thông báo", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn:\n" + duongDan,
					"Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void thanhToan() {
		String loi = hoaDonBUS.thanhToan(txtKhachDua.getText(), phuongThucDangChon,
				txtGhiChu.getText());
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		HoaDonDTO hoaDon = hoaDonBUS.layHoaDonVuaTao();
		String maHoaDon = hoaDon == null ? "" : hoaDon.getMaHD();
		JOptionPane.showMessageDialog(this,
				"Thanh toán thành công!\nMã hóa đơn: " + maHoaDon,
				"Thông báo", JOptionPane.INFORMATION_MESSAGE);

		txtKhachDua.setText("0");
		txtGhiChu.setText("");
		capNhatDuLieu();
	}

	/** Tạo thẻ hiển thị thông tin tổng tiền ở hàng trên cùng. */
	private JPanel taoTheThongKe(String tieuDe, JLabel lbGiaTri, Color mauNen, Color mauTieuDe, Color mauGiaTri) {
		JPanel card = new JPanel(new GridLayout(2, 1, 0, 2));
		card.setBackground(mauNen);
		card.setBorder(new EmptyBorder(12, 10, 12, 10));

		JLabel lbTitle = new JLabel(tieuDe, SwingConstants.CENTER);
		lbTitle.setFont(new Font("Arial", Font.BOLD, 13));
		lbTitle.setForeground(mauTieuDe);

		lbGiaTri.setFont(new Font("Arial", Font.BOLD, 13));
		lbGiaTri.setForeground(mauGiaTri);

		card.add(lbTitle);
		card.add(lbGiaTri);
		return card;
	}

	/** Tạo nút bấm phẳng vuông vức theo thiết kế màn hình thanh toán. */
	private JButton taoNutPhang(String text, Color mauNen, Color mauChu, Font font, Dimension kichThuoc) {
		JButton btn = new JButton(text);
		btn.setBackground(mauNen);
		btn.setForeground(mauChu);
		btn.setFont(font);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setPreferredSize(kichThuoc);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}
}