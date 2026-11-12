package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.utils.UIUtils;

public class CaTrucCuaToiPanel extends JPanel {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JComponent lbDanhSachCaTruc;
	private DefaultTableModel model;
	private JTable tbCaTruc;
	private JLabel lbThongTinCaTruc;
	private JLabel lbMaCaTruc;
	private JTextField txtMaCaTruc;
	private JLabel lbTenNhanVien;
	private JTextField txtTenNhanVien;
	private JLabel lbNgayTruc;
	private JTextField txtNgayTruc;
	private JLabel lbCaTrucField;
	private JComboBox<String> cbCaTruc;
	private JLabel lbTrangThai;
	private JComboBox<String> cbTrangThai;
	private JLabel lbSoLuong;
	private JTextField txtSoLuong;

	public CaTrucCuaToiPanel() {
		taoGiaoDien();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void taoGiaoDien() {
		setLayout(new BorderLayout());

		// Panel chính chứa 2 cột (Danh sách bên trái rộng hơn để đủ 6 cột bảng, Thông tin bên phải)
		JPanel pnMain = new JPanel(new GridBagLayout());
		pnMain.setBackground(UIUtils.COLOR_BACKGROUND);
		pnMain.setBorder(new EmptyBorder(22, 22, 22, 22));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 0;
		gbc.weighty = 1.0;

		// ==================== CỘT TRÁI (DANH SÁCH CA TRỰC) ====================
		JPanel cardLeft = UIUtils.createCardPanel(UIUtils.COLOR_CARD_SURFACE, 18, new Insets(18, 18, 18, 18));
		cardLeft.setLayout(new BorderLayout(0, 14));

		lbDanhSachCaTruc = UIUtils.createTitleLabel("DANH SÁCH CA TRỰC");
		cardLeft.add(lbDanhSachCaTruc, BorderLayout.NORTH);

		String[] headerTable = {"Mã ca trực", "Nhân viên", "Ca trực", "Ngày trực", "Số lượng", "Trạng thái"};
		model = new DefaultTableModel(headerTable, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tbCaTruc = new JTable(model);
		tbCaTruc.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		tbCaTruc.setFillsViewportHeight(true);
		JScrollPane panelCaTruc = UIUtils.createTableScrollPane(tbCaTruc);
		UIUtils.centerTableCells(tbCaTruc, 0, 2, 3, 4, 5);

		// Căn độ rộng từng cột để tiêu đề không bao giờ bị cắt "..."
		int[] colWidths = {95, 115, 85, 95, 82, 105};
		for (int i = 0; i < colWidths.length; i++) {
			tbCaTruc.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
			tbCaTruc.getColumnModel().getColumn(i).setMinWidth(75);
		}

		cardLeft.add(panelCaTruc, BorderLayout.CENTER);

		// ==================== CỘT PHẢI (THÔNG TIN CA TRỰC) ====================
		JPanel cardRight = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 18, new Insets(18, 22, 18, 22));
		cardRight.setLayout(new BoxLayout(cardRight, BoxLayout.Y_AXIS));

		lbThongTinCaTruc = UIUtils.createTitleLabel("THÔNG TIN CA TRỰC");

		// Kích thước chuẩn cho các ô nhập liệu và nhãn
		Dimension labelSize = new Dimension(115, 36);
		Dimension inputSize = new Dimension(255, 36);

		// 1. Mã ca trực
		lbMaCaTruc = UIUtils.createLbThongTinCaTruc("ID Ca trực: ");
		txtMaCaTruc = UIUtils.createTXTThongTinCaTruc("Ví dụ: CA001");
		Box pnMaCaTruc = UIUtils.createRowCaTruc(lbMaCaTruc, txtMaCaTruc, labelSize, inputSize);

		// 2. Tên nhân viên
		lbTenNhanVien = UIUtils.createLbThongTinCaTruc("Nhân viên: ");
		txtTenNhanVien = UIUtils.createTXTThongTinCaTruc("Ví dụ: Nguyễn Văn A");
		Box pnTenNhanVien = UIUtils.createRowCaTruc(lbTenNhanVien, txtTenNhanVien, labelSize, inputSize);

		// 3. Ngày trực
		lbNgayTruc = UIUtils.createLbThongTinCaTruc("Ngày trực: ");
		txtNgayTruc = UIUtils.createTXTThongTinCaTruc("Ví dụ: 12-11-2026");
		Box pnNgayTruc = UIUtils.createRowCaTruc(lbNgayTruc, txtNgayTruc, labelSize, inputSize);

		// 4. Ca trực (JComboBox)
		lbCaTrucField = UIUtils.createLbThongTinCaTruc("Ca trực: ");
		cbCaTruc = UIUtils.createComboBoxThongTinCaTruc(new String[]{"Ca sáng (6:00 - 11:00)", "Ca chiều (12:00 - 17:00)"});
		Box pnCaTruc = UIUtils.createRowCaTruc(lbCaTrucField, cbCaTruc, labelSize, inputSize);

		// 5. Trạng thái (JComboBox)
		lbTrangThai = UIUtils.createLbThongTinCaTruc("Trạng thái: ");
		cbTrangThai = UIUtils.createComboBoxThongTinCaTruc(new String[]{"Đã hoàn thành", "Vắng có phép", "Vắng không có phép"});
		Box pnTrangThai = UIUtils.createRowCaTruc(lbTrangThai, cbTrangThai, labelSize, inputSize);

		// 6. Số lượng
		lbSoLuong = UIUtils.createLbThongTinCaTruc("Số lượng: ");
		txtSoLuong = UIUtils.createTXTThongTinCaTruc("Ví dụ: 2");
		Box pnSoLuong = UIUtils.createRowCaTruc(lbSoLuong, txtSoLuong, labelSize, inputSize);

		// Thêm các thành phần vào cardRight theo thứ tự
		cardRight.add(Box.createVerticalStrut(2));
		cardRight.add(lbThongTinCaTruc);
		cardRight.add(Box.createVerticalStrut(18));
		cardRight.add(pnMaCaTruc);
		cardRight.add(Box.createVerticalStrut(15));
		cardRight.add(pnTenNhanVien);
		cardRight.add(Box.createVerticalStrut(15));
		cardRight.add(pnNgayTruc);
		cardRight.add(Box.createVerticalStrut(15));
		cardRight.add(pnCaTruc);
		cardRight.add(Box.createVerticalStrut(15));
		cardRight.add(pnSoLuong);
		cardRight.add(Box.createVerticalStrut(15));
		cardRight.add(pnTrangThai);
		cardRight.add(Box.createVerticalStrut(20));

		// Đẩy toàn bộ nội dung cột phải lên trên
		cardRight.add(Box.createVerticalGlue());

		// Đưa 2 cột vào panel chính với tỉ lệ 58% bảng - 42% form
		gbc.gridx = 0;
		gbc.weightx = 0.58;
		gbc.insets = new Insets(0, 0, 0, 18);
		pnMain.add(cardLeft, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.42;
		gbc.insets = new Insets(0, 0, 0, 0);
		pnMain.add(cardRight, gbc);

		add(pnMain, BorderLayout.CENTER);
	}
}
