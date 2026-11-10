package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.utils.UIUtils;

public class CaTrucPanel extends JPanel {
	private DefaultTableModel model;
	private JTable tbCaTruc;
	private JLabel lbDanhSachCaTruc; // Đổi kiểu từ Component sang JLabel để dễ set thuộc tính
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

	public CaTrucPanel() {
		taoGiaoDien();
	}

	private void taoGiaoDien() {
		setLayout(new BorderLayout());
		JPanel pnMain = new JPanel();
		pnMain.setBackground(UIUtils.COLOR_BACKGROUND);
		pnMain.setLayout(new GridLayout(1, 2, 15, 0)); // Chia 2 cột, khoảng cách 15px
		pnMain.setBorder(new EmptyBorder(15, 15, 15, 15)); // Tạo khoảng đệm viền xung quanh panel chính cho đẹp mắt
		
		// ==================== CỘT TRÁI (DANH SÁCH) ====================
		Box boxLeft = Box.createVerticalBox();
		boxLeft.setBorder(javax.swing.BorderFactory.createCompoundBorder(
		    // Tạo viền khung màu đen
		    javax.swing.BorderFactory.createTitledBorder(
		        javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1), // Đường viền màu đen, độ dày 1px
		        "",                                                         // Tiêu đề trống
		        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
		        javax.swing.border.TitledBorder.DEFAULT_POSITION,
		        null,
		        Color.BLACK                                                 
		    ),
		    new javax.swing.border.EmptyBorder(15, 15, 15, 15) // Thêm từ khóa "new" vào đây
		));
		lbDanhSachCaTruc = UIUtils.createTitleLabel("DANH SÁCH CA TRỰC");
		lbDanhSachCaTruc.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái
		
		String[] headerTable = {"Mã lịch trực", "Tên nhân viên", "Ca trực", "Ngày trực", "Trạng thái"};
		
		model = new DefaultTableModel(headerTable, 0);
		tbCaTruc = new JTable(model);
		JScrollPane panelCaTruc = new JScrollPane(tbCaTruc);
		panelCaTruc.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái cho bảng
		
		boxLeft.add(Box.createVerticalStrut(10));
		boxLeft.add(lbDanhSachCaTruc);
		boxLeft.add(Box.createVerticalStrut(10));
		boxLeft.add(panelCaTruc);
		
		// ==================== CỘT PHẢI (THÔNG TIN) ====================
		Box boxRight = Box.createVerticalBox();
		boxRight.setBorder(javax.swing.BorderFactory.createCompoundBorder(
		    // Tạo viền khung màu đen
		    javax.swing.BorderFactory.createTitledBorder(
		        javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1), // Đường viền màu đen, độ dày 1px
		        "",                                                         // Tiêu đề trống
		        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
		        javax.swing.border.TitledBorder.DEFAULT_POSITION,
		        null,
		        Color.BLACK                                                 
		    ),
		    new javax.swing.border.EmptyBorder(15, 15, 15, 15) // Thêm từ khóa "new" vào đây
		));
		lbThongTinCaTruc = UIUtils.createTitleLabel("THÔNG TIN CA TRỰC");
		lbThongTinCaTruc.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn lề trái
		
		// Kích thước chuẩn cho các ô nhập liệu và nhãn
		Dimension inputSize = new Dimension(290, 35);
		Dimension labelSize = new Dimension(110, 35); // Cố định chiều rộng nhãn để thẳng hàng tuyệt đối

		// 1. Mã ca trực
		Box pnMaCaTruc = Box.createHorizontalBox();
		pnMaCaTruc.setAlignmentX(Component.LEFT_ALIGNMENT);
		lbMaCaTruc = UIUtils.createLbThongTinCaTruc("ID lịch trực: ");
		lbMaCaTruc.setPreferredSize(labelSize);
		lbMaCaTruc.setMinimumSize(labelSize);
		lbMaCaTruc.setMaximumSize(labelSize);
		
		txtMaCaTruc = UIUtils.createTXTThongTinCaTruc();
		txtMaCaTruc.setPreferredSize(inputSize);
		txtMaCaTruc.setMaximumSize(inputSize);
		txtMaCaTruc.setMinimumSize(inputSize);
		
		pnMaCaTruc.add(lbMaCaTruc);
		pnMaCaTruc.add(txtMaCaTruc);
		pnMaCaTruc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

		// 2. Tên nhân viên
		Box pnTenNhanVien = Box.createHorizontalBox();
		pnTenNhanVien.setAlignmentX(Component.LEFT_ALIGNMENT);
		lbTenNhanVien = UIUtils.createLbThongTinCaTruc("Nhân viên: ");
		lbTenNhanVien.setPreferredSize(labelSize);
		lbTenNhanVien.setMinimumSize(labelSize);
		lbTenNhanVien.setMaximumSize(labelSize);
		
		txtTenNhanVien = UIUtils.createTXTThongTinCaTruc();
		txtTenNhanVien.setPreferredSize(inputSize);
		txtTenNhanVien.setMaximumSize(inputSize);
		txtTenNhanVien.setMinimumSize(inputSize);
		
		pnTenNhanVien.add(lbTenNhanVien);
		pnTenNhanVien.add(txtTenNhanVien);
		pnTenNhanVien.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

		// 3. Ngày trực
		Box pnNgayTruc = Box.createHorizontalBox();
		pnNgayTruc.setAlignmentX(Component.LEFT_ALIGNMENT);
		lbNgayTruc = UIUtils.createLbThongTinCaTruc("Ngày trực: ");
		lbNgayTruc.setPreferredSize(labelSize);
		lbNgayTruc.setMinimumSize(labelSize);
		lbNgayTruc.setMaximumSize(labelSize);
		
		txtNgayTruc = UIUtils.createTXTThongTinCaTruc();
		txtNgayTruc.setPreferredSize(inputSize);
		txtNgayTruc.setMaximumSize(inputSize);
		txtNgayTruc.setMinimumSize(inputSize);
		
		pnNgayTruc.add(lbNgayTruc);
		pnNgayTruc.add(txtNgayTruc);
		pnNgayTruc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

		// 4. Ca trực (JComboBox)
		Box pnCaTruc = Box.createHorizontalBox();
		pnCaTruc.setAlignmentX(Component.LEFT_ALIGNMENT);
		lbCaTrucField = UIUtils.createLbThongTinCaTruc("Ca trực: ");
		lbCaTrucField.setPreferredSize(labelSize);
		lbCaTrucField.setMinimumSize(labelSize);
		lbCaTrucField.setMaximumSize(labelSize);
		
		cbCaTruc = UIUtils.createComboBoxThongTinCaTruc(new String[]{"Ca sáng (6:00 - 11:00)", "Ca chiều (12:00 - 17:00)"});
		cbCaTruc.setPreferredSize(inputSize);
		cbCaTruc.setMaximumSize(inputSize);
		cbCaTruc.setMinimumSize(inputSize);
		
		pnCaTruc.add(lbCaTrucField);
		pnCaTruc.add(cbCaTruc);
		pnCaTruc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

		// 5. Trạng thái (JComboBox)
		Box pnTrangThai = Box.createHorizontalBox();
		pnTrangThai.setAlignmentX(Component.LEFT_ALIGNMENT);
		lbTrangThai = UIUtils.createLbThongTinCaTruc("Trạng thái: ");
		lbTrangThai.setPreferredSize(labelSize);
		lbTrangThai.setMinimumSize(labelSize);
		lbTrangThai.setMaximumSize(labelSize);
		
		cbTrangThai = UIUtils.createComboBoxThongTinCaTruc(new String[]{"Đã hoàn thành", "Vắng có phép", "Vắng không có phép"});
		cbTrangThai.setPreferredSize(inputSize);
		cbTrangThai.setMaximumSize(inputSize);
		cbTrangThai.setMinimumSize(inputSize);
		
		pnTrangThai.add(lbTrangThai);
		pnTrangThai.add(cbTrangThai);
		pnTrangThai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

		// Sắp xếp các thành phần vào boxRight
		boxRight.add(Box.createVerticalStrut(10));
		boxRight.add(lbThongTinCaTruc);
		boxRight.add(Box.createVerticalStrut(15));
		boxRight.add(pnMaCaTruc);
		boxRight.add(Box.createVerticalStrut(10));
		boxRight.add(pnTenNhanVien);
		boxRight.add(Box.createVerticalStrut(10));
		boxRight.add(pnNgayTruc);
		boxRight.add(Box.createVerticalStrut(10));
		boxRight.add(pnCaTruc);
		boxRight.add(Box.createVerticalStrut(10));
		boxRight.add(pnTrangThai);
		
		// Đẩy toàn bộ nội dung cột phải lên trên, không bị dãn dọc
		boxRight.add(Box.createVerticalGlue()); 

		// Đưa 2 cột vào panel chính
		pnMain.add(boxLeft);
		pnMain.add(boxRight);
		add(pnMain, BorderLayout.CENTER);
	}
}
