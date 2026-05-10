package com.agropredict.repository;

import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.photograph.Photograph;

public final class CapturingPhotographRepository implements IPhotographRepository {
    @Override public void store(Photograph photograph, Crop crop) {}
    @Override public Photograph find(String diagnosticIdentifier) { return null; }
}
