package com.agropredict.application.repository;

import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;

public interface IPhotographRepository {
    void store(Photograph photograph, Crop crop);
}