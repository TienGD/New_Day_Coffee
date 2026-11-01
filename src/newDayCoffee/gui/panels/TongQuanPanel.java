package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.utils.Auth;

/**
 * Màn hình tổng quan: doanh thu, số đơn, món bán chạy và danh sách cần chú ý.
 *
 * Panel chỉ hiển thị dữ liệu do DSHoaDonBUS cung cấp, không tự tính toán
 * nghiệp vụ. Các nút sắp xếp / thêm chú ý được xử lý trong actionPerformed.
 */
public class TongQuanPanel extends JPanel implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbBranch;
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

		Font fontTieuDe = new Font("Arial", Font.BOLD, 22);
		Font fontDam = new Font("Arial", Font.BOLD, 15);
		Font fontThuong = new Font("Arial", Font.PLAIN, 14);

		// HEADER PHÍA TRÊN
		JPanel pnHeader = new JPanel(new BorderLayout());
		pnHeader.setBackground(new Color(139, 69, 19));
		pnHeader.setPreferredSize(new Dimension(0, 75));
		pnHeader.setBorder(new EmptyBorder(0, 30, 0, 30));

		lbBranch = new JLabel("CHI NHÁNH GÒ VẤP");
		lbBranch.setFont(fontTieuDe);
		lbBranch.setForeground(Color.WHITE);
		pnHeader.add(lbBranch, BorderLayout.WEST);

		JPanel pnBadges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 18));
		pnBadges.setOpaque(false);

		// Nhãn thông tin Ca kết
		JPanel badgeCaKet = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(172, 118, 78));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
				g2.dispose();
			}
		};
		badgeCaKet.setOpaque(false);
		badgeCaKet.setPreferredSize(new Dimension(155, 38));
		JLabel lbCaKet = new JLabel("Ca kết đang mở");
		lbCaKet.setFont(new Font("Arial", Font.PLAIN, 13));
		lbCaKet.setForeground(Color.WHITE);
		badgeCaKet.add(lbCaKet);

		// Nhãn thông tin người đang đăng nhập
		JPanel badgeQuanLy = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(172, 118, 78));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
				g2.dispose();
			}
		};
		badgeQuanLy.setOpaque(false);
		badgeQuanLy.setPreferredSize(new Dimension(220, 38));
		JLabel lbQuanLy = new JLabel(taoNhanNguoiDung());
		lbQuanLy.setFont(new Font("Arial", Font.PLAIN, 13));
		lbQuanLy.setForeground(Color.WHITE);
		badgeQuanLy.add(lbQuanLy);

		pnBadges.add(badgeCaKet);
		pnBadges.add(badgeQuanLy);
		pnHeader.add(pnBadges, BorderLayout.EAST);

		add(pnHeader, BorderLayout.NORTH);

		// NỘI DUNG CHÍNH (TỔNG QUAN)
		JPanel pnMainContent = new JPanel(new BorderLayout(0, 20));
		pnMainContent.setOpaque(false);
		pnMainContent.setBorder(new EmptyBorder(20, 30, 30, 30));

		// PHẦN TRÊN: TIÊU ĐỀ TỔNG QUAN + 4 THẺ THỐNG KÊ
		JPanel pnTop = new JPanel(new BorderLayout(0, 15));
		pnTop.setOpaque(false);

		lbTitle = new JLabel("TỔNG QUAN");
		lbTitle.setFont(fontTieuDe);
		lbTitle.setForeground(new Color(139, 69, 19));
		pnTop.add(lbTitle, BorderLayout.NORTH);

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
			JPanel card = taoTheThongKe(tieuDeThe[i], dsGiaTriThe[i], fontThuong, coChuThe[i]);
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
		JPanel pnBestSellers = taoPanelNenNau();
		pnBestSellers.setLayout(new BorderLayout(0, 15));
		pnBestSellers.setBorder(new EmptyBorder(20, 25, 25, 30));

		JLabel lbBestSellerTitle = new JLabel("Món bán chạy hôm nay");
		lbBestSellerTitle.setFont(fontDam);
		lbBestSellerTitle.setForeground(Color.WHITE);
		pnBestSellers.add(lbBestSellerTitle, BorderLayout.NORTH);

		modelMonBanChay = new DefaultTableModel(new Object[]{"Đồ uống", "Số lượng"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblMonBanChay = new JTable(modelMonBanChay);
		tblMonBanChay.setFont(new Font("Arial", Font.PLAIN, 13));
		tblMonBanChay.setForeground(new Color(220, 200, 185));
		tblMonBanChay.setBackground(new Color(95, 58, 30));
		tblMonBanChay.setRowHeight(32);
		tblMonBanChay.setShowGrid(false);
		tblMonBanChay.setIntercellSpacing(new Dimension(0, 0));
		tblMonBanChay.setSelectionBackground(new Color(120, 80, 45));
		tblMonBanChay.setSelectionForeground(Color.WHITE);
		tblMonBanChay.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
		tblMonBanChay.getTableHeader().setBackground(new Color(139, 69, 19));
		tblMonBanChay.getTableHeader().setForeground(Color.WHITE);
		tblMonBanChay.getTableHeader().setBorder(null);
		tblMonBanChay.getColumnModel().getColumn(1).setMaxWidth(80);

		JScrollPane scrollMonBanChay = new JScrollPane(tblMonBanChay);
		scrollMonBanChay.setOpaque(false);
		scrollMonBanChay.getViewport().setOpaque(false);
		scrollMonBanChay.setBorder(null);

		pnBestSellers.add(scrollMonBanChay, BorderLayout.CENTER);

		// NÚT SẮP XẾP
		JPanel pnBtnSort = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
		pnBtnSort.setOpaque(false);

		btnBanChay = taoNutNenNau("Bán chạy ▲", fontDam);
		btnBanCham = taoNutNenNau("Bán chậm ▼", fontDam);

		pnBtnSort.add(btnBanChay);
		pnBtnSort.add(btnBanCham);
		pnBestSellers.add(pnBtnSort, BorderLayout.SOUTH);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.68;
		gbc.insets = new Insets(0, 0, 0, 22);
		pnBottom.add(pnBestSellers, gbc);

		// 3.2 KHUNG CẦN CHÚ Ý (BÊN PHẢI)
		JPanel pnAttention = taoPanelNenNau();
		pnAttention.setLayout(new BorderLayout(0, 20));
		pnAttention.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel lbAttentionTitle = new JLabel("Cần chú ý");
		lbAttentionTitle.setFont(fontDam);
		lbAttentionTitle.setForeground(Color.WHITE);
		pnAttention.add(lbAttentionTitle, BorderLayout.NORTH);

		modelChuY = new DefaultListModel<>();
		modelChuY.addElement("(i)  Bàn 4 chờ thanh toán");
		modelChuY.addElement("(i)  Sữa sắp hết hạn");

		listChuY = new JList<>(modelChuY);
		listChuY.setFont(new Font("Arial", Font.PLAIN, 13));
		listChuY.setForeground(new Color(220, 200, 185));
		listChuY.setBackground(new Color(95, 58, 30));
		listChuY.setSelectionBackground(new Color(120, 80, 45));
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
		JPanel pnBtnWrapThemChuY = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
		pnBtnWrapThemChuY.setOpaque(false);

		btnThemChuY = taoNutNenNau("+ Thêm chú ý", fontDam);
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

	/** Tạo một thẻ thống kê nền nâu, chữ trắng. */
	private JPanel taoTheThongKe(String tieuDe, JLabel lbGiaTri, Font fontThuong, int coChu) {
		JPanel card = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(139, 69, 19));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
				g2.dispose();
			}
		};
		card.setOpaque(false);
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(new EmptyBorder(18, 15, 18, 15));

		JLabel lbCardTitle = new JLabel(tieuDe);
		lbCardTitle.setFont(fontThuong);
		lbCardTitle.setForeground(new Color(220, 200, 185));
		lbCardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		lbGiaTri.setFont(new Font("Arial", Font.BOLD, coChu));
		lbGiaTri.setForeground(Color.WHITE);
		lbGiaTri.setAlignmentX(Component.CENTER_ALIGNMENT);

		card.add(lbCardTitle);
		card.add(Box.createVerticalStrut(18));
		card.add(lbGiaTri);
		return card;
	}

	/** Tạo panel nền nâu bo góc dùng cho các khối nội dung. */
	private JPanel taoPanelNenNau() {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(139, 69, 19));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
				g2.dispose();
			}
		};
		panel.setOpaque(false);
		return panel;
	}

	/** Tạo nút nền nâu dùng trong các khối tối. */
	private JButton taoNutNenNau(String text, Font font) {
		JButton btn = new JButton(text);
		btn.setFont(font);
		btn.setBackground(new Color(95, 58, 30));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setPreferredSize(new Dimension(130, 32));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	private String taoNhanNguoiDung() {
		if (Auth.laQuanLy()) {
			return "Quản lý: " + Auth.getTenHienThi();
		}
		return "Nhân viên: " + Auth.getTenHienThi();
	}

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnBanChay.setActionCommand("BAN_CHAY");
		btnBanCham.setActionCommand("BAN_CHAM");
		btnThemChuY.setActionCommand("THEM_CHU_Y");

		btnBanChay.addActionListener(this);
		btnBanCham.addActionListener(this);
		btnThemChuY.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String lenh = e.getActionCommand();

		if (lenh.equals("BAN_CHAY")) {
			sapXepGiam = true;
			capNhatBangMonBanChay();
		} else if (lenh.equals("BAN_CHAM")) {
			sapXepGiam = false;
			capNhatBangMonBanChay();
		} else if (lenh.equals("THEM_CHU_Y")) {
			themChuY();
		}
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp lại toàn bộ số liệu từ DSHoaDonBUS. */
	public void capNhatDuLieu() {
		lbDoanhThu.setText(DSHoaDonBUS.dinhDangTien(dsHoaDonBUS.doanhThuHomNay()));
		lbSoDon.setText(dsHoaDonBUS.soDonHomNay() + " đơn");
		lbSoMon.setText(dsHoaDonBUS.soLuongMonHomNay() + " món");

		List<ChiTietHoaDonDTO> dsBanChay = dsHoaDonBUS.layMonBanChay(true);
		if (dsBanChay.isEmpty()) {
			lbMonBanChayNhat.setText("Chưa có");
		} else {
			lbMonBanChayNhat.setText(dsBanChay.get(0).getTenMon());
		}

		capNhatBangMonBanChay();
	}

	/** Nạp lại bảng món bán chạy theo thứ tự đang chọn. */
	private void capNhatBangMonBanChay() {
		List<ChiTietHoaDonDTO> dsMon = dsHoaDonBUS.layMonBanChay(sapXepGiam);

		modelMonBanChay.setRowCount(0);
		for (int i = 0; i < dsMon.size(); i++) {
			ChiTietHoaDonDTO mon = dsMon.get(i);
			modelMonBanChay.addRow(new Object[]{mon.getTenMon(), mon.getSoLuong()});
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
