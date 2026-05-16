package com.agropredict.infrastructure.persistence.sync;

import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.photograph.Photograph;
import com.agropredict.infrastructure.persistence.repository.SqlitePhotographRepository;

import java.util.Objects;

public final class SyncingPhotographRepository implements IPhotographRepository {
    private final SqlitePhotographRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingPhotographRepository(SqlitePhotographRepository delegate, SqliteSyncRecorder recorder) {
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
