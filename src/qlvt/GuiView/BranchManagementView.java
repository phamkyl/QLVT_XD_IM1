package qlvt.GuiView;

import qlvt.connect.BranchDAO;
import qlvt.model.Branch;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BranchManagementView extends JPanel {
    private DefaultTableModel model;
    private BranchDAO branchDAO;
    private int maChiNhanh; // Variable to store branch ID

    private JLabel idLabel, nameLabel, addressLabel;

    public BranchManagementView(MainView_IM mainViewIm, int maChiNhanh) {
        this.maChiNhanh = maChiNhanh; // Initialize branch ID
        branchDAO = new BranchDAO();

        //setSize(900, 700); // Kích thước cửa sổ
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title label
        JLabel titleLabel = new JLabel("DANH SÁCH CHI NHÁNH");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(3, 166, 120));
        add(titleLabel, BorderLayout.NORTH);

        // Create table to display branch list
        String[] columnNames = {"Mã Chi Nhánh", "Tên Chi Nhánh", "Địa Chỉ"};
        model = new DefaultTableModel(columnNames, 0);
        JTable branchTable = new JTable(model);
        branchTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        branchTable.setBackground(Color.WHITE);
        branchTable.setForeground(Color.BLACK);
        branchTable.getSelectionModel().addListSelectionListener(event -> {
            updateBranchDetails(branchTable);
        });
        JScrollPane scrollPane = new JScrollPane(branchTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons for add, edit, delete, back, and exit
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton backButton = new JButton("Quay Lại");

        // Change button colors
        for (JButton button : new JButton[]{addButton, editButton, deleteButton, backButton}) {
            button.setBackground(new Color(3, 166, 120));
            button.setForeground(Color.WHITE);
            button.setSize(100, 60);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
        }
        if (maChiNhanh == 0) {
            addButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            //refreshButton.setVisible(false);
            backButton.setVisible(false);
        }

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action for Add button
        addButton.addActionListener(e -> {
            addBranch();
        });

        // Action for Edit button
        editButton.addActionListener(e -> {
            editBranch(branchTable);
        });

        // Action for Delete button
        deleteButton.addActionListener(e -> {
            deleteBranch(branchTable);
        });

        // Action for Back button
        backButton.addActionListener(e -> {
            //openMainView(); // Open the main view
        });

        if(maChiNhanh == 0)
        {
            loadBranchesTong();
        } else {
            loadBranches(); // Load branches
        }
    }

    private void updateBranchDetails(JTable branchTable) {
        int selectedRow = branchTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) model.getValueAt(selectedRow, 0);
            String name = (String) model.getValueAt(selectedRow, 1);
            String address = (String) model.getValueAt(selectedRow, 2);
        }
    }

    private void addBranch() {
        // Input branch information
        String idStr = JOptionPane.showInputDialog("Nhập Mã Chi Nhánh:");
        String name = JOptionPane.showInputDialog("Nhập Tên Chi Nhánh:");
        String address = JOptionPane.showInputDialog("Nhập Địa Chỉ:");

        if (idStr != null && name != null && address != null) {
            try {
                int id = Integer.parseInt(idStr);
                Branch newBranch = new Branch(id, name, address);
                branchDAO.addBranch(newBranch);
                model.addRow(new Object[]{id, name, address});
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void editBranch(JTable branchTable) {
        int selectedRow = branchTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) model.getValueAt(selectedRow, 0);
            String name = JOptionPane.showInputDialog("Nhập Tên Chi Nhánh:", model.getValueAt(selectedRow, 1));
            String address = JOptionPane.showInputDialog("Nhập Địa Chỉ:", model.getValueAt(selectedRow, 2));

            if (name != null && address != null) {
                try {
                    Branch updatedBranch = new Branch(id, name, address);
                    branchDAO.updateBranch(updatedBranch);
                    model.setValueAt(name, selectedRow, 1);
                    model.setValueAt(address, selectedRow, 2);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi nhánh để sửa.");
        }
    }

    private void deleteBranch(JTable branchTable) {
        int selectedRow = branchTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) model.getValueAt(selectedRow, 0);
            try {
                branchDAO.deleteBranch(id);
                model.removeRow(selectedRow);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi nhánh để xóa.");
        }
    }



    private void loadBranches() {
        try {
            List<Branch> branches = branchDAO.getAllBranches(maChiNhanh);
            for (Branch branch : branches) {
                model.addRow(new Object[]{
                        branch.getMaChiNhanh(),
                        branch.getTenChiNhanh(),
                        branch.getDiaChi()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void loadBranchesTong() {
        try {
            List<Branch> branches = branchDAO.getAllBranchesTong();
            for (Branch branch : branches) {
                model.addRow(new Object[]{
                        branch.getMaChiNhanh(),
                        branch.getTenChiNhanh(),
                        branch.getDiaChi()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }



}
