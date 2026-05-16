package com.agropredict.infrastructure.persistence.sync;

import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.photograph.Photograph;

import java.util.Objects;

public final class SyncingPhotographRepository implements IPhotographRepository {
    private final IPhotographRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingPhotographRepository(IPhotographRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = Objects.requireNonNull(delegate, "syncing photograph repository requires a delegate");
        this.recorder = Objects.requireNonNull(recorder, "syncing photograph repository requires a sync recorder");
    }

    @Override
    public void store(Photograph photograph, String cropIdentifier) {
        delegate.store(photograph, cropIdentifier);
        photograph.identify(identifier -> recorder.insert("image", identifier));
    }

    @Override
    public Photograph find(String diagnosticIdentifier) {
        return delegate.find(diagnosticIdentifier);
    }
}
