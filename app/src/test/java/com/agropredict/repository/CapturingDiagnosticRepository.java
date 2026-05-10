package com.agropredict.repository;

import com.agropredict.application.repository.ICropRecord;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.domain.diagnostic.Diagnostic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CapturingDiagnosticRepository implements IDiagnosticRepository, IRecordEraser, ICropRecord {
    private final Map<String, List<Diagnostic>> diagnosticsByCrop = new HashMap<>();
    private final Set<String> clearedCrops = new HashSet<>();

    public void enroll(String cropIdentifier, Diagnostic diagnostic) {
        diagnosticsByCrop.computeIfAbsent(cropIdentifier, key -> new ArrayList<>()).add(diagnostic);
    }

    public boolean clearedFor(String cropIdentifier) {
        return clearedCrops.contains(cropIdentifier);
    }

    public boolean remainsFor(String cropIdentifier) {
        List<Diagnostic> entries = diagnosticsByCrop.get(cropIdentifier);
        return entries != null && !entries.isEmpty();
    }

    @Override public void store(Diagnostic diagnostic) {}
    @Override public List<Diagnostic> list(String userIdentifier) { return Collections.emptyList(); }
    @Override public List<Diagnostic> filter(String userIdentifier, String cropIdentifier) {
        return diagnosticsByCrop.getOrDefault(cropIdentifier, Collections.emptyList());
    }
    @Override public Diagnostic find(String diagnosticIdentifier) { return null; }
    @Override public Diagnostic resolve(String userIdentifier, String cropIdentifier) { return null; }
    @Override public void erase(String diagnosticIdentifier) {}

    @Override
    public void discard(String cropIdentifier) {
        diagnosticsByCrop.remove(cropIdentifier);
        clearedCrops.add(cropIdentifier);
    }
}
