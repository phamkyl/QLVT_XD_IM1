package qlvt.model;

public class Material {
    private int maVatTu;
    private String tenVatTu;
    private String moTa;
    private String donViTinh;
    private double gia;
    private int maNhaCungCap;

    public Material(int maVatTu, String tenVatTu, String moTa, String donViTinh, double gia, int maNhaCungCap) {
        this.maVatTu = maVatTu;
        this.tenVatTu = tenVatTu;
        this.moTa = moTa;
        this.donViTinh = donViTinh;
        this.gia = gia;
        this.maNhaCungCap = maNhaCungCap;
    }

    public Material(int maVatTu, String tenVatTu) {
        this.maVatTu = maVatTu;
        this.tenVatTu = tenVatTu;
    }

    public int getMaVatTu() {
        return maVatTu;
    }

    public String getTenVatTu() {
        return tenVatTu;
    }

    public String getMoTa() {
        return moTa;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public double getGia() {
        return gia;
    }

    public int getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaVatTu(int maVatTu) {
        this.maVatTu = maVatTu;
    }

    public void setTenVatTu(String tenVatTu) {
        this.tenVatTu = tenVatTu;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public void setMaNhaCungCap(int maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    @Override
    public String toString() {
        return tenVatTu;
    }
}
