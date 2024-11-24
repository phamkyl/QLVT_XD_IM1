package qlvt.GuiView;

import qlvt.connect.PurchaseOrderDAO;
import qlvt.model.ChiTietPhieuNhap;
import qlvt.model.PhieuNhap;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PurchaseOrderGUI extends JDialog {
    private JTextField txtMaPhieuNhap, txtMaNhanVien, txtMaKho, txtMaNhaCungCap;
    private JFormattedTextField txtNgayNhap;
    private JButton btnSave, btnDelete, btnUpdate, btnClose;
    private JTable purchaseOrderTable, detailTable;
    private DefaultTableModel tableModel, detailTableModel;
    private PurchaseOrderDAO purchaseOrderDAO;

    public PurchaseOrderGUI(ImportExportView parent) {
       // super(parent, "Quản lý phiếu nhập", true);
        purchaseOrderDAO = new PurchaseOrderDAO();

        initUI();
        loadPurchaseOrders();
        setupTableSelectionListener();
        setSize(800, 600);
        setLocationRelativeTo(parent);
    }

    private void setupTableSelectionListener() {
        purchaseOrderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = purchaseOrderTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Populate text fields with the selected row's data
                    txtMaPhieuNhap.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtNgayNhap.setValue(tableModel.getValueAt(selectedRow, 1));
                    txtMaNhanVien.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtMaKho.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtMaNhaCungCap.setText(tableModel.getValueAt(selectedRow, 4).toString());

                    loadPurchaseOrderDetails(Integer.parseInt(txtMaPhieuNhap.getText()));
                }
            }
        });
    }

    private void loadPurchaseOrderDetails(int maPhieuNhap) {
        detailTableModel.setRowCount(0);
        List<ChiTietPhieuNhap> details = purchaseOrderDAO.getPurchaseOrderDetailsById(maPhieuNhap);
        for (ChiTietPhieuNhap detail : details) {
            detailTableModel.addRow(new Object[]{
                    detail.getMaVatTu(),
                    detail.getSoLuongNhap(),
                    detail.getGiaNhap()
            });
        }
    }

    private void initUI() {
        txtMaPhieuNhap = new JTextField(15);
        txtMaNhanVien = new JTextField(15);
        txtMaKho = new JTextField(15);
        txtMaNhaCungCap = new JTextField(15);
        txtNgayNhap = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        txtNgayNhap.setValue(new Date());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.add(new JLabel("Mã Phiếu Nhập:"));
        inputPanel.add(txtMaPhieuNhap);
        inputPanel.add(new JLabel("Ngày Nhập:"));
        inputPanel.add(txtNgayNhap);
        inputPanel.add(new JLabel("Mã Nhân Viên:"));
        inputPanel.add(txtMaNhanVien);
        inputPanel.add(new JLabel("Mã Kho:"));
        inputPanel.add(txtMaKho);
        inputPanel.add(new JLabel("Mã Nhà Cung Cấp:"));
        inputPanel.add(txtMaNhaCungCap);

        btnSave = new JButton("Thêm");
        btnUpdate = new JButton("Cập Nhật");
        btnDelete = new JButton("Xóa");
        btnClose = new JButton("Đóng");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClose);

        btnSave.addActionListener(e -> addPurchaseOrder());
        btnUpdate.addActionListener(e -> updatePurchaseOrder());
        btnDelete.addActionListener(e -> deletePurchaseOrder());
        btnClose.addActionListener(e -> dispose());

        tableModel = new DefaultTableModel(new Object[]{"Mã Phiếu Nhập", "Ngày Nhập", "Mã Nhân Viên", "Mã Kho", "Mã Nhà Cung Cấp"}, 0);
        purchaseOrderTable = new JTable(tableModel);

        detailTableModel = new DefaultTableModel(new Object[]{"Mã Vật Tư", "Số Lượng", "Giá Nhập"}, 0);
        detailTable = new JTable(detailTableModel);

        // Panel for purchase order details
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        // Buttons for detail management
        JButton btnAddDetail = new JButton("Thêm Chi Tiết");
        JButton btnUpdateDetail = new JButton("Cập Nhật Chi Tiết");
        JButton btnDeleteDetail = new JButton("Xóa Chi Tiết");

        for (JButton button : new JButton[]{btnSave, btnUpdate, btnDelete, btnClose,btnAddDetail,btnUpdateDetail,btnDeleteDetail}) {
            button.setBackground(new Color(3, 166, 120));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
        }

        JPanel detailButtonPanel = new JPanel();
        detailButtonPanel.add(btnAddDetail);
        detailButtonPanel.add(btnUpdateDetail);
        detailButtonPanel.add(btnDeleteDetail);

        detailPanel.add(detailButtonPanel, BorderLayout.SOUTH);

        btnAddDetail.addActionListener(e -> addDetail());
        btnUpdateDetail.addActionListener(e -> updateDetail());
        btnDeleteDetail.addActionListener(e -> deleteDetail());

        add(new JScrollPane(purchaseOrderTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(detailPanel, BorderLayout.EAST);
    }

    private void loadPurchaseOrders() {
        tableModel.setRowCount(0);
        List<PhieuNhap> orders = purchaseOrderDAO.getAllPurchaseOrders();
        for (PhieuNhap order : orders) {
            tableModel.addRow(new Object[]{
                    order.getMaPhieuNhap(),
                    order.getNgayNhap(),
                    order.getMaNhanVien(),
                    order.getMaKho(),
                    order.getMaNhaCungCap()
            });
        }
    }

    private void addPurchaseOrder() {
        try {
            PhieuNhap order = new PhieuNhap(
                    Integer.parseInt(txtMaPhieuNhap.getText()),
                    java.sql.Date.valueOf(txtNgayNhap.getText()),
                    Integer.parseInt(txtMaNhanVien.getText()),
                    Integer.parseInt(txtMaKho.getText()),
                    Integer.parseInt(txtMaNhaCungCap.getText())
            );
            purchaseOrderDAO.addPurchaseOrder(order);
            loadPurchaseOrders();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding purchase order: " + e.getMessage());
        }
    }

    private void updatePurchaseOrder() {
        try {
            PhieuNhap order = new PhieuNhap(
                    Integer.parseInt(txtMaPhieuNhap.getText()),
                    java.sql.Date.valueOf(txtNgayNhap.getText()),
                    Integer.parseInt(txtMaNhanVien.getText()),
                    Integer.parseInt(txtMaKho.getText()),
                    Integer.parseInt(txtMaNhaCungCap.getText())
            );
            purchaseOrderDAO.updatePurchaseOrder(order);
            loadPurchaseOrders();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating purchase order: " + e.getMessage());
        }
    }

    private void deletePurchaseOrder() {
        try {
            int maPhieuNhap = Integer.parseInt(txtMaPhieuNhap.getText());
            purchaseOrderDAO.deletePurchaseOrder(String.valueOf(maPhieuNhap));
            loadPurchaseOrders();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting purchase order: " + e.getMessage());
        }
    }

    private void addDetail() {
        try {
            int maPhieuNhap = Integer.parseInt(txtMaPhieuNhap.getText());
            int maVatTu = Integer.parseInt(JOptionPane.showInputDialog("Nhập Mã Vật Tư:"));
            int soLuong = Integer.parseInt(JOptionPane.showInputDialog("Nhập Số Lượng:"));
            String giaNhapInput = JOptionPane.showInputDialog("Nhập Giá Nhập:");
            BigDecimal giaNhap = new BigDecimal(giaNhapInput);


            ChiTietPhieuNhap detail = new ChiTietPhieuNhap(maPhieuNhap, maVatTu, soLuong, giaNhap);
            purchaseOrderDAO.addDetail(detail);
            loadPurchaseOrderDetails(maPhieuNhap);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding detail: " + e.getMessage());
        }
    }

    private void updateDetail() {
        try {
            int maPhieuNhap = Integer.parseInt(txtMaPhieuNhap.getText());
            int selectedRow = detailTable.getSelectedRow();

            if (selectedRow != -1) {
                int maVatTu = Integer.parseInt(detailTableModel.getValueAt(selectedRow, 0).toString());
                int soLuong = Integer.parseInt(JOptionPane.showInputDialog("Nhập Số Lượng Mới:", detailTableModel.getValueAt(selectedRow, 1)));
                BigDecimal giaNhap = new BigDecimal(JOptionPane.showInputDialog("Nhập Giá Nhập Mới:", detailTableModel.getValueAt(selectedRow, 2).toString()));


                ChiTietPhieuNhap detail = new ChiTietPhieuNhap(maPhieuNhap, maVatTu, soLuong, giaNhap);
                purchaseOrderDAO.updateDetail(detail);
                loadPurchaseOrderDetails(maPhieuNhap);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating detail: " + e.getMessage());
        }
    }

    private void deleteDetail() {
        try {
            int maPhieuNhap = Integer.parseInt(txtMaPhieuNhap.getText());
            int selectedRow = detailTable.getSelectedRow();

            if (selectedRow != -1) {
                int maVatTu = Integer.parseInt(detailTableModel.getValueAt(selectedRow, 0).toString());
                purchaseOrderDAO.deleteDetail(maPhieuNhap, maVatTu);
                loadPurchaseOrderDetails(maPhieuNhap);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting detail: " + e.getMessage());
        }
    }
}
