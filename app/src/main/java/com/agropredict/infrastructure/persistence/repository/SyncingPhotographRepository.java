package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.ICropRecord;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.photograph.Photograph;

public final class SyncingPhotographRepository implements IPhotographRepository, ICropRecord {
    private final SqlitePhotographRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingPhotographRepository(SqlitePhotographRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = delegate;
        this.recorder = recorder;
    }

    @Override
    public void store(Photograph photograph, Crop crop) {
        delegate.store(photograph, crop);
        photograph.identify(identifier -> recorder.insert("image", identifier));
    }

    @Override
    public Photograph find(String diagnosticIdentifier) {
        return delegate.find(diagnosticIdentifier);
    }

    @Override
    public void discard(String cropIdentifier) {
        delegate.discard(cropIdentifier);
    }
}
