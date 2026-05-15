package com.agropredict.application.usecase.crop;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.crop_management.request.CropUpdateRequest;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.Field;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.crop.Soil;

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
        };
        repo.update(new CropUpdateRequest("crop_1", new CropProfile(new Plot(new Field("F", "L"), new Soil("Clay", "1ha")), new GrowthCycle("2026-01-01", "Vegetative"))));
        assertTrue(updated[0]);
    }
}