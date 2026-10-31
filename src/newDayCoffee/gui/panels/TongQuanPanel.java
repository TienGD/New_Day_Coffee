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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import newDayCoffee.bus.NhanVienBUS;
import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.utils.Auth;

public class TongQuanPanel extends JPanel {
	private JLabel lbBranch;
	private JLabel lbTitle;
	private JLabel lbQuanLy;

	// Các nhãn giá trị trong 4 thẻ thống kê
	private final JLabel[] dsGiaTriThe = new JLabel[4];

	// Bảng món bán chạy/chậm và ghi chú
	private DefaultTableModel modelMonBanChay;
	private DefaultListModel<String> modelChuY;
	private List<ChiTietHoaDonDTO> dsThongKeMon = new ArrayList<>();

	// BUS lấy dữ liệu thống kê
	private final DSHoaDonBUS dsHoaDonBUS = DSHoaDonBUS.getInstance();
	private final NhanVienBUS nhanVienBUS = NhanVienBUS.getInstance();

	public TongQuanPanel() {
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

		// Nút thông tin Ca két
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

		// Nút thông tin Quản lý
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
		JLabel lbQuanLyBadge = new JLabel("Quản lý: ...");
		lbQuanLyBadge.setFont(new Font("Arial", Font.PLAIN, 13));
		lbQuanLyBadge.setForeground(Color.WHITE);
		badgeQuanLy.add(lbQuanLyBadge);
		lbQuanLy = lbQuanLyBadge;

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
			"Doanh thu hôm nay", "Đơn đã hoàn tất", "Món bán chạy nhất", "Nhân viên"
		};
		String[] giaTriThe = {"0 Đ", "0 đơn", "-", "0 người"};

		for (int i = 0; i < tieuDeThe.length; i++) {
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

			JLabel lbCardTitle = new JLabel(tieuDeThe[i]);
			lbCardTitle.setFont(fontThuong);
			lbCardTitle.setForeground(new Color(220, 200, 185));
			lbCardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

			JLabel lbCardValue = new JLabel(giaTriThe[i]);
			// Thẻ "Món bán chạy nhất" có thể chứa tên món dài nên dùng cỡ chữ nhỏ hơn
			lbCardValue.setFont(new Font("Arial", Font.BOLD, i == 2 ? 15 : 20));
			lbCardValue.setForeground(Color.WHITE);
			lbCardValue.setAlignmentX(Component.CENTER_ALIGNMENT);
			dsGiaTriThe[i] = lbCardValue;

			card.add(lbCardTitle);
			card.add(Box.createVerticalStrut(18));
			card.add(lbCardValue);

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
		JPanel pnBestSellers = new JPanel(new BorderLayout(0, 15)) {
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
		pnBestSellers.setOpaque(false);
		pnBestSellers.setBorder(new EmptyBorder(20, 25, 25, 30));

		JLabel lbBestSellerTitle = new JLabel("Món bán chạy hôm nay");
		lbBestSellerTitle.setFont(fontDam);
		lbBestSellerTitle.setForeground(Color.WHITE);
		pnBestSellers.add(lbBestSellerTitle, BorderLayout.NORTH);

		String[] cotMonBanChay = {"Đồ uống", "Số lượng"};
		modelMonBanChay = new DefaultTableModel(cotMonBanChay, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable tblMonBanChay = new JTable(modelMonBanChay);
		tblMonBanChay.setFont(new Font("Arial", Font.PLAIN, 13));
		tblMonBanChay.setForeground(new Color(220, 200, 185));
		tblMonBanChay.setBackground(new Color(95, 58, 30));
		tblMonBanChay.setRowHeight(32);
		tblMonBanChay.setShowGrid(false);
		tblMonBanChay.setIntercellSpacing(new java.awt.Dimension(0, 0));
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

		JButton btnBanChay = new JButton("Bán chạy ▲");
		btnBanChay.setFont(fontDam);
		btnBanChay.setBackground(new Color(95, 58, 30));
		btnBanChay.setForeground(Color.WHITE);
		btnBanChay.setFocusPainted(false);
		btnBanChay.setBorderPainted(false);
		btnBanChay.setPreferredSize(new Dimension(130, 32));
		btnBanChay.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JButton btnBanCham = new JButton("Bán chậm ▼");
		btnBanCham.setFont(fontDam);
		btnBanCham.setBackground(new Color(95, 58, 30));
		btnBanCham.setForeground(Color.WHITE);
		btnBanCham.setFocusPainted(false);
		btnBanCham.setBorderPainted(false);
		btnBanCham.setPreferredSize(new Dimension(130, 32));
		btnBanCham.setCursor(new Cursor(Cursor.HAND_CURSOR));

		pnBtnSort.add(btnBanChay);
		pnBtnSort.add(btnBanCham);
		pnBestSellers.add(pnBtnSort, BorderLayout.SOUTH);

		// Sự kiện sắp xếp danh sách món theo số lượng bán
		btnBanChay.addActionListener(e -> sapXepMonBanChay(false));
		btnBanCham.addActionListener(e -> sapXepMonBanChay(true));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.68;
		gbc.insets = new Insets(0, 0, 0, 22);
		pnBottom.add(pnBestSellers, gbc);

		// 3.2 KHUNG CẦN CHÚ Ý (BÊN PHẢI)
		JPanel pnAttention = new JPanel(new BorderLayout(0, 20)) {
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
		pnAttention.setOpaque(false);
		pnAttention.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel lbAttentionTitle = new JLabel("Cần chú ý");
		lbAttentionTitle.setFont(fontDam);
		lbAttentionTitle.setForeground(Color.WHITE);
		pnAttention.add(lbAttentionTitle, BorderLayout.NORTH);

		modelChuY = new DefaultListModel<>();
		modelChuY.addElement("(i)  Bàn 4 chờ thanh toán");
		modelChuY.addElement("(i)  Sữa sắp hết hạn");

		JList<String> listChuY = new JList<>(modelChuY);
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

		JButton btnThemChuYNew = new JButton("+ Thêm chú ý");
		btnThemChuYNew.setFont(fontDam);
		btnThemChuYNew.setBackground(new Color(95, 58, 30));
		btnThemChuYNew.setForeground(Color.WHITE);
		btnThemChuYNew.setFocusPainted(false);
		btnThemChuYNew.setBorderPainted(false);
		btnThemChuYNew.setPreferredSize(new Dimension(145, 34));
		btnThemChuYNew.setCursor(new Cursor(Cursor.HAND_CURSOR));

		pnBtnWrapThemChuY.add(btnThemChuYNew);
		pnAttention.add(pnBtnWrapThemChuY, BorderLayout.SOUTH);

		// Sự kiện thêm ghi chú cần chú ý
		btnThemChuYNew.addActionListener(e -> themChuY());

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.32;
		gbc.insets = new Insets(0, 0, 0, 0);
		pnBottom.add(pnAttention, gbc);

		pnMainContent.add(pnBottom, BorderLayout.CENTER);
		add(pnMainContent, BorderLayout.CENTER);

		// Nạp số liệu thật từ BUS khi panel được tạo
		capNhatDuLieu();
	}

	// ==================== KHU VỰC XỬ LÝ SỰ KIỆN ====================
	private void themChuY() {
		String noiDung = JOptionPane.showInputDialog(this,
				"Nhập nội dung cần chú ý:", "Thêm chú ý", JOptionPane.PLAIN_MESSAGE);
		if (noiDung == null || noiDung.trim().isEmpty()) {
			return;
		}
		modelChuY.addElement("(i)  " + noiDung.trim());
	}

	/** Sắp xếp bảng món theo số lượng bán tăng dần (banCham = true) hoặc giảm dần. */
	private void sapXepMonBanChay(boolean tangDan) {
		List<ChiTietHoaDonDTO> dsSapXep = new ArrayList<>(dsThongKeMon);
		dsSapXep.sort((a, b) -> tangDan
				? Integer.compare(a.getSoLuong(), b.getSoLuong())
				: Integer.compare(b.getSoLuong(), a.getSoLuong()));

		modelMonBanChay.setRowCount(0);
		if (dsSapXep.isEmpty()) {
			modelMonBanChay.addRow(new Object[] {"Chưa có dữ liệu", 0});
			return;
		}
		for (ChiTietHoaDonDTO ct : dsSapXep) {
			modelMonBanChay.addRow(new Object[] {ct.getTenMon(), ct.getSoLuong()});
		}
	}

	// ==================== KHU VỰC NẠP / HIỂN THỊ DỮ LIỆU ====================
	/** Làm mới toàn bộ số liệu, được MainFrame gọi khi chuyển màn hình. */
	public void capNhatDuLieu() {
		capNhatThongTinNguoiDung();

		double doanhThu = dsHoaDonBUS.doanhThuHomNay();
		int soDon = dsHoaDonBUS.demDonHoanTat();
		dsThongKeMon = dsHoaDonBUS.thongKeSoLuongMon();

		String tenBanChay = "-";
		if (!dsThongKeMon.isEmpty()) {
			tenBanChay = dsThongKeMon.get(0).getTenMon();
		}

		if (dsGiaTriThe[0] != null) {
			dsGiaTriThe[0].setText(dinhDangTien(doanhThu));
		}
		if (dsGiaTriThe[1] != null) {
			dsGiaTriThe[1].setText(soDon + " đơn");
		}
		if (dsGiaTriThe[2] != null) {
			dsGiaTriThe[2].setText(tenBanChay);
		}
		if (dsGiaTriThe[3] != null) {
			dsGiaTriThe[3].setText(nhanVienBUS.laySoLuongNhanVien() + " người");
		}

		sapXepMonBanChay(false);
	}

	private void capNhatThongTinNguoiDung() {
		if (Auth.getCurrentUser() == null) {
			lbQuanLy.setText("Quản lý: -");
			return;
		}
		String vaiTro = Auth.isManager() ? "Quản lý: " : "Nhân viên: ";
		lbQuanLy.setText(vaiTro + Auth.getCurrentUser().getTenNV());
	}

	private String dinhDangTien(double soTien) {
		return String.format(Locale.US, "%,.0f", soTien).replace(',', '.') + " Đ";
	}
}
