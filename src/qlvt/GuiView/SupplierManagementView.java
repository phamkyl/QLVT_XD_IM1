package qlvt.GuiView;

import qlvt.connect.SupplierDAO;
import qlvt.model.Supplier;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class SupplierManagementView extends JPanel {
    private DefaultTableModel model;
    private SupplierDAO supplierDAO;
    private JTextField txtMaNhaCungCap, txtTenNhaCungCap, txtDiaChi, txtSoDienThoai, txtEmail;
    private JTable supplierTable;

    public SupplierManagementView(MainView_IM mainViewIm) {
        supplierDAO = new SupplierDAO();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title label
        JLabel titleLabel = new JLabel("DANH SÁCH NHÀ CUNG CẤP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(3, 166, 120));

        // Input panel
        txtMaNhaCungCap = new JTextField(20);
        txtTenNhaCungCap = new JTextField(20);
        txtDiaChi = new JTextField(20);
        txtSoDienThoai = new JTextField(20);
        txtEmail = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 6, 6));
        inputPanel.add(new JLabel("Mã Nhà Cung Cấp:"));
        inputPanel.add(txtMaNhaCungCap);
        inputPanel.add(new JLabel("Tên Nhà Cung Cấp:"));
        inputPanel.add(txtTenNhaCungCap);
        inputPanel.add(new JLabel("Địa Chỉ:"));
        inputPanel.add(txtDiaChi);
        inputPanel.add(new JLabel("Số Điện Thoại:"));
        inputPanel.add(txtSoDienThoai);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);

        // Table panel
        model = new DefaultTableModel(new Object[]{"Mã Nhà Cung Cấp", "Tên Nhà Cung Cấp", "Địa Chỉ", "Số Điện Thoại", "Email"}, 0);
        supplierTable = new JTable(model);

        // Add MouseListener to table
        supplierTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = supplierTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtMaNhaCungCap.setText(model.getValueAt(selectedRow, 0).toString());
                    txtTenNhaCungCap.setText(model.getValueAt(selectedRow, 1).toString());
                    txtDiaChi.setText(model.getValueAt(selectedRow, 2).toString());
                    txtSoDienThoai.setText(model.getValueAt(selectedRow, 3).toString());
                    txtEmail.setText(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new LineBorder(Color.BLACK, 1));
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(supplierTable), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton exitButton = new JButton("Thoát");

        // Set button colors and font
        for (JButton button : new JButton[]{addButton, editButton, deleteButton, exitButton}) {
            button.setBackground(new Color(3, 166, 120));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
        }

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);

        // Add panels to main layout
        add(inputPanel);
        add(tablePanel);
        add(buttonPanel);

        // Button actions
        addButton.addActionListener(e -> addSupplier());
        editButton.addActionListener(e -> editSupplier());
        deleteButton.addActionListener(e -> deleteSupplier());
        exitButton.addActionListener(e -> System.exit(0));

        // Load suppliers into table
        loadSuppliers();
    }

    // Load suppliers from database
    private void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            model.setRowCount(0); // Clear existing rows
            for (Supplier supplier : suppliers) {
                model.addRow(new Object[]{
                        supplier.getMaNhaCungCap(),
                        supplier.getTenNhaCungCap(),
                        supplier.getDiaChi(),
                        supplier.getSoDienThoai(),
                        supplier.getEmail()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add supplier
    private void addSupplier() {
        try {
            Supplier supplier = new Supplier(
                    Integer.parseInt(txtMaNhaCungCap.getText()),
                    txtTenNhaCungCap.getText(),
                    txtDiaChi.getText(),
                    txtSoDienThoai.getText(),
                    txtEmail.getText()
            );
            supplierDAO.addSupplier(supplier);
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
            loadSuppliers();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit supplier
    private void editSupplier() {
        try {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để sửa.");
                return;
            }

            Supplier supplier = new Supplier(
                    Integer.parseInt(txtMaNhaCungCap.getText()),
                    txtTenNhaCungCap.getText(),
                    txtDiaChi.getText(),
                    txtSoDienThoai.getText(),
                    txtEmail.getText()
            );
            supplierDAO.updateSupplier(supplier);
            JOptionPane.showMessageDialog(this, "Cập nhật nhà cung cấp thành công!");
            loadSuppliers();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete supplier
    private void deleteSupplier() {
        try {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0);
                supplierDAO.deleteSupplier(id);
                JOptionPane.showMessageDialog(this, "Xóa nhà cung cấp thành công!");
                loadSuppliers();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
