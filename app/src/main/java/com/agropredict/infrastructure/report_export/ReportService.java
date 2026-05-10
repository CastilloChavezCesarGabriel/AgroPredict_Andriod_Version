package com.agropredict.infrastructure.report_export;

import android.util.Log;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportWriter;
import com.agropredict.application.usecase.report.DiagnosticReportComposer;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class ReportService implements IReportService {
    private static final String TAG = "ReportService";
    private static final String FILE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    protected final File outputDirectory;

    protected ReportService(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public IUseCaseResult generate(Crop crop, Diagnostic diagnostic) {
        try {
            String timestamp = stamp();
            IReportWriter writer = prepare(timestamp);
            new DiagnosticReportComposer(writer).write(diagnostic);
            File file = complete(writer, timestamp);
            return new SuccessfulOperation(file.getAbsolutePath());
        } catch (IOException exception) {
            Log.e(TAG, "Failed to generate report into " + outputDirectory.getAbsolutePath()
                    + ". Check storage permission and free space.", exception);
            return new FailedOperation();
        } catch (RuntimeException exception) {
            Log.e(TAG, "Report writer threw a runtime error (often: missing diagnostic fields"
                    + " when API submit failed earlier).", exception);
            return new FailedOperation();
        }
    }

    protected abstract IReportWriter prepare(String timestamp) throws IOException;

    protected abstract File complete(IReportWriter writer, String timestamp) throws IOException;

    protected String stamp() {
        return new SimpleDateFormat(FILE_FORMAT, Locale.getDefault()).format(new Date());
    }
}