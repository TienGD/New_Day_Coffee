package newDayCoffee.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import newDayCoffee.gui.panels.BanHangPOSPanel;
import newDayCoffee.gui.panels.QuanLyNhanVienPanel;
import newDayCoffee.gui.panels.QuanLyThucDonPanel;
import newDayCoffee.gui.panels.TongQuanPanel;

public class MainFrame extends JFrame {
	private JLabel lbSysName;
	private JLabel lbShift;
	private JPanel pnContainerCard;
	private CardLayout cardLayout;
	private List<JLabel> listMenuLabels = new ArrayList<>();

	// 4 Panel chức năng độc lập
	private TongQuanPanel tongQuanPanel;
	private BanHangPOSPanel banHangPOSPanel;
	private QuanLyThucDonPanel quanLyThucDonPanel;
	private QuanLyNhanVienPanel quanLyNhanVienPanel;

	public MainFrame() {
		setTitle("New Day Coffee");
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(new Color(245, 235, 224));

		Font fontMenu = new Font("Arial", Font.BOLD, 19);
		Font fontThuong = new Font("Arial", Font.PLAIN, 14);

		setLayout(new BorderLayout());

		// 1. THANH MENU BÊN TRÁI (SIDEBAR)
		JPanel pnSidebar = new JPanel(new BorderLayout());
		pnSidebar.setPreferredSize(new Dimension(220, 0));
		pnSidebar.setBackground(new Color(193, 148, 112));
		pnSidebar.setBorder(new EmptyBorder(25, 20, 20, 20));

		// LOGO VÀ TÊN HỆ THỐNG
		JPanel pnLogo = new JPanel();
		pnLogo.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pnLogo.setOpaque(false);

		JPanel circleLogo = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(139, 69, 19));
				g2.fillOval(0, 0, 32, 32);
				g2.dispose();
			}
		};
		circleLogo.setPreferredSize(new Dimension(32, 32));
		circleLogo.setOpaque(false);

		lbSysName = new JLabel("Hệ thống quản lý CAFE");
		lbSysName.setFont(new Font("Arial", Font.BOLD, 13));
		lbSysName.setForeground(new Color(140, 30, 30));

		pnLogo.add(circleLogo);
		pnLogo.add(lbSysName);
		pnSidebar.add(pnLogo, BorderLayout.NORTH);

		// DANH SÁCH MENU
		Box pnMenu = Box.createVerticalBox();
		pnMenu.setBorder(new EmptyBorder(40, 5, 0, 0));

		String[] dsMenu = {
			"Tổng quan", "Bán hàng", "Thực đơn", "Nhân viên"
		};

		for (int i = 0; i < dsMenu.length; i++) {
			JLabel lbMenu = new JLabel(dsMenu[i]);
			lbMenu.setFont(fontMenu);
			if (i == 0) {
				lbMenu.setForeground(Color.BLACK);
			} else {
				lbMenu.setForeground(Color.WHITE);
			}
			lbMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
			listMenuLabels.add(lbMenu);
			final String menuItem = dsMenu[i];
			lbMenu.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					switchCard(menuItem);
				}
			});
			pnMenu.add(lbMenu);
			pnMenu.add(Box.createVerticalStrut(22));
		}
		pnSidebar.add(pnMenu, BorderLayout.CENTER);

		add(pnSidebar, BorderLayout.WEST);

		// 2. KHU VỰC CONTAINER CHUYỂN ĐỔI BẰNG CARDLAYOUT
		cardLayout = new CardLayout();
		pnContainerCard = new JPanel(cardLayout);
		pnContainerCard.setOpaque(false);

		tongQuanPanel = new TongQuanPanel();
		banHangPOSPanel = new BanHangPOSPanel();
		quanLyThucDonPanel = new QuanLyThucDonPanel();
		quanLyNhanVienPanel = new QuanLyNhanVienPanel();

		pnContainerCard.add(tongQuanPanel, "Tổng quan");
		pnContainerCard.add(banHangPOSPanel, "Bán hàng");
		pnContainerCard.add(quanLyThucDonPanel, "Thực đơn");
		pnContainerCard.add(quanLyNhanVienPanel, "Nhân viên");

		add(pnContainerCard, BorderLayout.CENTER);
	}

	private void switchCard(String item) {
		if (item.equals("Tổng quan") || item.equals("Bán hàng") || item.equals("Thực đơn") || item.equals("Nhân viên")) {
			for (JLabel lb : listMenuLabels) {
				if (lb.getText().equals(item)) {
					lb.setForeground(Color.BLACK);
				} else {
					lb.setForeground(Color.WHITE);
				}
			}
			cardLayout.show(pnContainerCard, item);
		} else {
			JOptionPane.showMessageDialog(this, "Chức năng \"" + item + "\" đang trong quá trình phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void main(String[] args) {
		new MainFrame().setVisible(true);
	}
}
