package com.agropredict.repository;

import com.agropredict.application.repository.ICatalogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FixedCatalogRepository implements ICatalogRepository {
    private final Map<String, String> nameToIdentifier;

    public FixedCatalogRepository(Map<String, String> nameToIdentifier) {
        this.nameToIdentifier = nameToIdentifier;
    }

    @Override
    public List<String> list() {
        return new ArrayList<>(nameToIdentifier.keySet());
    }

    @Override
    public String resolve(String name) {
        return nameToIdentifier.get(name);
    }
}
