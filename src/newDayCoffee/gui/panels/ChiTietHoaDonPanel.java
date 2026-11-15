package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
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

/**
 * Dialog hiển thị chi tiết của một hóa đơn khi người dùng nhấn icon trong HoaDonPanel.
 *
 * Hiển thị: thông tin hóa đơn (mã, ngày lập, nhân viên) ở trên,
 * bảng chi tiết (tên món, số lượng, đơn giá, thành tiền) ở giữa,
 * tổng tiền và nút Đóng ở dưới.
 */
public class ChiTietHoaDonPanel extends JDialog {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	private JLabel lbMaHD;
	private JLabel lbNgayLap;
	private JLabel lbNhanVien;
	private DefaultTableModel model;
	private JTable tb;
	private JLabel lbSoMon;
	private JLabel lbTongTien;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final HoaDonDTO hoaDon;
	private final DSHoaDonBUS dsHoaDonBUS;

	public ChiTietHoaDonPanel(Window cuaSoCha, HoaDonDTO hoaDon, DSHoaDonBUS dsHoaDonBUS) {
		super(cuaSoCha, "Chi tiết hóa đơn", ModalityType.APPLICATION_MODAL);
		this.hoaDon = hoaDon;
		this.dsHoaDonBUS = dsHoaDonBUS;
		taoGiaoDien();
		napDuLieu();
		pack();
		setLocationRelativeTo(cuaSoCha);
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void taoGiaoDien() {
		setResizable(false);
		setBackground(UIUtils.COLOR_BACKGROUND);
		getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);

		// --- PANEL NỀN CHÍNH (toàn bộ dialog) ---
		JPanel pnNen = new JPanel(new BorderLayout(0, 14));
		pnNen.setBackground(UIUtils.COLOR_BACKGROUND);
		pnNen.setBorder(new EmptyBorder(0, 0, 0, 0));

		// --- THANH TIÊU ĐỀ ---
		JPanel pnTieuDe = taoThanhTieuDe();
		pnNen.add(pnTieuDe, BorderLayout.NORTH);

		// --- KHỐI NỘI DUNG CHÍNH ---
		JPanel pnNoiDung = new JPanel(new BorderLayout(0, 12));
		pnNoiDung.setBackground(UIUtils.COLOR_CARD_SURFACE);
		pnNoiDung.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(155, 112, 78), 1),
			new EmptyBorder(16, 20, 16, 20)
		));

		// Thông tin hóa đơn (mã, ngày, nhân viên)
		JPanel pnThongTin = taoKhoiThongTin();
		pnNoiDung.add(pnThongTin, BorderLayout.NORTH);

		// Bảng chi tiết các món
		JPanel pnBang = taoKhoiBang();
		pnNoiDung.add(pnBang, BorderLayout.CENTER);

		// Thanh tổng tiền + nút Đóng
		JPanel pnDuoi = taoKhoiDuoi();
		pnNoiDung.add(pnDuoi, BorderLayout.SOUTH);

		pnNen.add(pnNoiDung, BorderLayout.CENTER);
		setContentPane(pnNen);
	}

	/** Thanh tiêu đề nâu đậm với chữ CHI TIẾT HÓA ĐƠN và nút X. */
	private JPanel taoThanhTieuDe() {
		JPanel pn = new JPanel(new BorderLayout());
		pn.setBackground(UIUtils.COLOR_HEADER);
		pn.setBorder(new EmptyBorder(14, 20, 14, 16));
		pn.setPreferredSize(new Dimension(0, 52));

		JLabel lbTieuDe = new JLabel("CHI TIẾT HÓA ĐƠN");
		lbTieuDe.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 17));
		lbTieuDe.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pn.add(lbTieuDe, BorderLayout.WEST);

		return pn;
	}

	/** Khối 2 hàng thông tin: Mã HD, Ngày lập, Nhân viên. */
	private JPanel taoKhoiThongTin() {
		JPanel pn = new JPanel(new GridBagLayout());
		pn.setOpaque(false);
		pn.setBorder(new EmptyBorder(0, 0, 10, 0));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 0, 2, 20);
		gbc.anchor = GridBagConstraints.WEST;

		// Hàng 1: Mã HD | Ngày lập
		gbc.gridx = 0; gbc.gridy = 0;
		JLabel lbNhanMaHD = taoNhanThongTin("Mã HD: ");
		pn.add(lbNhanMaHD, gbc);

		gbc.gridx = 1;
		lbMaHD = taoGiaTriThongTin("");
		pn.add(lbMaHD, gbc);

		gbc.gridx = 2;
		JLabel lbNhanNgay = taoNhanThongTin("Ngày lập: ");
		pn.add(lbNhanNgay, gbc);

		gbc.gridx = 3; gbc.insets = new Insets(2, 0, 2, 0);
		lbNgayLap = taoGiaTriThongTin("");
		pn.add(lbNgayLap, gbc);

		// Hàng 2: Nhân viên
		gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(2, 0, 2, 20);
		JLabel lbNhanNV = taoNhanThongTin("Nhân viên: ");
		pn.add(lbNhanNV, gbc);

		gbc.gridx = 1; gbc.gridwidth = 3; gbc.insets = new Insets(2, 0, 2, 0);
		lbNhanVien = taoGiaTriThongTin("");
		pn.add(lbNhanVien, gbc);

		return pn;
	}

	/** Bảng chi tiết các món trong hóa đơn. */
	private JPanel taoKhoiBang() {
		JPanel pn = new JPanel(new BorderLayout());
		pn.setOpaque(false);

		String[] headerTable = {"Tên món", "Số lượng", "Đơn giá", "Thành tiền"};
		model = new DefaultTableModel(headerTable, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tb = new JTable(model);
		JScrollPane pane = UIUtils.createTableScrollPane(tb);
		pane.setPreferredSize(new Dimension(560, 220));

		// Căn giữa các cột số/tiền
		UIUtils.centerTableCells(tb, 1, 2, 3);

		// Độ rộng cột
		tb.getColumnModel().getColumn(0).setPreferredWidth(200);
		tb.getColumnModel().getColumn(1).setPreferredWidth(90);
		tb.getColumnModel().getColumn(2).setPreferredWidth(120);
		tb.getColumnModel().getColumn(3).setPreferredWidth(120);

		pn.add(pane, BorderLayout.CENTER);
		return pn;
	}

	/** Khối dưới: số món - số sản phẩm bên trái, TỔNG TIỀN + giá trị bên phải, nút Đóng. */
	private JPanel taoKhoiDuoi() {
		JPanel pn = new JPanel(new BorderLayout(0, 10));
		pn.setOpaque(false);
		pn.setBorder(new EmptyBorder(10, 0, 0, 0));

		// Thanh TỔNG TIỀN
		JPanel pnTongTien = new JPanel(new BorderLayout());
		pnTongTien.setOpaque(false);

		// Bên trái: số món / số sản phẩm
		lbSoMon = new JLabel("0 món - 0 sản phẩm");
		lbSoMon.setFont(UIUtils.FONT_LABEL);
		lbSoMon.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pnTongTien.add(lbSoMon, BorderLayout.WEST);

		// Bên phải: nhãn TỔNG TIỀN + giá trị
		JPanel pnGiaTri = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
		pnGiaTri.setOpaque(false);

		JLabel lbNhanTongTien = new JLabel("TỔNG TIỀN");
		lbNhanTongTien.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 14));
		lbNhanTongTien.setForeground(UIUtils.COLOR_WHITE);

		lbTongTien = new JLabel("0Đ");
		lbTongTien.setFont(new Font(UIUtils.FONT_FAMILY, Font.BOLD, 18));
		lbTongTien.setForeground(UIUtils.COLOR_ACCENT_GOLD);

		pnGiaTri.add(lbNhanTongTien);
		pnGiaTri.add(lbTongTien);
		pnTongTien.add(pnGiaTri, BorderLayout.EAST);

		pn.add(pnTongTien, BorderLayout.CENTER);

		// Nút Đóng căn phải
		JPanel pnNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		pnNut.setOpaque(false);

		javax.swing.JButton btnDong = UIUtils.createButton(
			"Đóng", UIUtils.COLOR_DEEP_BROWN,
			new Font(UIUtils.FONT_FAMILY, Font.BOLD, 13), 100, 36
		);
		btnDong.addActionListener(e -> dispose());
		pnNut.add(btnDong);

		pn.add(Box.createVerticalStrut(4), BorderLayout.NORTH);
		pn.add(pnNut, BorderLayout.SOUTH);

		return pn;
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================

	/** Nạp thông tin hóa đơn và danh sách chi tiết vào giao diện. */
	private void napDuLieu() {
		if (hoaDon == null) {
			return;
		}

		// Thông tin tiêu đề
		lbMaHD.setText(hoaDon.getMaHD());
		lbNgayLap.setText(UIUtils.dinhDangNgay(hoaDon.getNgayLap()));
		NhanVienDTO nv = hoaDon.getMaNV();
		lbNhanVien.setText(nv == null ? "" : nv.getTenNV());

		// Nạp bảng chi tiết
		model.setRowCount(0);
		List<ChiTietHoaDonDTO> dsChiTiet = dsHoaDonBUS.layChiTiet(hoaDon.getMaHD());
		int tongSoLuong = 0;
		for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
			model.addRow(new Object[]{
				chiTiet.getTenMon(),
				chiTiet.getSoLuong(),
				UIUtils.dinhDangTien(chiTiet.getDonGia()),
				UIUtils.dinhDangTien(chiTiet.getThanhTien())
			});
			tongSoLuong += chiTiet.getSoLuong();
		}

		// Thống kê dưới bảng
		int soMon = dsChiTiet.size();
		lbSoMon.setText(soMon + " món - " + tongSoLuong + " sản phẩm");

		// Tổng tiền
		double tongTien = dsHoaDonBUS.tongTienHoaDon(hoaDon);
		lbTongTien.setText(UIUtils.dinhDangTien(tongTien));
	}

	// ==================== PHƯƠNG THỨC HỖ TRỢ ====================

	private static JLabel taoNhanThongTin(String text) {
		JLabel lb = new JLabel(text);
		lb.setFont(UIUtils.FONT_LABEL_BOLD);
		lb.setForeground(UIUtils.COLOR_WHITE);
		return lb;
	}

	private static JLabel taoGiaTriThongTin(String text) {
		JLabel lb = new JLabel(text);
		lb.setFont(UIUtils.FONT_LABEL);
		lb.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		return lb;
	}
}
