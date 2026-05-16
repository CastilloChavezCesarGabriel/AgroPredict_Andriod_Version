package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.service.ReportFormat;
import com.agropredict.presentation.user_interface.export.CsvExportedFile;
import com.agropredict.presentation.user_interface.export.ICsvSharer;
import com.agropredict.presentation.user_interface.export.IExportedFile;
import com.agropredict.presentation.user_interface.export.IExportedFileConsumer;
import com.agropredict.presentation.user_interface.export.IPdfOpener;
import com.agropredict.presentation.user_interface.export.PdfExportedFile;
import java.util.Objects;

public final class ExportPresentation {
    private final IPdfOpener pdfOpener;
    private final ICsvSharer csvSharer;

    public ExportPresentation(IPdfOpener pdfOpener, ICsvSharer csvSharer) {
        this.pdfOpener = Objects.requireNonNull(pdfOpener, "export presentation requires a pdf opener");
        this.csvSharer = Objects.requireNonNull(csvSharer, "export presentation requires a csv sharer");
    }

    public void choose(ReportFormat format, String filePath, IExportedFileConsumer consumer) {
        IExportedFile artifact = format == ReportFormat.CSV
                ? new CsvExportedFile(filePath, csvSharer)
                : new PdfExportedFile(filePath, pdfOpener);
        consumer.accept(artifact);
    }
}