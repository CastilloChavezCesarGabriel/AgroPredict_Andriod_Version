package com.agropredict.application.usecase.crop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.application.operation_result.HistoryRecord;
import com.agropredict.domain.entity.Crop;
import com.agropredict.visitor.TestOperationResultVisitor;

import org.junit.Test;

import java.util.List;

public final class DeleteCropUseCaseTest {

    private ICropRepository fakeRepo(boolean throwOnDelete) {
        return new ICropRepository() {
            @Override public void store(Crop crop) {}
            @Override public void update(CropUpdateRequest request) {}
            @Override public List<Crop> list(String userIdentifier) { return List.of(); }
            @Override public Crop find(String cropIdentifier) { return null; }
            @Override public List<HistoryRecord> trace(String cropIdentifier) { return List.of(); }
            @Override public void delete(String cropIdentifier) {
                if (throwOnDelete) throw new RuntimeException("DB error");
            }
        };
    }

    @Test
    public void testSuccessfulDeletion() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new DeleteCropUseCase(fakeRepo(false)).delete("crop_123").accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testDeletionReturnsIdentifier() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new DeleteCropUseCase(fakeRepo(false)).delete("crop_456").accept(visitor);
        assertTrue(visitor.isCompleted("crop_456"));
    }

    @Test
    public void testDeletionDatabaseError() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new DeleteCropUseCase(fakeRepo(true)).delete("crop_789").accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}
