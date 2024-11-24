package qlvt.model;

public class Warehouse {
    private int maKho;
    private String tenKho;
    private String diaChi;
    private int maChiNhanh;

    public Warehouse(int maKho, String tenKho, String diaChi, int maChiNhanh) {
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.diaChi = diaChi;
        this.maChiNhanh = maChiNhanh;
    }

    public Warehouse(int maKho, String tenKho) {
        this.maKho = maKho;
        this.tenKho = tenKho;
    }



    // Getters and setters
    public int getMaKho() {
        return maKho;
    }

    public void setMaKho(int maKho) {
        this.maKho = maKho;
    }

    public String getTenKho() {
        return tenKho;
    }

    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(int maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }

    @Override
    public String toString() {
        return tenKho ;
    }
}
