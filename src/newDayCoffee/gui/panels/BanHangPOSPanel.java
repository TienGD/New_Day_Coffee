package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.utils.UIUtils;

public class BanHangPOSPanel extends JPanel {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JTextField txtTimKiem;
	private JButton btnTimKiem;
	private JPanel pnGridMon;
	private JTable tblDonHang;
	private DefaultTableModel modelDonHang;
	private JLabel lbTongTienGiaTri;
	private JButton btnXoa;
	private JButton btnThanhToan;
	private JTextArea txtGhiChu;

	private String HintTimKiem = "Tìm kiếm món";
	private String nhomDangLoc = "Tất cả";

	// Lưu nhãn số lượng trên từng thẻ món để cập nhật nhanh
	private final Map<String, JLabel> nhanSoLuong = new HashMap<>();
	// Thứ tự mã món tương ứng với từng dòng trong bảng đơn hàng
	private final List<String> maMonTrongBang = new ArrayList<>();
	// Nút lọc theo nhóm món
	private final Map<JButton, String> nhomCuaNutLoc = new HashMap<>();
	// Nút tăng / giảm trên thẻ món
	private final Map<JButton, String> maMonCuaNutCong = new HashMap<>();
	private final Map<JButton, String> maMonCuaNutTru = new HashMap<>();

	public BanHangPOSPanel() {
		khoiTaoGiaoDien();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new BorderLayout());
		setBackground(UIUtils.COLOR_BACKGROUND);

		Font fontDam = new Font(UIUtils.FONT_FAMILY, Font.BOLD, 16);
		Font fontThuong = UIUtils.FONT_LABEL;

		// NỘI DUNG CHÍNH (CHIA 2 CỘT: DANH SÁCH MÓN & THÔNG TIN ĐƠN HÀNG)
		JPanel pnMainContent = new JPanel(new GridBagLayout());
		pnMainContent.setOpaque(false);
		pnMainContent.setBorder(new EmptyBorder(18, 24, 22, 24));
		pnMainContent.setBackground(UIUtils.COLOR_BACKGROUND);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;

		// ==================== CỘT TRÁI: DANH SÁCH CÁC MÓN ====================
		JPanel pnLeftCol = UIUtils.createCardPanel(UIUtils.COLOR_CARD_SURFACE, 18, new Insets(16, 18, 16, 18));
		pnLeftCol.setLayout(new BorderLayout(0, 14));

		JPanel pnTopFilter = new JPanel(new BorderLayout(0, 10));
		pnTopFilter.setOpaque(false);
		pnTopFilter.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(228, 176, 114, 55)),
			new EmptyBorder(0, 0, 12, 0)
		));

		JLabel lbDanhSachMon = new JLabel("Danh sách các món");
		lbDanhSachMon.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 18));
		lbDanhSachMon.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pnTopFilter.add(lbDanhSachMon, BorderLayout.NORTH);

		JPanel pnFilterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		pnFilterBar.setOpaque(false);

		// Ô tìm kiếm món bo góc (tái sử dụng từ UIUtils)
		txtTimKiem = UIUtils.taoOTimKiemPOS(HintTimKiem, fontThuong, 132, 34);
		pnFilterBar.add(txtTimKiem);

		btnTimKiem = UIUtils.taoNutLocPOS("Tìm", 50, UIUtils.FONT_LABEL_BOLD);
		btnTimKiem.setBackground(UIUtils.COLOR_BTN_TIM);
		pnFilterBar.add(btnTimKiem);

		// Các nút lọc loại món (bấm vào nút nào nút đó sẽ sáng nổi bật lên)
		String[] dsLoai = {"Tất cả", "Cà phê", "Trà", "Trà sữa"};
		JButton nutMacDinh = null;
		for (int i = 0; i < dsLoai.length; i++) {
			JButton btnLoai = UIUtils.taoNutLocPOS(dsLoai[i], 65, UIUtils.FONT_LABEL_BOLD);
			nhomCuaNutLoc.put(btnLoai, dsLoai[i]);
			if ("Tất cả".equals(dsLoai[i])) {
				nutMacDinh = btnLoai;
			}
			btnLoai.addActionListener(e -> {
				nhomDangLoc = nhomCuaNutLoc.get(btnLoai);
				UIUtils.capNhatTrangThaiNutLocPOS(btnLoai, nhomCuaNutLoc.keySet());
			});
			pnFilterBar.add(btnLoai);
		}
		if (nutMacDinh != null) {
			UIUtils.capNhatTrangThaiNutLocPOS(nutMacDinh, nhomCuaNutLoc.keySet());
		}

		pnTopFilter.add(pnFilterBar, BorderLayout.CENTER);
		pnLeftCol.add(pnTopFilter, BorderLayout.NORTH);

		// Lưới danh sách thẻ món
		pnGridMon = new JPanel(new GridLayout(0, 3, 16, 16));
		pnGridMon.setOpaque(false);
		pnGridMon.setBorder(new EmptyBorder(4, 4, 4, 4));

		JPanel coffee = UIUtils.taoTheMonPOS("cof1", "Coffee", "ngon", "1234");
		pnGridMon.add(coffee);

		JPanel pnGridWrapper = new JPanel(new BorderLayout());
		pnGridWrapper.setOpaque(false);
		pnGridWrapper.add(pnGridMon, BorderLayout.NORTH);

		JScrollPane scrollMon = new JScrollPane(pnGridWrapper);
		scrollMon.setOpaque(false);
		scrollMon.getViewport().setOpaque(false);
		scrollMon.setBorder(null);
		scrollMon.getVerticalScrollBar().setUnitIncrement(18);

		pnLeftCol.add(scrollMon, BorderLayout.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.61;
		gbc.insets = new Insets(0, 0, 0, 18);
		pnMainContent.add(pnLeftCol, gbc);

		// ==================== CỘT PHẢI: THÔNG TIN ĐƠN HÀNG ====================
		JPanel pnRightCol = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 18, new Insets(16, 18, 18, 18));
		pnRightCol.setLayout(new BorderLayout(0, 12));

		JPanel pnOrderTop = new JPanel(new BorderLayout(0, 10));
		pnOrderTop.setOpaque(false);

		JLabel lbOrderTitle = new JLabel("Thông tin đơn hàng");
		lbOrderTitle.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 18));
		lbOrderTitle.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		lbOrderTitle.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(228, 176, 114, 55)),
			new EmptyBorder(0, 0, 8, 0)
		));
		pnOrderTop.add(lbOrderTitle, BorderLayout.NORTH);

		// Phần Nhập ghi chú phía trên bảng đơn hàng
		JPanel pnGhiChuContainer = new JPanel(new BorderLayout(0, 5));
		pnGhiChuContainer.setOpaque(false);

		JLabel lbGhiChu = new JLabel("Ghi chú:");
		lbGhiChu.setFont(UIUtils.FONT_LABEL_BOLD);
		lbGhiChu.setForeground(new Color(242, 232, 220));
		pnGhiChuContainer.add(lbGhiChu, BorderLayout.NORTH);

		txtGhiChu = new JTextArea(3, 20);
		txtGhiChu.setLineWrap(true);
		txtGhiChu.setWrapStyleWord(true);
		txtGhiChu.setBackground(UIUtils.COLOR_BG_INPUT);
		txtGhiChu.setForeground(new Color(40, 24, 14));
		txtGhiChu.setFont(new Font(UIUtils.FONT_FAMILY, Font.PLAIN, 13));
		txtGhiChu.setBorder(new EmptyBorder(6, 8, 6, 8));

		JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
		scrollGhiChu.setBorder(new LineBorder(new Color(165, 135, 110), 1, true));
		pnGhiChuContainer.add(scrollGhiChu, BorderLayout.CENTER);

		pnOrderTop.add(pnGhiChuContainer, BorderLayout.CENTER);
		pnRightCol.add(pnOrderTop, BorderLayout.NORTH);

		// Bảng danh sách món đặt
		String[] cotBang = {"STT", "Tên món", "SL", "Đơn giá", "Thành tiền", "Ghi chú"};
		modelDonHang = new DefaultTableModel(cotBang, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblDonHang = new JTable(modelDonHang);
		JScrollPane scrollBang = UIUtils.createTableScrollPane(tblDonHang);
		UIUtils.centerTableCells(tblDonHang);
		tblDonHang.getColumnModel().getColumn(0).setPreferredWidth(42);
		tblDonHang.getColumnModel().getColumn(1).setPreferredWidth(92);
		tblDonHang.getColumnModel().getColumn(2).setPreferredWidth(36);
		tblDonHang.getColumnModel().getColumn(3).setPreferredWidth(70);
		tblDonHang.getColumnModel().getColumn(4).setPreferredWidth(82);
		tblDonHang.getColumnModel().getColumn(5).setPreferredWidth(68);

		pnRightCol.add(scrollBang, BorderLayout.CENTER);

		// Phần dưới: Tổng tiền + Nút Xóa & Thanh toán
		JPanel pnOrderBottom = new JPanel(new BorderLayout(0, 14));
		pnOrderBottom.setOpaque(false);

		JPanel pnTotalRow = UIUtils.createCardPanel(UIUtils.COLOR_DARK_ROAST, 12, new Insets(10, 14, 10, 14));
		pnTotalRow.setLayout(new BorderLayout());

		JLabel lbTongTienText = new JLabel("Tổng tiền:");
		lbTongTienText.setFont(fontDam);
		lbTongTienText.setForeground(new Color(242, 232, 220));

		lbTongTienGiaTri = new JLabel("0Đ");
		lbTongTienGiaTri.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 18));
		lbTongTienGiaTri.setForeground(UIUtils.COLOR_ACCENT_GOLD);

		pnTotalRow.add(lbTongTienText, BorderLayout.WEST);
		pnTotalRow.add(lbTongTienGiaTri, BorderLayout.EAST);
		pnOrderBottom.add(pnTotalRow, BorderLayout.NORTH);

		JPanel pnOrderButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
		pnOrderButtons.setOpaque(false);

		Font fontBtnAction = new Font(UIUtils.FONT_FAMILY, Font.BOLD, 14);
		btnXoa = UIUtils.taoNutBoGocPOS("Xóa", UIUtils.COLOR_BTN_XOA, null, fontBtnAction, 95, 42);
		btnThanhToan = UIUtils.taoNutBoGocPOS("Thanh toán", UIUtils.COLOR_BTN_LUU, null, fontBtnAction, 145, 42);

		pnOrderButtons.add(btnXoa);
		pnOrderButtons.add(btnThanhToan);
		pnOrderBottom.add(pnOrderButtons, BorderLayout.CENTER);

		pnRightCol.add(pnOrderBottom, BorderLayout.SOUTH);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.39;
		gbc.insets = new Insets(0, 0, 0, 0);
		pnMainContent.add(pnRightCol, gbc);

		add(pnMainContent, BorderLayout.CENTER);
	}
}
