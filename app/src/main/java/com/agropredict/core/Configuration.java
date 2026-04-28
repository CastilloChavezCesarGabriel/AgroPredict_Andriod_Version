package com.agropredict.core;

import android.content.Context;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.factory.IPredictionFactory;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.infrastructure.composer.AccessComposer;
import com.agropredict.infrastructure.composer.CatalogComposer;
import com.agropredict.infrastructure.composer.DashboardComposer;
import com.agropredict.infrastructure.composer.PredictionComposer;
import com.agropredict.infrastructure.composer.ReportingComposer;
import com.agropredict.infrastructure.composer.ReviewComposer;
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

    public IAccessFactory access() {
        return new AccessComposer(database, context);
    }

    public IDashboardFactory dashboard() {
        return new DashboardComposer(context);
    }

    public ICatalogFactory catalog() {
        return new CatalogComposer(database, context);
    }

    public IReviewFactory review() {
        return new ReviewComposer(database, context);
    }

    public IPredictionFactory prediction() {
        return new PredictionComposer(database, context);
    }

    public IReportingFactory reporting() {
        return new ReportingComposer(database, context);
    }

    public void backup() {
        String name = database.getDatabaseName();
        File source = context.getDatabasePath(name);
        File destination = new File(context.getExternalFilesDir(null), "backups/" + name);
        new DatabaseBackup(source, destination).backup();
    }
}