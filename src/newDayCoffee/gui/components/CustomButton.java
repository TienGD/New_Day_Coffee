package newDayCoffee.gui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import newDayCoffee.utils.UIUtils;

public class CustomButton extends JButton {

    public CustomButton(String text, Color bg, Font font, int width, int height) {
        super(text);
        setBackground(bg);
        setForeground(Color.WHITE);
        setFont(font != null ? font : UIUtils.FONT_BUTTON);
        setFocusPainted(false);
        setBorderPainted(false);
        setPreferredSize(new Dimension(width, height));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public CustomButton(String text, Color bg, int width, int height) {
        this(text, bg, UIUtils.FONT_BUTTON, width, height);
    }
}
