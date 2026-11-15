package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import newDayCoffee.bus.DSHoaDonBUS;
import newDayCoffee.dto.ThongKeMonDTO;
import newDayCoffee.utils.UIUtils;

/**
 * Màn hình tổng quan: doanh thu, số đơn, món bán chạy nhất và bảng thống kê món bán hôm nay.
 *
 * Panel chỉ hiển thị dữ liệu do DSHoaDonBUS cung cấp, không tự tính toán nghiệp vụ.
 */
public class TongQuanPanel extends JPanel implements ActionListener {

	// ==================== KHU VỰC THÀNH PHẦN GIAO DIỆN ====================
	// 4 thẻ thống kê
	private JLabel lbDoanhThu;
	private JLabel lbSoDon;
	private JLabel lbSoMon;
	private JLabel lbMonBanChayNhat;

	// Bảng món bán chạy
	private JTable tblMonBanChay;
	private DefaultTableModel modelMonBanChay;
	private JButton btnSapXepBanChay;

	// ==================== KHU VỰC TẦNG XỬ LÝ ====================
	private final DSHoaDonBUS dsHoaDonBUS;
	private boolean sapXepGiam = false;
	private List<ThongKeMonDTO> dsMonHienTai = new ArrayList<>();

	public TongQuanPanel(DSHoaDonBUS dsHoaDonBUS) {
		this.dsHoaDonBUS = dsHoaDonBUS;
		khoiTaoGiaoDien();
		ganSuKien();
		capNhatDuLieu();
	}

	// ==================== KHU VỰC GIAO DIỆN ====================
	private void khoiTaoGiaoDien() {
		setLayout(new BorderLayout());
		setOpaque(false);

		Font fontDam = new Font(UIUtils.FONT_FAMILY, Font.BOLD, 15);
		Font fontThuong = new Font(UIUtils.FONT_FAMILY, Font.PLAIN, 14);

		// NỘI DUNG CHÍNH (TỔNG QUAN)
		JPanel pnMainContent = new JPanel(new BorderLayout(0, 22));
		pnMainContent.setOpaque(false);
		pnMainContent.setBorder(new EmptyBorder(22, 30, 28, 30));

		// PHẦN TRÊN: 4 THẺ THỐNG KÊ
		JPanel pnTop = new JPanel(new BorderLayout(0, 15));
		pnTop.setOpaque(false);
		JPanel pnCards = new JPanel(new GridLayout(1, 4, 22, 0));
		pnCards.setOpaque(false);
		pnCards.setPreferredSize(new Dimension(0, 115));

		String[] tieuDeThe = {
			"Doanh thu hôm nay", "Đơn đã hoàn tất", "Món đã bán", "Món bán chạy nhất"
		};
		int[] coChuThe = {20, 20, 20, 16};

		lbDoanhThu = new JLabel("0Đ");
		lbSoDon = new JLabel("0 đơn");
		lbSoMon = new JLabel("0 món");
		lbMonBanChayNhat = new JLabel("Chưa có");
		JLabel[] dsGiaTriThe = {lbDoanhThu, lbSoDon, lbSoMon, lbMonBanChayNhat};

		for (int i = 0; i < tieuDeThe.length; i++) {
			JPanel card = UIUtils.taoTheThongKeTongQuan(tieuDeThe[i], dsGiaTriThe[i], fontThuong, coChuThe[i]);
			pnCards.add(card);
		}
		pnTop.add(pnCards, BorderLayout.CENTER);
		pnMainContent.add(pnTop, BorderLayout.NORTH);

		// PHẦN DƯỚI: KHUNG MÓN BÁN CHẠY HÔM NAY (CHIẾM TOÀN BỘ CHIỀU NGANG)
		JPanel pnBestSellers = UIUtils.taoPanelNenNau();
		pnBestSellers.setLayout(new BorderLayout(0, 15));
		pnBestSellers.setBorder(new EmptyBorder(20, 25, 22, 25));

		JLabel lbBestSellerTitle = new JLabel("Món bán chạy hôm nay");
		lbBestSellerTitle.setFont(fontDam);
		lbBestSellerTitle.setForeground(UIUtils.COLOR_ACCENT_GOLD);
		pnBestSellers.add(lbBestSellerTitle, BorderLayout.NORTH);

		modelMonBanChay = new DefaultTableModel(new Object[]{"Đồ uống", "Số lượng"}, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblMonBanChay = new JTable(modelMonBanChay);
		tblMonBanChay.setFont(UIUtils.FONT_LABEL);
		tblMonBanChay.setForeground(Color.WHITE);
		tblMonBanChay.setBackground(UIUtils.COLOR_DARK_ROAST);
		tblMonBanChay.setRowHeight(34);
		tblMonBanChay.setShowGrid(false);
		tblMonBanChay.setIntercellSpacing(new Dimension(0, 0));
		tblMonBanChay.setSelectionBackground(new Color(125, 80, 45));
		tblMonBanChay.setSelectionForeground(Color.WHITE);
		tblMonBanChay.getTableHeader().setFont(UIUtils.FONT_LABEL_BOLD);
		tblMonBanChay.getTableHeader().setBackground(UIUtils.COLOR_HEADER);
		tblMonBanChay.getTableHeader().setForeground(UIUtils.COLOR_ACCENT_GOLD);
		tblMonBanChay.getTableHeader().setBorder(null);
		tblMonBanChay.getColumnModel().getColumn(1).setMaxWidth(90);

		JScrollPane scrollMonBanChay = new JScrollPane(tblMonBanChay);
		scrollMonBanChay.setOpaque(false);
		scrollMonBanChay.getViewport().setOpaque(false);
		scrollMonBanChay.setBorder(null);

		pnBestSellers.add(scrollMonBanChay, BorderLayout.CENTER);

		// NÚT SẮP XẾP TOGGLE
		JPanel pnBtnSort = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
		pnBtnSort.setOpaque(false);

		btnSapXepBanChay = UIUtils.taoNutNenNau("Sắp xếp món theo món bán chạy", fontDam);
		btnSapXepBanChay.setPreferredSize(new Dimension(285, 36));
		capNhatTrangThaiNutSapXep();

		pnBtnSort.add(btnSapXepBanChay);
		pnBestSellers.add(pnBtnSort, BorderLayout.SOUTH);

		pnMainContent.add(pnBestSellers, BorderLayout.CENTER);
		add(pnMainContent, BorderLayout.CENTER);
	}

	// ==================== KHU VỰC GẮN SỰ KIỆN ====================
	private void ganSuKien() {
		btnSapXepBanChay.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSapXepBanChay) {
			sapXepGiam = !sapXepGiam;
			capNhatTrangThaiNutSapXep();
			capNhatBangMonBanChay();
		}
	}

	// ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
	/** Nạp lại toàn bộ số liệu từ DSHoaDonBUS. */
	public void capNhatDuLieu() {
		lbDoanhThu.setText(UIUtils.dinhDangTien(dsHoaDonBUS.doanhThuHomNay()));
		lbSoDon.setText(dsHoaDonBUS.soDonHomNay() + " đơn");
		lbSoMon.setText(dsHoaDonBUS.soLuongMonHomNay() + " món");

		dsMonHienTai = dsHoaDonBUS.layDanhSachThongKeMonHomNay();
		if (dsMonHienTai.isEmpty()) {
			lbMonBanChayNhat.setText("Chưa có");
		} else {
			List<ThongKeMonDTO> dsGiam = sapXepDanhSach(dsMonHienTai, true);
			lbMonBanChayNhat.setText(dsGiam.get(0).getTenMon());
		}

		capNhatTrangThaiNutSapXep();
		capNhatBangMonBanChay();
	}

	/** Nạp lại bảng món bán chạy bằng cách sắp xếp bản sao dsMonHienTai. */
	private void capNhatBangMonBanChay() {
		List<ThongKeMonDTO> dsSapXep = sapXepDanhSach(dsMonHienTai, sapXepGiam);

		modelMonBanChay.setRowCount(0);
		for (ThongKeMonDTO mon : dsSapXep) {
			modelMonBanChay.addRow(new Object[]{mon.getTenMon(), mon.getSoLuong()});
		}
	}

	private void capNhatTrangThaiNutSapXep() {
		if (sapXepGiam) {
			btnSapXepBanChay.setBackground(new Color(235, 158, 52));
			btnSapXepBanChay.setForeground(new Color(38, 18, 8));
		} else {
			btnSapXepBanChay.setBackground(UIUtils.COLOR_DARK_ROAST);
			btnSapXepBanChay.setForeground(UIUtils.COLOR_WHITE);
		}
		btnSapXepBanChay.repaint();
	}

	private static List<ThongKeMonDTO> sapXepDanhSach(List<ThongKeMonDTO> nguon, final boolean giamDan) {
		List<ThongKeMonDTO> banSao = nguon == null ? new ArrayList<>() : new ArrayList<>(nguon);
		Collections.sort(banSao, new Comparator<ThongKeMonDTO>() {
			@Override
			public int compare(ThongKeMonDTO a, ThongKeMonDTO b) {
				int cmp = giamDan
						? Integer.compare(b.getSoLuong(), a.getSoLuong())
						: Integer.compare(a.getSoLuong(), b.getSoLuong());
				if (cmp != 0) {
					return cmp;
				}
				String maA = a.getMaMon() == null ? "" : a.getMaMon();
				String maB = b.getMaMon() == null ? "" : b.getMaMon();
				cmp = maA.compareToIgnoreCase(maB);
				if (cmp != 0) {
					return cmp;
				}
				cmp = maA.compareTo(maB);
				if (cmp != 0) {
					return cmp;
				}
				String tenA = a.getTenMon() == null ? "" : a.getTenMon();
				String tenB = b.getTenMon() == null ? "" : b.getTenMon();
				return tenA.compareToIgnoreCase(tenB);
			}
		});
		return banSao;
	}
}
