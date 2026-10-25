package com.coffeeshop.utils;

import com.coffeeshop.dto.NhanVienDTO;

public class Auth {
    // Lưu thông tin phiên đăng nhập hiện tại
    private static NhanVienDTO currentUser = null;

    public static void login(NhanVienDTO user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static NhanVienDTO getCurrentUser() {
        return currentUser;
    }

    public static boolean isManager() {
        return currentUser != null && currentUser.getChucVu() != null 
                && currentUser.getChucVu().toLowerCase().contains("quản l");
    }

    public static String getDisplayName() {
        if (currentUser == null) return "Khách";
        return currentUser.getChucVu() + ": " + currentUser.getTenNV();
    }
}
