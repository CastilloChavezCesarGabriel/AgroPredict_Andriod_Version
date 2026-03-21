package com.agropredict.application.usecase.catalog;

import com.agropredict.application.repository.ICatalogRepository;
import java.util.List;

public final class ListCatalogUseCase {
    private final ICatalogRepository catalogRepository;

    public ListCatalogUseCase(ICatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<String> list() {
        return catalogRepository.list();
    }
}