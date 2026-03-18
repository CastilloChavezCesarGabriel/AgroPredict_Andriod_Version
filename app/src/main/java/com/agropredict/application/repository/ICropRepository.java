package com.agropredict.application.repository;

import com.agropredict.domain.entity.Crop;
import java.util.List;

public interface ICropRepository {
    void store(Crop crop);
    void update(Crop crop);
    List<Crop> list(String userIdentifier);
    Crop load(String cropIdentifier);
}