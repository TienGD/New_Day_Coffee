package newDayCoffee.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UIUtils {
	public static final Font titleFont = new Font("Arial", Font.BOLD, 22);
	public static final Font lbFontThongTinCaTruc = new Font("Arial", Font.BOLD, 18);
	public static final Color COLOR_WHITE = Color.white;
	public static final Color COLOR_GRAY = Color.GRAY;
	public static final Color COLOR_BACKGROUND = Color.decode("#AC7F5E");
	
	public static JLabel createTitleLabel(String text) {
		JLabel label = new JLabel(text, JLabel.LEFT);
	    label.setFont(titleFont);
	    label.setForeground(COLOR_WHITE);
	    label.setAlignmentX(0.0f); // 0.0f tương đương với Component.LEFT_ALIGNMENT
	    return label;
    }
	
	public static JLabel createLbThongTinCaTruc(String text) {
		JLabel label = new JLabel(text);
        label.setFont(lbFontThongTinCaTruc);
        label.setForeground(COLOR_WHITE);
        return label;
	}
	
	public static JTextField createTXTThongTinCaTruc() {
		JTextField txt = new JTextField() {
            private final Color borderColor = new Color(130, 130, 130);
            private final Color backgroundColor = new Color(225, 225, 225);

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose();
            }
        };

        txt.setOpaque(false);
        txt.setFont(new Font("Arial", Font.PLAIN, 18));
        txt.setForeground(Color.BLACK);
        // Thiết lập khoảng đệm (padding) bên trong: trên, trái, dưới, phải
        txt.setBorder(new EmptyBorder(6, 10, 6, 10));
        // Đặt kích thước mặc định (có thể điều chỉnh chiều cao theo ý muốn)
        txt.setPreferredSize(new Dimension(250, 35)); 

        return txt;
 
	}
	
	public static JComboBox<String> createComboBoxThongTinCaTruc(String[] items) {
	    JComboBox<String> comboBox = new JComboBox<>(items);
	    
	    // Thiết lập kích thước cố định (Đồng bộ với JTextField)
	    Dimension size = new Dimension(290, 35);
	    comboBox.setPreferredSize(size);
	    comboBox.setMaximumSize(size);
	    comboBox.setMinimumSize(size);

	    // Thiết lập font chữ và màu sắc
	    comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
	    comboBox.setBackground(new Color(225, 225, 225)); // Màu nền xám nhạt
	    comboBox.setForeground(Color.BLACK);
	    
	    // Thiết lập viền khung xám đậm và khoảng đệm bên trong
	    comboBox.setBorder(javax.swing.BorderFactory.createCompoundBorder(
	        javax.swing.BorderFactory.createLineBorder(new Color(130, 130, 130), 1),
	        javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5)
	    ));

	    return comboBox;
	}
}
