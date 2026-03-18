package com.agropredict.infrastructure.export;

import com.agropredict.application.result.OperationResult;
import com.agropredict.application.service.IReportGeneratorService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public final class PdfReportGeneratorService implements IReportGeneratorService {
    private static final String REPORT_TITLE = "AgroPredict - Reporte de Campo";
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private static final String DISPLAY_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private final File outputDirectory;

    public PdfReportGeneratorService(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public OperationResult generate(Map<String, Object> reportData) {
        try {
            String filePath = compose(reportData);
            return OperationResult.succeed(filePath);
        } catch (IOException exception) {
            return OperationResult.fail();
        }
    }

    private String compose(Map<String, Object> reportData) throws IOException {
        File reportFile = createReportFile();
        PdfWriter writer = new PdfWriter(new FileOutputStream(reportFile));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        writeContent(document, reportData);
        document.close();
        return reportFile.getAbsolutePath();
    }

    private File createReportFile() {
        String timestamp = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
        String fileName = "reporte_" + timestamp + ".pdf";
        return new File(outputDirectory, fileName);
    }

    private void writeContent(Document document, Map<String, Object> reportData) {
        writeHeader(document);
        writeCropSection(document, reportData);
        writeDiagnosticSection(document, reportData);
        writeFooter(document);
    }

    private void writeHeader(Document document) {
        Paragraph title = new Paragraph(REPORT_TITLE).setBold().setFontSize(18);
        document.add(title);
        document.add(new Paragraph(" "));
    }

    private void writeCropSection(Document document, Map<String, Object> reportData) {
        document.add(new Paragraph("Cultivo Detectado").setBold().setFontSize(14));
        String cropName = extractString(reportData, "cultivo_nombre");
        String confidence = extractString(reportData, "confianza");
        document.add(new Paragraph("Nombre: " + cropName));
        document.add(new Paragraph("Confianza: " + confidence));
        document.add(new Paragraph(" "));
    }

    private void writeDiagnosticSection(Document document, Map<String, Object> reportData) {
        document.add(new Paragraph("Diagnostico").setBold().setFontSize(14));
        String longText = extractString(reportData, "texto_largo");
        String summary = extractString(reportData, "reporte_resumido");
        document.add(new Paragraph("Resumen: " + summary));
        document.add(new Paragraph("Detalle: " + longText));
        document.add(new Paragraph(" "));
    }

    private void writeFooter(Document document) {
        String displayDate = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault()).format(new Date());
        document.add(new Paragraph("Generado: " + displayDate).setFontSize(10));
    }

    private String extractString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "N/A";
    }
}