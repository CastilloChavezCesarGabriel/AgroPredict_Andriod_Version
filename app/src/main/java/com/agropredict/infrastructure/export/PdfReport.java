package com.agropredict.infrastructure.export;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class PdfReport implements IReportWriter {
    private static final String TITLE = "AgroPredict - Reporte de Campo";
    private Document document;

    @Override
    public void write(String label, String value) {
        if (document != null) {
            document.add(new Paragraph(label + ": " + value));
        }
    }

    public void export(File file) throws IOException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDocument = new PdfDocument(writer);
        document = new Document(pdfDocument);
        document.add(new Paragraph(TITLE).simulateBold().setFontSize(18));
        document.add(new Paragraph(" "));
    }

    public void close(String date) {
        if (document != null) {
            document.add(new Paragraph("Generado: " + date).setFontSize(10));
            document.close();
        }
    }
}