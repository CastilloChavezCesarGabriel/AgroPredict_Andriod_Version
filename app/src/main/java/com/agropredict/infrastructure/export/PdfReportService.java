package com.agropredict.infrastructure.export;

import com.agropredict.application.result.OperationResult;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class PdfReportService extends ReportService {
    private static final String DISPLAY_FORMAT = "dd/MM/yyyy HH:mm";

    public PdfReportService(File outputDirectory) {
        super(outputDirectory);
    }

    @Override
    public OperationResult generate(Crop crop, Diagnostic diagnostic) {
        try {
            String timestamp = stamp();
            File file = new File(outputDirectory, "reporte_" + timestamp + ".pdf");
            PdfReport report = new PdfReport();
            report.export(file);
            new DiagnosticTraversal(report).traverse(diagnostic);
            String displayDate = new SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault()).format(new Date());
            report.close(displayDate);
            return OperationResult.succeed(file.getAbsolutePath());
        } catch (IOException exception) {
            return OperationResult.fail();
        }
    }
}
