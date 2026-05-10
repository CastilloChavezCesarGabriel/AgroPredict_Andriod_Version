package com.agropredict.application.usecase.crop;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.crop.Crop;

import org.junit.Test;

import java.util.List;

public final class FindCropUseCaseTest {
    private ICropRepository stubCrop(Crop returnValue) {
        return new ICropRepository() {
            @Override public void store(Crop crop) {}
            @Override public void update(CropUpdateRequest request) {}
            @Override public List<Crop> list(String userId) { return List.of(); }
            @Override public Crop find(String id) { return returnValue; }
            @Override public List<HistoryRecord> trace(String id) { return List.of(); }
        };
    }

    @Test
    public void testFindExistingCrop() {
        Crop crop = new Crop("crop_1", "wheat", new CropProfile(new Plot(null, null), new GrowthCycle(null, null)));
        Crop result = new FindCropUseCase(stubCrop(crop)).find("crop_1");
        assertNotNull(result);
    }

    @Test
    public void testFindNonExistingCrop() {
        Crop result = new FindCropUseCase(stubCrop(null)).find("crop_999");
        assertNull(result);
    }
}