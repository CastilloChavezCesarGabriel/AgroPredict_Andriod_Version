package com.agropredict.application.usecase.crop;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.application.operation_result.HistoryRecord;
import com.agropredict.domain.entity.Crop;

import org.junit.Test;

import java.util.List;

public final class UpdateCropUseCaseTest {

    @Test
    public void testUpdateDelegatesToRepository() {
        boolean[] updated = {false};
        ICropRepository repo = new ICropRepository() {
            @Override public void store(Crop crop) {}
            @Override public void update(CropUpdateRequest request) { updated[0] = true; }
            @Override public List<Crop> list(String userId) { return List.of(); }
            @Override public Crop find(String id) { return null; }
            @Override public List<HistoryRecord> trace(String id) { return List.of(); }
            @Override public void delete(String id) {}
        };
        Crop crop = new Crop("crop_1", "wheat");
        repo.update(new CropUpdateRequest(crop));
        assertTrue(updated[0]);
    }
}
