package com.agropredict.repository;

import com.agropredict.application.repository.ICropRecord;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.photograph.Photograph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class CapturingPhotographRepository implements IPhotographRepository, ICropRecord {
    private final Map<String, Integer> countsByCrop = new HashMap<>();
    private final Set<String> clearedCrops = new HashSet<>();

    public void enroll(String cropIdentifier) {
        countsByCrop.merge(cropIdentifier, 1, Integer::sum);
    }

    public boolean clearedFor(String cropIdentifier) {
        return clearedCrops.contains(cropIdentifier);
    }

    public boolean remainsFor(String cropIdentifier) {
        Integer count = countsByCrop.get(cropIdentifier);
        return count != null && count > 0;
    }

    @Override public void store(Photograph photograph, Crop crop) {}

    @Override public Photograph find(String diagnosticIdentifier) { return null; }

    @Override
    public void discard(String cropIdentifier) {
        countsByCrop.remove(cropIdentifier);
        clearedCrops.add(cropIdentifier);
    }
}
