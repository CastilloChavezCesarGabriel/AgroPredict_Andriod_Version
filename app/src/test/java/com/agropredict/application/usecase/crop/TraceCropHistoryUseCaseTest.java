package com.agropredict.application.usecase.crop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.crop_management.usecase.TraceCropHistoryUseCase;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.crop_management.request.CropUpdateRequest;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.history.HistoryTransition;
import com.agropredict.domain.history.FieldModification;
import com.agropredict.domain.history.ChangeMoment;
import com.agropredict.domain.crop.Crop;

import org.junit.Test;

import java.util.List;

public final class TraceCropHistoryUseCaseTest {

    private ICropRepository stubCrop(List<HistoryRecord> history) {
        return new ICropRepository() {
            @Override public void store(Crop crop) {}
            @Override public void update(CropUpdateRequest request) {}
            @Override public List<Crop> list(String userId) { return List.of(); }
            @Override public Crop find(String id) { return null; }
            @Override public List<HistoryRecord> trace(String id) { return history; }
        };
    }

    @Test
    public void testTraceReturnsHistory() {
        HistoryRecord record = new HistoryRecord(
                new FieldModification("field_name"),
                new HistoryTransition("old", "new"),
                new ChangeMoment("2026-03-01"));
        List<HistoryRecord> result = new TraceCropHistoryUseCase(stubCrop(List.of(record))).trace("crop_1");
        assertEquals(1, result.size());
    }

    @Test
    public void testTraceEmptyHistory() {
        List<HistoryRecord> result = new TraceCropHistoryUseCase(stubCrop(List.of())).trace("crop_1");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}