package qlvt.GuiView;

import qlvt.connect.MaterialDAO;
import qlvt.model.Material;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class MaterialManagementView extends JPanel {
    private DefaultTableModel model;
    private MaterialDAO materialDAO;
    private JTextField txtMaVatTu, txtTenVatTu, txtMoTa, txtDonViTinh, txtGia, txtMaNhaCungCap;
    private JTable tableMaterial;
    private int maChiNhanh;

    public MaterialManagementView(MainView_IM mainViewIm,int maChiNhanh) {
        materialDAO = new MaterialDAO();
        this.maChiNhanh = maChiNhanh;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title label
        JLabel titleLabel = new JLabel("DANH SÁCH VẬT TƯ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(3, 166, 120));

        // Input panel
        txtMaVatTu = new JTextField(20);
        txtTenVatTu = new JTextField(20);
        txtMoTa = new JTextField(20);
        txtDonViTinh = new JTextField(20);
        txtGia = new JTextField(20);
        txtMaNhaCungCap = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 6, 6));
        inputPanel.add(new JLabel("Mã Vật Tư:"));
        inputPanel.add(txtMaVatTu);
        inputPanel.add(new JLabel("Tên Vật Tư:"));
        inputPanel.add(txtTenVatTu);
        inputPanel.add(new JLabel("Mô Tả:"));
        inputPanel.add(txtMoTa);
        inputPanel.add(new JLabel("Đơn Vị Tính:"));
        inputPanel.add(txtDonViTinh);
        inputPanel.add(new JLabel("Giá:"));
        inputPanel.add(txtGia);
        inputPanel.add(new JLabel("Mã Nhà Cung Cấp:"));
        inputPanel.add(txtMaNhaCungCap);

        // Table panel
        model = new DefaultTableModel(new Object[]{"Mã Vật Tư", "Tên Vật Tư", "Mô Tả", "Đơn Vị Tính", "Giá", "Mã Nhà Cung Cấp"}, 0);
        tableMaterial = new JTable(model);

        // Add MouseListener to table
        tableMaterial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableMaterial.getSelectedRow();
                if (selectedRow != -1) {
                    txtMaVatTu.setText(model.getValueAt(selectedRow, 0).toString());
                    txtTenVatTu.setText(model.getValueAt(selectedRow, 1).toString());
                    txtMoTa.setText(model.getValueAt(selectedRow, 2).toString());
                    txtDonViTinh.setText(model.getValueAt(selectedRow, 3).toString());
                    txtGia.setText(model.getValueAt(selectedRow, 4).toString());
                    txtMaNhaCungCap.setText(model.getValueAt(selectedRow, 5).toString());
                }
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new LineBorder(Color.BLACK, 1));
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(tableMaterial), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton removeButton = new JButton("Remove");
        JButton exitButton = new JButton("Thoát");


        for (JButton button : new JButton[]{addButton, editButton, deleteButton, exitButton,removeButton}) {
            button.setBackground(new Color(3, 166, 120));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
        }
        if (maChiNhanh == 0) {
            addButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            removeButton.setVisible(false);
            exitButton.setVisible(false);
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
        addButton.addActionListener(e -> addMaterial());
        editButton.addActionListener(e -> editMaterial());
        deleteButton.addActionListener(e -> deleteMaterial());
        exitButton.addActionListener(e -> System.exit(0));
        removeButton.addActionListener(e -> removeButton());

        // Load materials into table
        loadMaterials();
    }

    private void removeButton() {
        clearFields();
    }

    private void loadMaterials() {
        try {
            List<Material> materials = materialDAO.getAllMaterials();
            model.setRowCount(0); // Clear existing rows
            for (Material material : materials) {
                model.addRow(new Object[]{
                        material.getMaVatTu(),
                        material.getTenVatTu(),
                        material.getMoTa(),
                        material.getDonViTinh(),
                        material.getGia(),
                        material.getMaNhaCungCap()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addMaterial() {
        try {
            Material material = new Material(
                    Integer.parseInt(txtMaVatTu.getText()),
                    txtTenVatTu.getText(),
                    txtMoTa.getText(),
                    txtDonViTinh.getText(),
                    Double.parseDouble(txtGia.getText()),
                    Integer.parseInt(txtMaNhaCungCap.getText())
            );
            materialDAO.addMaterial(material);
            JOptionPane.showMessageDialog(this, "Thêm vật tư thành công!");
            loadMaterials();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editMaterial() {
        try {
            int selectedRow = tableMaterial.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để sửa.");
                return;
            }

            String maVatTu = model.getValueAt(selectedRow, 0).toString();
            Material material = new Material(
                    Integer.parseInt(txtMaVatTu.getText()),
                    txtTenVatTu.getText(),
                    txtMoTa.getText(),
                    txtDonViTinh.getText(),
                    Double.parseDouble(txtGia.getText()),
                    Integer.parseInt(txtMaNhaCungCap.getText())
            );
            materialDAO.updateMaterial(material);
            JOptionPane.showMessageDialog(this, "Sửa vật tư thành công!");
            loadMaterials();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMaterial() {
        try {
            int selectedRow = tableMaterial.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để xóa.");
                return;
            }

            String maVatTu = model.getValueAt(selectedRow, 0).toString();
            materialDAO.deleteMaterial(Integer.parseInt(maVatTu));
            JOptionPane.showMessageDialog(this, "Xóa vật tư thành công!");
            loadMaterials();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtMaVatTu.setText("");
        txtTenVatTu.setText("");
        txtMoTa.setText("");
        txtDonViTinh.setText("");
        txtGia.setText("");
        txtMaNhaCungCap.setText("");
    }
}
