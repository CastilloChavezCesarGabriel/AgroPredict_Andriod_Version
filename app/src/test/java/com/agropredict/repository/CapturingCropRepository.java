package com.agropredict.repository;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CapturingCropRepository implements ICropRepository, IRecordEraser {
    private final List<Crop> stored = new ArrayList<>();
    private final List<String> deleted = new ArrayList<>();

    @Override
    public void store(Crop crop) {
        stored.add(crop);
    }

    public void walk(IPlantingConsumer consumer) {
        for (Crop crop : stored) crop.track(consumer);
    }

    public boolean storedExactly(int count) {
        return stored.size() == count;
    }

    public boolean deletedAccordingTo(String identifier) {
        return deleted.contains(identifier);
    }

    @Override public void update(CropUpdateRequest request) {}
    @Override public List<Crop> list(String userIdentifier) { return Collections.emptyList(); }
    @Override public Crop find(String cropIdentifier) { return null; }
    @Override public List<HistoryRecord> trace(String cropIdentifier) { return Collections.emptyList(); }
    @Override public void erase(String identifier) { deleted.add(identifier); }
}
