package com.agropredict.core;

import android.content.Context;
import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.IFactoryConsumer;
import com.agropredict.infrastructure.database_backup.DatabaseBackup;
import com.agropredict.infrastructure.factory.RepositoryFactory;
import com.agropredict.infrastructure.persistence.database.Database;
import java.io.File;

public final class Configuration {
    private final IRepositoryFactory repositoryFactory;
    private final Database database;

    public Configuration(Context context) {
        this.database = new Database(context);
        this.repositoryFactory = new RepositoryFactory(database, context);
    }

    public void provide(IFactoryConsumer consumer) {
        consumer.accept(repositoryFactory);
    }

    public void backup(Context context) {
        String databaseName = database.getDatabaseName();
        File source = context.getDatabasePath(databaseName);
        File destination = new File(context.getExternalFilesDir(null), "backups/" + databaseName);
        new DatabaseBackup(source, destination).backup();
    }
}