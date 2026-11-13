package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.utils.UIUtils;
import newDayCoffee.utils.Auth;
import newDayCoffee.bus.DSCaTrucBUS;
import newDayCoffee.dto.CaTrucDTO;
import newDayCoffee.dto.ChiTietCaTrucDTO;

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
	private JLabel lbSoLuong;
	private JTextField txtSoLuong;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private DSCaTrucBUS dsCaTrucBUS;

	public CaTrucCuaToiPanel() {
		taoGiaoDien();
		khoaTruongThongTin();
	}

	public CaTrucCuaToiPanel(DSCaTrucBUS dsCaTrucBUS) {
		this();
		this.dsCaTrucBUS = dsCaTrucBUS;
		ganSuKien();
		capNhatDuLieu();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void taoGiaoDien() {
		setLayout(new BorderLayout());

		// Panel chính chứa 2 cột (Danh sách bên trái rộng hơn, Thông tin bên phải)
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

		String[] headerTable = {"Mã ca trực", "Nhân viên", "Ca trực", "Ngày trực", "Số lượng"};
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
		UIUtils.centerTableCells(tbCaTruc, 0, 2, 3, 4);

		// Căn độ rộng tương ứng với 5 cột hiển thị.
		int[] colWidths = {90, 110, 85, 90, 75};
		for (int i = 0; i < colWidths.length; i++) {
			tbCaTruc.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
			tbCaTruc.getColumnModel().getColumn(i).setMinWidth(65);
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
		txtMaCaTruc = UIUtils.createTXTThongTinCaTruc("Ví dụ: CA01");
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

		// 5. Số lượng
		lbSoLuong = UIUtils.createLbThongTinCaTruc("Số lượng: ");
		txtSoLuong = UIUtils.createTXTThongTinCaTruc("Ví dụ: 3");
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

		// Đẩy toàn bộ nội dung cột phải lên trên
		cardRight.add(Box.createVerticalGlue());

		// Đưa 2 cột vào panel chính với tỉ lệ 58% bảng, 42% form
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

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	/** Chỉ xem thông tin, không nhận nhập liệu hay focus tại các ô thông tin. */
	private void khoaTruongThongTin() {
		txtMaCaTruc.setEditable(false);
		txtTenNhanVien.setEditable(false);
		txtNgayTruc.setEditable(false);
		txtSoLuong.setEditable(false);
		txtMaCaTruc.setFocusable(false);
		txtTenNhanVien.setFocusable(false);
		txtNgayTruc.setFocusable(false);
		txtSoLuong.setFocusable(false);
		cbCaTruc.setEnabled(false);
	}

	private void ganSuKien() {
		txtMaCaTruc.setEditable(false);
		txtTenNhanVien.setEditable(false);
		txtNgayTruc.setEditable(false);
		txtSoLuong.setEditable(false);
		cbCaTruc.setEnabled(false);
		tbCaTruc.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					hienThiCaTrucDangChon();
				}
			}
		});
	}

	// ==================== KHU VỰC CẬP NHẬT GIAO DIỆN ====================
	/** Chỉ hiển thị các ca có phân công nhân viên đang đăng nhập. */
	public void capNhatDuLieu() {
		model.setRowCount(0);
		xoaThongTin();
		if (dsCaTrucBUS == null || !Auth.coQuyenTruyCapManHinh("Ca trực của tôi")) {
			return;
		}
		String maNV = Auth.getNguoiDung().getMaNV();
		for (CaTrucDTO caTruc : dsCaTrucBUS.layCaTrucTheoNhanVien(maNV)) {
			model.addRow(new Object[]{
				caTruc.getMaCaTruc(),
				dsCaTrucBUS.layTenNhanVien(caTruc.getMaCaTruc()),
				caTruc.getTenCaTruc(),
				UIUtils.dinhDangNgay(caTruc.getNgayTruc()),
				soLuongNhanVien(caTruc.getMaCaTruc())
			});
		}
	}

	private void hienThiCaTrucDangChon() {
		int dong = tbCaTruc.getSelectedRow();
		if (dong < 0 || dsCaTrucBUS == null || !Auth.coQuyenTruyCapManHinh("Ca trực của tôi")) {
			return;
		}
		dong = tbCaTruc.convertRowIndexToModel(dong);
		CaTrucDTO caTruc = dsCaTrucBUS.timTheoMa(String.valueOf(model.getValueAt(dong, 0)));
		if (caTruc == null) {
			return;
		}
		txtMaCaTruc.setText(caTruc.getMaCaTruc());
		txtTenNhanVien.setText(dsCaTrucBUS.layTenNhanVien(caTruc.getMaCaTruc()));
		txtNgayTruc.setText(UIUtils.dinhDangNgay(caTruc.getNgayTruc()));
		cbCaTruc.setSelectedItem(caTruc.getTenCaTruc());
		txtSoLuong.setText(String.valueOf(soLuongNhanVien(caTruc.getMaCaTruc())));
	}

	/** Chi tiết cá nhân giữ số lượng của ca, không cần đọc phân công người khác. */
	private int soLuongNhanVien(String maCaTruc) {
		List<ChiTietCaTrucDTO> chiTiet = dsCaTrucBUS.layChiTiet(maCaTruc);
		return chiTiet.isEmpty() ? 0 : chiTiet.get(0).getSoLuong();
	}

	private void xoaThongTin() {
		txtMaCaTruc.setText("");
		txtTenNhanVien.setText("");
		txtNgayTruc.setText("");
		txtSoLuong.setText("");
		cbCaTruc.setSelectedIndex(-1);
	}
}
