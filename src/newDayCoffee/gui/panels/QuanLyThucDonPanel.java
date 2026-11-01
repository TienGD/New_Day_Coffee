package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import newDayCoffee.bus.DSDoUongBUS;
import newDayCoffee.bus.DoUongBUS;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Auth;

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
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLuu;

    private JTextField txtTimKiem;
    private JButton btnTimKiem;
    private JButton btnLocTatCa;
    private JButton btnLocCaPhe;
    private JButton btnLocTra;
    private JButton btnLocTraSua;
    private JButton btnLocBanh;

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

        Color bgHeader    = new Color(138,  82,  36);
        Color bgMain      = new Color(166, 123,  91);
        Color bgInput     = new Color(235, 232, 228);
        Color bgHeaderBtn = new Color(179, 134,  96);
        Color btnColor    = new Color( 90,  50,  20);

        Font fontLabel = new Font("Arial", Font.PLAIN, 13);
        Font fontBold  = new Font("Arial", Font.BOLD,  13);

        // --- HEADER ---
        JPanel pnHeader = new JPanel(new BorderLayout());
        pnHeader.setBackground(bgHeader);
        pnHeader.setPreferredSize(new Dimension(0, 68));
        pnHeader.setBorder(new EmptyBorder(0, 30, 0, 25));

        JLabel lbHeaderTitle = new JLabel("THỰC ĐƠN QUÁN");
        lbHeaderTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lbHeaderTitle.setForeground(Color.WHITE);
        pnHeader.add(lbHeaderTitle, BorderLayout.WEST);

        JPanel pnHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 16));
        pnHeaderRight.setOpaque(false);
        JLabel lbCaKet = taoNhanHeader("Ca kết đang mở", bgHeaderBtn, fontLabel, 145);
        JLabel lbQuanLy = taoNhanHeader(taoNhanNguoiDung(), bgHeaderBtn, fontLabel, 210);
        pnHeaderRight.add(lbCaKet);
        pnHeaderRight.add(lbQuanLy);
        pnHeader.add(pnHeaderRight, BorderLayout.EAST);
        add(pnHeader, BorderLayout.NORTH);

        // --- CONTENT ---
        JPanel pnContent = new JPanel(new BorderLayout(0, 15));
        pnContent.setBackground(bgMain);
        pnContent.setBorder(new EmptyBorder(20, 25, 25, 25));

        // ---- FORM NHẬP LIỆU ----
        JPanel pnForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnForm.setOpaque(false);

        JLabel lbMaMon = new JLabel("Mã món");
        lbMaMon.setFont(fontBold);
        lbMaMon.setForeground(Color.WHITE);
        txtMaMon = new JTextField();
        txtMaMon.setBackground(bgInput);
        txtMaMon.setFont(fontLabel);
        txtMaMon.setPreferredSize(new Dimension(110, 34));
        txtMaMon.setBorder(new EmptyBorder(5, 8, 5, 8));

        JLabel lbTenMon = new JLabel("Tên món");
        lbTenMon.setFont(fontBold);
        lbTenMon.setForeground(Color.WHITE);
        txtTenMon = new JTextField();
        txtTenMon.setBackground(bgInput);
        txtTenMon.setFont(fontLabel);
        txtTenMon.setPreferredSize(new Dimension(160, 34));
        txtTenMon.setBorder(new EmptyBorder(5, 8, 5, 8));

        JLabel lbDonGia = new JLabel("Giá bán");
        lbDonGia.setFont(fontBold);
        lbDonGia.setForeground(Color.WHITE);
        txtDonGia = new JTextField("0");
        txtDonGia.setBackground(bgInput);
        txtDonGia.setFont(fontLabel);
        txtDonGia.setPreferredSize(new Dimension(110, 34));
        txtDonGia.setBorder(new EmptyBorder(5, 8, 5, 8));

        btnThem = makeBtn("+ Thêm",    btnColor, fontBold, 95);
        btnSua  = makeBtn("Sửa",       btnColor, fontBold, 80);
        btnXoa  = makeBtn("Xóa",       new Color(160, 40, 30), fontBold, 80);
        btnLuu  = makeBtn("Lưu CSDL",  new Color(30, 100, 40), fontBold, 100);

        pnForm.add(lbMaMon);  pnForm.add(txtMaMon);
        pnForm.add(lbTenMon); pnForm.add(txtTenMon);
        pnForm.add(lbDonGia); pnForm.add(txtDonGia);
        pnForm.add(Box.createHorizontalStrut(10));
        pnForm.add(btnThem);
        pnForm.add(btnSua);
        pnForm.add(btnXoa);
        pnForm.add(Box.createHorizontalStrut(20));
        pnForm.add(btnLuu);

        // --- HÀNG TÌM KIẾM & BỘ LỌC DANH MỤC ---
        JPanel pnSearchFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnSearchFilter.setOpaque(false);

        JLabel lbTimKiem = new JLabel("Tìm kiếm mã");
        lbTimKiem.setFont(fontBold);
        lbTimKiem.setForeground(Color.WHITE);

        txtTimKiem = new JTextField();
        txtTimKiem.setBackground(bgInput);
        txtTimKiem.setFont(fontLabel);
        txtTimKiem.setPreferredSize(new Dimension(180, 34));
        txtTimKiem.setBorder(new EmptyBorder(5, 8, 5, 8));
        txtTimKiem.setToolTipText("Nhập mã hoặc tên món: cofXX, teaXX, milXX...");

        btnTimKiem = makeBtn("Tìm kiếm", btnColor, fontBold, 95);

        JLabel lbLoc = new JLabel("Lọc danh mục:");
        lbLoc.setFont(fontBold);
        lbLoc.setForeground(Color.WHITE);

        btnLocTatCa  = makeBtn("Tất cả", new Color(110, 80, 50), fontBold, 80);
        btnLocCaPhe  = makeBtn("Cà phê", new Color(120, 60, 20), fontBold, 90);
        btnLocTra    = makeBtn("Trà", new Color(50, 110, 60), fontBold, 80);
        btnLocTraSua = makeBtn("Trà sữa", new Color(100, 60, 120), fontBold, 90);
        btnLocBanh   = makeBtn("Bánh", new Color(140, 100, 40), fontBold, 80);

        pnSearchFilter.add(lbTimKiem);
        pnSearchFilter.add(txtTimKiem);
        pnSearchFilter.add(btnTimKiem);
        pnSearchFilter.add(Box.createHorizontalStrut(20));
        pnSearchFilter.add(lbLoc);
        pnSearchFilter.add(btnLocTatCa);
        pnSearchFilter.add(btnLocCaPhe);
        pnSearchFilter.add(btnLocTra);
        pnSearchFilter.add(btnLocTraSua);
        pnSearchFilter.add(btnLocBanh);

        JPanel pnTop = new JPanel();
        pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));
        pnTop.setOpaque(false);
        pnForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnSearchFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnTop.add(pnForm);
        pnTop.add(Box.createVerticalStrut(12));
        pnTop.add(pnSearchFilter);

        pnContent.add(pnTop, BorderLayout.NORTH);

        // ---- JTABLE (chỉ khởi tạo một lần, sau khi đã có model) ----
        String[] columnNames = {"Mã món", "Tên món", "Giá bán"};
        modelThucDon = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblThucDon = new JTable(modelThucDon);
        tblThucDon.setRowHeight(34);
        tblThucDon.setFont(fontLabel);
        tblThucDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblThucDon.setGridColor(new Color(200, 180, 160));
        tblThucDon.setBackground(new Color(245, 238, 230));
        tblThucDon.setSelectionBackground(new Color(200, 160, 110));
        tblThucDon.setSelectionForeground(Color.BLACK);

        JTableHeader th = tblThucDon.getTableHeader();
        th.setFont(fontBold);
        th.setBackground(new Color(90, 50, 20));
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 36));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        tblThucDon.getColumnModel().getColumn(0).setCellRenderer(center);
        tblThucDon.getColumnModel().getColumn(2).setCellRenderer(center);
        tblThucDon.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblThucDon.getColumnModel().getColumn(1).setPreferredWidth(280);
        tblThucDon.getColumnModel().getColumn(2).setPreferredWidth(120);

        JScrollPane scrollTable = new JScrollPane(tblThucDon);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(120, 80, 40), 1));
        pnContent.add(scrollTable, BorderLayout.CENTER);

        add(pnContent, BorderLayout.CENTER);

        // Nhân viên không phải quản lí chỉ được xem thực đơn
        if (!Auth.laQuanLy()) {
            btnThem.setEnabled(false);
            btnSua.setEnabled(false);
            btnXoa.setEnabled(false);
            btnLuu.setEnabled(false);
            txtMaMon.setEditable(false);
            txtTenMon.setEditable(false);
            txtDonGia.setEditable(false);
        }
    }

    private JButton makeBtn(String text, Color bg, Font font, int width) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(font);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(width, 34));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    /** Nhãn trang trí trên header (thay cho nút không có chức năng). */
    private JLabel taoNhanHeader(String text, Color bg, Font font, int width) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(bg);
        label.setPreferredSize(new Dimension(width, 36));
        return label;
    }

    private String taoNhanNguoiDung() {
        if (Auth.laQuanLy()) {
            return "Quản lý: " + Auth.getTenHienThi();
        }
        return "Nhân viên: " + Auth.getTenHienThi();
    }

    // ==================== KHU VỰC GẮN SỰ KIỆN ====================
    private void ganSuKien() {
        btnThem.setActionCommand("THEM");
        btnSua.setActionCommand("SUA");
        btnXoa.setActionCommand("XOA");
        btnLuu.setActionCommand("LUU");
        btnTimKiem.setActionCommand("TIM_KIEM");

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLuu.addActionListener(this);
        btnTimKiem.addActionListener(this);
        txtTimKiem.setActionCommand("TIM_KIEM");
        txtTimKiem.addActionListener(this);

        btnLocTatCa.setActionCommand("LOC:Tất cả");
        btnLocCaPhe.setActionCommand("LOC:Cà phê");
        btnLocTra.setActionCommand("LOC:Trà");
        btnLocTraSua.setActionCommand("LOC:Trà sữa");
        btnLocBanh.setActionCommand("LOC:Bánh");

        btnLocTatCa.addActionListener(this);
        btnLocCaPhe.addActionListener(this);
        btnLocTra.addActionListener(this);
        btnLocTraSua.addActionListener(this);
        btnLocBanh.addActionListener(this);

        tblThucDon.getSelectionModel().addListSelectionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String lenh = e.getActionCommand();

        if (lenh.equals("THEM")) {
            themMon();
        } else if (lenh.equals("SUA")) {
            suaMon();
        } else if (lenh.equals("XOA")) {
            xoaMon();
        } else if (lenh.equals("LUU")) {
            luuDuLieu();
        } else if (lenh.equals("TIM_KIEM")) {
            capNhatBang();
        } else if (lenh.startsWith("LOC:")) {
            nhomDangLoc = lenh.substring(4);
            capNhatBang();
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
        txtTenMon.setText(doUong.getTenMon());
        txtDonGia.setText(DoUongBUS.dinhDangGiaNhap(doUong.getDonGia()));
    }

    // ==================== KHU VỰC XỬ LÝ NGHIỆP VỤ ====================
    /** Nạp lại bảng theo từ khóa và nhóm đang chọn. */
    public void capNhatBang() {
        List<DoUongDTO> dsMon = dsDoUongBUS.locVaTim(txtTimKiem.getText(), nhomDangLoc);
        hienThiDanhSach(dsMon);
    }

    private void hienThiDanhSach(List<DoUongDTO> dsMon) {
        modelThucDon.setRowCount(0);
        for (int i = 0; i < dsMon.size(); i++) {
            DoUongDTO doUong = dsMon.get(i);
            modelThucDon.addRow(new Object[]{
                doUong.getMaMon(),
                doUong.getTenMon(),
                DoUongBUS.dinhDangGia(doUong.getDonGia())
            });
        }
    }

    private void themMon() {
        String loi = dsDoUongBUS.themDoUong(txtMaMon.getText(), txtTenMon.getText(), txtDonGia.getText());
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Không thể thêm món", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Đã thêm món mới.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        xoaForm();
        capNhatBang();
    }

    private void suaMon() {
        String loi = dsDoUongBUS.suaDoUong(txtMaMon.getText(), txtTenMon.getText(), txtDonGia.getText());
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Không thể sửa món", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Đã cập nhật món.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        capNhatBang();
    }

    private void xoaMon() {
        String maMon = txtMaMon.getText().trim();

        // Nếu chưa nhập mã nhưng có chọn dòng thì lấy mã ở dòng đang chọn
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
            JOptionPane.showMessageDialog(this, "Đã lưu danh sách món vào cơ sở dữ liệu.",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lưu dữ liệu thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaForm() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtDonGia.setText("0");
        tblThucDon.clearSelection();
        txtMaMon.requestFocusInWindow();
    }
}
