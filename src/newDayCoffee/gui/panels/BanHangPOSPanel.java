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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSDoUongBUS;
import newDayCoffee.bus.DoUongBUS;
import newDayCoffee.bus.HoaDonBUS;
import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.utils.Auth;

public class BanHangPOSPanel extends JPanel {
	private JLabel lbHeaderTitle;
	private JTextField txtTimKiem;
	private JPanel pnGridMon;
	private JTable tblDonHang;
	private DefaultTableModel modelDonHang;
	private JLabel lbTongTienGiaTri;
	private JButton btnXoa;
	private JButton btnThanhToan;
	private JLabel lbQuanLy;
	private String HintTimKiem = "Tìm kiếm món";

	// BUS dùng chung
	private final DSDoUongBUS dsDoUongBUS = DSDoUongBUS.getInstance();
	private final HoaDonBUS hoaDonBUS = HoaDonBUS.getInstance();

	// Giỏ hàng hiện tại của ca bán
	private final List<ChiTietHoaDonDTO> gioHang = new ArrayList<>();

	// Ánh xạ mã món -> nhãn số lượng trên thẻ món để cập nhật nhanh
	private final Map<String, JLabel> mapNhanSoLuong = new HashMap<>();

	// Trạng thái lọc danh mục
	private String nhomDangLoc = "Tất cả";

	public BanHangPOSPanel() {
		setLayout(new BorderLayout());
		setOpaque(false);

		Font fontTieuDe = new Font("Arial", Font.BOLD, 22);
		Font fontDam = new Font("Arial", Font.BOLD, 15);
		Font fontThuong = new Font("Arial", Font.PLAIN, 13);

		// HEADER PHÍA TRÊN
		JPanel pnHeader = new JPanel(new BorderLayout());
		pnHeader.setBackground(new Color(139, 69, 19));
		pnHeader.setPreferredSize(new Dimension(0, 75));
		pnHeader.setBorder(new EmptyBorder(0, 30, 0, 30));

		lbHeaderTitle = new JLabel("BÁN HÀNG");
		lbHeaderTitle.setFont(fontTieuDe);
		lbHeaderTitle.setForeground(Color.WHITE);
		pnHeader.add(lbHeaderTitle, BorderLayout.WEST);

		JPanel pnBadges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 18));
		pnBadges.setOpaque(false);

		// Nút thông tin Ca kết
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
		lbCaKet.setFont(fontThuong);
		lbCaKet.setForeground(Color.WHITE);
		badgeCaKet.add(lbCaKet);

		// Nút thông tin người đang đăng nhập
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
		lbQuanLy = new JLabel("Quản lý: ...");
		lbQuanLy.setFont(fontThuong);
		lbQuanLy.setForeground(Color.WHITE);
		badgeQuanLy.add(lbQuanLy);

		pnBadges.add(badgeCaKet);
		pnBadges.add(badgeQuanLy);
		pnHeader.add(pnBadges, BorderLayout.EAST);

		add(pnHeader, BorderLayout.NORTH);

		// NỘI DUNG CHÍNH (CHIA 2 CỘT: DANH SÁCH MÓN & THÔNG TIN ĐƠN HÀNG)
		JPanel pnMainContent = new JPanel(new GridBagLayout());
		pnMainContent.setOpaque(false);
		pnMainContent.setBorder(new EmptyBorder(15, 25, 20, 25));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;

		// ==================== CỘT TRÁI: DANH SÁCH CÁC MÓN ====================
		JPanel pnLeftCol = new JPanel(new BorderLayout(0, 12));
		pnLeftCol.setOpaque(false);

		// Tiêu đề + Thanh tìm kiếm & Nút lọc danh mục
		JPanel pnTopFilter = new JPanel(new BorderLayout(0, 10));
		pnTopFilter.setOpaque(false);

		JLabel lbDanhSachMon = new JLabel("Danh sách các món");
		lbDanhSachMon.setFont(new Font("Arial", Font.BOLD, 18));
		lbDanhSachMon.setForeground(new Color(139, 69, 19));
		pnTopFilter.add(lbDanhSachMon, BorderLayout.NORTH);

		JPanel pnFilterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pnFilterBar.setOpaque(false);

		// Ô tìm kiếm món bo góc
		txtTimKiem = new JTextField() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(186, 145, 114));
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.setColor(new Color(139, 69, 19));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.dispose();

				super.paintComponent(g);

				if (getText().isEmpty()) {
					Graphics2D gHint = (Graphics2D) g.create();
					gHint.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					gHint.setColor(Color.WHITE);
					gHint.setFont(fontThuong);
					Insets insets = getInsets();
					int textY = (getHeight() - gHint.getFontMetrics().getHeight()) / 2 + gHint.getFontMetrics().getAscent();
					gHint.drawString(HintTimKiem, insets.left, textY);
					gHint.dispose();
				}
			}
		};
		txtTimKiem.setOpaque(false);
		txtTimKiem.setForeground(Color.WHITE);
		txtTimKiem.setPreferredSize(new Dimension(180, 34));
		txtTimKiem.setBorder(new EmptyBorder(5, 12, 5, 12));
		pnFilterBar.add(txtTimKiem);

		// Các nút lọc loại món (Tất cả, Cafe, Trà, Bánh)
		String[] dsLoai = {"Tất cả", "Cafe", "Trà", "Bánh"};
		for (int i = 0; i < dsLoai.length; i++) {
			final String nhom = dsLoai[i];
			JButton btnLoai = new JButton(dsLoai[i]) {
				@Override
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(new Color(186, 145, 114));
					g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
					g2.setColor(new Color(139, 69, 19));
					g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
					g2.dispose();
					super.paintComponent(g);
				}
			};
			btnLoai.setOpaque(false);
			btnLoai.setContentAreaFilled(false);
			btnLoai.setBorderPainted(false);
			btnLoai.setFocusPainted(false);
			btnLoai.setForeground(Color.WHITE);
			btnLoai.setFont(fontThuong);
			btnLoai.setPreferredSize(new Dimension(78, 34));
			btnLoai.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnLoai.addActionListener(e -> {
				nhomDangLoc = nhom;
				capNhatLuoiMon();
			});
			pnFilterBar.add(btnLoai);
		}

		pnTopFilter.add(pnFilterBar, BorderLayout.CENTER);
		pnLeftCol.add(pnTopFilter, BorderLayout.NORTH);

		// Lưới danh sách thẻ món (số hàng tự động theo dữ liệu)
		pnGridMon = new JPanel(new GridLayout(0, 3, 18, 18));
		pnGridMon.setOpaque(false);
		pnGridMon.setBorder(new EmptyBorder(5, 5, 5, 5));

		JScrollPane scrollGridMon = new JScrollPane(pnGridMon);
		scrollGridMon.setOpaque(false);
		scrollGridMon.getViewport().setOpaque(false);
		scrollGridMon.setBorder(null);
		scrollGridMon.getVerticalScrollBar().setUnitIncrement(16);
		pnLeftCol.add(scrollGridMon, BorderLayout.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.62;
		gbc.insets = new Insets(0, 0, 0, 20);
		pnMainContent.add(pnLeftCol, gbc);

		// ==================== CỘT PHẢI: THÔNG TIN ĐƠN HÀNG ====================
		JPanel pnRightCol = new JPanel(new BorderLayout(0, 12));
		pnRightCol.setBackground(new Color(172, 130, 98));
		pnRightCol.setBorder(BorderFactory.createCompoundBorder(
			new LineBorder(new Color(120, 75, 40), 1),
			new EmptyBorder(15, 18, 18, 18)
		));

		JPanel pnOrderTop = new JPanel(new BorderLayout(0, 8));
		pnOrderTop.setOpaque(false);

		JLabel lbOrderTitle = new JLabel("Thông tin đơn hàng");
		lbOrderTitle.setFont(new Font("Arial", Font.BOLD, 18));
		lbOrderTitle.setForeground(Color.WHITE);
		pnOrderTop.add(lbOrderTitle, BorderLayout.NORTH);

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
		tblDonHang.setRowHeight(42);
		tblDonHang.setBackground(new Color(222, 222, 222));
		tblDonHang.setGridColor(new Color(120, 120, 120));
		tblDonHang.setFont(new Font("Arial", Font.PLAIN, 12));
		tblDonHang.getTableHeader().setBackground(new Color(222, 222, 222));
		tblDonHang.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 11));
		tblDonHang.getTableHeader().setPreferredSize(new Dimension(0, 32));
		tblDonHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < tblDonHang.getColumnCount(); i++) {
			tblDonHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		tblDonHang.getColumnModel().getColumn(0).setPreferredWidth(35);
		tblDonHang.getColumnModel().getColumn(1).setPreferredWidth(95);
		tblDonHang.getColumnModel().getColumn(2).setPreferredWidth(30);

		JScrollPane scrollBang = new JScrollPane(tblDonHang);
		scrollBang.getViewport().setBackground(new Color(222, 222, 222));
		scrollBang.setBorder(new LineBorder(new Color(90, 90, 90), 1));
		pnRightCol.add(scrollBang, BorderLayout.CENTER);

		// Phần dưới: Tổng tiền + Nút Xóa & Thanh toán
		JPanel pnOrderBottom = new JPanel(new BorderLayout(0, 15));
		pnOrderBottom.setOpaque(false);

		JPanel pnTotalRow = new JPanel(new BorderLayout());
		pnTotalRow.setOpaque(false);

		JLabel lbTongTienText = new JLabel("Tổng tiền:");
		lbTongTienText.setFont(fontDam);
		lbTongTienText.setForeground(new Color(128, 25, 25));

		lbTongTienGiaTri = new JLabel("0Đ");
		lbTongTienGiaTri.setFont(fontDam);
		lbTongTienGiaTri.setForeground(new Color(128, 25, 25));

		pnTotalRow.add(lbTongTienText, BorderLayout.WEST);
		pnTotalRow.add(lbTongTienGiaTri, BorderLayout.EAST);
		pnOrderBottom.add(pnTotalRow, BorderLayout.NORTH);

		JPanel pnOrderButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		pnOrderButtons.setOpaque(false);

		btnXoa = new JButton("Xóa") {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(186, 145, 114));
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.setColor(new Color(92, 32, 0));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		btnXoa.setOpaque(false);
		btnXoa.setContentAreaFilled(false);
		btnXoa.setBorderPainted(false);
		btnXoa.setFocusPainted(false);
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFont(new Font("Arial", Font.BOLD, 14));
		btnXoa.setPreferredSize(new Dimension(75, 42));
		btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btnThanhToan = new JButton("Thanh toán") {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(78, 30, 0));
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		btnThanhToan.setOpaque(false);
		btnThanhToan.setContentAreaFilled(false);
		btnThanhToan.setBorderPainted(false);
		btnThanhToan.setFocusPainted(false);
		btnThanhToan.setForeground(Color.WHITE);
		btnThanhToan.setFont(new Font("Arial", Font.BOLD, 14));
		btnThanhToan.setPreferredSize(new Dimension(130, 42));
		btnThanhToan.setCursor(new Cursor(Cursor.HAND_CURSOR));

		pnOrderButtons.add(btnXoa);
		pnOrderButtons.add(btnThanhToan);
		pnOrderBottom.add(pnOrderButtons, BorderLayout.CENTER);

		pnRightCol.add(pnOrderBottom, BorderLayout.SOUTH);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.38;
		gbc.insets = new Insets(0, 0, 0, 0);
		pnMainContent.add(pnRightCol, gbc);

		add(pnMainContent, BorderLayout.CENTER);

		// ==================== KHU VỰC GẮN SỰ KIỆN ====================
		btnXoa.addActionListener(e -> xuLyXoaMon());
		btnThanhToan.addActionListener(e -> xuLyThanhToan());

		// Tìm món trực tiếp khi gõ
		txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				capNhatLuoiMon();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				capNhatLuoiMon();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				capNhatLuoiMon();
			}
		});

		// Nạp dữ liệu ban đầu
		capNhatDuLieu();
	}

	// ==================== KHU VỰC TẠO THẺ MÓN ====================
	/** Tạo một thẻ món ăn/đồ uống kèm nút tăng giảm số lượng. */
	private JPanel taoTheMon(DoUongDTO du) {
		JPanel cardMon = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
				g2.setColor(new Color(28, 28, 28));
				g2.fillRoundRect(0, 0, getWidth(), (int) (getHeight() * 0.48), 18, 18);
				g2.fillRect(0, (int) (getHeight() * 0.48) - 10, getWidth(), 10);
				g2.dispose();
			}
		};
		cardMon.setOpaque(false);

		JPanel pnImgHolder = new JPanel();
		pnImgHolder.setOpaque(false);
		pnImgHolder.setPreferredSize(new Dimension(0, 90));
		cardMon.add(pnImgHolder, BorderLayout.NORTH);

		JPanel pnInfo = new JPanel();
		pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.Y_AXIS));
		pnInfo.setOpaque(false);
		pnInfo.setBorder(new EmptyBorder(8, 12, 10, 12));

		JLabel lbTenMon = new JLabel(du.getTenMon());
		lbTenMon.setFont(new Font("Arial", Font.BOLD, 14));
		lbTenMon.setForeground(Color.BLACK);
		lbTenMon.setAlignmentX(Component.LEFT_ALIGNMENT);

		String moTa = du.getMoTa() == null || du.getMoTa().trim().isEmpty()
				? "Đậm đà, đánh thức ngày mới" : du.getMoTa();
		JLabel lbMoTa = new JLabel(moTa);
		lbMoTa.setFont(new Font("Arial", Font.PLAIN, 10));
		lbMoTa.setForeground(Color.GRAY);
		lbMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lbGia = new JLabel(DoUongBUS.dinhDangGia(du.getDonGia()));
		lbGia.setFont(new Font("Arial", Font.BOLD, 12));
		lbGia.setForeground(new Color(255, 80, 80));
		lbGia.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel pnSpinner = new JPanel(new BorderLayout());
		pnSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnSpinner.setMaximumSize(new Dimension(200, 24));
		pnSpinner.setPreferredSize(new Dimension(140, 24));

		JLabel lbSoLuong = new JLabel(String.valueOf(laySoLuongTrongGio(du.getMaMon())), SwingConstants.CENTER);
		lbSoLuong.setOpaque(true);
		lbSoLuong.setBackground(new Color(215, 215, 215));
		lbSoLuong.setFont(new Font("Arial", Font.BOLD, 12));
		mapNhanSoLuong.put(du.getMaMon(), lbSoLuong);

		JButton btnTru = new JButton("-");
		btnTru.setBackground(new Color(92, 32, 0));
		btnTru.setForeground(Color.WHITE);
		btnTru.setFont(new Font("Arial", Font.BOLD, 13));
		btnTru.setFocusPainted(false);
		btnTru.setBorderPainted(false);
		btnTru.setPreferredSize(new Dimension(40, 24));
		btnTru.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JButton btnCong = new JButton("+");
		btnCong.setBackground(new Color(92, 32, 0));
		btnCong.setForeground(Color.WHITE);
		btnCong.setFont(new Font("Arial", Font.BOLD, 10));
		btnCong.setFocusPainted(false);
		btnCong.setBorderPainted(false);
		btnCong.setPreferredSize(new Dimension(40, 24));
		btnCong.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Sự kiện tăng / giảm số lượng món trong giỏ
		btnCong.addActionListener(e -> {
			themVaoGio(du);
			capNhatSoLuongTrenThe();
		});
		btnTru.addActionListener(e -> {
			botKhoiGio(du);
			capNhatSoLuongTrenThe();
		});

		pnSpinner.add(btnTru, BorderLayout.WEST);
		pnSpinner.add(lbSoLuong, BorderLayout.CENTER);
		pnSpinner.add(btnCong, BorderLayout.EAST);

		pnInfo.add(lbTenMon);
		pnInfo.add(Box.createVerticalStrut(3));
		pnInfo.add(lbMoTa);
		pnInfo.add(Box.createVerticalStrut(5));
		pnInfo.add(lbGia);
		pnInfo.add(Box.createVerticalStrut(6));
		pnInfo.add(pnSpinner);

		cardMon.add(pnInfo, BorderLayout.CENTER);
		return cardMon;
	}

	// ==================== KHU VỰC GIỎ HÀNG ====================
	private ChiTietHoaDonDTO timTrongGio(String maMon) {
		for (ChiTietHoaDonDTO ct : gioHang) {
			if (ct.getMaMon().equalsIgnoreCase(maMon)) {
				return ct;
			}
		}
		return null;
	}

	private int laySoLuongTrongGio(String maMon) {
		ChiTietHoaDonDTO ct = timTrongGio(maMon);
		return ct == null ? 0 : ct.getSoLuong();
	}

	/** Thêm 1 đơn vị món vào giỏ; nếu đã có thì tăng số lượng. */
	private void themVaoGio(DoUongDTO du) {
		ChiTietHoaDonDTO ct = timTrongGio(du.getMaMon());
		if (ct == null) {
			ct = new ChiTietHoaDonDTO("", du.getMaMon(), du.getTenMon(), 0, du.getDonGia());
			gioHang.add(ct);
		}
		ct.setSoLuong(ct.getSoLuong() + 1);
		capNhatGioHang();
	}

	/** Giảm 1 đơn vị món; nếu về 0 thì xóa khỏi giỏ. */
	private void botKhoiGio(DoUongDTO du) {
		ChiTietHoaDonDTO ct = timTrongGio(du.getMaMon());
		if (ct == null) {
			return;
		}
		ct.setSoLuong(ct.getSoLuong() - 1);
		if (ct.getSoLuong() <= 0) {
			gioHang.remove(ct);
		}
		capNhatGioHang();
	}

	/** Vẽ lại bảng đơn hàng và tổng tiền từ giỏ hiện tại. */
	private void capNhatGioHang() {
		modelDonHang.setRowCount(0);
		int stt = 1;
		for (ChiTietHoaDonDTO ct : gioHang) {
			modelDonHang.addRow(new Object[] {
				stt,
				ct.getTenMon(),
				ct.getSoLuong(),
				DoUongBUS.dinhDangGia(ct.getDonGia()),
				DoUongBUS.dinhDangGia(ct.getThanhTien()),
				""
			});
			stt++;
		}
		lbTongTienGiaTri.setText(dinhDangTien(hoaDonBUS.tinhTongTien(gioHang)));
		capNhatSoLuongTrenThe();
	}

	/** Cập nhật nhãn số lượng trên tất cả thẻ món theo giỏ hàng. */
	private void capNhatSoLuongTrenThe() {
		for (Map.Entry<String, JLabel> entry : mapNhanSoLuong.entrySet()) {
			entry.getValue().setText(String.valueOf(laySoLuongTrongGio(entry.getKey())));
		}
	}

	/** Xóa món đang chọn khỏi giỏ hàng. */
	private void xuLyXoaMon() {
		int dong = tblDonHang.getSelectedRow();
		if (dong < 0 || dong >= gioHang.size()) {
			JOptionPane.showMessageDialog(this,
					"Vui lòng chọn món cần xóa trong bảng đơn hàng.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		gioHang.remove(dong);
		capNhatGioHang();
	}

	// ==================== KHU VỰC THANH TOÁN ====================
	private void xuLyThanhToan() {
		if (gioHang.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Đơn hàng chưa có món nào. Vui lòng chọn món trước khi thanh toán.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String[] dsPhuongThuc = {"Tiền mặt", "Chuyển khoản"};
		int chon = JOptionPane.showOptionDialog(this,
				"Chọn phương thức thanh toán:", "Thanh toán",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, dsPhuongThuc, dsPhuongThuc[0]);
		if (chon < 0) {
			return;
		}
		String phuongThuc = dsPhuongThuc[chon];
		double tongTien = hoaDonBUS.tinhTongTien(gioHang);

		// Nhập tiền khách đưa, lặp lại nếu dữ liệu chưa hợp lệ
		String tienKhachDuaText = String.format(Locale.US, "%.0f", tongTien);
		while (true) {
			tienKhachDuaText = JOptionPane.showInputDialog(this,
					"Tổng tiền: " + dinhDangTien(tongTien)
							+ "\nNhập tiền khách đưa:",
					tienKhachDuaText);
			if (tienKhachDuaText == null) {
				return;
			}

			String loi = hoaDonBUS.kiemTraThanhToan(gioHang, tienKhachDuaText, phuongThuc);
			if (loi == null) {
				break;
			}
			JOptionPane.showMessageDialog(this, loi, "Thanh toán",
					JOptionPane.WARNING_MESSAGE);
		}

		HoaDonDTO hoaDon = hoaDonBUS.thanhToan(gioHang, tienKhachDuaText, phuongThuc, "");
		if (hoaDon == null) {
			JOptionPane.showMessageDialog(this, hoaDonBUS.getLoiGanNhat(),
					"Thanh toán thất bại", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Hiển thị hóa đơn và hỏi có xuất file hay không
		hienThiHoaDon(hoaDon);

		gioHang.clear();
		capNhatGioHang();
	}

	/** Hiển thị nội dung hóa đơn, cho phép xuất ra file TXT. */
	private void hienThiHoaDon(HoaDonDTO hoaDon) {
		String noiDung = hoaDonBUS.taoNoiDungHoaDon(hoaDon);

		JTextArea txtHoaDon = new JTextArea(noiDung);
		txtHoaDon.setEditable(false);
		txtHoaDon.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JScrollPane scroll = new JScrollPane(txtHoaDon);
		scroll.setPreferredSize(new Dimension(500, 420));

		int luaChon = JOptionPane.showConfirmDialog(this, scroll,
				"Hóa đơn " + hoaDon.getMaHD(), JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Lưu hóa đơn");
		chooser.setSelectedFile(new File("hoadon_" + hoaDon.getMaHD() + ".txt"));
		int ketQua = chooser.showSaveDialog(this);
		if (ketQua != JFileChooser.APPROVE_OPTION) {
			return;
		}

		boolean thanhCong = hoaDonBUS.xuatHoaDonRaFile(hoaDon, chooser.getSelectedFile().getAbsolutePath());
		if (thanhCong) {
			JOptionPane.showMessageDialog(this, "Đã xuất hóa đơn ra file.",
					"Thành công", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Xuất hóa đơn thất bại.",
					"Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	// ==================== KHU VỰC NẠP / HIỂN THỊ DỮ LIỆU ====================
	/** Làm mới panel, được MainFrame gọi khi chuyển sang màn hình bán hàng. */
	public void capNhatDuLieu() {
		capNhatThongTinNguoiDung();
		capNhatLuoiMon();
		capNhatGioHang();
	}

	private void capNhatThongTinNguoiDung() {
		if (Auth.getCurrentUser() == null) {
			lbQuanLy.setText("Quản lý: -");
			return;
		}
		String vaiTro = Auth.isManager() ? "Quản lý: " : "Nhân viên: ";
		lbQuanLy.setText(vaiTro + Auth.getCurrentUser().getTenNV());
	}

	/** Dựng lại lưới thẻ món theo từ khóa tìm kiếm và nhóm đang lọc. */
	private void capNhatLuoiMon() {
		pnGridMon.removeAll();
		mapNhanSoLuong.clear();

		List<DoUongDTO> dsMon = dsDoUongBUS.locVaTim(txtTimKiem.getText(), nhomDangLoc);
		if (dsMon.isEmpty()) {
			JLabel lbTrong = new JLabel("Không tìm thấy món phù hợp.");
			lbTrong.setFont(new Font("Arial", Font.ITALIC, 13));
			lbTrong.setForeground(Color.GRAY);
			pnGridMon.add(lbTrong);
		} else {
			for (DoUongDTO du : dsMon) {
				pnGridMon.add(taoTheMon(du));
			}
		}

		pnGridMon.revalidate();
		pnGridMon.repaint();
	}

	private String dinhDangTien(double soTien) {
		return String.format(Locale.US, "%,.0f", soTien).replace(',', '.') + "Đ";
	}
}
