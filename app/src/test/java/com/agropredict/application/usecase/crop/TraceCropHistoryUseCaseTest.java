package com.agropredict.application.usecase.crop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.history.HistoryTransition;
import com.agropredict.domain.history.Modification;
import com.agropredict.domain.entity.Crop;

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
            @Override public void delete(String id) {}
        };
    }

    @Test
    public void testTraceReturnsHistory() {
        HistoryRecord record = new HistoryRecord(new Modification("field_name", "2026-03-01"), new HistoryTransition("old", "new"));
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