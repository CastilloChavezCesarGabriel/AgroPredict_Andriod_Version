package com.agropredict.infrastructure.export;

import com.agropredict.application.result.OperationResult;
import com.agropredict.application.service.IReportService;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CsvReportService implements IReportService {
    private static final String FILE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private final File outputDirectory;

    public CsvReportService(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public OperationResult generate(Crop crop, Diagnostic diagnostic) {
        try {
            CsvReport report = new CsvReport();
            new DiagnosticTraversal(report).traverse(diagnostic);
            String timestamp = new SimpleDateFormat(FILE_FORMAT, Locale.getDefault()).format(new Date());
            report.write("fecha", timestamp);
            File file = new File(outputDirectory, "reporte_" + timestamp + ".csv");
            report.export(file);
            return OperationResult.succeed(file.getAbsolutePath());
        } catch (IOException exception) {
            return OperationResult.fail();
        }
    }
}