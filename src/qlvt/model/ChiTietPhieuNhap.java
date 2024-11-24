package qlvt.model;

import java.math.BigDecimal;

public class ChiTietPhieuNhap {
    private int maPhieuNhap;
    private int maVatTu;
    private int soLuongNhap;
    private BigDecimal giaNhap;

    public ChiTietPhieuNhap(int maPhieuNhap, int maVatTu, int soLuongNhap, BigDecimal giaNhap) {
        this.maPhieuNhap = maPhieuNhap;
        this.maVatTu = maVatTu;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
    }



    // Getters and Setters
    public int getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public int getMaVatTu() {
        return maVatTu;
    }

    public void setMaVatTu(int maVatTu) {
        this.maVatTu = maVatTu;
    }

    public int getSoLuongNhap() {
        return soLuongNhap;
    }

    public void setSoLuongNhap(int soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
    }

    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = BigDecimal.valueOf(giaNhap);
    }
}
