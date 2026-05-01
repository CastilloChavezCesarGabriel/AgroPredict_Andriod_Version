package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.schema.CatalogName;

public final class AndroidCatalogFactory implements ICatalogFactory {
    private final Database database;
    private final Context context;

    public AndroidCatalogFactory(Database database, Context context) {
        this.database = database;
        this.context = context;
    }

    @Override
    public ICropRepository createCropRepository() {
        return new SqliteCropRepository(database, new SessionRepository(context));
    }

    @Override
    public ICatalogRepository createSoilTypeCatalog() {
        return CatalogName.SOIL_TYPE.open(database);
    }

    @Override
    public ICatalogRepository createStageCatalog() {
        return CatalogName.PHENOLOGICAL_STAGE.open(database);
    }
}