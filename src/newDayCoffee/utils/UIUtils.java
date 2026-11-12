package newDayCoffee.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class UIUtils {

	// ==================== BẢNG MÀU CHUẨN GIAO DIỆN (COFFEE PALETTE) ====================
	public static final Color COLOR_WHITE = Color.WHITE;
	public static final Color COLOR_GRAY = new Color(135, 125, 118);
	public static final Color COLOR_BACKGROUND = new Color(132, 92, 64);        // Nâu mocha ấm áp dịu mắt
	public static final Color COLOR_BG_MAIN = new Color(132, 92, 64);           // Nâu mocha chủ đạo đồng bộ
	public static final Color COLOR_SIDEBAR = new Color(48, 28, 18);            // Nâu Espresso rang đậm sang trọng
	public static final Color COLOR_HEADER = new Color(68, 38, 24);             // Nâu đậm Espresso cho header/card
	public static final Color COLOR_HEADER_BADGE = new Color(108, 66, 40);      // Nâu caramel cho badge trên header
	public static final Color COLOR_DARK_ROAST = new Color(84, 48, 30);         // Nâu rang đậm cho bảng/nút phụ
	public static final Color COLOR_DEEP_BROWN = new Color(58, 30, 14);         // Nâu sẫm tiêu đề & nút chính
	public static final Color COLOR_CARD_SURFACE = new Color(96, 60, 38);       // Nền card thông tin nổi bật
	public static final Color COLOR_ACCENT_GOLD = new Color(255, 240, 215);     // Kem vàng sáng rõ nổi bật trên nền nâu
	public static final Color COLOR_BG_INPUT = new Color(252, 249, 245);        // Kem sáng dịu mắt cho ô nhập liệu
	public static final Color COLOR_BORDER_LINE = new Color(168, 128, 96);      // Màu vạch kẻ phân cách hài hòa
	public static final Color COLOR_TABLE_BG = new Color(252, 249, 245);        // Nền bảng kem ấm
	public static final Color COLOR_TABLE_ALT_ROW = new Color(244, 236, 226);   // Dòng xen kẽ trên bảng
	public static final Color COLOR_TABLE_GRID = new Color(224, 212, 198);      // Lưới bảng mảnh hài hòa
	public static final Color COLOR_TABLE_BORDER = new Color(115, 75, 45);      // Viền khung bảng nâu đồng bộ
	public static final Color COLOR_TABLE_HEADER_BG = new Color(68, 38, 24);    // Nền header bảng nâu cà phê đậm
	public static final Color COLOR_TABLE_SELECTION = new Color(226, 194, 162); // Màu dòng đang chọn trên bảng

	// Màu chuẩn các nút thao tác hài hòa với tông nâu Coffee
	public static final Color COLOR_BTN_THEM = new Color(42, 157, 88);          // Xanh lá dịu
	public static final Color COLOR_BTN_XOA = new Color(196, 56, 56);           // Đỏ trầm
	public static final Color COLOR_BTN_SUA = new Color(138, 108, 86);          // Xám nâu ấm
	public static final Color COLOR_BTN_LUU = new Color(28, 122, 62);           // Xanh lá đậm
	public static final Color COLOR_BTN_TIM = new Color(48, 120, 178);          // Xanh dương dịu
	public static final Color COLOR_BTN_LAM_MOI = new Color(208, 112, 42);      // Cam đất ấm

	// ==================== BẢNG FONT CHỮ CHUẨN ====================
	public static final String FONT_FAMILY = "Segoe UI";
	public static final Font titleFont = new Font(FONT_FAMILY, Font.BOLD, 22);
	public static final Font lbFontThongTinCaTruc = new Font(FONT_FAMILY, Font.BOLD, 15);
	public static final Font FONT_TITLE_PANEL = new Font(FONT_FAMILY, Font.BOLD, 16);
	public static final Font FONT_TITLE_SEARCH = new Font(FONT_FAMILY, Font.BOLD, 15);
	public static final Font FONT_LABEL = new Font(FONT_FAMILY, Font.PLAIN, 13);
	public static final Font FONT_LABEL_BOLD = new Font(FONT_FAMILY, Font.BOLD, 13);
	public static final Font FONT_BUTTON = new Font(FONT_FAMILY, Font.BOLD, 13);
	public static final Font FONT_TABLE = new Font(FONT_FAMILY, Font.PLAIN, 13);
	public static final Font FONT_TABLE_HEADER = new Font(FONT_FAMILY, Font.BOLD, 13);

	// Hằng số tái sử dụng để tránh cấp phát bộ nhớ khi render bảng/ô nhập
	private static final Color COLOR_TEXT_DARK = new Color(40, 24, 14);
	private static final Color COLOR_INPUT_BORDER = new Color(185, 162, 140);
	private static final Color COLOR_CARD_BORDER = new Color(155, 112, 78);

	// ==================== HỖ TRỢ TẠO KHỐI CARD MƯỢT MÀ ====================
	public static JPanel createCardPanel(Color bgColor, int radius, Insets padding) {
		JPanel card = new JPanel();
		card.setOpaque(true);
		card.setBackground(bgColor != null ? bgColor : COLOR_CARD_SURFACE);
		Border lineBorder = BorderFactory.createLineBorder(COLOR_CARD_BORDER, 1);
		if (padding != null) {
			card.setBorder(BorderFactory.createCompoundBorder(lineBorder, new EmptyBorder(padding)));
		} else {
			card.setBorder(lineBorder);
		}
		return card;
	}

	// ==================== CÁC PHƯƠNG THỨC CHO CA TRỰC ====================
	public static JLabel createTitleLabel(String text) {
		JLabel label = new JLabel(text, JLabel.LEFT);
		label.setFont(new Font(FONT_FAMILY, Font.BOLD, 19));
		label.setForeground(COLOR_ACCENT_GOLD);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	public static JLabel createLbThongTinCaTruc(String text) {
		JLabel label = new JLabel(text);
		label.setFont(lbFontThongTinCaTruc);
		label.setForeground(COLOR_WHITE);
		return label;
	}

	public static JTextField createTXTThongTinCaTruc() {
		JTextField txt = new JTextField();
		txt.setOpaque(true);
		txt.setBackground(COLOR_BG_INPUT);
		txt.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
		txt.setForeground(COLOR_TEXT_DARK);
		txt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1),
			new EmptyBorder(5, 10, 5, 10)
		));
		txt.setPreferredSize(new Dimension(250, 36));
		return txt;
	}

	public static JComboBox<String> createComboBoxThongTinCaTruc(String[] items) {
		JComboBox<String> comboBox = new JComboBox<>(items);
		Dimension size = new Dimension(290, 36);
		comboBox.setPreferredSize(size);
		comboBox.setMaximumSize(size);
		comboBox.setMinimumSize(size);

		comboBox.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
		comboBox.setBackground(COLOR_BG_INPUT);
		comboBox.setForeground(COLOR_TEXT_DARK);

		comboBox.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1),
			BorderFactory.createEmptyBorder(2, 6, 2, 6)
		));
		return comboBox;
	}

	public static JButton createJbuttonThongTinCaTruc(String ten, String maMau) {
		JButton btn = new JButton(ten);
		Dimension size = new Dimension(115, 38);
		btn.setPreferredSize(size);
		btn.setMaximumSize(size);
		btn.setMinimumSize(size);
		btn.setBackground(Color.decode(maMau));
		btn.setForeground(COLOR_WHITE);
		btn.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	/**
	 * Tạo một dòng nhập liệu trong form Ca trực (Nhãn + Ô nhập).
	 */
	public static Box createRowCaTruc(JLabel lb, Component comp, Dimension labelSize, Dimension inputSize) {
		Box row = Box.createHorizontalBox();
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		lb.setPreferredSize(labelSize);
		lb.setMinimumSize(labelSize);
		lb.setMaximumSize(labelSize);

		comp.setPreferredSize(inputSize);
		comp.setMaximumSize(inputSize);
		comp.setMinimumSize(inputSize);

		row.add(lb);
		row.add(comp);
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		return row;
	}

	/**
	 * Tạo viền khung có padding dùng cho các khối danh sách / thông tin.
	 */
	public static Border createSectionBorder(int top, int left, int bottom, int right) {
		return BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(COLOR_BORDER_LINE, 1),
				"",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				null,
				COLOR_BORDER_LINE
			),
			new EmptyBorder(top, left, bottom, right)
		);
	}

	// ==================== CÁC PHƯƠNG THỨC TÁI SỬ DỤNG CHUNG ====================

	public static JLabel createTitlePanel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(FONT_TITLE_PANEL);
		label.setForeground(COLOR_ACCENT_GOLD);
		return label;
	}

	public static JLabel createSearchTitle(String text) {
		JLabel label = new JLabel(text);
		label.setFont(FONT_TITLE_SEARCH);
		label.setForeground(COLOR_ACCENT_GOLD);
		return label;
	}

	public static JTextField createTextField(Color bg, int width, int height) {
		JTextField txt = new JTextField();
		txt.setOpaque(true);
		txt.setBackground(bg);
		txt.setForeground(COLOR_TEXT_DARK);
		txt.setFont(FONT_LABEL);
		txt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1),
			new EmptyBorder(5, 10, 5, 10)
		));
		Dimension size = new Dimension(width, height);
		txt.setPreferredSize(size);
		txt.setMaximumSize(size);
		return txt;
	}

	public static JTextField createTextField(int width, int height) {
		return createTextField(COLOR_BG_INPUT, width, height);
	}

	public static JPasswordField createPasswordField(Color bg, int width, int height) {
		JPasswordField txt = new JPasswordField();
		txt.setOpaque(true);
		txt.setBackground(bg);
		txt.setForeground(COLOR_TEXT_DARK);
		txt.setFont(FONT_LABEL);
		txt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(COLOR_INPUT_BORDER, 1),
			new EmptyBorder(5, 10, 5, 10)
		));
		Dimension size = new Dimension(width, height);
		txt.setPreferredSize(size);
		txt.setMaximumSize(size);
		return txt;
	}

	public static JPasswordField createPasswordField(int width, int height) {
		return createPasswordField(COLOR_BG_INPUT, width, height);
	}

	public static JComboBox<String> createComboBox(String[] items, int width, int height) {
		JComboBox<String> cbo = new JComboBox<>(items);
		cbo.setBackground(COLOR_BG_INPUT);
		cbo.setForeground(COLOR_TEXT_DARK);
		cbo.setFont(FONT_LABEL);
		Dimension size = new Dimension(width, height);
		cbo.setPreferredSize(size);
		cbo.setMaximumSize(size);
		return cbo;
	}

	public static JRadioButton createRadioButton(String text, Font font) {
		JRadioButton rad = new JRadioButton(text);
		rad.setOpaque(false);
		rad.setForeground(COLOR_WHITE);
		rad.setFont(font != null ? font : FONT_LABEL);
		rad.setFocusPainted(false);
		rad.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return rad;
	}

	public static JButton createButton(String text, Color bg, int width, int height) {
		return createButton(text, bg, FONT_BUTTON, width, height);
	}

	public static JButton createButton(String text, Color bg, Font font, int width, int height) {
		JButton btn = new JButton(text);
		btn.setBackground(bg);
		btn.setForeground(COLOR_WHITE);
		btn.setFont(font != null ? font : FONT_BUTTON);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setMargin(new Insets(0, 6, 0, 6));
		btn.setPreferredSize(new Dimension(width, height));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	public static JButton taoNutPhang(String text, Color mauNen, Color mauChu, Font font, Dimension kichThuoc) {
		JButton btn = new JButton(text);
		btn.setBackground(mauNen);
		btn.setForeground(mauChu);
		btn.setFont(font != null ? font : FONT_BUTTON);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setMargin(new Insets(0, 6, 0, 6));
		if (kichThuoc != null) {
			btn.setPreferredSize(kichThuoc);
		}
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	public static JPanel createInputRow(String tenNhan, Component comp, Font font, int doRongNhan) {
		JPanel pnDong = new JPanel();
		pnDong.setLayout(new BoxLayout(pnDong, BoxLayout.X_AXIS));
		pnDong.setOpaque(false);

		JLabel lb = new JLabel(tenNhan);
		lb.setFont(font != null ? font : FONT_LABEL_BOLD);
		lb.setForeground(COLOR_WHITE);
		Dimension labelSize = new Dimension(doRongNhan, 34);
		lb.setPreferredSize(labelSize);
		lb.setMinimumSize(labelSize);
		lb.setMaximumSize(labelSize);

		pnDong.add(lb);
		pnDong.add(comp);
		return pnDong;
	}

	public static JPanel createInputRow(String tenNhan, Component comp, int doRongNhan) {
		return createInputRow(tenNhan, comp, FONT_LABEL_BOLD, doRongNhan);
	}

	public static void styleTable(JTable table) {
		table.setRowHeight(40);
		table.setBackground(COLOR_TABLE_BG);
		table.setForeground(COLOR_TEXT_DARK);
		table.setGridColor(COLOR_TABLE_GRID);
		table.setFont(FONT_TABLE);
		table.setSelectionBackground(COLOR_TABLE_SELECTION);
		table.setSelectionForeground(Color.BLACK);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		JTableHeader header = table.getTableHeader();
		header.setBackground(COLOR_TABLE_HEADER_BG);
		header.setForeground(COLOR_ACCENT_GOLD);
		header.setFont(FONT_TABLE_HEADER);
		header.setPreferredSize(new Dimension(0, 40));
		header.setReorderingAllowed(false);
	}

	public static JScrollPane createTableScrollPane(JTable table) {
		styleTable(table);
		JScrollPane scrollBang = new JScrollPane(table);
		scrollBang.getViewport().setBackground(COLOR_TABLE_BG);
		scrollBang.setBorder(BorderFactory.createLineBorder(COLOR_TABLE_BORDER, 1));
		return scrollBang;
	}

	public static void centerTableCells(JTable table, int... columns) {
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(JLabel.CENTER);
		if (columns == null || columns.length == 0) {
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(center);
			}
		} else {
			for (int col : columns) {
				if (col >= 0 && col < table.getColumnCount()) {
					table.getColumnModel().getColumn(col).setCellRenderer(center);
				}
			}
		}
	}

	// ==================== CÁC PHƯƠNG THỨC DÙNG CHO LOGIN FRAME ====================

	public static JTextField createRoundedTextFieldWithHint(int radius, String hintText, Font font, Dimension size) {
		JTextField txt = new JTextField() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
				g2.dispose();
				super.paintComponent(g);

				if (getText().isEmpty()) {
					Graphics2D gHint = (Graphics2D) g.create();
					gHint.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					gHint.setColor(Color.GRAY);
					gHint.setFont(font);
					Insets insets = getInsets();
					int textY = (getHeight() - gHint.getFontMetrics().getHeight()) / 2 + gHint.getFontMetrics().getAscent();
					gHint.drawString(hintText, insets.left, textY);
					gHint.dispose();
				}
			}

			@Override
			protected void paintBorder(Graphics g) {
			}
		};
		txt.setOpaque(false);
		txt.setBorder(new EmptyBorder(0, 12, 0, 12));
		txt.setFont(font);
		txt.setForeground(COLOR_TEXT_DARK);
		txt.setBackground(COLOR_WHITE);
		txt.setPreferredSize(size);
		txt.setMaximumSize(size);
		return txt;
	}

	public static JPasswordField createRoundedPasswordFieldWithHint(int radius, String hintText, Font font) {
		JPasswordField pass = new JPasswordField() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
				g2.dispose();
				super.paintComponent(g);

				if (getPassword().length == 0) {
					Graphics2D gHint = (Graphics2D) g.create();
					gHint.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					gHint.setColor(Color.GRAY);
					gHint.setFont(font);
					Insets insets = getInsets();
					int textY = (getHeight() - gHint.getFontMetrics().getHeight()) / 2 + gHint.getFontMetrics().getAscent();
					gHint.drawString(hintText, insets.left, textY);
					gHint.dispose();
				}
			}

			@Override
			protected void paintBorder(Graphics g) {
			}
		};
		pass.setOpaque(false);
		pass.setBackground(COLOR_WHITE);
		pass.setForeground(COLOR_TEXT_DARK);
		pass.setBorder(new EmptyBorder(0, 10, 0, 10));
		pass.setFont(font);
		return pass;
	}

	public static JPanel createRoundedContainer(int radius, Color bg, Dimension size) {
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
				g2.dispose();
				super.paintComponent(g);
			}
		};
		panel.setOpaque(false);
		panel.setBackground(bg);
		if (size != null) {
			panel.setPreferredSize(size);
			panel.setMaximumSize(size);
		}
		panel.setBorder(new EmptyBorder(0, 5, 0, 8));
		return panel;
	}

	// ==================== CÁC PHƯƠNG THỨC DÙNG CHO MAIN FRAME ====================

	public static JPanel taoLogoTron(int size, Color color) {
		JPanel circleLogo = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_ACCENT_GOLD);
				g2.fillOval(0, 0, size - 1, size - 1);
				g2.setColor(COLOR_SIDEBAR);
				g2.fillOval(3, 3, size - 7, size - 7);
				g2.setColor(COLOR_ACCENT_GOLD);
				int cx = size / 2;
				int cy = size / 2 + 1;
				g2.fillRoundRect(cx - 6, cy - 4, 11, 9, 4, 4);
				g2.drawArc(cx + 2, cy - 3, 5, 6, -90, 180);
				g2.drawLine(cx - 7, cy + 6, cx + 6, cy + 6);
				g2.dispose();
			}
		};
		circleLogo.setPreferredSize(new Dimension(size, size));
		circleLogo.setOpaque(false);
		return circleLogo;
	}

	public static JButton taoNutMenu(String text, Font font, ActionListener listener) {
		JButton btn = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				if (Color.BLACK.equals(getForeground())) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(COLOR_ACCENT_GOLD);
					g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
					g2.dispose();
				}
				super.paintComponent(g);
			}
		};
		btn.setFont(font != null ? font : new Font(FONT_FAMILY, Font.BOLD, 15));
		btn.setForeground(COLOR_WHITE);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setRolloverEnabled(false);
		btn.setBorder(new EmptyBorder(8, 14, 8, 12));
		btn.setPreferredSize(new Dimension(190, 38));
		btn.setMaximumSize(new Dimension(200, 38));
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if (listener != null) {
			btn.addActionListener(listener);
		}
		return btn;
	}

	public static JPanel taoBadge(int width) {
		JPanel badge = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_HEADER_BADGE);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
				g2.dispose();
			}
		};
		badge.setOpaque(false);
		badge.setPreferredSize(new Dimension(width, 38));
		return badge;
	}

	public static String taoNhanNguoiDung() {
		if (Auth.laQuanLy()) {
			return "Quản lý: " + Auth.getTenHienThi();
		}
		return "Nhân viên: " + Auth.getTenHienThi();
	}

	public static void resetMauMenu(JButton... buttons) {
		if (buttons == null) return;
		for (JButton btn : buttons) {
			if (btn != null) {
				btn.setForeground(COLOR_WHITE);
				btn.repaint();
			}
		}
	}

	// ==================== CÁC PHƯƠNG THỨC DÙNG CHO TỔNG QUAN PANEL ====================

	public static JPanel taoTheThongKeTongQuan(String tieuDe, JLabel lbGiaTri, Font fontThuong, int coChu) {
		JPanel card = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_HEADER);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
				g2.dispose();
			}
		};
		card.setOpaque(false);
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(new EmptyBorder(18, 15, 18, 15));

		JLabel lbCardTitle = new JLabel(tieuDe);
		lbCardTitle.setFont(fontThuong);
		lbCardTitle.setForeground(COLOR_WHITE);
		lbCardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		lbGiaTri.setFont(new Font(FONT_FAMILY, Font.BOLD, coChu));
		lbGiaTri.setForeground(COLOR_ACCENT_GOLD);
		lbGiaTri.setAlignmentX(Component.CENTER_ALIGNMENT);

		card.add(lbCardTitle);
		card.add(Box.createVerticalStrut(14));
		card.add(lbGiaTri);
		return card;
	}

	public static JPanel taoPanelNenNau() {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_HEADER);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
				g2.dispose();
			}
		};
		panel.setOpaque(false);
		return panel;
	}

	public static JButton taoNutNenNau(String text, Font font) {
		JButton btn = new JButton(text);
		btn.setFont(font);
		btn.setBackground(COLOR_DARK_ROAST);
		btn.setForeground(COLOR_WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setPreferredSize(new Dimension(130, 34));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	// ==================== CÁC PHƯƠNG THỨC DÙNG CHO BÁN HÀNG POS & THANH TOÁN POS ====================

	public static JTextField taoOTimKiemPOS(String hintText, Font font, int width, int height) {
		JTextField txt = new JTextField() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_BG_INPUT);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.setColor(COLOR_HEADER);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.dispose();

				super.paintComponent(g);

				if (getText().isEmpty()) {
					Graphics2D gHint = (Graphics2D) g.create();
					gHint.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					gHint.setColor(COLOR_GRAY);
					gHint.setFont(font);
					Insets insets = getInsets();
					int textY = (getHeight() - gHint.getFontMetrics().getHeight()) / 2 + gHint.getFontMetrics().getAscent();
					gHint.drawString(hintText, insets.left, textY);
					gHint.dispose();
				}
			}

			@Override
			protected void paintBorder(Graphics g) {
			}
		};
		txt.setOpaque(false);
		txt.setForeground(COLOR_TEXT_DARK);
		txt.setPreferredSize(new Dimension(width, height));
		txt.setBorder(new EmptyBorder(5, 10, 5, 10));
		return txt;
	}

	public static JButton taoNutLocPOS(String text, int width, Font font) {
		JButton btn = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.setColor(COLOR_ACCENT_GOLD);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setBackground(COLOR_HEADER);
		btn.setForeground(COLOR_WHITE);
		btn.setFont(font);
		btn.setMargin(new Insets(0, 2, 0, 2));
		btn.setPreferredSize(new Dimension(width, 34));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	public static void capNhatTrangThaiNutLocPOS(JButton nutDangChon, Iterable<JButton> dsNutLoc) {
		if (dsNutLoc == null) return;
		for (JButton btn : dsNutLoc) {
			if (btn == null) continue;
			if (btn.equals(nutDangChon)) {
				btn.setBackground(new Color(235, 158, 52));
				btn.setForeground(new Color(38, 18, 8));
			} else {
				btn.setBackground(COLOR_HEADER);
				btn.setForeground(COLOR_WHITE);
			}
			btn.repaint();
		}
	}

	public static JButton taoNutBoGocPOS(String text, Color bg, Color borderColor, Font font, int width, int height) {
		JButton btn = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(bg);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				if (borderColor != null) {
					g2.setColor(borderColor);
					g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				}
				g2.dispose();
				super.paintComponent(g);
			}
		};
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setForeground(COLOR_WHITE);
		btn.setFont(font);
		btn.setPreferredSize(new Dimension(width, height));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	public static JPanel taoTheMonPOS(String maMon, String tenMon, String moTa, String giaBan) {
		return taoTheMonPOS(maMon, tenMon, moTa, giaBan, null);
	}

	public static JPanel taoTheMonPOS(String maMon, String tenMon, String moTa, String giaBan,
			ActionListener listener) {
		JPanel pnInfo = new JPanel();
		pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.Y_AXIS));
		pnInfo.setOpaque(false);
		pnInfo.setBorder(new EmptyBorder(8, 12, 10, 12));

		JLabel lbTenMon = new JLabel(tenMon);
		lbTenMon.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
		lbTenMon.setForeground(Color.BLACK);
		lbTenMon.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lbMaMon = new JLabel("Mã món: " + maMon);
		lbMaMon.setFont(new Font(FONT_FAMILY, Font.BOLD, 11));
		lbMaMon.setForeground(new Color(80, 50, 30));
		lbMaMon.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lbMoTa = new JLabel("Mô tả: " + moTa);
		lbMoTa.setFont(new Font(FONT_FAMILY, Font.PLAIN, 11));
		lbMoTa.setForeground(Color.GRAY);
		lbMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lbGia = new JLabel("Đơn giá: " + giaBan);
		lbGia.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
		lbGia.setForeground(new Color(200, 50, 40));
		lbGia.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel pnSpinner = new JPanel(new BorderLayout());
		pnSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnSpinner.setMaximumSize(new Dimension(200, 26));
		pnSpinner.setPreferredSize(new Dimension(140, 26));

		JTextField txtSoLuong = new JTextField("0");
		txtSoLuong.setHorizontalAlignment(JTextField.CENTER);
		txtSoLuong.setOpaque(true);
		txtSoLuong.setBackground(COLOR_BG_INPUT);
		txtSoLuong.setBorder(new LineBorder(new Color(190, 175, 160), 1));
		txtSoLuong.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));

		JButton btnTru = createButton("-", COLOR_HEADER, new Font(FONT_FAMILY, Font.BOLD, 13), 40, 26);
		JButton btnCong = createButton("+", COLOR_BTN_THEM, new Font(FONT_FAMILY, Font.BOLD, 12), 40, 26);
		btnTru.setActionCommand("GIAM:" + maMon);
		btnCong.setActionCommand("TANG:" + maMon);
		if (listener != null) {
			btnTru.addActionListener(listener);
			btnCong.addActionListener(listener);
		}

		pnSpinner.add(btnTru, BorderLayout.WEST);
		pnSpinner.add(txtSoLuong, BorderLayout.CENTER);
		pnSpinner.add(btnCong, BorderLayout.EAST);

		pnInfo.add(lbTenMon);
		pnInfo.add(Box.createVerticalStrut(3));
		pnInfo.add(lbMaMon);
		pnInfo.add(Box.createVerticalStrut(3));
		pnInfo.add(lbMoTa);
		pnInfo.add(Box.createVerticalStrut(5));
		pnInfo.add(lbGia);
		pnInfo.add(Box.createVerticalStrut(6));
		pnInfo.add(pnSpinner);

		JPanel cardMon = new JPanel(null) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_WHITE);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
				int chieuCaoAnh = (int) Math.round(getHeight() * 0.46);
				g2.setColor(COLOR_HEADER);
				g2.fillRoundRect(0, 0, getWidth(), chieuCaoAnh, 18, 18);
				g2.fillRect(0, chieuCaoAnh - 10, getWidth(), 10);

				int cx = getWidth() / 2;
				int cy = chieuCaoAnh / 2 + 2;
				g2.setColor(new Color(228, 176, 114, 42));
				g2.fillOval(cx - 26, cy - 26, 52, 52);
				g2.setColor(COLOR_ACCENT_GOLD);
				g2.fillRoundRect(cx - 12, cy - 8, 22, 16, 6, 6);
				g2.drawArc(cx + 6, cy - 6, 9, 11, -90, 180);
				g2.drawLine(cx - 15, cy + 11, cx + 13, cy + 11);

				g2.dispose();
			}

			@Override
			public void doLayout() {
				int chieuCaoAnh = (int) Math.round(getHeight() * 0.46);
				pnInfo.setBounds(0, chieuCaoAnh, getWidth(), getHeight() - chieuCaoAnh);
			}

			@Override
			public Dimension getPreferredSize() {
				int chieuCaoThongTin = pnInfo.getPreferredSize().height;
				int chieuCao = (int) Math.round(chieuCaoThongTin / 0.54);
				return new Dimension(0, Math.max(chieuCao, 210));
			}
		};
		cardMon.setOpaque(false);
		cardMon.putClientProperty("txtSoLuong", txtSoLuong);
		cardMon.add(pnInfo);

		return cardMon;
	}

	public static JPanel taoTheThongKeThanhToan(String tieuDe, JLabel lbGiaTri, Color mauNen, Color mauTieuDe, Color mauGiaTri) {
		JPanel card = new JPanel(new GridLayout(2, 1, 0, 2));
		card.setOpaque(true);
		card.setBackground(mauNen);
		card.setBorder(new EmptyBorder(12, 10, 12, 10));

		JLabel lbTitle = new JLabel(tieuDe, SwingConstants.CENTER);
		lbTitle.setFont(FONT_LABEL_BOLD);
		lbTitle.setForeground(mauTieuDe);

		lbGiaTri.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
		lbGiaTri.setForeground(mauGiaTri);

		card.add(lbTitle);
		card.add(lbGiaTri);
		return card;
	}

	// ==================== CÁC PHƯƠNG THỨC TIẾNG VIỆT TƯƠNG ĐƯƠNG ====================
	public static JTextField taoOChu(Color bg, int width, int height) {
		return createTextField(bg, width, height);
	}

	public static JPasswordField taoOMatKhau(Color bg, int width, int height) {
		return createPasswordField(bg, width, height);
	}

	public static JButton taoNutBam(String text, Color bg, int width, int height) {
		return createButton(text, bg, width, height);
	}

	public static JPanel taoDongNhap(String tenNhan, Component comp, Font font, int doRongNhan) {
		return createInputRow(tenNhan, comp, font, doRongNhan);
	}
}
