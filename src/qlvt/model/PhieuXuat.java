package qlvt.model;

import java.sql.Date;

public class PhieuXuat {
    private int maPhieuXuat;
    private Date ngayXuat;
    private int maNhanVien;
    private int maKho;
    private int maDonHang;

    public PhieuXuat(int maPhieuXuat, Date ngayXuat, int maNhanVien, int maKho, int maDonHang) {
        this.maPhieuXuat = maPhieuXuat;
        this.ngayXuat = ngayXuat;
        this.maNhanVien = maNhanVien;
        this.maKho = maKho;
        this.maDonHang = maDonHang;
    }

    public PhieuXuat() {

    }

    public int getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(int maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public Date getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(Date ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getMaKho() {
        return maKho;
    }

    public void setMaKho(int maKho) {
        this.maKho = maKho;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }
}
