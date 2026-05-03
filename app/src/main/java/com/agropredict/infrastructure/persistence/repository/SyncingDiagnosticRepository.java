package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

public final class SyncingDiagnosticRepository implements IDiagnosticRepository {
    private final IDiagnosticRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingDiagnosticRepository(IDiagnosticRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = delegate;
        this.recorder = recorder;
    }

    @Override
    public void store(Diagnostic diagnostic) {
        delegate.store(diagnostic);
        diagnostic.identify(identifier -> recorder.insert("diagnostic", identifier));
    }

    @Override
    public void delete(String diagnosticIdentifier) {
        delegate.delete(diagnosticIdentifier);
        recorder.delete("diagnostic", diagnosticIdentifier);
    }

    @Override
    public void clear(String cropIdentifier) {
        delegate.clear(cropIdentifier);
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
