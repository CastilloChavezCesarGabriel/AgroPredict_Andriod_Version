package com.agropredict.infrastructure.persistence.sync;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;

import java.util.List;
import java.util.Objects;

public final class SyncingDiagnosticRepository implements IDiagnosticRepository, IRecordEraser {
    private final SqliteDiagnosticRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingDiagnosticRepository(SqliteDiagnosticRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = Objects.requireNonNull(delegate, "syncing diagnostic repository requires a delegate");
        this.recorder = Objects.requireNonNull(recorder, "syncing diagnostic repository requires a sync recorder");
    }

    @Override
    public void store(Diagnostic diagnostic) {
        delegate.store(diagnostic);
        diagnostic.identify(identifier -> recorder.insert("diagnostic", identifier));
    }

    @Override
    public void erase(String diagnosticIdentifier) {
        delegate.erase(diagnosticIdentifier);
        recorder.delete("diagnostic", diagnosticIdentifier);
    }

    @Override
    public List<Diagnostic> list(String userIdentifier) {
        return delegate.list(userIdentifier);
    }

    @Override
    public List<Diagnostic> filter(String userIdentifier, String cropType) {
        return delegate.filter(userIdentifier, cropType);
    }

    @Override
    public Diagnostic find(String diagnosticIdentifier) {
        return delegate.find(diagnosticIdentifier);
    }

    @Override
    public Diagnostic resolve(String userIdentifier, String cropIdentifier) {
        return delegate.resolve(userIdentifier, cropIdentifier);
    }
}
