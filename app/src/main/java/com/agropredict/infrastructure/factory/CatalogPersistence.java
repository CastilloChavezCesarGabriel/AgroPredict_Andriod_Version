package com.agropredict.infrastructure.factory;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.schema.CatalogName;
import java.util.Objects;

public final class CatalogPersistence {
    private final Database database;

    public CatalogPersistence(Database database) {
        this.database = Objects.requireNonNull(database, "catalog persistence requires a database");
    }

    public ICatalogRepository createSoilTypeCatalog() {
        return CatalogName.SOIL_TYPE.open(database);
    }

    public ICatalogRepository createStageCatalog() {
        return CatalogName.PHENOLOGICAL_STAGE.open(database);
    }

    public ICatalogRepository createOccupationCatalog() {
        return CatalogName.OCCUPATION.open(database);
    }
}