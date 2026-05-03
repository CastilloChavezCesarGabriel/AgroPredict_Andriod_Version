package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.request.report_generation.Destination;
import com.agropredict.application.request.report_generation.ReportRequest;

public final class SyncingReportRepository implements IReportRepository {
    private final IReportRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingReportRepository(IReportRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = delegate;
        this.recorder = recorder;
    }

    @Override
    public void store(ReportRequest request, Destination destination) {
        delegate.store(request, destination);
        request.identify((reportIdentifier, diagnosticIdentifier) -> recorder.insert("report", reportIdentifier));
    }

    @Override
    public void clear(String cropIdentifier) {
        delegate.clear(cropIdentifier);
    }
}
