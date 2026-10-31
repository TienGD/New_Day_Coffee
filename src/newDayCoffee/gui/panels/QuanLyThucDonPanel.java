package newDayCoffee.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import newDayCoffee.bus.DSDoUongBUS;
import newDayCoffee.bus.DoUongBUS;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Auth;

public class QuanLyThucDonPanel extends JPanel {
    // ==================== FIELDS ====================
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
    private JLabel lbQuanLy;

    private JTable tblThucDon;
    private DefaultTableModel modelThucDon;

    // BUS dùng chung và trạng thái lọc hiện tại
    private final DSDoUongBUS dsDoUongBUS = DSDoUongBUS.getInstance();
    private final DoUongBUS doUongBUS = new DoUongBUS();
    private String nhomDangLoc = "Tất cả";
    private List<DoUongDTO> dsDangHienThi = new ArrayList<>();

    public QuanLyThucDonPanel() {
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
        JButton btnCaKet = makeBtn("Ca kết đang mở", bgHeaderBtn, fontLabel, 145);
        lbQuanLy = makeBadge("Quản lý: ...", bgHeaderBtn, fontLabel, 210);
        pnHeaderRight.add(btnCaKet);
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
        txtMaMon.setToolTipText("Ví dụ: cof04, tea04, mil03, bak03");

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
        txtDonGia.setToolTipText("Nhập số tiền, ví dụ 15000 hoặc 15.000");

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
        txtTimKiem.setToolTipText("Nhập mã hoặc tên món cần tìm");

        btnTimKiem = makeBtn("Tìm kiếm", btnColor, fontBold, 95);

        JLabel lbLoc = new JLabel("Lọc danh mục:");
        lbLoc.setFont(fontBold);
        lbLoc.setForeground(Color.WHITE);

        btnLocTatCa  = makeBtn("Tất cả", new Color(110, 80, 50), fontBold, 80);
        btnLocCaPhe  = makeBtn("Cà phê", new Color(120, 60, 20), fontBold, 90);
        btnLocTra    = makeBtn("Trà", new Color(50, 110, 60), fontBold, 80);
        btnLocTraSua = makeBtn("Trà sữa", new Color(100, 60, 120), fontBold, 90);

        pnSearchFilter.add(lbTimKiem);
        pnSearchFilter.add(txtTimKiem);
        pnSearchFilter.add(btnTimKiem);
        pnSearchFilter.add(Box.createHorizontalStrut(20));
        pnSearchFilter.add(lbLoc);
        pnSearchFilter.add(btnLocTatCa);
        pnSearchFilter.add(btnLocCaPhe);
        pnSearchFilter.add(btnLocTra);
        pnSearchFilter.add(btnLocTraSua);

        JPanel pnTop = new JPanel();
        pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));
        pnTop.setOpaque(false);
        pnForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnSearchFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnTop.add(pnForm);
        pnTop.add(Box.createVerticalStrut(12));
        pnTop.add(pnSearchFilter);

        pnContent.add(pnTop, BorderLayout.NORTH);

        // ---- JTABLE ----
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

        // ==================== KHU VỰC GẮN SỰ KIỆN ====================
        btnThem.addActionListener(e -> xuLyThem());
        btnSua.addActionListener(e -> xuLySua());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnLuu.addActionListener(e -> xuLyLuu());

        btnTimKiem.addActionListener(e -> xuLyTimKiem());
        btnLocTatCa.addActionListener(e -> doiNhomLoc("Tất cả"));
        btnLocCaPhe.addActionListener(e -> doiNhomLoc("Cà phê"));
        btnLocTra.addActionListener(e -> doiNhomLoc("Trà"));
        btnLocTraSua.addActionListener(e -> doiNhomLoc("Trà sữa"));

        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                taiDanhSach();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                taiDanhSach();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                taiDanhSach();
            }
        });

        tblThucDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiDongDuocChon();
            }
        });

        // Phân quyền: chỉ quản lí được thay đổi thực đơn
        boolean duocSua = Auth.isManager();
        btnThem.setEnabled(duocSua);
        btnSua.setEnabled(duocSua);
        btnXoa.setEnabled(duocSua);
        btnLuu.setEnabled(duocSua);

        capNhatDuLieu();
    }

    // ==================== KHU VỰC XỬ LÝ SỰ KIỆN ====================
    private void xuLyThem() {
        String loi = dsDoUongBUS.themDoUong(txtMaMon.getText(), txtTenMon.getText(), txtDonGia.getText());
        if (loi != null) {
            thongBaoLoi(loi);
            return;
        }
        JOptionPane.showMessageDialog(this, "Đã thêm món mới.", "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        lamMoiForm();
        taiDanhSach();
    }

    private void xuLySua() {
        String maMon = txtMaMon.getText().trim();
        if (maMon.isEmpty()) {
            thongBaoLoi("Vui lòng chọn món cần sửa hoặc nhập mã món.");
            return;
        }
        String loi = dsDoUongBUS.suaDoUong(maMon, txtTenMon.getText(), txtDonGia.getText());
        if (loi != null) {
            thongBaoLoi(loi);
            return;
        }
        JOptionPane.showMessageDialog(this, "Đã cập nhật món.", "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        taiDanhSach();
    }

    private void xuLyXoa() {
        int dong = tblThucDon.getSelectedRow();
        String maMon = txtMaMon.getText().trim();
        if (maMon.isEmpty() && dong >= 0) {
            maMon = String.valueOf(modelThucDon.getValueAt(dong, 0));
        }
        if (maMon.isEmpty()) {
            thongBaoLoi("Vui lòng chọn món cần xóa.");
            return;
        }

        int luaChon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa món \"" + maMon + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (luaChon != JOptionPane.YES_OPTION) {
            return;
        }

        String loi = dsDoUongBUS.xoaDoUong(maMon);
        if (loi != null) {
            thongBaoLoi(loi);
            return;
        }
        JOptionPane.showMessageDialog(this, "Đã xóa món khỏi thực đơn.", "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        lamMoiForm();
        taiDanhSach();
    }

    private void xuLyLuu() {
        if (!Auth.isManager()) {
            thongBaoLoi("Chỉ quản lí mới có quyền lưu thực đơn.");
            return;
        }
        if (dsDoUongBUS.luuVaoCSDL()) {
            JOptionPane.showMessageDialog(this, "Đã lưu danh sách món vào CSDL.", "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            thongBaoLoi("Lưu CSDL thất bại.");
        }
    }

    private void xuLyTimKiem() {
        taiDanhSach();
        if (dsDangHienThi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy món phù hợp.",
                    "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void doiNhomLoc(String nhom) {
        nhomDangLoc = nhom;
        taiDanhSach();
    }

    private void hienThiDongDuocChon() {
        int dong = tblThucDon.getSelectedRow();
        if (dong < 0 || dong >= dsDangHienThi.size()) {
            return;
        }
        DoUongDTO du = dsDangHienThi.get(dong);
        txtMaMon.setText(du.getMaMon());
        txtTenMon.setText(du.getTenMon());
        txtDonGia.setText(DoUongBUS.dinhDangGiaNhap(du.getDonGia()));
    }

    // ==================== KHU VỰC NẠP / HIỂN THỊ DỮ LIỆU ====================
    /** Làm mới toàn bộ panel, được MainFrame gọi khi chuyển màn hình. */
    public void capNhatDuLieu() {
        capNhatThongTinNguoiDung();
        taiDanhSach();
    }

    private void capNhatThongTinNguoiDung() {
        if (Auth.getCurrentUser() == null) {
            lbQuanLy.setText("Quản lý: -");
            return;
        }
        String vaiTro = Auth.isManager() ? "Quản lý: " : "Nhân viên: ";
        lbQuanLy.setText(vaiTro + Auth.getCurrentUser().getTenNV());
    }

    private void taiDanhSach() {
        dsDangHienThi = dsDoUongBUS.locVaTim(txtTimKiem.getText(), nhomDangLoc);
        modelThucDon.setRowCount(0);
        for (DoUongDTO du : dsDangHienThi) {
            modelThucDon.addRow(new Object[] {
                du.getMaMon(),
                du.getTenMon(),
                DoUongBUS.dinhDangGia(du.getDonGia())
            });
        }
    }

    private void lamMoiForm() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtDonGia.setText("0");
        tblThucDon.clearSelection();
        txtMaMon.requestFocusInWindow();
    }

    private void thongBaoLoi(String noiDung) {
        JOptionPane.showMessageDialog(this, noiDung, "Thông báo",
                JOptionPane.WARNING_MESSAGE);
    }

    // ==================== KHU VỰC TẠO COMPONENT ====================
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

    /**
     * Tạo nhãn giả lập nút bấm cho phần header.
     * (Giữ nguyên tên hàm để đồng bộ với giao diện ban đầu.)
     */
    private JLabel makeBadge(String text, Color bg, Font font, int width) {
        JLabel lb = new JLabel(text, JLabel.CENTER);
        lb.setOpaque(true);
        lb.setBackground(bg);
        lb.setForeground(Color.WHITE);
        lb.setFont(font);
        lb.setPreferredSize(new Dimension(width, 36));
        return lb;
    }
}
