package com.agropredict.core;

import android.content.Context;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.factory.IPredictionFactory;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.infrastructure.factory.AndroidAccessFactory;
import com.agropredict.infrastructure.factory.AndroidCatalogFactory;
import com.agropredict.infrastructure.factory.AndroidDashboardFactory;
import com.agropredict.infrastructure.factory.AndroidPredictionFactory;
import com.agropredict.infrastructure.factory.AndroidReportingFactory;
import com.agropredict.infrastructure.factory.AndroidReviewFactory;
import com.agropredict.infrastructure.database_backup.DatabaseBackup;
import com.agropredict.infrastructure.persistence.database.Database;
import java.io.File;

public final class Configuration {
    private final Database database;
    private final Context context;

    public Configuration(Context context) {
        this.database = new Database(context);
        this.context = context;
    }

    public IAccessFactory createAccess() {
        return new AndroidAccessFactory(database, context);
    }

    public IDashboardFactory createDashboard() {
        return new AndroidDashboardFactory(context);
    }

    public ICatalogFactory createCatalog() {
        return new AndroidCatalogFactory(database, context);
    }

    public IReviewFactory createReview() {
        return new AndroidReviewFactory(database, context);
    }

    public IPredictionFactory createPrediction() {
        return new AndroidPredictionFactory(database, context);
    }

    public IReportingFactory createReporting() {
        return new AndroidReportingFactory(database, context);
    }

    public void backup() {
        String name = database.getDatabaseName();
        File source = context.getDatabasePath(name);
        File destination = new File(context.getExternalFilesDir(null), "backups/" + name);
        new DatabaseBackup(source, destination).backup();
    }
}