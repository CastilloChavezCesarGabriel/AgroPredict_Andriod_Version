package com.agropredict.application.usecase.diagnostic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.visitor.TestOperationResultVisitor;

import org.junit.Test;

import java.util.List;

public final class DeleteDiagnosticUseCaseTest {

    private IDiagnosticRepository fakeRepo(boolean throwOnDelete) {
        return new IDiagnosticRepository() {
            @Override public void store(Diagnostic d) {}
            @Override public void delete(String id) { if (throwOnDelete) throw new RuntimeException("DB error"); }
            @Override public List<Diagnostic> list(String userId) { return List.of(); }
            @Override public List<Diagnostic> filter(String userId, String cropId) { return List.of(); }
            @Override public Diagnostic find(String id) { return null; }
            @Override public Diagnostic resolve(String userId, String cropId) { return null; }
        };
    }

    @Test
    public void testSuccessfulDeletion() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new DeleteDiagnosticUseCase(fakeRepo(false)).delete("diag_1").accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testDeletionDatabaseError() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new DeleteDiagnosticUseCase(fakeRepo(true)).delete("diag_1").accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}
