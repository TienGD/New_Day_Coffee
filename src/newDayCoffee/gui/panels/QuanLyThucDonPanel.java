package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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

import newDayCoffee.bus.DSDoUongBUS;
import newDayCoffee.bus.DoUongBUS;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.UIUtils;

/**
 * Màn hình quản lý thực đơn.
 *
 * Panel nhận DSDoUongBUS để thêm / sửa / xóa món. Việc kiểm tra dữ liệu và
 * quyền quản lí nằm trong tầng BUS, giao diện chỉ hiển thị thông báo trả về.
 */
public class QuanLyThucDonPanel extends JPanel implements ActionListener, ListSelectionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JTextField txtMaMon;
	private JTextField txtTenMon;
	private JTextField txtDonGia;
	private JTextField txtMoTa;
	private JButton btnThem;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLuu;
	private JButton btnMoi;

	private JTextField txtTimKiem;
	private JButton btnTimKiem;
	private JButton btnLocTatCa;
	private JButton btnLocCaPhe;
	private JButton btnLocTra;
	private JButton btnLocTraSua;
	private JButton[] dsNutLoc;

	private JTable tblThucDon;
	private DefaultTableModel modelThucDon;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSDoUongBUS dsDoUongBUS;
	private String nhomDangLoc = "Tất cả";

	public QuanLyThucDonPanel(DSDoUongBUS dsDoUongBUS) {
		this.dsDoUongBUS = dsDoUongBUS;
		khoiTaoGiaoDien();
		ganSuKien();
		capNhatBang();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new BorderLayout());
		Font fontBold = UIUtils.FONT_LABEL_BOLD;

		// --- CONTENT ---
		JPanel pnContent = new JPanel(new BorderLayout(0, 16));
		pnContent.setBackground(UIUtils.COLOR_BG_MAIN);
		pnContent.setBorder(new EmptyBorder(20, 25, 25, 25));

		// ---- KHỐI CARD NHẬP LIỆU ----
		JPanel pnFormCard = UIUtils.createCardPanel(UIUtils.COLOR_CARD_SURFACE, 16, new Insets(14, 16, 14, 16));
		pnFormCard.setLayout(new BoxLayout(pnFormCard, BoxLayout.Y_AXIS));

		JPanel pnForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		pnForm.setOpaque(false);

		JLabel lbMaMon = new JLabel("Mã món");
		lbMaMon.setFont(fontBold);
		lbMaMon.setForeground(Color.WHITE);
		txtMaMon = UIUtils.createTextField(120, 34);

		JLabel lbTenMon = new JLabel("Tên món");
		lbTenMon.setFont(fontBold);
		lbTenMon.setForeground(Color.WHITE);
		txtTenMon = UIUtils.createTextField(155, 34);

		JLabel lbDonGia = new JLabel("Giá bán");
		lbDonGia.setFont(fontBold);
		lbDonGia.setForeground(Color.WHITE);
		txtDonGia = UIUtils.createTextField(120, 34);
		txtDonGia.setText("0");

		btnThem = UIUtils.createButton("Thêm", UIUtils.COLOR_BTN_THEM, fontBold, 85, 34);
		btnSua  = UIUtils.createButton("Sửa", UIUtils.COLOR_BTN_SUA, fontBold, 80, 34);
		btnXoa  = UIUtils.createButton("Xóa", UIUtils.COLOR_BTN_XOA, fontBold, 80, 34);
		btnLuu  = UIUtils.createButton("Lưu", UIUtils.COLOR_BTN_LUU, fontBold, 90, 34);
		btnMoi  = UIUtils.createButton("Mới", UIUtils.COLOR_BTN_LAM_MOI, fontBold, 78, 34);

		pnForm.add(lbMaMon);
		pnForm.add(txtMaMon);
		pnForm.add(lbTenMon);
		pnForm.add(txtTenMon);
		pnForm.add(lbDonGia);
		pnForm.add(txtDonGia);
		pnForm.add(Box.createHorizontalStrut(8));
		pnForm.add(btnThem);
		pnForm.add(btnSua);
		pnForm.add(btnXoa);
		pnForm.add(Box.createHorizontalStrut(4));
		pnForm.add(btnLuu);

		// --- HÀNG MÔ TẢ MÓN ---
		JPanel pnMoTa = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		pnMoTa.setOpaque(false);

		JLabel lbMoTa = new JLabel("Mô tả");
		lbMoTa.setFont(fontBold);
		lbMoTa.setForeground(Color.WHITE);

		txtMoTa = UIUtils.createTextField(590, 34);

		pnMoTa.add(lbMoTa);
		pnMoTa.add(Box.createHorizontalStrut(11));
		pnMoTa.add(txtMoTa);
		pnMoTa.add(Box.createHorizontalStrut(8));
		pnMoTa.add(btnMoi);

		pnForm.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnFormCard.add(pnForm);
		pnFormCard.add(Box.createVerticalStrut(12));
		pnFormCard.add(pnMoTa);

		// --- HÀNG TÌM KIẾM & BỘ LỌC DANH MỤC ---
		JPanel pnSearchFilter = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 16, new Insets(12, 16, 12, 16));
		pnSearchFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));

		JLabel lbTimKiem = new JLabel("Tìm kiếm mã");
		lbTimKiem.setFont(fontBold);
		lbTimKiem.setForeground(Color.WHITE);

		txtTimKiem = UIUtils.createTextField(185, 34);
		txtTimKiem.setToolTipText("Nhập mã hoặc tên món: cofXX, teaXX, milXX...");

		btnTimKiem = UIUtils.createButton("Tìm kiếm", UIUtils.COLOR_BTN_TIM, fontBold, 95, 34);

		JLabel lbLoc = new JLabel("Lọc danh mục:");
		lbLoc.setFont(fontBold);
		lbLoc.setForeground(Color.WHITE);

		btnLocTatCa  = UIUtils.createButton("Tất cả", new Color(235, 158, 52), fontBold, 82, 34);
		btnLocTatCa.setForeground(new Color(38, 18, 8));
		btnLocCaPhe  = UIUtils.createButton("Cà phê", UIUtils.COLOR_DARK_ROAST, fontBold, 90, 34);
		btnLocTra    = UIUtils.createButton("Trà", UIUtils.COLOR_DARK_ROAST, fontBold, 80, 34);
		btnLocTraSua = UIUtils.createButton("Trà sữa", UIUtils.COLOR_DARK_ROAST, fontBold, 90, 34);

		dsNutLoc = new JButton[]{btnLocTatCa, btnLocCaPhe, btnLocTra, btnLocTraSua};

		pnSearchFilter.add(lbTimKiem);
		pnSearchFilter.add(txtTimKiem);
		pnSearchFilter.add(btnTimKiem);
		pnSearchFilter.add(Box.createHorizontalStrut(18));
		pnSearchFilter.add(lbLoc);
		pnSearchFilter.add(btnLocTatCa);
		pnSearchFilter.add(btnLocCaPhe);
		pnSearchFilter.add(btnLocTra);
		pnSearchFilter.add(btnLocTraSua);

		JPanel pnTop = new JPanel();
		pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));
		pnTop.setOpaque(false);
		pnFormCard.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnSearchFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnTop.add(pnFormCard);
		pnTop.add(Box.createVerticalStrut(12));
		pnTop.add(pnSearchFilter);

		pnContent.add(pnTop, BorderLayout.NORTH);

		// ---- BẢNG THỰC ĐƠN ----
		String[] columnNames = {"Mã món", "Tên món", "Giá bán", "Mô tả"};
		modelThucDon = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblThucDon = new JTable(modelThucDon);
		JScrollPane scrollTable = UIUtils.createTableScrollPane(tblThucDon);
		UIUtils.centerTableCells(tblThucDon, 0, 2);

		tblThucDon.getColumnModel().getColumn(0).setPreferredWidth(120);
		tblThucDon.getColumnModel().getColumn(1).setPreferredWidth(280);
		tblThucDon.getColumnModel().getColumn(2).setPreferredWidth(120);

		pnContent.add(scrollTable, BorderLayout.CENTER);
		add(pnContent, BorderLayout.CENTER);
	}

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnThem.addActionListener(this);
		btnSua.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLuu.addActionListener(this);
		btnMoi.addActionListener(this);
		btnTimKiem.addActionListener(this);
		txtTimKiem.addActionListener(this);

		for (int i = 0; i < dsNutLoc.length; i++) {
			dsNutLoc[i].addActionListener(this);
		}

		tblThucDon.getSelectionModel().addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object nguon = e.getSource();

		if (nguon == btnThem) {
			themMon();
		} else if (nguon == btnSua) {
			suaMon();
		} else if (nguon == btnXoa) {
			xoaMon();
		} else if (nguon == btnLuu) {
			luuDuLieu();
		} else if (nguon == btnMoi) {
			xoaForm();
		} else if (nguon == btnTimKiem || nguon == txtTimKiem) {
			capNhatBang();
		} else if (nguon == btnLocTatCa) {
			locTheoNhom("Tất cả");
		} else if (nguon == btnLocCaPhe) {
			locTheoNhom("Cà phê");
		} else if (nguon == btnLocTra) {
			locTheoNhom("Trà");
		} else if (nguon == btnLocTraSua) {
			locTheoNhom("Trà sữa");
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		int dong = tblThucDon.getSelectedRow();
		if (dong < 0) {
			return;
		}

		String maMon = String.valueOf(modelThucDon.getValueAt(dong, 0));
		DoUongDTO doUong = dsDoUongBUS.timTheoMa(maMon);
		if (doUong == null) {
			return;
		}

		txtMaMon.setText(doUong.getMaMon());
		txtMaMon.setEditable(false);
		txtTenMon.setText(doUong.getTenMon());
		txtDonGia.setText(DoUongBUS.dinhDangGiaNhap(doUong.getDonGia()));
		txtMoTa.setText(doUong.getMoTa());
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp lại bảng theo từ khóa và nhóm đang chọn. */
	public void capNhatBang() {
		String tuKhoa = txtTimKiem.getText();
		List<DoUongDTO> dsMon = dsDoUongBUS.locVaTim(tuKhoa, nhomDangLoc);

		modelThucDon.setRowCount(0);
		for (int i = 0; i < dsMon.size(); i++) {
			DoUongDTO doUong = dsMon.get(i);
			modelThucDon.addRow(new Object[]{
				doUong.getMaMon(),
				doUong.getTenMon(),
				DoUongBUS.dinhDangGia(doUong.getDonGia()),
				doUong.getMoTa()
			});
		}

		if (dsMon.isEmpty() && !tuKhoa.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy món.",
					"Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void locTheoNhom(String nhom) {
		nhomDangLoc = nhom;
		capNhatMauNutLoc();
		capNhatBang();
	}

	private void capNhatMauNutLoc() {
		for (int i = 0; i < dsNutLoc.length; i++) {
			JButton nut = dsNutLoc[i];
			if (nut.getText().equals(nhomDangLoc)) {
				nut.setBackground(new Color(235, 158, 52));
				nut.setForeground(new Color(38, 18, 8));
			} else {
				nut.setBackground(UIUtils.COLOR_DARK_ROAST);
				nut.setForeground(Color.WHITE);
			}
		}
	}

	private void themMon() {
		String loi = dsDoUongBUS.themDoUong(txtMaMon.getText(), txtTenMon.getText(),
				txtDonGia.getText(), txtMoTa.getText());
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể thêm món", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã thêm món mới.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		xoaForm();
		capNhatBang();
	}

	private void suaMon() {
		String loi = dsDoUongBUS.suaDoUong(txtMaMon.getText(), txtTenMon.getText(),
				txtDonGia.getText(), txtMoTa.getText());
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể sửa món", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã cập nhật món.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		capNhatBang();
	}

	private void xoaMon() {
		String maMon = txtMaMon.getText().trim();

		int dong = tblThucDon.getSelectedRow();
		if (maMon.isEmpty() && dong >= 0) {
			maMon = String.valueOf(modelThucDon.getValueAt(dong, 0));
		}
		if (maMon.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập mã món cần xóa.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		DoUongDTO doUong = dsDoUongBUS.timTheoMa(maMon);
		String tenMon = (doUong == null) ? maMon : doUong.getTenMon();

		int luaChon = JOptionPane.showConfirmDialog(this, "Xóa món \"" + tenMon + "\"?",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}

		String loi = dsDoUongBUS.xoaDoUong(maMon);
		if (loi != null) {
			JOptionPane.showMessageDialog(this, loi, "Không thể xóa món", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã xóa món.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		xoaForm();
		capNhatBang();
	}

	private void luuDuLieu() {
		if (dsDoUongBUS.luuVaoCSDL()) {
			JOptionPane.showMessageDialog(this, "Đã lưu danh sách món.",
					"Thành công", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Lưu dữ liệu thất bại.",
					"Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void xoaForm() {
		txtMaMon.setEditable(true);
		txtMaMon.setText("");
		txtTenMon.setText("");
		txtDonGia.setText("0");
		txtMoTa.setText("");
		tblThucDon.clearSelection();
		txtMaMon.requestFocusInWindow();
	}
}
