package qlvt.GuiView;

import qlvt.connect.WarehouseDAO;
import qlvt.model.Warehouse;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class WarehouseManagementView extends JPanel {
    private DefaultTableModel model;
    private WarehouseDAO warehouseDAO;
    private int maChiNhanh; // Branch ID
    private JTable tableWarehouse;

    private JTextField txtMakho, txtTenkho, txtDiachi, txtMachinhanh;

    public WarehouseManagementView(MainView_IM mainViewIm, int maChiNhanh) throws SQLException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.maChiNhanh = maChiNhanh; // Initialize branch ID
        warehouseDAO = new WarehouseDAO();

        // Title label
        JLabel titleLabel = new JLabel("DANH SÁCH KHO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(3, 166, 120));

        // Input panel
        txtMakho = new JTextField(20);
        txtTenkho = new JTextField(20);
        txtDiachi = new JTextField(20);
        txtMachinhanh = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 6, 6));
        inputPanel.add(new JLabel("Mã Kho:"));
        inputPanel.add(txtMakho);
        inputPanel.add(new JLabel("Tên Kho:"));
        inputPanel.add(txtTenkho);
        inputPanel.add(new JLabel("Địa Chỉ:"));
        inputPanel.add(txtDiachi);
        inputPanel.add(new JLabel("Mã Chi Nhánh:"));
        inputPanel.add(txtMachinhanh);

        // Table panel
        model = new DefaultTableModel(new Object[]{"Mã Kho", "Tên Kho", "Địa Chỉ", "Mã Chi Nhánh"}, 0);
        tableWarehouse = new JTable(model);

        // Add MouseListener to table
        tableWarehouse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableWarehouse.getSelectedRow();
                if (selectedRow != -1) {
                    txtMakho.setText(model.getValueAt(selectedRow, 0).toString());
                    txtTenkho.setText(model.getValueAt(selectedRow, 1).toString());
                    txtDiachi.setText(model.getValueAt(selectedRow, 2).toString());
                    txtMachinhanh.setText(model.getValueAt(selectedRow, 3).toString());
                }
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new LineBorder(Color.BLACK, 1));
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(tableWarehouse), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton removeButton = new JButton("Remove");
        JButton exitButton = new JButton("Thoát");

        for (JButton button : new JButton[]{addButton, editButton, deleteButton, exitButton, removeButton}) {
            button.setBackground(new Color(3, 166, 120));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
        }

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(removeButton);

        // Add panels to main layout
        add(inputPanel);
        add(tablePanel);
        add(buttonPanel);

        // Button actions
        addButton.addActionListener(e -> addWarehouse());
        editButton.addActionListener(e -> editWarehouse());
        deleteButton.addActionListener(e -> deleteWarehouse());
        exitButton.addActionListener(e -> System.exit(0));
        removeButton.addActionListener(e -> clearFields());

        loadWarehouses(); // Load data from the database on initialization
    }

    private void clearFields() {
        txtMakho.setText("");
        txtTenkho.setText("");
        txtDiachi.setText("");
        txtMachinhanh.setText("");
    }

    private void addWarehouse() {
        try {
            int maKho = Integer.parseInt(txtMakho.getText());
            String tenKho = txtTenkho.getText();
            String diaChi = txtDiachi.getText();
            int maChiNhanh = Integer.parseInt(txtMachinhanh.getText());

            if (tenKho.isEmpty() || diaChi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Warehouse newWarehouse = new Warehouse(maKho, tenKho, diaChi, maChiNhanh);
            warehouseDAO.addWarehouse(newWarehouse);
            loadWarehouses();  // Refresh the table
            clearFields();  // Clear the input fields
        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm kho: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editWarehouse() {
        try {
            int maKho = Integer.parseInt(txtMakho.getText());
            String tenKho = txtTenkho.getText();
            String diaChi = txtDiachi.getText();
            int maChiNhanh = Integer.parseInt(txtMachinhanh.getText());

            if (tenKho.isEmpty() || diaChi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Warehouse updatedWarehouse = new Warehouse(maKho, tenKho, diaChi, maChiNhanh);
            warehouseDAO.updateWarehouse(updatedWarehouse);
            loadWarehouses();  // Refresh the table
            clearFields();  // Clear the input fields
        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa kho: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteWarehouse() {
        try {
            // Get MaKho from the input field (e.g., a text field)
            int maKho = Integer.parseInt(txtMakho.getText());

            // Get MaChiNhanh from the user input or from the selected row in the table
            int maChiNhanh = Integer.parseInt(txtMachinhanh.getText());  // Assuming you have a field for MaChiNhanh

            // Ensure that the user has selected a warehouse to delete
            if (maKho == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kho để xóa!", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Call the deleteWarehouse method with MaKho and MaChiNhanh
            warehouseDAO.deleteWarehouse(maKho, maChiNhanh);

            // Refresh the warehouse list after deletion
            loadWarehouses();  // Assuming loadWarehouses() refreshes the displayed list of warehouses
            clearFields();  // Clear input fields after deletion

        } catch (NumberFormatException ex) {
            // Handle invalid number format
            JOptionPane.showMessageDialog(this, "Mã kho hoặc mã chi nhánh không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            // Handle database-related errors
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa kho: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void loadWarehouses() {
        try {
            List<Warehouse> warehouses = warehouseDAO.getWarehousesByBranch(maChiNhanh);
            model.setRowCount(0);  // Clear existing rows
            if (warehouses.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy kho trong chi nhánh này", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            }
            for (Warehouse warehouse : warehouses) {
                model.addRow(new Object[]{
                        warehouse.getMaKho(),
                        warehouse.getTenKho(),
                        warehouse.getDiaChi(),
                        warehouse.getMaChiNhanh()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải kho: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
