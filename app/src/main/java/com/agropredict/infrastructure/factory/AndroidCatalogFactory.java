package com.agropredict.infrastructure.factory;

import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;
import java.util.Objects;

public final class AndroidCatalogFactory implements ICatalogFactory {
    private final CropPersistence cropPersistence;
    private final CatalogPersistence catalogPersistence;

    public AndroidCatalogFactory(CropPersistence cropPersistence, CatalogPersistence catalogPersistence) {
        this.cropPersistence = Objects.requireNonNull(cropPersistence,
                "android catalog factory requires a crop persistence");
        this.catalogPersistence = Objects.requireNonNull(catalogPersistence,
                "android catalog factory requires a catalog persistence");
    }

    @Override
    public ICropRepository createCropRepository() {
        return cropPersistence.createCropRepository();
    }

    @Override
    public ICatalogRepository createSoilTypeCatalog() {
        return catalogPersistence.createSoilTypeCatalog();
    }

    @Override
    public ICatalogRepository createStageCatalog() {
        return catalogPersistence.createStageCatalog();
    }
}