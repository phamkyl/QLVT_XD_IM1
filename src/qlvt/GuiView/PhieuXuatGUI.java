package qlvt.GuiView;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.draw.LineSeparator;
import qlvt.connect.PhieuXuatDAO;
import qlvt.model.ChiTietPhieuXuat;
import qlvt.model.PhieuXuat;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class PhieuXuatGUI extends JDialog {
    private JTextField txtMaPhieuXuat, txtMaNhanVien, txtMaKho, txtMaDonHang;
    private JFormattedTextField txtNgayXuat;
    private JButton btnSave, btnDelete, btnUpdate, btnClose, btnExportPDF;
    private JTable tablePhieuXuat, detailTable;
    private DefaultTableModel tableModel, detailTableModel;
    private PhieuXuatDAO phieuXuatDAO;

    public PhieuXuatGUI(ImportExportView parent) {
        phieuXuatDAO = new PhieuXuatDAO();

        initUI();
        loadPhieuXuats();
        setupTableSelectionListener();
        setSize(800, 600);
        setLocationRelativeTo(parent);
    }

    private void setupTableSelectionListener() {
        tablePhieuXuat.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tablePhieuXuat.getSelectedRow();
                if (selectedRow != -1) {
                    // Populate text fields with the selected row's data
                    txtMaPhieuXuat.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtNgayXuat.setValue(tableModel.getValueAt(selectedRow, 1));
                    txtMaNhanVien.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtMaKho.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtMaDonHang.setText(tableModel.getValueAt(selectedRow, 4).toString());

                    loadChiTietPhieuXuat(Integer.parseInt(txtMaPhieuXuat.getText()));
                }
            }
        });
    }

    private void loadChiTietPhieuXuat(int maPhieuXuat) {
        detailTableModel.setRowCount(0);
        List<ChiTietPhieuXuat> details = phieuXuatDAO.getChiTietPhieuXuatByMaPhieuXuat(maPhieuXuat);
        for (ChiTietPhieuXuat detail : details) {
            detailTableModel.addRow(new Object[]{
                    detail.getMaVatTu(),
                    detail.getSoLuong(),
                    detail.getGia()
            });
        }
    }

    private void initUI() {
        txtMaPhieuXuat = new JTextField(15);
        txtMaNhanVien = new JTextField(15);
        txtMaKho = new JTextField(15);
        txtMaDonHang = new JTextField(15);
        txtNgayXuat = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        txtNgayXuat.setValue(new Date(System.currentTimeMillis()));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.add(new JLabel("Mã Phiếu Xuất:"));
        inputPanel.add(txtMaPhieuXuat);
        inputPanel.add(new JLabel("Ngày Xuất:"));
        inputPanel.add(txtNgayXuat);
        inputPanel.add(new JLabel("Mã Nhân Viên:"));
        inputPanel.add(txtMaNhanVien);
        inputPanel.add(new JLabel("Mã Kho:"));
        inputPanel.add(txtMaKho);
        inputPanel.add(new JLabel("Mã Đơn Hàng:"));
        inputPanel.add(txtMaDonHang);

        btnSave = new JButton("Thêm");
        btnUpdate = new JButton("Cập Nhật");
        btnDelete = new JButton("Xóa");
        btnClose = new JButton("Đóng");
        btnExportPDF = new JButton("Xuất PDF");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClose);
        buttonPanel.add(btnExportPDF);

        btnSave.addActionListener(e -> addPhieuXuat());
        btnUpdate.addActionListener(e -> updatePhieuXuat());
        btnDelete.addActionListener(e -> deletePhieuXuat());
        btnClose.addActionListener(e -> dispose());
        btnExportPDF.addActionListener(e -> exportToPDF());

        tableModel = new DefaultTableModel(new Object[]{"Mã Phiếu Xuất", "Ngày Xuất", "Mã Nhân Viên", "Mã Kho", "Mã Đơn Hàng"}, 0);
        tablePhieuXuat = new JTable(tableModel);

        detailTableModel = new DefaultTableModel(new Object[]{"Mã Vật Tư", "Số Lượng", "Giá Xuất"}, 0);
        detailTable = new JTable(detailTableModel);

        // Panel for detail management
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        JButton btnAddDetail = new JButton("Thêm Chi Tiết");
        JButton btnUpdateDetail = new JButton("Cập Nhật Chi Tiết");
        JButton btnDeleteDetail = new JButton("Xóa Chi Tiết");

        for (JButton button : new JButton[]{btnSave, btnUpdate, btnDelete, btnClose, btnAddDetail, btnUpdateDetail, btnDeleteDetail, btnExportPDF}) {
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

        add(new JScrollPane(tablePhieuXuat), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(detailPanel, BorderLayout.EAST);
    }

    private void loadPhieuXuats() {
        tableModel.setRowCount(0);
        List<PhieuXuat> phieuXuats = phieuXuatDAO.getAllPhieuXuat();
        for (PhieuXuat phieuXuat : phieuXuats) {
            tableModel.addRow(new Object[]{
                    phieuXuat.getMaPhieuXuat(),
                    phieuXuat.getNgayXuat(),
                    phieuXuat.getMaNhanVien(),
                    phieuXuat.getMaKho(),
                    phieuXuat.getMaDonHang()
            });
        }
    }

    private void addPhieuXuat() {
        PhieuXuat phieuXuat = new PhieuXuat();
        phieuXuat.setMaPhieuXuat(Integer.parseInt(txtMaPhieuXuat.getText()));
        phieuXuat.setNgayXuat((Date) txtNgayXuat.getValue());
        phieuXuat.setMaNhanVien(Integer.parseInt(txtMaNhanVien.getText()));
        phieuXuat.setMaKho(Integer.parseInt(txtMaKho.getText()));
        phieuXuat.setMaDonHang(Integer.parseInt(txtMaDonHang.getText()));
        phieuXuatDAO.addPhieuXuat(phieuXuat);
        loadPhieuXuats(); // Refresh table
    }

    private void updatePhieuXuat() {
        PhieuXuat phieuXuat = new PhieuXuat();
        phieuXuat.setMaPhieuXuat(Integer.parseInt(txtMaPhieuXuat.getText()));
        phieuXuat.setNgayXuat((Date) txtNgayXuat.getValue());
        phieuXuat.setMaNhanVien(Integer.parseInt(txtMaNhanVien.getText()));
        phieuXuat.setMaKho(Integer.parseInt(txtMaKho.getText()));
        phieuXuat.setMaDonHang(Integer.parseInt(txtMaDonHang.getText()));
        phieuXuatDAO.updatePhieuXuat(phieuXuat);
        loadPhieuXuats(); // Refresh table
    }

    private void deletePhieuXuat() {
        int maPhieuXuat = Integer.parseInt(txtMaPhieuXuat.getText());
        phieuXuatDAO.deletePhieuXuat(maPhieuXuat);
        loadPhieuXuats(); // Refresh table
    }

    private void addDetail() {
        int maPhieuXuat = Integer.parseInt(txtMaPhieuXuat.getText());
        int maVatTu = Integer.parseInt(JOptionPane.showInputDialog("Nhập Mã Vật Tư:"));
        int soLuong = Integer.parseInt(JOptionPane.showInputDialog("Nhập Số Lượng:"));
        BigDecimal gia = BigDecimal.valueOf(Double.parseDouble(JOptionPane.showInputDialog("Nhập Giá:")));
        ChiTietPhieuXuat chiTiet = new ChiTietPhieuXuat(maPhieuXuat, maVatTu, soLuong, gia);
        phieuXuatDAO.addChiTietPhieuXuat(chiTiet);
        loadChiTietPhieuXuat(maPhieuXuat); // Refresh details
    }

    private void updateDetail() {
        int selectedRow = detailTable.getSelectedRow();  // Assuming detailTable is a JTable

        if (selectedRow != -1) {
            // Get the current values of the selected row
            String currentMaterialCode = detailTable.getValueAt(selectedRow, 0).toString();  // Mã Vật Tư (first column)
            String currentQuantity = detailTable.getValueAt(selectedRow, 1).toString();  // Số Lượng (second column)
            String currentPrice = detailTable.getValueAt(selectedRow, 2).toString();  // Giá Xuất (third column)

            // Show input dialog to update details
            String updatedMaterialCode = JOptionPane.showInputDialog(this, "Update Mã Vật Tư", currentMaterialCode);
            String updatedQuantity = JOptionPane.showInputDialog(this, "Update Số Lượng", currentQuantity);
            String updatedPrice = JOptionPane.showInputDialog(this, "Update Giá Xuất", currentPrice);

            // Check if the user provided valid inputs
            if (updatedMaterialCode != null && !updatedMaterialCode.isEmpty() &&
                    updatedQuantity != null && !updatedQuantity.isEmpty() &&
                    updatedPrice != null && !updatedPrice.isEmpty()) {

                // Update the model or data structure with the new values
                detailTable.setValueAt(updatedMaterialCode, selectedRow, 0);  // Update Mã Vật Tư
                detailTable.setValueAt(updatedQuantity, selectedRow, 1);      // Update Số Lượng
                detailTable.setValueAt(updatedPrice, selectedRow, 2);         // Update Giá Xuất

                JOptionPane.showMessageDialog(this, "Detail updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter valid details.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
        }
    }

    private void deleteDetail() {
        int selectedRow = detailTable.getSelectedRow();  // Assuming detailTable is a JTable

        if (selectedRow != -1) {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this detail?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Remove the row from the table model
                DefaultTableModel model = (DefaultTableModel) detailTable.getModel();
                model.removeRow(selectedRow);  // Remove the selected row

                JOptionPane.showMessageDialog(this, "Detail deleted successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    private void exportToPDF() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("phieuxuat4.pdf"));
            document.open();

            // Set font (Ensure the font file exists)
            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 12);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(baseFont, 18, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY);
            com.itextpdf.text.Font fontDetail = new com.itextpdf.text.Font(baseFont, 10);  // Font cho chi tiết

            // Title with enhanced styling
            Paragraph title = new Paragraph("Danh Sách Phiếu Xuất", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(20f); // Add space before title
            title.setSpacingAfter(20f); // Add space after title

            // Add a decorative line (for visual appeal)
            LineSeparator line = new LineSeparator();
            line.setPercentage(100);
            line.setLineWidth(1f);
            line.setLineColor(BaseColor.BLACK);

            document.add(title);
            document.add(line);

            // Create a table for the data (assuming JTable has 5 columns)
            PdfPTable table = new PdfPTable(5); // Number of columns in the table
            table.addCell("Mã Phiếu Xuất");
            table.addCell("Ngày Xuất");
            table.addCell("Mã Nhân Viên");
            table.addCell("Mã Kho");
            table.addCell("Mã Đơn Hàng");

            // Loop through rows and columns of tablePhieuXuat (assumed to be a JTable)
            for (int i = 0; i < tablePhieuXuat.getRowCount(); i++) {
                for (int j = 0; j < tablePhieuXuat.getColumnCount(); j++) {
                    // Get value from JTable and add it to PdfPTable
                    table.addCell(tablePhieuXuat.getValueAt(i, j).toString());
                }
            }

            document.add(table);

            // Add details section
            Paragraph detailTitle = new Paragraph("Chi Tiết Phiếu Xuất", fontBold);
            detailTitle.setAlignment(Element.ALIGN_LEFT);
            detailTitle.setSpacingBefore(20f); // Add space before details title
            detailTitle.setSpacingAfter(10f); // Add space after details title
            document.add(detailTitle);

            // Create a table for details data (Chi Tiết Phiếu Xuất)
            PdfPTable detailTable = new PdfPTable(3); // Number of columns in the detail table
            detailTable.addCell(new PdfPCell(new Phrase("Mã Vật Tư", fontTitle)));  // Use fontTitle for the header
            detailTable.addCell(new PdfPCell(new Phrase("Số Lượng", fontTitle)));    // Use fontTitle for the header
            detailTable.addCell(new PdfPCell(new Phrase("Giá Xuất", fontTitle)));    // Use fontTitle for the header

            // Loop through rows and columns of detailTable (assuming it’s a JTable or similar data structure) and add data
            for (int i = 0; i < this.detailTable.getRowCount(); i++) {
                for (int j = 0; j < this.detailTable.getColumnCount(); j++) {
                    PdfPCell cell = new PdfPCell(new Phrase(this.detailTable.getValueAt(i, j).toString(), fontDetail));
                    detailTable.addCell(cell);  // Apply the fontDetail for each cell
                }
            }

            document.add(detailTable);
            document.close();
            JOptionPane.showMessageDialog(this, "Xuất PDF thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + e.getMessage());
        }
    }





}
