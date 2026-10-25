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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BanHangPOSPanel extends JPanel {
	private JLabel lbHeaderTitle;
	private JTextField txtTimKiem;
	private JTable tblDonHang;
	private DefaultTableModel modelDonHang;
	private JLabel lbTongTienGiaTri;
	private JButton btnXoa;
	private JButton btnThanhToan;
	private String HintTimKiem = "Tìm kiếm món";

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
		JLabel lbQuanLy = new JLabel("Quản lý: Lâm Nguyễn Như Ý");
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
			pnFilterBar.add(btnLoai);
		}

		pnTopFilter.add(pnFilterBar, BorderLayout.CENTER);
		pnLeftCol.add(pnTopFilter, BorderLayout.NORTH);

		// Lưới danh sách 6 thẻ món ăn (2 hàng x 3 cột)
		JPanel pnGridMon = new JPanel(new GridLayout(2, 3, 18, 18));
		pnGridMon.setOpaque(false);
		pnGridMon.setBorder(new EmptyBorder(5, 5, 5, 5));

		String[] tenCacMon = {
			"Cà phê đen", "Bạc sỉu", "Trà Đào",
			"Trà Măng cục", "Bánh Tiramisu", "Trà mãng cầu"
		};
		int[] soLuongMacDinh = {1, 0, 0, 0, 0, 0};

		for (int i = 0; i < tenCacMon.length; i++) {
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
			pnImgHolder.setPreferredSize(new Dimension(0, 105));
			cardMon.add(pnImgHolder, BorderLayout.NORTH);

			JPanel pnInfo = new JPanel();
			pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.Y_AXIS));
			pnInfo.setOpaque(false);
			pnInfo.setBorder(new EmptyBorder(8, 12, 10, 12));

			JLabel lbTenMon = new JLabel(tenCacMon[i]);
			lbTenMon.setFont(new Font("Arial", Font.BOLD, 14));
			lbTenMon.setForeground(Color.BLACK);
			lbTenMon.setAlignmentX(Component.LEFT_ALIGNMENT);

			JLabel lbMoTa = new JLabel("Đậm đà, đánh thức ngày mới");
			lbMoTa.setFont(new Font("Arial", Font.PLAIN, 10));
			lbMoTa.setForeground(Color.GRAY);
			lbMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);

			JLabel lbGia = new JLabel("15.000Đ");
			lbGia.setFont(new Font("Arial", Font.BOLD, 12));
			lbGia.setForeground(new Color(255, 80, 80));
			lbGia.setAlignmentX(Component.LEFT_ALIGNMENT);

			JPanel pnSpinner = new JPanel(new BorderLayout());
			pnSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
			pnSpinner.setMaximumSize(new Dimension(200, 24));
			pnSpinner.setPreferredSize(new Dimension(140, 24));

			JLabel lbSoLuong = new JLabel(String.valueOf(soLuongMacDinh[i]), SwingConstants.CENTER);
			lbSoLuong.setOpaque(true);
			lbSoLuong.setBackground(new Color(215, 215, 215));
			lbSoLuong.setFont(new Font("Arial", Font.BOLD, 12));

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
			pnGridMon.add(cardMon);
		}

		pnLeftCol.add(pnGridMon, BorderLayout.CENTER);

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
		modelDonHang = new DefaultTableModel(cotBang, 0);
		modelDonHang.addRow(new Object[]{"1", "Cà phê đen", "1", "15.000Đ", "15.000Đ", "Ít đường"});
		modelDonHang.addRow(new Object[]{"", "", "", "", "", ""});
		modelDonHang.addRow(new Object[]{"", "", "", "", "", ""});
		modelDonHang.addRow(new Object[]{"", "", "", "", "", ""});

		tblDonHang = new JTable(modelDonHang);
		tblDonHang.setRowHeight(42);
		tblDonHang.setBackground(new Color(222, 222, 222));
		tblDonHang.setGridColor(new Color(120, 120, 120));
		tblDonHang.setFont(new Font("Arial", Font.PLAIN, 12));
		tblDonHang.getTableHeader().setBackground(new Color(222, 222, 222));
		tblDonHang.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 11));
		tblDonHang.getTableHeader().setPreferredSize(new Dimension(0, 32));

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

		lbTongTienGiaTri = new JLabel("15.000Đ");
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
	}
}
