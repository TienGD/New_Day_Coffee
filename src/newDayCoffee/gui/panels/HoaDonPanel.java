package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.utils.UIUtils;

public class HoaDonPanel extends JPanel {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbHeader;
	private JLabel lbSoHoaDon;
	private DefaultTableModel model;
	private JTable tb;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSHoaDonBUS dsHoaDonBUS;

	public HoaDonPanel(DSHoaDonBUS dsHoaDonBUS) {
		this.dsHoaDonBUS = dsHoaDonBUS;
		taoGiaoDien();
		capNhatDuLieu();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void taoGiaoDien() {
		setLayout(new BorderLayout(0, 16));
		setBackground(UIUtils.COLOR_BACKGROUND);
		setBorder(new EmptyBorder(22, 25, 25, 25));

		// --- HEADER CARD ---
		JPanel pnHeaderCard = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 16, new Insets(14, 20, 14, 20));
		pnHeaderCard.setLayout(new BorderLayout());
		lbHeader = UIUtils.createTitleLabel("DANH SÁCH TẤT CẢ HÓA ĐƠN");
		pnHeaderCard.add(lbHeader, BorderLayout.WEST);
		add(pnHeaderCard, BorderLayout.NORTH);

		// --- BẢNG DANH SÁCH HÓA ĐƠN ---
		JPanel pnTableCard = UIUtils.createCardPanel(UIUtils.COLOR_CARD_SURFACE, 18, new Insets(16, 16, 16, 16));
		pnTableCard.setLayout(new BorderLayout(0, 10));

		// Cột: icon xem | Mã HD | Ngày lập | Nhân viên | Trạng thái | Thành tiền
		String[] headerTable = {"", "Mã HD", "Ngày lập", "Nhân viên", "Trạng thái", "Thành tiền"};
		model = new DefaultTableModel(headerTable, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tb = new JTable(model);
		JScrollPane pane = UIUtils.createTableScrollPane(tb);

		// Căn giữa các cột trừ cột Nhân viên
		UIUtils.centerTableCells(tb, 0, 1, 2, 4, 5);

		// Độ rộng cột
		tb.getColumnModel().getColumn(0).setPreferredWidth(42);
		tb.getColumnModel().getColumn(0).setMinWidth(42);
		tb.getColumnModel().getColumn(0).setMaxWidth(52);
		tb.getColumnModel().getColumn(1).setPreferredWidth(150);
		tb.getColumnModel().getColumn(2).setPreferredWidth(110);
		tb.getColumnModel().getColumn(3).setPreferredWidth(190);
		tb.getColumnModel().getColumn(4).setPreferredWidth(130);
		tb.getColumnModel().getColumn(5).setPreferredWidth(120);

		// Renderer cho cột icon (cột 0) — vẽ icon tài liệu tùy chỉnh
		tb.getColumnModel().getColumn(0).setCellRenderer(new IconXemChiTietRenderer());

		// Renderer màu sắc cho cột Trạng thái (cột 4)
		tb.getColumnModel().getColumn(4).setCellRenderer(new TrangThaiRenderer());

		// Click icon → hiện ChiTietHoaDonPanel
		tb.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int viewCol = tb.columnAtPoint(e.getPoint());
				int viewRow = tb.rowAtPoint(e.getPoint());
				if (viewCol >= 0 && viewRow >= 0) {
					int modelCol = tb.convertColumnIndexToModel(viewCol);
					int modelRow = tb.convertRowIndexToModel(viewRow);
					if (modelCol == 0 && modelRow >= 0) {
						moChiTietHoaDon(modelRow);
					}
				}
			}
		});

		// Thay đổi con trỏ khi hover cột icon
		tb.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			@Override
			public void mouseMoved(java.awt.event.MouseEvent e) {
				int viewCol = tb.columnAtPoint(e.getPoint());
				int modelCol = viewCol >= 0 ? tb.convertColumnIndexToModel(viewCol) : -1;
				if (modelCol == 0) {
					tb.setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					tb.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		pnTableCard.add(pane, BorderLayout.CENTER);

		// --- NHÃN SỐ HÓA ĐƠN PHÍA DƯỚI ---
		lbSoHoaDon = new JLabel("0 hóa đơn");
		lbSoHoaDon.setFont(UIUtils.FONT_LABEL);
		lbSoHoaDon.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pnTableCard.add(lbSoHoaDon, BorderLayout.SOUTH);

		add(pnTableCard, BorderLayout.CENTER);
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================

	
	
	public void capNhatDuLieu() {
	
	    // Xóa dữ liệu cũ trên bảng
	    model.setRowCount(0);
	
	    // Lấy tất cả hóa đơn
	    List<HoaDonDTO> dsHoaDon =
	            dsHoaDonBUS.layDanhSachHoaDon();
	
	    int soHoaDon = 0;
	
	    for (HoaDonDTO hoaDon : dsHoaDon) {
	
	        if (hoaDon == null) {
	            continue;
	        }
	
	        // Lấy thông tin nhân viên lập hóa đơn
	        NhanVienDTO nhanVien = hoaDon.getMaNV();
	
	        String tenNV = nhanVien == null
	                ? ""
	                : nhanVien.getTenNV();
	
	        // Lấy trạng thái hóa đơn
	        String trangThai = hoaDon.getTrangThai() == null
	                ? ""
	                : hoaDon.getTrangThai();
	
	        // Tính tổng tiền hóa đơn
	        double tongTien =
	                dsHoaDonBUS.tongTienHoaDon(hoaDon);
	
	        // Thêm dữ liệu vào JTable
	        model.addRow(new Object[] {
	            hoaDon,
	            hoaDon.getMaHD(),
	            UIUtils.dinhDangNgay(hoaDon.getNgayLap()),
	            tenNV,
	            trangThai,
	            UIUtils.dinhDangTien(tongTien)
	        });
	
	        soHoaDon++;
	    }
	
	    // Cập nhật tổng số hóa đơn
	    lbSoHoaDon.setText(soHoaDon + " hóa đơn");
	}


	/** Mở dialog Chi tiết hóa đơn theo dòng được chọn. */
	private void moChiTietHoaDon(int row) {
		Object giaTri = model.getValueAt(row, 0);
		if (!(giaTri instanceof HoaDonDTO)) {
			return;
		}
		HoaDonDTO hoaDon = (HoaDonDTO) giaTri;

		Window cuaSoCha = SwingUtilities.getWindowAncestor(this);
		ChiTietHoaDonPanel dialog = new ChiTietHoaDonPanel(cuaSoCha, hoaDon, dsHoaDonBUS);
		dialog.setVisible(true);
	}

	// ==================== INNER CLASS: RENDERER ICON ====================

	/** Vẽ icon tài liệu (📄) trong cột đầu tiên của bảng hóa đơn. */
	private static class IconXemChiTietRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			JPanel pnIcon = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					int w = getWidth();
					int h = getHeight();
					int cx = w / 2;
					int cy = h / 2;

					// Thân tài liệu
					int dw = 16, dh = 20;
					int dx = cx - dw / 2;
					int dy = cy - dh / 2;
					g2.setColor(new Color(200, 160, 110));
					g2.fillRoundRect(dx, dy, dw, dh, 4, 4);

					// Góc gấp
					int gapSize = 5;
					g2.setColor(new Color(240, 220, 190));
					int[] xGoc = {dx + dw - gapSize, dx + dw, dx + dw};
					int[] yGoc = {dy, dy, dy + gapSize};
					g2.fillPolygon(xGoc, yGoc, 3);
					g2.setColor(new Color(180, 140, 90));
					g2.drawLine(dx + dw - gapSize, dy, dx + dw - gapSize, dy + gapSize);
					g2.drawLine(dx + dw - gapSize, dy + gapSize, dx + dw, dy + gapSize);

					// Các dòng trên tài liệu
					g2.setColor(new Color(120, 80, 40));
					int lineX1 = dx + 3;
					int lineX2 = dx + dw - 7;
					for (int i = 0; i < 3; i++) {
						int ly = dy + 7 + i * 4;
						g2.drawLine(lineX1, ly, lineX2, ly);
					}
					g2.dispose();
				}
			};
			pnIcon.setOpaque(true);
			if (isSelected) {
				pnIcon.setBackground(UIUtils.COLOR_TABLE_SELECTION);
			} else {
				pnIcon.setBackground(row % 2 == 0 ? UIUtils.COLOR_TABLE_BG : UIUtils.COLOR_TABLE_ALT_ROW);
			}
			pnIcon.setToolTipText("Xem chi tiết hóa đơn");
			return pnIcon;
		}
	}

	// ==================== INNER CLASS: RENDERER TRẠNG THÁI ====================

	/** Căn giữa và giữ màu xen kẽ/chọn cho cột Trạng thái hóa đơn. */
	private static class TrangThaiRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			String trangThai = value == null ? "" : value.toString().trim();
			super.getTableCellRendererComponent(table, trangThai, isSelected, hasFocus, row, column);
			setHorizontalAlignment(JLabel.CENTER);

			if (isSelected) {
				setBackground(UIUtils.COLOR_TABLE_SELECTION);
				setForeground(Color.BLACK);
			} else {
				setBackground(row % 2 == 0 ? UIUtils.COLOR_TABLE_BG : UIUtils.COLOR_TABLE_ALT_ROW);
				if ("Đã thanh toán".equalsIgnoreCase(trangThai)) {
					setForeground(UIUtils.COLOR_BTN_LUU);
				} else if (trangThai.toLowerCase().contains("hủy")) {
					setForeground(UIUtils.COLOR_BTN_XOA);
				} else {
					setForeground(table.getForeground());
				}
			}
			setFont(UIUtils.FONT_LABEL_BOLD);
			return this;
		}
	}
}
