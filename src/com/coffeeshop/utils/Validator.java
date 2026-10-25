package com.coffeeshop.utils;

public class Validator {

    /**
     * Kiểm tra định dạng mã đồ uống: phải bắt đầu bằng cof, tea hoặc mil (không phân biệt hoa thường)
     * và theo sau là ít nhất 2 ký tự (ví dụ: cof01, tea02, mil03).
     */
    public static boolean isValidDrinkCode(String code) {
        if (code == null) return false;
        return code.trim().matches("(?i)^(cof|tea|mil).{2,}$");
    }

    /**
     * Lấy tiền tố mã đồ uống (trả về "cof", "tea", "mil" hoặc null)
     */
    public static String getDrinkPrefix(String code) {
        if (code == null || code.trim().length() < 3) return null;
        String prefix = code.trim().substring(0, 3).toLowerCase();
        if (prefix.equals("cof") || prefix.equals("tea") || prefix.equals("mil")) {
            return prefix;
        }
        return null;
    }

    /**
     * Kiểm tra số điện thoại Việt Nam hợp lệ (10 chữ số, bắt đầu bằng 0)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.trim().matches("^0[3|5|7|8|9][0-9]{8}$") || phone.trim().matches("^0[0-9]{9}$");
    }

    /**
     * Kiểm tra định dạng Email hợp lệ
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Kiểm tra chuỗi có phải là số thực không âm
     */
    public static boolean isPositiveDouble(String val) {
        if (val == null) return false;
        try {
            double d = Double.parseDouble(val.trim().replace(".", "").replace(",", ""));
            return d >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
