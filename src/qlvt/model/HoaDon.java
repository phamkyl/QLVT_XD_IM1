package qlvt.model;

import java.math.BigDecimal;
import java.sql.Date;

public class HoaDon {

    private int maHoaDon;
    private int maDonHang;
    private Date ngayLap;
    private BigDecimal tongTien;
    private String tinhTrangThanhToan;

    public HoaDon(int maHoaDon, int maDonHang, Date ngayLap, BigDecimal tongTien, String tinhTrangThanhToan) {
        this.maHoaDon = maHoaDon;
        this.maDonHang = maDonHang;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.tinhTrangThanhToan = tinhTrangThanhToan;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getTinhTrangThanhToan() {
        return tinhTrangThanhToan;
    }

    public void setTinhTrangThanhToan(String tinhTrangThanhToan) {
        this.tinhTrangThanhToan = tinhTrangThanhToan;
    }


}
