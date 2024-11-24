package qlvt.model;

import java.sql.Date;

public class Order {
    private int maDonHang;
    private int maKhachHang;
    private Date ngayDat;
    private String tinhTrangDonHang;

    public Order(int maDonHang, int maKhachHang, Date ngayDat, String tinhTrangDonHang) {
        this.maDonHang = maDonHang;
        this.maKhachHang = maKhachHang;
        this.ngayDat = ngayDat;
        this.tinhTrangDonHang = tinhTrangDonHang;
    }

    public Order() {

    }


    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public Date getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getTinhTrangDonHang() {
        return tinhTrangDonHang;
    }

    public void setTinhTrangDonHang(String tinhTrangDonHang) {
        this.tinhTrangDonHang = tinhTrangDonHang;
    }

    @Override
    public String toString() {
        return "Order{" +
                "maDonHang=" + maDonHang +
                ", maKhachHang=" + maKhachHang +
                ", ngayDat=" + ngayDat +
                ", tinhTrangDonHang='" + tinhTrangDonHang + '\'' +
                '}';
    }
}
