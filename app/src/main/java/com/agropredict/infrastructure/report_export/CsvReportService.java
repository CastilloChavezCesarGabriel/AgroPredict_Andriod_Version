package com.agropredict.infrastructure.report_export;

import com.agropredict.application.report_generation.usecase.CropReportComposer;
import com.agropredict.application.report_generation.usecase.DiagnosticReportComposer;
import com.agropredict.application.service.IClock;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.io.File;
import java.io.IOException;

public final class CsvReportService extends ReportService {
    public CsvReportService(File outputDirectory, IClock clock) {
        super(outputDirectory, clock);
    }

    @Override
    protected File produce(Crop crop, Diagnostic diagnostic) throws IOException {
        String timestamp = stamp();
        CsvReport report = new CsvReport();
        report.write("date", timestamp);
        new CropReportComposer(report).compose(crop);
        new DiagnosticReportComposer(report).compose(diagnostic);
        File file = new File(outputDirectory, "report_" + timestamp + ".csv");
        report.export(file);
        return file;
    }
}
