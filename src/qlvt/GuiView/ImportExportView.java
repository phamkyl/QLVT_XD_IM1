package qlvt.GuiView;

import qlvt.connect.PurchaseOrderDAO;
import qlvt.connect.PhieuXuatDAO;
import qlvt.model.PhieuNhap;
import qlvt.model.PhieuXuat;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ImportExportView extends JPanel {

    private JTable tablePhieunhap;
    private JTable tablePhieuXuat;
    private PurchaseOrderDAO purchaseOrderDAO;
    private PhieuXuatDAO phieuXuatDAO;

    public ImportExportView(MainView_IM mainViewIm, PurchaseOrderDAO purchaseOrderDAO, PhieuXuatDAO phieuXuatDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
        this.phieuXuatDAO = phieuXuatDAO;

        initializeView();
    }

    private void initializeView() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600)); // Default size

        // Title Label
        add(createTitleLabel(), BorderLayout.NORTH);

        // Main Panel: Contains both tables and buttons
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel for Tables (Left part of the screen)
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Panel for Buttons (Bottom part of the screen)
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("QUẢN LÝ PHIẾU NHẬP XUẤT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(3, 166, 120));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        return titleLabel;
    }

    // Creates the table panel (contains the import and export tables)
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tablePanel.setBorder(new LineBorder(Color.BLACK, 1));

        // Panel for Import Table
        JPanel importTablePanel = createTableSubPanel("Danh sách phiếu nhập", createImportTable());
        tablePanel.add(importTablePanel);

        // Panel for Export Table
        JPanel exportTablePanel = createTableSubPanel("Danh sách phiếu xuất", createExportTable());
        tablePanel.add(exportTablePanel);

        // Load data for both tables
        loadPurchaseOrders();
        loadPhieuXuats();

        return tablePanel;
    }

    // Helper method to create table sub-panels
    private JPanel createTableSubPanel(String label, JScrollPane tableScrollPane) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel tableLabel = new JLabel(label);
        panel.add(tableLabel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Creates the import table (Phiếu Nhập)
    private JScrollPane createImportTable() {
        DefaultTableModel importTableModel = new DefaultTableModel(new Object[]{"Mã Phiếu Nhập", "Ngày Nhập", "Mã Nhân Viên", "Mã Kho", "Mã Nhà Cung Cấp"}, 0);
        tablePhieunhap = new JTable(importTableModel);
        return new JScrollPane(tablePhieunhap);
    }

    // Creates the export table (Phiếu Xuất)
    private JScrollPane createExportTable() {
        DefaultTableModel exportTableModel = new DefaultTableModel(new Object[]{"Mã Phiếu Xuất", "Ngày Xuất", "Mã Nhân Viên", "Mã Kho", "Mã Đơn Hàng"}, 0);
        tablePhieuXuat = new JTable(exportTableModel);
        return new JScrollPane(tablePhieuXuat);
    }

    // Loads data for the purchase orders table
    private void loadPurchaseOrders() {
        DefaultTableModel importTableModel = (DefaultTableModel) tablePhieunhap.getModel();
        importTableModel.setRowCount(0);
        List<PhieuNhap> orders = purchaseOrderDAO.getAllPurchaseOrders();
        for (PhieuNhap order : orders) {
            importTableModel.addRow(new Object[]{
                    order.getMaPhieuNhap(),
                    order.getNgayNhap(),
                    order.getMaNhanVien(),
                    order.getMaKho(),
                    order.getMaNhaCungCap()
            });
        }
    }

    // Loads data for the export invoices table
    private void loadPhieuXuats() {
        DefaultTableModel exportTableModel = (DefaultTableModel) tablePhieuXuat.getModel();
        exportTableModel.setRowCount(0);
        List<PhieuXuat> phieuXuats = phieuXuatDAO.getAllPhieuXuat();
        for (PhieuXuat phieuXuat : phieuXuats) {
            exportTableModel.addRow(new Object[]{
                    phieuXuat.getMaPhieuXuat(),
                    phieuXuat.getNgayXuat(),
                    phieuXuat.getMaNhanVien(),
                    phieuXuat.getMaKho(),
                    phieuXuat.getMaDonHang()
            });
        }
    }

    // Creates the button panel (Bottom part of the screen)
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createLineBorder(new Color(3, 166, 120), 1));
        buttonPanel.setBackground(Color.WHITE);

        // Create and add buttons
        JButton importButton = createButton("Phiếu Nhập", e -> openImportView());
        JButton exportButton = createButton("Phiếu Xuất", e -> openExportView());

        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);

        return buttonPanel;
    }

    // Helper method to create a button
    private JButton createButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(3, 166, 120));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addActionListener(actionListener);
        return button;
    }

    // Action to open the import view (can be expanded)
    private void openImportView() {
        // Mở cửa sổ PurchaseOrderGUI với tham chiếu đối tượng của lớp hiện tại (parent).
        PurchaseOrderGUI receiptNoteView = new PurchaseOrderGUI(this);
        receiptNoteView.setVisible(true);
    }


    // Action to open the export view (can be expanded)
    private void openExportView() {
        PhieuXuatGUI PhieuXuatGUI = new PhieuXuatGUI(this);
        PhieuXuatGUI.setVisible(true);
    }
}
