package com.agropredict.infrastructure.persistence.sync;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.report_generation.request.Destination;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.infrastructure.persistence.report.SqliteReportRepository;

import java.util.Objects;

public final class SyncingReportRepository implements IReportRepository {
    private final SqliteReportRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingReportRepository(SqliteReportRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = Objects.requireNonNull(delegate, "syncing report repository requires a delegate");
        this.recorder = Objects.requireNonNull(recorder, "syncing report repository requires a sync recorder");
    }

    @Override
    public void store(ReportRequest request, Destination destination) {
        delegate.store(request, destination);
        request.identify((reportIdentifier, diagnosticIdentifier) -> recorder.insert("report", reportIdentifier));
    }
}
