package newDayCoffee.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DSDoUongDTO {
    private List<DoUongDTO> dsDoUong = new ArrayList<>();
    private Set<String> maDaXoa = new HashSet<>(); // lưu mã viết thường

    public DSDoUongDTO() {
    }

    public DSDoUongDTO(List<DoUongDTO> dsDoUong) {
        if (dsDoUong != null) {
            this.dsDoUong = dsDoUong;
        }
    }

    public boolean them(DoUongDTO du) {
        if (du == null || du.getMaMon() == null || kiemTraTrungMa(du.getMaMon())) {
            return false;
        }
        dsDoUong.add(du);
        return true;
    }

    public boolean xoaMem(String maDoUong) {
        DoUongDTO du = timTheoMa(maDoUong);
        if (du == null) {
            return false;
        }
        maDaXoa.add(du.getMaMon().toLowerCase());
        return true;
    }

    public boolean sua(DoUongDTO du) {
        if (du == null) {
            return false;
        }
        DoUongDTO cu = timTheoMa(du.getMaMon());
        if (cu == null) {
            return false;
        }
        cu.setTenMon(du.getTenMon());
        cu.setDonGia(du.getDonGia());
        cu.setMoTa(du.getMoTa());
        return true;
    }

    public DoUongDTO timTheoMa(String maDoUong) {
        if (maDoUong == null) {
            return null;
        }
        for (DoUongDTO du : dsDoUong) {
            if (!daXoa(du) && du.getMaMon().equalsIgnoreCase(maDoUong)) {
                return du;
            }
        }
        return null;
    }

    public List<DoUongDTO> timTheoTienTo(String tienTo) {
        List<DoUongDTO> kq = new ArrayList<>();
        if (tienTo == null) {
            return kq;
        }
        String t = tienTo.trim().toLowerCase();
        for (DoUongDTO du : dsDoUong) {
            if (!daXoa(du) && du.getMaMon().toLowerCase().startsWith(t)) {
                kq.add(du);
            }
        }
        return kq;
    }

    public boolean kiemTraTrungMa(String maDoUong) {
        for (DoUongDTO du : dsDoUong) {
            if (du.getMaMon().equalsIgnoreCase(maDoUong)) {
                return true;
            }
        }
        return false;
    }

    public List<DoUongDTO> getDsHienThi() {
        List<DoUongDTO> kq = new ArrayList<>();
        for (DoUongDTO du : dsDoUong) {
            if (!daXoa(du)) {
                kq.add(du);
            }
        }
        return kq;
    }

    public List<DoUongDTO> getDsDoUong() {
        return dsDoUong;
    }

    /** TODO: nối với DoUongDAO khi nhóm viết xong DAO. Hiện chỉ trả về true. */
    public boolean luuVaoCSDL() {
        return true;
    }

    private boolean daXoa(DoUongDTO du) {
        return maDaXoa.contains(du.getMaMon().toLowerCase());
    }
}