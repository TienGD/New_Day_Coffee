package newDayCoffee.dto;

import java.util.ArrayList;
import java.util.List;


public class DSNhanVienDTO {
    private List<NhanVienDTO> dsNhanVien = new ArrayList<>();

    public DSNhanVienDTO() {
    }

    public DSNhanVienDTO(List<NhanVienDTO> dsNhanVien) {
        if (dsNhanVien != null) {
            this.dsNhanVien = dsNhanVien;
        }
    }

    public boolean them(NhanVienDTO nv) {
        if (nv == null || nv.getMaNV() == null || kiemTraTrungMa(nv.getMaNV())) {
            return false;
        }
        dsNhanVien.add(nv);
        return true;
    }

    /** Xóa nhân viên theo mã. Trả về false nếu không tìm thấy. */
    public boolean xoa(String maNV) {
        NhanVienDTO nv = timTheoMa(maNV);
        if (nv == null) {
            return false;
        }
        return dsNhanVien.remove(nv);
    }

    public boolean sua(NhanVienDTO nv) {
        if (nv == null) {
            return false;
        }
        NhanVienDTO cu = timTheoMa(nv.getMaNV());
        if (cu == null) {
            return false;
        }
        cu.setTenNV(nv.getTenNV());
        cu.setNgaySinh(nv.getNgaySinh());
        cu.setSdt(nv.getSdt());
        cu.setEmail(nv.getEmail());
        cu.setChucVu(nv.getChucVu());
        cu.setGioiTinh(nv.getGioiTinh());
        cu.setMatKhau(nv.getMatKhau());
        return true;
    }

    public NhanVienDTO timTheoMa(String maNV) {
        if (maNV == null) {
            return null;
        }
        for (NhanVienDTO nv : dsNhanVien) {
            if (nv.getMaNV().equalsIgnoreCase(maNV)) {
                return nv;
            }
        }
        return null;
    }

    public List<NhanVienDTO> timTheoTen(String tenNV) {
        List<NhanVienDTO> kq = new ArrayList<>();
        if (tenNV == null) {
            return kq;
        }
        String t = tenNV.trim().toLowerCase();
        for (NhanVienDTO nv : dsNhanVien) {
            if (nv.getTenNV().toLowerCase().contains(t)) {
                kq.add(nv);
            }
        }
        return kq;
    }

    public boolean kiemTraTrungMa(String maNV) {
        return timTheoMa(maNV) != null;
    }

    public NhanVienDTO kiemTraDangNhap(String maNV, String matKhau) {
        NhanVienDTO nv = timTheoMa(maNV);
        if (nv != null && nv.getMatKhau() != null && nv.getMatKhau().equals(matKhau)) {
            return nv;
        }
        return null;
    }

    public List<NhanVienDTO> getDsNhanVien() {
        return dsNhanVien;
    }

    /** TODO: nối với NhanVienDAO khi nhóm viết xong DAO. Hiện chỉ trả về true. */
    public boolean luuVaoCSDL() {
        return true;
    }
}