package com.agropredict.infrastructure.report_export;

import com.agropredict.application.report_generation.usecase.CropReportComposer;
import com.agropredict.application.report_generation.usecase.DiagnosticReportComposer;
import com.agropredict.application.service.IClock;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.infrastructure.persistence.database.UtcTimestamp;
import java.io.File;
import java.io.IOException;

public final class PdfReportService extends ExportService {
    private final UtcTimestamp displayTimestamp;

    public PdfReportService(File outputDirectory, IClock clock) {
        super(outputDirectory, clock);
        this.displayTimestamp = new UtcTimestamp("dd/MM/yyyy HH:mm");
    }

    @Override
    protected File produce(Crop crop, Diagnostic diagnostic) throws IOException {
        String timestamp = stamp();
        File file = new File(outputDirectory, "report_" + timestamp + ".pdf");
        PdfReport report = new PdfReport(file);
        new CropReportComposer(report).compose(crop);
        new DiagnosticReportComposer(report).compose(diagnostic);
        report.close(displayTimestamp.serialize(clock.read()));
        return file;
    }
}