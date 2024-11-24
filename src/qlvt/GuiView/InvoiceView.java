package qlvt.GuiView;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import qlvt.connect.OrderDAO;
import qlvt.model.ChiTietDonHang;
import qlvt.model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceView extends JDialog {
    private OrderDAO orderDAO;
    private JTextArea invoiceTextArea;
    private JButton btnGeneratePDF;

    public InvoiceView(int maDonHang) {
        orderDAO = new OrderDAO();
        initUI(maDonHang);
        setSize(400, 300);
    }

    private void initUI(int maDonHang) {
        // Create a JTextArea for the invoice details
        invoiceTextArea = new JTextArea();
        invoiceTextArea.setEditable(false);
        invoiceTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Set a readable font
        invoiceTextArea.setBackground(new Color(245, 245, 245)); // Light background color
        invoiceTextArea.setForeground(Color.BLACK); // Text color

        // Add a JScrollPane to enable scrolling if content exceeds the viewable area
        JScrollPane scrollPane = new JScrollPane(invoiceTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around text area
        add(scrollPane, BorderLayout.CENTER);

        // Load the invoice details into the text area
        loadInvoiceDetails(maDonHang);

        // Create the buttons at the bottom
        btnGeneratePDF = new JButton("Tạo hóa đơn");
        btnGeneratePDF.setFont(new Font("Arial", Font.BOLD, 14)); // Make the button text larger
        btnGeneratePDF.setBackground(new Color(34, 167, 240)); // Blue background color
        btnGeneratePDF.setForeground(Color.WHITE); // White text
        btnGeneratePDF.addActionListener(e -> generatePDF(maDonHang));

        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setBackground(new Color(242, 85, 96)); // Red background for close button
        btnClose.setForeground(Color.WHITE); // White text
        btnClose.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
        buttonPanel.add(btnGeneratePDF);
        buttonPanel.add(btnClose);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    private void loadInvoiceDetails(int maDonHang) {
        Order order = orderDAO.getOrderById(maDonHang);
        List<ChiTietDonHang> details = orderDAO.getOrderDetailsById(maDonHang);

        StringBuilder invoiceBuilder = new StringBuilder();

        // Add header section with bold text and underlines
        invoiceBuilder.append("\n\n");
        invoiceBuilder.append("====================================\n");
        invoiceBuilder.append("              HÓA ĐƠN              \n");
        invoiceBuilder.append("====================================\n\n");

        invoiceBuilder.append(String.format("Mã Đơn Hàng: %-20s %n", order.getMaDonHang()));
        invoiceBuilder.append(String.format("Mã Khách Hàng: %-15s %n", order.getMaKhachHang()));
        invoiceBuilder.append(String.format("Ngày Đặt: %-20s %n", order.getNgayDat()));
        invoiceBuilder.append(String.format("Tình Trạng: %-15s %n", order.getTinhTrangDonHang()));

        invoiceBuilder.append("\nChi Tiết Đơn Hàng:\n");
        invoiceBuilder.append("====================================\n");

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Loop through each item in the order
        for (ChiTietDonHang detail : details) {
            invoiceBuilder.append(String.format("Mã Vật Tư: %-15s", detail.getMaVatTu()));
            invoiceBuilder.append(String.format("Số Lượng: %-5d", detail.getSoLuong()));
            invoiceBuilder.append(String.format("Giá: %-10.2f VND", detail.getGia()));
            totalAmount = totalAmount.add(detail.getGia().multiply(BigDecimal.valueOf(detail.getSoLuong())));
            invoiceBuilder.append("\n------------------------------------\n");
        }

        invoiceBuilder.append(String.format("\nTổng Tiền: %.2f VND\n", totalAmount));
        invoiceBuilder.append("====================================\n");

        // Set the formatted text to the text area
        invoiceTextArea.setText(invoiceBuilder.toString());
    }


    private void generatePDF(int maDonHang) {
        Order order = orderDAO.getOrderById(maDonHang);
        List<ChiTietDonHang> details = orderDAO.getOrderDetailsById(maDonHang);

        String filePath = "HoaDon_" + order.getMaDonHang() + ".pdf";
        Document document = new Document();
        try {
            // Set document margins
            document.setMargins(36, 36, 36, 36);

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Set font (Make sure the font file is available)
            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 12);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(baseFont, 16, com.itextpdf.text.Font.BOLD);

            // Add title with larger font
            Paragraph title = new Paragraph("Hóa Đơn", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add some space before order information
            document.add(new Paragraph("\n"));

            // Add order information with bold labels
            document.add(new Paragraph("Mã Đơn Hàng: " + order.getMaDonHang(), fontBold));
            document.add(new Paragraph("Mã Khách Hàng: " + order.getMaKhachHang(), fontBold));
            document.add(new Paragraph("Ngày Đặt: " + order.getNgayDat(), fontBold));
            document.add(new Paragraph("Tình Trạng: " + order.getTinhTrangDonHang(), fontBold));

            // Add some space before order details
            document.add(new Paragraph("\nChi Tiết Đơn Hàng:", fontBold));
            document.add(new Paragraph("\n"));

            // Create a table for details
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers with background color and bold font
            PdfPCell cell = new PdfPCell(new Paragraph("Mã Vật Tư", fontBold));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Số Lượng", fontBold));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Giá", fontBold));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (ChiTietDonHang detail : details) {
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(detail.getMaVatTu()), font)));
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(detail.getSoLuong()), font)));
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(detail.getGia()), font)));
                totalAmount = totalAmount.add(detail.getGia().multiply(BigDecimal.valueOf(detail.getSoLuong())));
            }

            document.add(table);

            // Add total amount with bold and larger font size
            document.add(new Paragraph("\nTổng Tiền: " + totalAmount, fontBold));

            // Add some space at the bottom
            document.add(new Paragraph("\n"));

            // Add a line at the bottom (optional)
            document.add(new Chunk(new LineSeparator()));

            document.close();
            writer.close();

            // Notify user
            JOptionPane.showMessageDialog(this, "Tạo PDF thành công: " + filePath);

            // Close the dialog
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo PDF.", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }

}
