package dev.andervilo.gestao_frotas.application.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {
    
    public byte[] exportToExcel(String sheetName, List<String> headers, List<List<Object>> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet(sheetName);
            
            // Estilo do cabeçalho
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // Criar linha de cabeçalho
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }
            
            // Criar linhas de dados
            int rowNum = 1;
            for (List<Object> rowData : data) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.size(); i++) {
                    org.apache.poi.ss.usermodel.Cell cell = row.createCell(i);
                    Object value = rowData.get(i);
                    
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                }
            }
            
            // Auto-ajustar largura das colunas
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
    
    public byte[] exportToPDF(String title, List<String> headers, List<List<Object>> data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        // Título
        document.add(new Paragraph(title)
            .setFontSize(18)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER));
        
        // Data de geração
        document.add(new Paragraph("Gerado em: " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setFontSize(10)
            .setTextAlignment(TextAlignment.RIGHT));
        
        document.add(new Paragraph("\n"));
        
        // Tabela
        Table table = new Table(headers.size());
        
        // Cabeçalhos
        for (String header : headers) {
            table.addHeaderCell(new Cell()
                .add(new Paragraph(header).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        }
        
        // Dados
        for (List<Object> rowData : data) {
            for (Object value : rowData) {
                table.addCell(value != null ? value.toString() : "");
            }
        }
        
        document.add(table);
        document.close();
        
        return out.toByteArray();
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
