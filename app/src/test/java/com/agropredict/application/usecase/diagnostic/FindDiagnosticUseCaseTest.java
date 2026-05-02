package com.agropredict.application.usecase.diagnostic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.entity.Diagnostic;

import org.junit.Test;

import java.util.List;

public final class FindDiagnosticUseCaseTest {

    private IDiagnosticRepository stubDiag(Diagnostic returnValue) {
        return new IDiagnosticRepository() {
            @Override public void store(Diagnostic d) {}
            @Override public void delete(String id) {}
            @Override public void clear(String cropId) {}
            @Override public List<Diagnostic> list(String userId) { return List.of(); }
            @Override public List<Diagnostic> filter(String userId, String cropId) { return List.of(); }
            @Override public Diagnostic find(String id) { return returnValue; }
            @Override public Diagnostic resolve(String userId, String cropId) { return null; }
        };
    }

    @Test
    public void testFindExistingDiagnostic() {
        Diagnostic diag = new Diagnostic("diag_1", new Prediction("wheat", 0.85));
        assertNotNull(new FindDiagnosticUseCase(stubDiag(diag)).find("diag_1"));
    }

    @Test
    public void testFindNonExistingDiagnostic() {
        assertNull(new FindDiagnosticUseCase(stubDiag(null)).find("diag_999"));
    }
}
