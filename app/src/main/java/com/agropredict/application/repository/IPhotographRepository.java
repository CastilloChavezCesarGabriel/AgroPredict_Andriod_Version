package com.agropredict.application.repository;

import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;

public interface IPhotographRepository extends IClearable {
    void store(Photograph photograph, Crop crop);
    Photograph find(String diagnosticIdentifier);
}