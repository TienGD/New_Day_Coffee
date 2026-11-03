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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSDoUongBUS;
import newDayCoffee.bus.DoUongBUS;
import newDayCoffee.bus.HoaDonBUS;
import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.gui.MainFrame;
import newDayCoffee.utils.Auth;

/**
 * Màn hình bán hàng (POS).
 *
 * Panel nhận DSDoUongBUS để hiển thị thực đơn và HoaDonBUS để quản lý giỏ hàng,
 * thanh toán, in hóa đơn. Mọi sự kiện đều dùng ActionListener, không dùng lambda.
 */
public class BanHangPOSPanel extends JPanel implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbHeaderTitle;
	private JLabel lbNhanVien;
	private JTextField txtTimKiem;
	private JButton btnTimKiem;
	private JPanel pnGridMon;
	private JTable tblDonHang;
	private DefaultTableModel modelDonHang;
	private JLabel lbTongTienGiaTri;
	private JButton btnXoa;
	private JButton btnThanhToan;

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

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSDoUongBUS dsDoUongBUS;
	private final HoaDonBUS hoaDonBUS;
	private final MainFrame mainFrame;

	public BanHangPOSPanel(MainFrame mainFrame,DSDoUongBUS dsDoUongBUS, HoaDonBUS hoaDonBUS) {
		this.mainFrame = mainFrame;
		this.dsDoUongBUS = dsDoUongBUS;
		this.hoaDonBUS = hoaDonBUS;

		khoiTaoGiaoDien();
		ganSuKien();
		capNhatDanhSachMon();
		capNhatGioHang();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
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

		// Nhãn thông tin Ca kết
		JPanel badgeCaKet = taoBadgeHeader(155);
		JLabel lbCaKet = new JLabel("Ca kết đang mở");
		lbCaKet.setFont(fontThuong);
		lbCaKet.setForeground(Color.WHITE);
		badgeCaKet.add(lbCaKet);

		// Nhãn thông tin người đang đăng nhập
		JPanel badgeNhanVien = taoBadgeHeader(240);
		lbNhanVien = new JLabel(taoNhanNguoiDung());
		lbNhanVien.setFont(fontThuong);
		lbNhanVien.setForeground(Color.WHITE);
		badgeNhanVien.add(lbNhanVien);

		pnBadges.add(badgeCaKet);
		pnBadges.add(badgeNhanVien);
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

		btnTimKiem = taoNutLoc("Tìm", 60, fontThuong);
		pnFilterBar.add(btnTimKiem);

		// Các nút lọc loại món
		String[] dsLoai = {"Tất cả", "Cà phê", "Trà", "Trà sữa", "Bánh"};
		for (int i = 0; i < dsLoai.length; i++) {
			JButton btnLoai = taoNutLoc(dsLoai[i], 78, fontThuong);
			nhomCuaNutLoc.put(btnLoai, dsLoai[i]);
			btnLoai.addActionListener(this);
			pnFilterBar.add(btnLoai);
		}

		pnTopFilter.add(pnFilterBar, BorderLayout.CENTER);
		pnLeftCol.add(pnTopFilter, BorderLayout.NORTH);

		// Lưới danh sách thẻ món - được nạp động từ DSDoUongBUS
		pnGridMon = new JPanel(new GridLayout(0, 3, 18, 18));
		pnGridMon.setOpaque(false);
		pnGridMon.setBorder(new EmptyBorder(5, 5, 5, 5));

		JScrollPane scrollMon = new JScrollPane(pnGridMon);
		scrollMon.setOpaque(false);
		scrollMon.getViewport().setOpaque(false);
		scrollMon.setBorder(null);
		scrollMon.getVerticalScrollBar().setUnitIncrement(18);

		pnLeftCol.add(scrollMon, BorderLayout.CENTER);

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
	}

	/** Tạo panel nền nâu bo góc dùng trên header. */
	private JPanel taoBadgeHeader(int width) {
		JPanel badge = new JPanel(new GridBagLayout()) {
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
		badge.setOpaque(false);
		badge.setPreferredSize(new Dimension(width, 38));
		return badge;
	}

	/** Tạo nút lọc / tìm kiếm bo góc. */
	private JButton taoNutLoc(String text, int width, Font font) {
		JButton btn = new JButton(text) {
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
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setForeground(Color.WHITE);
		btn.setFont(font);
		btn.setPreferredSize(new Dimension(width, 34));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	/** Tạo thẻ hiển thị một món trong thực đơn. */
	private JPanel taoTheMon(DoUongDTO doUong) {
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

		JLabel lbTenMon = new JLabel(doUong.getTenMon());
		lbTenMon.setFont(new Font("Arial", Font.BOLD, 14));
		lbTenMon.setForeground(Color.BLACK);
		lbTenMon.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lbMoTa = new JLabel(doUong.getMoTa());
		lbMoTa.setFont(new Font("Arial", Font.PLAIN, 10));
		lbMoTa.setForeground(Color.GRAY);
		lbMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lbGia = new JLabel(DoUongBUS.dinhDangGia(doUong.getDonGia()));
		lbGia.setFont(new Font("Arial", Font.BOLD, 12));
		lbGia.setForeground(new Color(255, 80, 80));
		lbGia.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel pnSpinner = new JPanel(new BorderLayout());
		pnSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnSpinner.setMaximumSize(new Dimension(200, 24));
		pnSpinner.setPreferredSize(new Dimension(140, 24));

		JLabel lbSoLuong = new JLabel("0", SwingConstants.CENTER);
		lbSoLuong.setOpaque(true);
		lbSoLuong.setBackground(new Color(215, 215, 215));
		lbSoLuong.setFont(new Font("Arial", Font.BOLD, 12));
		nhanSoLuong.put(doUong.getMaMon(), lbSoLuong);

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

		// Gắn sự kiện cho nút tăng / giảm của thẻ
		maMonCuaNutTru.put(btnTru, doUong.getMaMon());
		maMonCuaNutCong.put(btnCong, doUong.getMaMon());
		btnTru.addActionListener(this);
		btnCong.addActionListener(this);

		return cardMon;
	}

	private String taoNhanNguoiDung() {
		if (Auth.laQuanLy()) {
			return "Quản lý: " + Auth.getTenHienThi();
		}
		return "Nhân viên: " + Auth.getTenHienThi();
	}

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnTimKiem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnThanhToan.addActionListener(this);
		// Nhấn Enter trong ô tìm kiếm cũng tìm
		txtTimKiem.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object nguon = e.getSource();

		if (nguon == btnTimKiem || nguon == txtTimKiem) {
			capNhatDanhSachMon();
		} else if (nguon == btnXoa) {
			xoaMonDangChon();
		} else if (nguon == btnThanhToan) {
			thanhToan();
		} else if (nhomCuaNutLoc.containsKey(nguon)) {
			nhomDangLoc = nhomCuaNutLoc.get(nguon);
			capNhatDanhSachMon();
		} else if (maMonCuaNutCong.containsKey(nguon)) {
			themMon(maMonCuaNutCong.get(nguon));
		} else if (maMonCuaNutTru.containsKey(nguon)) {
			botMon(maMonCuaNutTru.get(nguon));
		}
	}

	// ==================== KHU VỰC GIỎ HÀNG ====================
	private void themMon(String maMon) {
		DoUongDTO doUong = dsDoUongBUS.timTheoMa(maMon);
		if (hoaDonBUS.themMon(doUong)) {
			capNhatGioHang();
		}
	}

	private void botMon(String maMon) {
		if (hoaDonBUS.giamMon(maMon)) {
			capNhatGioHang();
		}
	}

	// ==================== KHU VỰC CẬP NHẬT GIAO DIỆN ====================
	/** Nạp lại danh sách thẻ món theo từ khóa và nhóm đang chọn. */
	public void capNhatDanhSachMon() {
		pnGridMon.removeAll();
		nhanSoLuong.clear();
		maMonCuaNutCong.clear();
		maMonCuaNutTru.clear();

		List<DoUongDTO> dsMon = dsDoUongBUS.locVaTim(txtTimKiem.getText(), nhomDangLoc);
		for (int i = 0; i < dsMon.size(); i++) {
			pnGridMon.add(taoTheMon(dsMon.get(i)));
		}

		capNhatSoLuongTrenThe();
		pnGridMon.revalidate();
		pnGridMon.repaint();
	}

	/** Nạp lại bảng đơn hàng và tổng tiền từ HoaDonBUS. */
	public void capNhatGioHang() {
		modelDonHang.setRowCount(0);
		maMonTrongBang.clear();

		List<ChiTietHoaDonDTO> gioHang = hoaDonBUS.layGioHang();
		for (int i = 0; i < gioHang.size(); i++) {
			ChiTietHoaDonDTO dong = gioHang.get(i);
			modelDonHang.addRow(new Object[]{
				String.valueOf(i + 1),
				dong.getTenMon(),
				String.valueOf(dong.getSoLuong()),
				HoaDonBUS.dinhDangTien(dong.getDonGia()),
				HoaDonBUS.dinhDangTien(dong.getThanhTien()),
				""
			});
			maMonTrongBang.add(dong.getMaMon());
		}

		lbTongTienGiaTri.setText(HoaDonBUS.dinhDangTien(hoaDonBUS.tongTien()));
		capNhatSoLuongTrenThe();
	}

	/** Cập nhật số lượng hiển thị trên từng thẻ món. */
	private void capNhatSoLuongTrenThe() {
		for (Map.Entry<String, JLabel> muc : nhanSoLuong.entrySet()) {
			ChiTietHoaDonDTO dong = hoaDonBUS.timTrongGio(muc.getKey());
			if (dong == null) {
				muc.getValue().setText("0");
			} else {
				muc.getValue().setText(String.valueOf(dong.getSoLuong()));
			}
		}
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	private void xoaMonDangChon() {
		int dongDangChon = tblDonHang.getSelectedRow();
		if (dongDangChon < 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa trong đơn hàng.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String maMon = maMonTrongBang.get(dongDangChon);
		ChiTietHoaDonDTO dong = hoaDonBUS.timTrongGio(maMon);
		if (dong == null) {
			return;
		}

		int luaChon = JOptionPane.showConfirmDialog(this,
				"Xóa \"" + dong.getTenMon() + "\" khỏi đơn hàng?",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		if (luaChon == JOptionPane.YES_OPTION) {
			hoaDonBUS.xoaMon(maMon);
			capNhatGioHang();
		}
	}

	private void thanhToan() {
		if (hoaDonBUS.gioHangRong()) {
			JOptionPane.showMessageDialog(this, "Đơn hàng chưa có món nào để thanh toán.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Điều hướng sang màn hình ThanhToanPOSPanel
		if (mainFrame != null) {
			mainFrame.switchCard("Thanh toán");
		}
	}
//	private void hienThiHoaDon(HoaDonDTO hoaDon) {
//		if (hoaDon == null) {
//			return;
//		}
//
//		JTextArea txtHoaDon = new JTextArea(hoaDonBUS.taoNoiDungHoaDon(hoaDon));
//		txtHoaDon.setEditable(false);
//		txtHoaDon.setFont(new Font("Monospaced", Font.PLAIN, 13));
//		txtHoaDon.setBorder(new EmptyBorder(10, 10, 10, 10));
//
//		JScrollPane scroll = new JScrollPane(txtHoaDon);
//		scroll.setPreferredSize(new Dimension(470, 380));
//
//		JOptionPane.showMessageDialog(this, scroll, "Hóa đơn " + hoaDon.getMaHD(),
//				JOptionPane.INFORMATION_MESSAGE);
//
//		int luaChon = JOptionPane.showConfirmDialog(this, "Xuất hóa đơn ra file văn bản?",
//				"Xuất hóa đơn", JOptionPane.YES_NO_OPTION);
//		if (luaChon == JOptionPane.YES_OPTION) {
//			String duongDan = hoaDonBUS.xuatHoaDonRaFile(hoaDon);
//			if (duongDan == null) {
//				JOptionPane.showMessageDialog(this, "Không thể xuất hóa đơn ra file.",
//						"Lỗi", JOptionPane.ERROR_MESSAGE);
//			} else {
//				JOptionPane.showMessageDialog(this, "Đã xuất hóa đơn:\n" + duongDan,
//						"Thành công", JOptionPane.INFORMATION_MESSAGE);
//			}
//		}
//	}
}
