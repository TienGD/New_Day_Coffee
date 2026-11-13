package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import newDayCoffee.utils.Validator;
import newDayCoffee.bus.DSCaTrucBUS;
import newDayCoffee.bus.DSNhanVienBUS;
import newDayCoffee.dto.CaTrucDTO;
import newDayCoffee.dto.ChiTietCaTrucDTO;
import newDayCoffee.dto.NhanVienDTO;

public class CaTrucPanel extends JPanel implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private DefaultTableModel model;
	private JTable tbCaTruc;
	private JLabel lbDanhSachCaTruc;
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
	private JButton btnThem;
	private JButton btnSua;
	private JButton btnLuu;
	private JButton btnXoa;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private DSNhanVienBUS DSNhanVienBUS;
	private DSCaTrucBUS dsCaTrucBUS;
	private String maCaTrucDangSua = null;

	public CaTrucPanel() {
		taoGiaoDien();
	}

	public CaTrucPanel(DSNhanVienBUS DSNhanVienBUS, DSCaTrucBUS dsCaTrucBUS) {
		this();
		this.DSNhanVienBUS = DSNhanVienBUS;
		this.dsCaTrucBUS = dsCaTrucBUS;
		ganSuKien();
		xoaForm();
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

		// Căn độ rộng tương ứng cho đủ 6 cột
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
		lbMaCaTruc = UIUtils.createLbThongTinCaTruc("ID lịch trực: ");
		txtMaCaTruc = UIUtils.createTXTThongTinCaTruc("Ví dụ: CA01");
		Box pnMaCaTruc = UIUtils.createRowCaTruc(lbMaCaTruc, txtMaCaTruc, labelSize, inputSize);

		// 2. Tên nhân viên
		lbTenNhanVien = UIUtils.createLbThongTinCaTruc("Nhân viên");
		txtTenNhanVien = UIUtils.createTXTThongTinCaTruc("Ví dụ: Nguyễn Văn An, Lê Thị Mai");
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
		cardRight.add(Box.createVerticalStrut(14));
		cardRight.add(pnTenNhanVien);
		cardRight.add(Box.createVerticalStrut(14));
		cardRight.add(pnNgayTruc);
		cardRight.add(Box.createVerticalStrut(14));
		cardRight.add(pnCaTruc);
		cardRight.add(Box.createVerticalStrut(14));
		cardRight.add(pnSoLuong);
		cardRight.add(Box.createVerticalStrut(14));

		// Nhóm các nút Thêm, Sửa, Xóa trên cùng 1 hàng ngang
		JPanel pnBTN = new JPanel();
		pnBTN.setLayout(new BoxLayout(pnBTN, BoxLayout.X_AXIS));
		pnBTN.setOpaque(false);
		pnBTN.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnBTN.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

		btnThem = UIUtils.createJbuttonThongTinCaTruc("Thêm ca", "#2EA043");
		btnSua  = UIUtils.createJbuttonThongTinCaTruc("Sửa ca", "#377DAF");
		btnXoa  = UIUtils.createJbuttonThongTinCaTruc("Xóa ca", "#B92D2D");

		pnBTN.add(Box.createHorizontalGlue());
		pnBTN.add(btnThem);
		pnBTN.add(Box.createHorizontalStrut(10));
		pnBTN.add(btnSua);
		pnBTN.add(Box.createHorizontalStrut(10));
		pnBTN.add(btnXoa);
		pnBTN.add(Box.createHorizontalGlue());
		cardRight.add(pnBTN);
		cardRight.add(Box.createVerticalStrut(14));

		// Nhóm nút Lưu ca
		JPanel pnBtnLuu = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnBtnLuu.setOpaque(false);
		pnBtnLuu.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnBtnLuu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

		btnLuu = UIUtils.taoNutPhang("Lưu ca", UIUtils.COLOR_BTN_LUU, Color.WHITE, new Font(UIUtils.FONT_FAMILY, Font.BOLD, 15), new Dimension(210, 40));

		pnBtnLuu.add(btnLuu);
		cardRight.add(pnBtnLuu);

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
	private void ganSuKien() {
		btnThem.addActionListener(this);
		btnLuu.addActionListener(this);
		btnSua.addActionListener(this);
		btnXoa.addActionListener(this);
		txtTenNhanVien.setToolTipText("Nhập họ tên nhân viên, cách nhau bằng dấu phẩy hoặc chấm phẩy.");
		txtNgayTruc.setToolTipText("Nhập đúng dd-MM-yyyy, ngày trực không được trong quá khứ.");
		txtSoLuong.setToolTipText("Ít nhất 3 và bằng số nhân viên trong danh sách.");
		tbCaTruc.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					hienThiCaTrucDangChon();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object nguon = e.getSource();
		if (nguon == btnSua) {
			String loi = batDauSuaCaTruc();
			if (loi != null) {
				JOptionPane.showMessageDialog(this, loi, "Không thể sửa ca trực", JOptionPane.WARNING_MESSAGE);
			}
			return;
		}
		if (nguon == btnXoa) {
			xoaCaTruc();
			return;
		}
		if (nguon == btnThem && (maCaTrucDangSua != null || tbCaTruc.getSelectedRow() >= 0)) {
			xoaForm();
		} else if (nguon == btnThem || nguon == btnLuu) {
			boolean dangSua = maCaTrucDangSua != null;
			String loi = luuCaTrucTuForm();
			if (loi != null) {
				JOptionPane.showMessageDialog(this, loi, "Không thể lưu ca trực", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(this, dangSua ? "Cập nhật ca trực thành công." : "Thêm ca trực thành công.",
					"Thành công", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// ==================== KHU VỰC KIỂM TRA DỮ LIỆU ====================
	private String kiemTraDuLieuCaTruc(List<NhanVienDTO> dsNhanVienNhap) {
		String maCaTruc = txtMaCaTruc.getText().trim();
		if (maCaTruc.isEmpty()) {
			return "Vui lòng nhập mã ca trực.";
		}
		if (!maCaTruc.matches(Validator.MA_CA_TRUC)) {
			return "Mã ca trực phải có đúng 4 ký tự dạng CAXX (ví dụ CA01).";
		}
		if (dsCaTrucBUS.timTheoMa(maCaTruc) != null && !maCaTruc.equals(maCaTrucDangSua)) {
			return "Mã ca trực đã tồn tại.";
		}
		if (maCaTrucDangSua != null && !maCaTrucDangSua.equals(maCaTruc)) {
			return "Không được thay đổi mã ca trực khi sửa.";
		}

		String ngayNhap = txtNgayTruc.getText().trim();
		if (!ngayNhap.matches(Validator.NGAY_TRUC) || docNgayTruc(ngayNhap) == null) {
			return "Ngày trực không hợp lệ. Vui lòng nhập đúng dd-MM-yyyy (ví dụ 13-11-2026).";
		}
		if (docNgayTruc(ngayNhap).isBefore(LocalDate.now())) {
			return "Ngày trực phải từ ngày hiện tại trở đi.";
		}
		Object caTruc = cbCaTruc.getSelectedItem();
		if (!DSCaTrucBUS.CA_SANG.equals(caTruc) && !DSCaTrucBUS.CA_CHIEU.equals(caTruc)) {
			return "Vui lòng chọn ca sáng hoặc ca chiều.";
		}

		String nhanVienNhap = txtTenNhanVien.getText().trim();
		if (nhanVienNhap.isEmpty()) {
			return "Vui lòng nhập danh sách họ tên nhân viên, cách nhau bằng dấu phẩy hoặc chấm phẩy.";
		}
		String[] dsGiaTri = nhanVienNhap.split("[,;]", -1);
		for (int i = 0; i < dsGiaTri.length; i++) {
			String giaTri = dsGiaTri[i].trim().replaceAll("\\s+", " ");
			if (giaTri.isEmpty()) {
				return "Danh sách nhân viên có mục bị bỏ trống.";
			}
			NhanVienDTO nhanVien = null;
				for (NhanVienDTO nv : DSNhanVienBUS.layDanhSachNhanVien()) {
					if (nv.getTenNV() != null && nv.getTenNV().trim().replaceAll("\\s+", " ").equalsIgnoreCase(giaTri)) {
						if (nhanVien != null) {
							return "Có nhiều nhân viên cùng họ tên " + giaTri + ". Không thể xác định người được phân công.";
						}
						nhanVien = nv;
					}
				}
			if (nhanVien == null) {
				return "Không tìm thấy nhân viên có họ tên: " + giaTri + ".";
			}
			for (NhanVienDTO nv : dsNhanVienNhap) {
				if (nv.getMaNV().equalsIgnoreCase(nhanVien.getMaNV())) {
					return "Nhân viên " + nhanVien.getTenNV() + " bị trùng trong cùng ca trực.";
				}
			}
			dsNhanVienNhap.add(nhanVien);
		}

		String soLuongNhap = txtSoLuong.getText().trim();
		if (!soLuongNhap.matches(Validator.SO_LUONG_CA_TRUC)) {
			return "Số lượng nhân viên phải là số nguyên, ít nhất 3.";
		}
		int soLuong;
		try {
			soLuong = Integer.parseInt(soLuongNhap);
		} catch (NumberFormatException e) {
			return "Số lượng nhân viên không hợp lệ.";
		}
		if (soLuong < 3) {
			return "Mỗi ca trực phải có ít nhất 3 nhân viên.";
		}
		if (soLuong != dsNhanVienNhap.size()) {
			return "Số lượng phải bằng số nhân viên được nhập trong danh sách.";
		}
		return null;
	}

	/** Đọc ngày đúng định dạng và kiểm tra ngày có thật theo lịch. */
	private LocalDate docNgayTruc(String ngayNhap) {
		try {
			DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("dd-MM-uuuu")
					.withResolverStyle(ResolverStyle.STRICT);
			return LocalDate.parse(ngayNhap, dinhDang);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	// ==================== KHU VỰC THÊM CA TRỰC ====================
	/** Tạo DTO tại giao diện, chỉ cập nhật bảng khi BUS lưu thành công. */
	private String luuCaTrucTuForm() {
		if (!Auth.laQuanLy()) {
			return "Chỉ quản lí mới có quyền thêm ca trực.";
		}
		if (maCaTrucDangSua != null && dsCaTrucBUS.timTheoMa(maCaTrucDangSua) == null) {
			return "Không tìm thấy ca trực cần sửa.";
		}
		List<NhanVienDTO> dsNhanVienNhap = new ArrayList<>();
		String loi = kiemTraDuLieuCaTruc(dsNhanVienNhap);
		if (loi != null) {
			return loi;
		}

		CaTrucDTO caTruc = new CaTrucDTO(txtMaCaTruc.getText().trim());
		caTruc.setTenCaTruc(cbCaTruc.getSelectedItem().toString());
		caTruc.setNgayTruc(docNgayTruc(txtNgayTruc.getText().trim()));
		List<ChiTietCaTrucDTO> dsChiTiet = new ArrayList<>();
		for (NhanVienDTO nhanVien : dsNhanVienNhap) {
			dsChiTiet.add(new ChiTietCaTrucDTO(caTruc, nhanVien, dsNhanVienNhap.size()));
		}

		loi = maCaTrucDangSua == null ? dsCaTrucBUS.themCaTruc(caTruc, dsChiTiet)
				: dsCaTrucBUS.suaCaTruc(maCaTrucDangSua, caTruc, dsChiTiet);
		if (loi != null) {
			return loi;
		}
		capNhatDuLieu();
		xoaForm();
		return null;
	}

	// ==================== KHU VỰC CẬP NHẬT GIAO DIỆN ====================
	public void capNhatDuLieu() {
		xoaForm();
		model.setRowCount(0);
		if (dsCaTrucBUS == null || !Auth.laQuanLy()) {
			return;
		}
		for (CaTrucDTO caTruc : dsCaTrucBUS.layDanhSachCaTruc()) {
			model.addRow(new Object[]{
				caTruc.getMaCaTruc(),
				dsCaTrucBUS.layTenNhanVien(caTruc.getMaCaTruc()),
				caTruc.getTenCaTruc(),
				UIUtils.dinhDangNgay(caTruc.getNgayTruc()),
				dsCaTrucBUS.layChiTiet(caTruc.getMaCaTruc()).size()
			});
		}
	}

	private void hienThiCaTrucDangChon() {
		int dong = tbCaTruc.getSelectedRow();
		if (dong < 0 || dsCaTrucBUS == null || !Auth.laQuanLy()) {
			return;
		}
		dong = tbCaTruc.convertRowIndexToModel(dong);
		CaTrucDTO caTruc = dsCaTrucBUS.timTheoMa(String.valueOf(model.getValueAt(dong, 0)));
		if (caTruc == null) {
			return;
		}
		maCaTrucDangSua = null;
		txtMaCaTruc.setText(caTruc.getMaCaTruc());
		txtTenNhanVien.setText(dsCaTrucBUS.layTenNhanVien(caTruc.getMaCaTruc()));
		txtNgayTruc.setText(UIUtils.dinhDangNgay(caTruc.getNgayTruc()));
		cbCaTruc.setSelectedItem(caTruc.getTenCaTruc());
		txtSoLuong.setText(String.valueOf(dsCaTrucBUS.layChiTiet(caTruc.getMaCaTruc()).size()));
		btnLuu.setEnabled(false);
		datTrangThaiNhapLieu(false);
		btnSua.setEnabled(Auth.laQuanLy());
		btnXoa.setEnabled(Auth.laQuanLy());
	}

	/** Xóa thông tin và mở lại form nhập ca mới. */
	private void xoaForm() {
		maCaTrucDangSua = null;
		tbCaTruc.clearSelection();
		txtMaCaTruc.setText("");
		txtTenNhanVien.setText("");
		txtNgayTruc.setText("");
		txtSoLuong.setText("");
		cbCaTruc.setSelectedIndex(0);
		btnThem.setEnabled(Auth.laQuanLy());
		btnLuu.setEnabled(Auth.laQuanLy());
		btnSua.setEnabled(false);
		btnXoa.setEnabled(false);
		datTrangThaiNhapLieu(true);
		txtMaCaTruc.requestFocusInWindow();
	}

	// ==================== KHU VỰC SỬA / XÓA CA TRỰC ====================
	private String batDauSuaCaTruc() {
		if (!Auth.laQuanLy()) {
			return "Chỉ quản lí mới có quyền sửa ca trực.";
		}
		int dong = tbCaTruc.getSelectedRow();
		if (dong < 0) {
			return "Vui lòng chọn ca trực cần sửa trong bảng.";
		}
		dong = tbCaTruc.convertRowIndexToModel(dong);
		String maCaTruc = String.valueOf(model.getValueAt(dong, 0));
		if (dsCaTrucBUS.timTheoMa(maCaTruc) == null) {
			return "Không tìm thấy ca trực cần sửa.";
		}
		hienThiCaTrucDangChon();
		maCaTrucDangSua = maCaTruc;
		datTrangThaiNhapLieu(true);
		btnLuu.setEnabled(true);
		btnSua.setEnabled(false);
		txtTenNhanVien.requestFocusInWindow();
		return null;
	}

	private void xoaCaTruc() {
		if (!Auth.laQuanLy()) {
			JOptionPane.showMessageDialog(this, "Chỉ quản lí mới có quyền xóa ca trực.",
					"Không thể xóa ca trực", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int dong = tbCaTruc.getSelectedRow();
		if (dong < 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ca trực cần xóa trong bảng.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		dong = tbCaTruc.convertRowIndexToModel(dong);
		String maCaTruc = String.valueOf(model.getValueAt(dong, 0));
		int luaChon = JOptionPane.showConfirmDialog(this,
				"Xóa ca trực " + maCaTruc + " và toàn bộ danh sách phân công của ca?",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}
		String loi = xoaCaTrucTheoMa(maCaTruc);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể xóa ca trực", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Xóa ca trực thành công.",
				"Thành công", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Tách bước lưu dữ liệu khỏi hộp xác nhận; chỉ làm mới form khi xóa thành công. */
	private String xoaCaTrucTheoMa(String maCaTruc) {
		String loi = dsCaTrucBUS.xoaCaTruc(maCaTruc);
		if (loi != null) {
			return loi;
		}
		capNhatDuLieu();
		xoaForm();
		return null;
	}

	private void datTrangThaiNhapLieu(boolean choPhep) {
		boolean duocNhap = choPhep && Auth.laQuanLy();
		txtMaCaTruc.setEditable(duocNhap && maCaTrucDangSua == null);
		txtTenNhanVien.setEditable(duocNhap);
		txtNgayTruc.setEditable(duocNhap);
		txtSoLuong.setEditable(duocNhap);
		cbCaTruc.setEnabled(duocNhap);
	}
}
