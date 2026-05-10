package com.agropredict.application.repository;

import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.photograph.Photograph;

public interface IPhotographRepository {
    void store(Photograph photograph, Crop crop);
    Photograph find(String diagnosticIdentifier);
}