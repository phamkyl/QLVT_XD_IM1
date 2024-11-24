package qlvt.model;

import java.math.BigDecimal;

public class ChiTietPhieuXuat {
    private int maPhieuXuat;
    private int maVatTu;
    private int soLuong;
    private BigDecimal gia;

    public ChiTietPhieuXuat(int maPhieuXuat, int maVatTu, int soLuong, BigDecimal gia) {
        this.maPhieuXuat = maPhieuXuat;
        this.maVatTu = maVatTu;
        setSoLuong(soLuong); // Sử dụng setter để kiểm tra
        setGia(gia); // Sử dụng setter để kiểm tra
    }

    public int getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(int maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
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
        return "ChiTietPhieuXuat{" +
                "maPhieuXuat=" + maPhieuXuat +
                ", maVatTu=" + maVatTu +
                ", soLuong=" + soLuong +
                ", gia=" + gia +
                '}';
    }
}
