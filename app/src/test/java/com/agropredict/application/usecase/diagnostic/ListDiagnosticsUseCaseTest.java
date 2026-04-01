package com.agropredict.application.usecase.diagnostic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.entity.Diagnostic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public final class ListDiagnosticsUseCaseTest {

    private IDiagnosticRepository stubDiag(List<Diagnostic> all, List<Diagnostic> filtered) {
        return new IDiagnosticRepository() {
            @Override public void store(Diagnostic d) {}
            @Override public void delete(String id) {}
            @Override public List<Diagnostic> list(String userId) { return all; }
            @Override public List<Diagnostic> filter(String userId, String cropId) { return filtered; }
            @Override public Diagnostic find(String id) { return null; }
            @Override public Diagnostic resolve(String userId, String cropId) { return null; }
        };
    }

    @Test
    public void testListAllDiagnostics() {
        Prediction prediction = new Prediction("wheat", 0.85);
        List<Diagnostic> diags = List.of(
            new Diagnostic("d1", prediction),
            new Diagnostic("d2", prediction)
        );
        List<Diagnostic> result = new ListDiagnosticUseCase(stubDiag(diags, List.of())).list("user_1");
        assertEquals(2, result.size());
    }

    @Test
    public void testListEmpty() {
        List<Diagnostic> result = new ListDiagnosticUseCase(stubDiag(new ArrayList<>(), List.of())).list("user_1");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterByCrop() {
        List<Diagnostic> filtered = List.of(new Diagnostic("d1", new Prediction("corn", 0.9)));
        List<Diagnostic> result = new ListDiagnosticUseCase(stubDiag(List.of(), filtered)).filter("user_1", "crop_1");
        assertEquals(1, result.size());
    }

    @Test
    public void testFilterEmptyResult() {
        List<Diagnostic> result = new ListDiagnosticUseCase(stubDiag(List.of(), new ArrayList<>())).filter("user_1", "crop_999");
        assertTrue(result.isEmpty());
    }
}
