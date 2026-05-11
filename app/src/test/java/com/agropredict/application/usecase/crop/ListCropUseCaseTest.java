package com.agropredict.application.usecase.crop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.crop_management.usecase.ListCropUseCase;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.crop_management.request.CropUpdateRequest;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.crop.Crop;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public final class ListCropUseCaseTest {
    private ICropRepository stubCrop(List<Crop> crops) {
        return new ICropRepository() {
            @Override public void store(Crop crop) {}
            @Override public void update(CropUpdateRequest request) {}
            @Override public List<Crop> list(String userId) { return crops; }
            @Override public Crop find(String id) { return null; }
            @Override public List<HistoryRecord> trace(String id) { return List.of(); }
        };
    }

    @Test
    public void testListReturnsAllCrops() {
        List<Crop> crops = List.of(
            new Crop("crop_1", "wheat", new CropProfile(new Plot(null, null), new GrowthCycle(null, null))),
            new Crop("crop_2", "corn", new CropProfile(new Plot(null, null), new GrowthCycle(null, null)))
        );
        List<Crop> result = new ListCropUseCase(stubCrop(crops)).list("user_1");
        assertEquals(2, result.size());
    }

    @Test
    public void testListEmptyResult() {
        List<Crop> result = new ListCropUseCase(stubCrop(new ArrayList<>())).list("user_1");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}