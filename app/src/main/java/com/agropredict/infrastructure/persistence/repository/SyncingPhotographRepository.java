package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;

public final class SyncingPhotographRepository implements IPhotographRepository {
    private final IPhotographRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingPhotographRepository(IPhotographRepository delegate, SqliteSyncRecorder recorder) {
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
    public void clear(String cropIdentifier) {
        delegate.clear(cropIdentifier);
    }
}
