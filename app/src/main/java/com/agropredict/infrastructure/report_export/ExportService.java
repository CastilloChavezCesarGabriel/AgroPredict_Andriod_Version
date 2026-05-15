package com.agropredict.infrastructure.report_export;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.service.IClock;
import com.agropredict.application.service.IReportService;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.infrastructure.persistence.database.UtcTimestamp;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class ExportService implements IReportService {
    protected final File outputDirectory;
    protected final IClock clock;
    private final UtcTimestamp filenameTimestamp;

    protected ExportService(File outputDirectory, IClock clock) {
        this.outputDirectory = Objects.requireNonNull(outputDirectory,
                "report service requires an output directory");
        this.clock = Objects.requireNonNull(clock,
                "report service requires a clock");
        this.filenameTimestamp = new UtcTimestamp("yyyy-MM-dd_HH-mm-ss");
    }

    @Override
    public final IUseCaseResult generate(Crop crop, Diagnostic diagnostic) {
        try {
            File file = produce(crop, diagnostic);
            return new SuccessfulOperation(file.getAbsolutePath());
        } catch (IOException | RuntimeException reportFailure) {
            return new FailedOperation();
        }
    }

    protected abstract File produce(Crop crop, Diagnostic diagnostic) throws IOException;

    protected final String stamp() {
        return filenameTimestamp.serialize(clock.read());
    }
}