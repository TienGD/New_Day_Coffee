package com.coffeeshop.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class QuanLyThucDonPanel extends JPanel {

    // ==================== MODEL ====================
    static class MonUong {
        String ma, ten, gia;
        boolean isDeleted;

        MonUong(String ma, String ten, String gia) {
            this.ma = ma;
            this.ten = ten;
            this.gia = gia;
            this.isDeleted = false;
        }
    }

    // ==================== TABLE MODEL ====================
    static class ThucDonTableModel extends AbstractTableModel {
        private final String[] cols = {"Mã đồ uống", "Tên món", "Giá bán"};
        private List<MonUong> visible = new ArrayList<>();
        private List<MonUong> database = new ArrayList<>();

        ThucDonTableModel() {
            String[][] seed = {
                {"cof01", "Bạc xỉu",        "15.000"},
                {"cof02", "Cafe đen",        "15.000"},
                {"cof03", "Bạc xỉu đá",     "15.000"},
                {"tea01", "Trà Đào",         "15.000"},
                {"tea02", "Trà trái cây",    "15.000"},
                {"tea03", "Trà măng cụt",    "15.000"},
                {"tea04", "Trà dưa lưới",    "15.000"},
                {"mil01", "Bạc xỉu kem",     "20.000"},
                {"mil02", "Trà sữa thái",    "15.000"},
            };
            for (String[] row : seed) {
                MonUong m = new MonUong(row[0], row[1], row[2]);
                database.add(m);
                visible.add(m);
            }
        }

        @Override public int getRowCount() { return visible.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override
        public Object getValueAt(int r, int c) {
            if (r < 0 || r >= visible.size()) return "";
            MonUong m = visible.get(r);
            switch (c) {
                case 0: return m.ma;
                case 1: return m.ten;
                case 2: return m.gia.endsWith("Đ") ? m.gia : (m.gia + "Đ");
                default: return "";
            }
        }

        MonUong getAt(int row) {
            if (row >= 0 && row < visible.size()) return visible.get(row);
            return null;
        }

        boolean existsMa(String ma) {
            for (MonUong m : database) {
                if (m.ma.equalsIgnoreCase(ma)) return true;
            }
            return false;
        }

        void addMon(MonUong m) {
            database.add(m);
            visible.add(m);
            fireTableRowsInserted(visible.size() - 1, visible.size() - 1);
        }

        void updateMon(int viewRow, String tenMoi, String giaMoi) {
            MonUong m = visible.get(viewRow);
            m.ten = tenMoi;
            m.gia = giaMoi;
            fireTableRowsUpdated(viewRow, viewRow);
        }

        void softDeleteAt(int viewRow) {
            MonUong m = visible.get(viewRow);
            m.isDeleted = true;
            visible.remove(viewRow);
            fireTableRowsDeleted(viewRow, viewRow);
        }

        void filterByPrefix(String prefix) {
            visible.clear();
            for (MonUong m : database) {
                if (m.isDeleted) continue;
                if (prefix == null || prefix.isEmpty() || m.ma.toLowerCase().startsWith(prefix.toLowerCase())) {
                    visible.add(m);
                }
            }
            fireTableDataChanged();
        }

        void searchMa(String input) {
            String q = (input == null) ? "" : input.trim().toLowerCase();
            if (q.isEmpty()) {
                filterByPrefix(null);
                return;
            }
            String prefix = null;
            if (q.startsWith("cof")) prefix = "cof";
            else if (q.startsWith("tea")) prefix = "tea";
            else if (q.startsWith("mil")) prefix = "mil";

            if (prefix == null) {
                visible.clear();
                fireTableDataChanged();
                return;
            }

            visible.clear();
            for (MonUong m : database) {
                if (!m.isDeleted && m.ma.equalsIgnoreCase(q)) {
                    visible.add(m);
                }
            }
            if (!visible.isEmpty()) {
                fireTableDataChanged();
                return;
            }

            for (MonUong m : database) {
                if (!m.isDeleted && m.ma.toLowerCase().startsWith(prefix)) {
                    visible.add(m);
                }
            }
            fireTableDataChanged();
        }
    }

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

    private JTable tblThucDon;
    private ThucDonTableModel modelThucDon;

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
        JButton btnQuanLy = makeBtn("Quản lý: Lâm Nguyễn Như Ý", bgHeaderBtn, fontLabel, 210);
        pnHeaderRight.add(btnCaKet);
        pnHeaderRight.add(btnQuanLy);
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
        txtTimKiem.setToolTipText("Nhập mã: cofXX, teaXX, milXX...");

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
        modelThucDon = new ThucDonTableModel();
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

        // Sự kiện chọn dòng bảng -> đổ lên form
        tblThucDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int r = tblThucDon.getSelectedRow();
                if (r >= 0) {
                    MonUong m = modelThucDon.getAt(r);
                    if (m != null) {
                        txtMaMon.setText(m.ma);
                        txtTenMon.setText(m.ten);
                        txtDonGia.setText(m.gia.replace("Đ", "").trim());
                        txtMaMon.setEditable(false);
                        txtMaMon.setBackground(new Color(210, 200, 190));
                    }
                }
            }
        });

        // Nút Thêm
        btnThem.addActionListener(e -> {
            String ma = txtMaMon.getText().trim();
            String ten = txtTenMon.getText().trim();
            String gia = txtDonGia.getText().trim();

            if (ma.isEmpty() || ten.isEmpty() || gia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã món, Tên món, Giá bán!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!ma.matches("(?i)^(cof|tea|mil).{2,}$")) {
                JOptionPane.showMessageDialog(this, "Mã món không hợp lệ! Phải bắt đầu bằng cof, tea, hoặc mil (vd: cof01, tea02, mil03).", "Sai định dạng mã", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modelThucDon.existsMa(ma)) {
                JOptionPane.showMessageDialog(this, "Mã món đã tồn tại", "Cảnh báo trùng lặp", JOptionPane.ERROR_MESSAGE);
                return;
            }

            modelThucDon.addMon(new MonUong(ma, ten, gia));
            JOptionPane.showMessageDialog(this, "Thêm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            resetForm(bgInput);
        });

        // Nút Sửa
        btnSua.addActionListener(e -> {
            int r = tblThucDon.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần sửa!", "Chưa chọn món", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String ten = txtTenMon.getText().trim();
            String gia = txtDonGia.getText().trim();
            if (ten.isEmpty() || gia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên món và Giá bán không được rỗng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            modelThucDon.updateMon(r, ten, gia);
            JOptionPane.showMessageDialog(this, "Đã sửa thông tin món!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            resetForm(bgInput);
        });

        // Nút Xóa
        btnXoa.addActionListener(e -> {
            int r = tblThucDon.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!", "Chưa chọn món", JOptionPane.WARNING_MESSAGE);
                return;
            }
            MonUong m = modelThucDon.getAt(r);
            int opt = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa món \"" + m.ten + "\" (" + m.ma + ") không?", "Xác nhận xóa món", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                modelThucDon.softDeleteAt(r);
                JOptionPane.showMessageDialog(this, "Đã xóa món (xóa mềm).", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                resetForm(bgInput);
            }
        });

        // Nút Lưu CSDL
        btnLuu.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã lưu mọi thao tác vào cơ sở dữ liệu thành công!", "Lưu CSDL", JOptionPane.INFORMATION_MESSAGE);
        });

        // Tìm kiếm
        btnTimKiem.addActionListener(e -> {
            String input = txtTimKiem.getText();
            modelThucDon.searchMa(input);
            if (modelThucDon.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy món", "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Bộ lọc
        btnLocTatCa.addActionListener(e -> modelThucDon.filterByPrefix(null));
        btnLocCaPhe.addActionListener(e -> modelThucDon.filterByPrefix("cof"));
        btnLocTra.addActionListener(e -> modelThucDon.filterByPrefix("tea"));
        btnLocTraSua.addActionListener(e -> modelThucDon.filterByPrefix("mil"));
    }

    private void resetForm(Color bgInput) {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtDonGia.setText("0");
        txtMaMon.setEditable(true);
        txtMaMon.setBackground(bgInput);
        tblThucDon.clearSelection();
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
}
