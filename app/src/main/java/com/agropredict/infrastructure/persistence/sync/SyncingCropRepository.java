package com.agropredict.infrastructure.persistence.sync;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.application.crop_management.request.CropUpdateRequest;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;

import java.util.List;
import java.util.Objects;

public final class SyncingCropRepository implements ICropRepository, IRecordEraser {
    private final SqliteCropRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingCropRepository(SqliteCropRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = Objects.requireNonNull(delegate, "syncing crop repository requires a delegate");
        this.recorder = Objects.requireNonNull(recorder, "syncing crop repository requires a sync recorder");
    }

    @Override
    public void store(Crop crop) {
        delegate.store(crop);
        crop.identify(identifier -> recorder.insert("crop", identifier));
    }

    @Override
    public void update(CropUpdateRequest request) {
        delegate.update(request);
        request.identify(identifier -> recorder.update("crop", identifier));
    }

    @Override
    public void erase(String cropIdentifier) {
        delegate.erase(cropIdentifier);
        recorder.delete("crop", cropIdentifier);
    }

    @Override
    public List<Crop> list(String userIdentifier) {
        return delegate.list(userIdentifier);
    }

    @Override
    public Crop find(String cropIdentifier) {
        return delegate.find(cropIdentifier);
    }

    @Override
    public List<HistoryRecord> trace(String cropIdentifier) {
        return delegate.trace(cropIdentifier);
    }
}