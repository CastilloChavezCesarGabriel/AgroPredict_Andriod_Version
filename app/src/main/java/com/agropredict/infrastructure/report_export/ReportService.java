package com.agropredict.infrastructure.report_export;

import android.util.Log;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportWriter;
import com.agropredict.application.usecase.report.DiagnosticReportScribe;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
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
    public OperationResult generate(Crop crop, Diagnostic diagnostic) {
        try {
            String timestamp = stamp();
            IReportWriter writer = prepare(timestamp);
            new DiagnosticReportScribe(writer).write(diagnostic);
            File file = complete(writer, timestamp);
            return OperationResult.succeed(file.getAbsolutePath());
        } catch (IOException exception) {
            Log.e(TAG, "Failed to generate report into " + outputDirectory.getAbsolutePath()
                    + ". Check storage permission and free space.", exception);
            return OperationResult.fail();
        } catch (RuntimeException exception) {
            Log.e(TAG, "Report writer threw a runtime error (often: missing diagnostic fields"
                    + " when API submit failed earlier).", exception);
            return OperationResult.fail();
        }
    }

    protected abstract IReportWriter prepare(String timestamp) throws IOException;

    protected abstract File complete(IReportWriter writer, String timestamp) throws IOException;

    protected String stamp() {
        return new SimpleDateFormat(FILE_FORMAT, Locale.getDefault()).format(new Date());
    }
}