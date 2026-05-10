package com.agropredict.application.usecase;

import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.visitor.FailExpecter;
import com.agropredict.visitor.SucceedExpecter;

import org.junit.Test;

public final class DeleteUseCaseTest {

    private IRecordEraser fakeRepo(boolean throwOnDelete) {
        return identifier -> {
            if (throwOnDelete) throw new RuntimeException("DB error");
        };
    }

    @Test
    public void testSuccessfulDeletion() {
        new DeleteUseCase(fakeRepo(false)).delete("entity_123").accept(new SucceedExpecter(null));
    }

    @Test
    public void testDeletionReturnsIdentifier() {
        new DeleteUseCase(fakeRepo(false)).delete("entity_456").accept(new SucceedExpecter("entity_456"));
    }

    @Test
    public void testDeletionDatabaseError() {
        new DeleteUseCase(fakeRepo(true)).delete("entity_789").accept(new FailExpecter());
    }
}
