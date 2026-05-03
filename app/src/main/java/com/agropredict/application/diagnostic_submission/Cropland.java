package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;

public final class Cropland {
    private final FieldStorage storage;
    private final ICatalogRepository stageCatalog;

    public Cropland(FieldStorage storage, ICatalogRepository stageCatalog) {
        this.storage = storage;
        this.stageCatalog = stageCatalog;
    }

    public void persist(Crop crop) {
        storage.persist(crop);
    }

    public void capture(Photograph photograph, Crop crop) {
        storage.capture(photograph, crop);
    }

    public String resolve(String stageName) {
        return stageCatalog.resolve(stageName);
    }
}
