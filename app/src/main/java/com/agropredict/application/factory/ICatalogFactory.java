package com.agropredict.application.factory;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;

public interface ICatalogFactory {
    ICropRepository createCropRepository();
    ICatalogRepository createSoilTypeCatalog();
    ICatalogRepository createStageCatalog();
}