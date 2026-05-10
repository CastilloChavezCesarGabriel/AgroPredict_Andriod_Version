package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.history.HistoryRecord;
import java.util.List;

public final class SyncingCropRepository implements ICropRepository, IRecordEraser {
    private final SqliteCropRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingCropRepository(SqliteCropRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = delegate;
        this.recorder = recorder;
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