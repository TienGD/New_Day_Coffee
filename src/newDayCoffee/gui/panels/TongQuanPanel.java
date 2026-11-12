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
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.utils.UIUtils;

/**
 * Màn hình tổng quan: doanh thu, số đơn, món bán chạy và danh sách cần chú ý.
 *
 * Panel chỉ hiển thị dữ liệu do DSHoaDonBUS cung cấp, không tự tính toán
 * nghiệp vụ. Các nút sắp xếp / thêm chú ý được xử lý bằng ActionListener.
 */
public class TongQuanPanel extends JPanel implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbTitle;

	// 4 thẻ thống kê
	private JLabel lbDoanhThu;
	private JLabel lbSoDon;
	private JLabel lbSoMon;
	private JLabel lbMonBanChayNhat;

	// Bảng món bán chạy
	private JTable tblMonBanChay;
	private DefaultTableModel modelMonBanChay;

	// Danh sách cần chú ý
	private DefaultListModel<String> modelChuY;
	private JList<String> listChuY;

	private JButton btnBanChay;
	private JButton btnBanCham;
	private JButton btnThemChuY;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSHoaDonBUS dsHoaDonBUS;
	private boolean sapXepGiam = true;

	public TongQuanPanel(DSHoaDonBUS dsHoaDonBUS) {
		this.dsHoaDonBUS = dsHoaDonBUS;
		khoiTaoGiaoDien();
		ganSuKien();
		capNhatDuLieu();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new BorderLayout());
		setOpaque(false);

		Font fontDam = new Font(UIUtils.FONT_FAMILY, Font.BOLD, 15);
		Font fontThuong = new Font(UIUtils.FONT_FAMILY, Font.PLAIN, 14);

		// NỘI DUNG CHÍNH (TỔNG QUAN)
		JPanel pnMainContent = new JPanel(new BorderLayout(0, 22));
		pnMainContent.setOpaque(false);
		pnMainContent.setBorder(new EmptyBorder(22, 30, 28, 30));

		// PHẦN TRÊN: 4 THẺ THỐNG KÊ
		JPanel pnTop = new JPanel(new BorderLayout(0, 15));
		pnTop.setOpaque(false);
		JPanel pnCards = new JPanel(new GridLayout(1, 4, 22, 0));
		pnCards.setOpaque(false);
		pnCards.setPreferredSize(new Dimension(0, 115));

		String[] tieuDeThe = {
			"Doanh thu hôm nay", "Đơn đã hoàn tất", "Món đã bán", "Món bán chạy nhất"
		};
		int[] coChuThe = {20, 20, 20, 16};

		lbDoanhThu = new JLabel("0Đ");
		lbSoDon = new JLabel("0 đơn");
		lbSoMon = new JLabel("0 món");
		lbMonBanChayNhat = new JLabel("Chưa có");
		JLabel[] dsGiaTriThe = {lbDoanhThu, lbSoDon, lbSoMon, lbMonBanChayNhat};

		for (int i = 0; i < tieuDeThe.length; i++) {
			JPanel card = UIUtils.taoTheThongKeTongQuan(tieuDeThe[i], dsGiaTriThe[i], fontThuong, coChuThe[i]);
			pnCards.add(card);
		}
		pnTop.add(pnCards, BorderLayout.CENTER);
		pnMainContent.add(pnTop, BorderLayout.NORTH);

		// PHẦN DƯỚI: MÓN BÁN CHẠY & CẦN CHÚ Ý
		JPanel pnBottom = new JPanel(new GridBagLayout());
		pnBottom.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;

		// 3.1 KHUNG MÓN BÁN CHẠY HÔM NAY (BÊN TRÁI)
		JPanel pnBestSellers = UIUtils.taoPanelNenNau();
		pnBestSellers.setLayout(new BorderLayout(0, 15));
		pnBestSellers.setBorder(new EmptyBorder(20, 25, 22, 25));

		JLabel lbBestSellerTitle = new JLabel("Món bán chạy hôm nay");
		lbBestSellerTitle.setFont(fontDam);
		lbBestSellerTitle.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pnBestSellers.add(lbBestSellerTitle, BorderLayout.NORTH);

		modelMonBanChay = new DefaultTableModel(new Object[]{"Đồ uống", "Số lượng"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblMonBanChay = new JTable(modelMonBanChay);
		tblMonBanChay.setFont(UIUtils.FONT_LABEL);
		tblMonBanChay.setForeground(Color.WHITE);
		tblMonBanChay.setBackground(UIUtils.COLOR_DARK_ROAST);
		tblMonBanChay.setRowHeight(34);
		tblMonBanChay.setShowGrid(false);
		tblMonBanChay.setIntercellSpacing(new Dimension(0, 0));
		tblMonBanChay.setSelectionBackground(new Color(125, 80, 45));
		tblMonBanChay.setSelectionForeground(Color.WHITE);
		tblMonBanChay.getTableHeader().setFont(UIUtils.FONT_LABEL_BOLD);
		tblMonBanChay.getTableHeader().setBackground(UIUtils.COLOR_HEADER);
		tblMonBanChay.getTableHeader().setForeground(UIUtils.COLOR_ACCENT_GOLD);
		tblMonBanChay.getTableHeader().setBorder(null);
		tblMonBanChay.getColumnModel().getColumn(1).setMaxWidth(90);

		JScrollPane scrollMonBanChay = new JScrollPane(tblMonBanChay);
		scrollMonBanChay.setOpaque(false);
		scrollMonBanChay.getViewport().setOpaque(false);
		scrollMonBanChay.setBorder(null);

		pnBestSellers.add(scrollMonBanChay, BorderLayout.CENTER);

		// NÚT SẮP XẾP
		JPanel pnBtnSort = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
		pnBtnSort.setOpaque(false);

		btnBanChay = UIUtils.taoNutNenNau("Bán chạy ▲", fontDam);
		btnBanCham = UIUtils.taoNutNenNau("Bán chậm ▼", fontDam);

		pnBtnSort.add(btnBanChay);
		pnBtnSort.add(btnBanCham);
		pnBestSellers.add(pnBtnSort, BorderLayout.SOUTH);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.68;
		gbc.insets = new Insets(0, 0, 0, 22);
		pnBottom.add(pnBestSellers, gbc);

		// 3.2 KHUNG CẦN CHÚ Ý (BÊN PHẢI)
		JPanel pnAttention = UIUtils.taoPanelNenNau();
		pnAttention.setLayout(new BorderLayout(0, 18));
		pnAttention.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel lbAttentionTitle = new JLabel("Cần chú ý");
		lbAttentionTitle.setFont(fontDam);
		lbAttentionTitle.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pnAttention.add(lbAttentionTitle, BorderLayout.NORTH);

		modelChuY = new DefaultListModel<>();

		listChuY = new JList<>(modelChuY);
		listChuY.setFont(UIUtils.FONT_LABEL);
		listChuY.setForeground(Color.WHITE);
		listChuY.setBackground(UIUtils.COLOR_DARK_ROAST);
		listChuY.setSelectionBackground(new Color(125, 80, 45));
		listChuY.setSelectionForeground(Color.WHITE);
		listChuY.setFixedCellHeight(44);
		listChuY.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listChuY.setBorder(new EmptyBorder(5, 10, 5, 10));

		JScrollPane scrollChuY = new JScrollPane(listChuY);
		scrollChuY.setOpaque(false);
		scrollChuY.getViewport().setOpaque(false);
		scrollChuY.setBorder(null);

		pnAttention.add(scrollChuY, BorderLayout.CENTER);

		// NÚT THÊM CHÚ Ý
		JPanel pnBtnWrapThemChuY = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
		pnBtnWrapThemChuY.setOpaque(false);

		btnThemChuY = UIUtils.taoNutNenNau("+ Thêm chú ý", fontDam);
		btnThemChuY.setPreferredSize(new Dimension(145, 34));

		pnBtnWrapThemChuY.add(btnThemChuY);
		pnAttention.add(pnBtnWrapThemChuY, BorderLayout.SOUTH);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.32;
		gbc.insets = new Insets(0, 0, 0, 0);
		pnBottom.add(pnAttention, gbc);

		pnMainContent.add(pnBottom, BorderLayout.CENTER);
		add(pnMainContent, BorderLayout.CENTER);
	}

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnBanChay.addActionListener(this);
		btnBanCham.addActionListener(this);
		btnThemChuY.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object nguon = e.getSource();

		if (nguon == btnBanChay) {
			sapXepGiam = true;
			capNhatBangMonBanChay();
		} else if (nguon == btnBanCham) {
			sapXepGiam = false;
			capNhatBangMonBanChay();
		} else if (nguon == btnThemChuY) {
			themChuY();
		}
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp lại toàn bộ số liệu từ DSHoaDonBUS. */
	public void capNhatDuLieu() {
		lbDoanhThu.setText(UIUtils.dinhDangTien(dsHoaDonBUS.doanhThuHomNay()));
		lbSoDon.setText(dsHoaDonBUS.soDonHomNay() + " đơn");
		lbSoMon.setText(dsHoaDonBUS.soLuongMonHomNay() + " món");

		List<Map.Entry<String, Integer>> dsBanChay = dsHoaDonBUS.layMonBanChay(true);
		if (dsBanChay.isEmpty()) {
			lbMonBanChayNhat.setText("Chưa có");
		} else {
			lbMonBanChayNhat.setText(dsBanChay.get(0).getKey());
		}

		capNhatBangMonBanChay();
	}

	/** Nạp lại bảng món bán chạy theo thứ tự đang chọn. */
	private void capNhatBangMonBanChay() {
		List<Map.Entry<String, Integer>> dsMon = dsHoaDonBUS.layMonBanChay(sapXepGiam);

		modelMonBanChay.setRowCount(0);
		for (int i = 0; i < dsMon.size(); i++) {
			Map.Entry<String, Integer> mon = dsMon.get(i);
			modelMonBanChay.addRow(new Object[]{mon.getKey(), mon.getValue()});
		}
	}

	private void themChuY() {
		String noiDung = JOptionPane.showInputDialog(this, "Nhập nội dung cần chú ý:", "Thêm chú ý",
				JOptionPane.INFORMATION_MESSAGE);
		if (noiDung == null) {
			return;
		}
		noiDung = noiDung.trim();
		if (noiDung.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Nội dung chú ý không được để trống.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		modelChuY.addElement("(i)  " + noiDung);
	}
}
