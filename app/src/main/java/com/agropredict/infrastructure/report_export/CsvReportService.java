package com.agropredict.infrastructure.report_export;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.report.DiagnosticTraversal;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
import java.io.File;
import java.io.IOException;

public final class CsvReportService extends ReportService {
    public CsvReportService(File outputDirectory) {
        super(outputDirectory);
    }

    @Override
    public OperationResult generate(Crop crop, Diagnostic diagnostic) {
        try {
            CsvReport report = new CsvReport();
            new DiagnosticTraversal(report).traverse(diagnostic);
            String timestamp = stamp();
            report.write("date", timestamp);
            File file = new File(outputDirectory, "report_" + timestamp + ".csv");
            report.export(file);
            return OperationResult.succeed(file.getAbsolutePath());
        } catch (IOException exception) {
            return OperationResult.fail();
        }
    }
}