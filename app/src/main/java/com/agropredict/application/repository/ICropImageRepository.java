package com.agropredict.application.repository;

import com.agropredict.domain.entity.CropImage;

public interface ICropImageRepository {
    void store(CropImage image);
    CropImage load(String imageIdentifier);
}