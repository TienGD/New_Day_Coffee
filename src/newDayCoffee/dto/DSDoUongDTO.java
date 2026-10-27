package newDayCoffee.dto;

import java.util.ArrayList;
import java.util.List;


public class DSDoUongDTO {
    private List<DoUongDTO> dsDoUong = new ArrayList<>();

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
        du.setDaXoa(true);
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
        return true;
    }

    public DoUongDTO timTheoMa(String maDoUong) {
        for (DoUongDTO du : dsDoUong) {
            if (!du.isDaXoa() && du.getMaMon().equalsIgnoreCase(maDoUong)) {
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
            if (!du.isDaXoa() && du.getMaMon().toLowerCase().startsWith(t)) {
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
            if (!du.isDaXoa()) {
                kq.add(du);
            }
        }
        return kq;
    }

    public List<DoUongDTO> getDsDoUong() {
        return dsDoUong;
    }

    public boolean luuVaoCSDL() {
    	// TODO: goi DAO de luu khi DAO viet xong
        return true;
    }
}