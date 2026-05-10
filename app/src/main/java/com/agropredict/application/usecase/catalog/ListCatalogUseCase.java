package com.agropredict.application.usecase.catalog;

import com.agropredict.application.repository.ICatalogRepository;
import java.util.List;
import java.util.Objects;

public final class ListCatalogUseCase {
    private final ICatalogRepository catalogRepository;

    public ListCatalogUseCase(ICatalogRepository catalogRepository) {
        this.catalogRepository = Objects.requireNonNull(catalogRepository, "list catalog use case requires a catalog repository");
    }

    public List<String> list() {
        return catalogRepository.list();
    }
}