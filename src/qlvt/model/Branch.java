package qlvt.model;

public class Branch {
    private int maChiNhanh; // Mã chi nhánh
    private String tenChiNhanh; // Tên chi nhánh
    private String diaChi;      // Địa chỉ
       // GUID

    public Branch(int maChiNhanh, String tenChiNhanh, String diaChi) {
        this.maChiNhanh = maChiNhanh;
        this.tenChiNhanh = tenChiNhanh;
        this.diaChi = diaChi;

    }



    public int getMaChiNhanh() {
        return maChiNhanh;
    }

    public String getTenChiNhanh() {
        return tenChiNhanh;
    }

    public String getDiaChi() {
        return diaChi;
    }


}
