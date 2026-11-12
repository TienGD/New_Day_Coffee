package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.utils.UIUtils;

public class HoaDonPanel extends JPanel {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbHeader;
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

		JPanel pnHeaderCard = UIUtils.createCardPanel(UIUtils.COLOR_HEADER, 16, new Insets(14, 20, 14, 20));
		pnHeaderCard.setLayout(new BorderLayout());
		lbHeader = UIUtils.createTitleLabel("DANH SÁCH CÁC HÓA ĐƠN TRONG NGÀY: "
				+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		pnHeaderCard.add(lbHeader, BorderLayout.WEST);
		add(pnHeaderCard, BorderLayout.NORTH);

		JPanel pnTableCard = UIUtils.createCardPanel(UIUtils.COLOR_CARD_SURFACE, 18, new Insets(16, 16, 16, 16));
		pnTableCard.setLayout(new BorderLayout());

		String[] headerTable = {"Mã HD", "Ngày lập", "Nhân viên", "Tên món", "Số lượng", "Đơn giá", "Thành tiền"};
		model = new DefaultTableModel(headerTable, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tb = new JTable(model);
		JScrollPane pane = UIUtils.createTableScrollPane(tb);
		UIUtils.centerTableCells(tb, 0, 1, 4, 5, 6);

		pnTableCard.add(pane, BorderLayout.CENTER);
		add(pnTableCard, BorderLayout.CENTER);
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp lại danh sách hóa đơn và chi tiết của từng hóa đơn trong ngày. */
	public void capNhatDuLieu() {
		model.setRowCount(0);

		List<HoaDonDTO> dsHoaDon = dsHoaDonBUS.layDanhSachHoaDon();
		for (int i = 0; i < dsHoaDon.size(); i++) {
			HoaDonDTO hoaDon = dsHoaDon.get(i);
			if (hoaDon.getNgayLap() == null || !LocalDate.now().equals(hoaDon.getNgayLap())) {
				continue;
			}

			NhanVienDTO nhanVien = hoaDon.getMaNV();
			String tenNV = nhanVien == null ? "" : nhanVien.getTenNV();

			List<ChiTietHoaDonDTO> dsChiTiet = dsHoaDonBUS.layChiTiet(hoaDon.getMaHD());
			for (int j = 0; j < dsChiTiet.size(); j++) {
				ChiTietHoaDonDTO chiTiet = dsChiTiet.get(j);
				model.addRow(new Object[]{
					hoaDon.getMaHD(),
					UIUtils.dinhDangNgay(hoaDon.getNgayLap()),
					tenNV,
					chiTiet.getTenMon(),
					String.valueOf(chiTiet.getSoLuong()),
					UIUtils.dinhDangTien(chiTiet.getDonGia()),
					UIUtils.dinhDangTien(chiTiet.getThanhTien())
				});
			}
		}
	}
}
