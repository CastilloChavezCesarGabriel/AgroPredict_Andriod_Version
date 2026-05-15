package com.agropredict.infrastructure.report_export;

import com.agropredict.application.service.IReportWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public final class PdfReport implements IReportWriter {
    private static final String TITLE = "AgroPredict - Field Report";
    private final Document document;

    public PdfReport(File file) throws IOException {
        Objects.requireNonNull(file, "pdf report requires a file");
        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDocument = new PdfDocument(writer);
        this.document = new Document(pdfDocument);
        document.add(new Paragraph(TITLE).simulateBold().setFontSize(18));
        document.add(new Paragraph(" "));
    }

    @Override
    public void write(String label, String value) {
        document.add(new Paragraph(label + ": " + value));
    }

    public void close(String date) {
        document.add(new Paragraph("Generated: " + date).setFontSize(10));
        document.close();
    }
}
