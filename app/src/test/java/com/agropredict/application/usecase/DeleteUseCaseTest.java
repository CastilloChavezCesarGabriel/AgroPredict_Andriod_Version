package com.agropredict.application.usecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.IDeletable;
import com.agropredict.visitor.TestOperationResultVisitor;

import org.junit.Test;

public final class DeleteUseCaseTest {

    private IDeletable fakeRepo(boolean throwOnDelete) {
        return identifier -> {
            if (throwOnDelete) throw new RuntimeException("DB error");
        };
    }

    @Test
    public void testSuccessfulDeletion() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new DeleteUseCase(fakeRepo(false)).delete("entity_123").accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testDeletionReturnsIdentifier() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new DeleteUseCase(fakeRepo(false)).delete("entity_456").accept(visitor);
        assertTrue(visitor.isCompleted("entity_456"));
    }

    @Test
    public void testDeletionDatabaseError() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new DeleteUseCase(fakeRepo(true)).delete("entity_789").accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}
