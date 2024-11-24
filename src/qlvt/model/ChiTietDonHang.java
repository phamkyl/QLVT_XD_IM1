package qlvt.model;

import java.math.BigDecimal;

public class ChiTietDonHang {

    private int maDonHang;
    private int maVatTu;
    private int soLuong;
    private BigDecimal gia;

    public ChiTietDonHang(int maDonHang, int maVatTu, int soLuong, BigDecimal gia) {
        this.maDonHang = maDonHang;
        this.maVatTu = maVatTu;
        setSoLuong(soLuong); // Use setter for validation
        setGia(gia); // Use setter for validation
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getMaVatTu() {
        return maVatTu;
    }

    public void setMaVatTu(int maVatTu) {
        this.maVatTu = maVatTu;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không thể âm.");
        }
        this.soLuong = soLuong;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        if (gia.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá không thể âm.");
        }
        this.gia = gia;
    }

    @Override
    public String toString() {
        return "ChiTietDonHang{" +
                "maDonHang=" + maDonHang +
                ", maVatTu=" + maVatTu +
                ", soLuong=" + soLuong +
                ", gia=" + gia +
                '}';
    }
}
