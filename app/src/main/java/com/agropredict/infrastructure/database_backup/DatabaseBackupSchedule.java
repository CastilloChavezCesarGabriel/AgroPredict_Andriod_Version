package com.agropredict.infrastructure.database_backup;

import android.content.Context;
import com.agropredict.infrastructure.persistence.database.Database;
import java.io.File;
import java.util.Objects;

public final class DatabaseBackupSchedule {
    private static final long BACKUP_INTERVAL_MILLIS = 24L * 60L * 60L * 1000L;
    private final Database database;
    private final Context context;

    public DatabaseBackupSchedule(Database database, Context context) {
        this.database = Objects.requireNonNull(database, "database backup schedule requires a database");
        this.context = Objects.requireNonNull(context, "database backup schedule requires a context");
    }

    public void backup() {
        String name = database.getDatabaseName();
        File destination = new File(context.getExternalFilesDir(null), "backups/" + name);
        if (skip(destination)) return;
        File source = context.getDatabasePath(name);
        new DatabaseBackup(source, destination).backup();
    }

    private boolean skip(File destination) {
        boolean backupExists = destination.exists();
        if (!backupExists) return false;
        long currentTime = System.currentTimeMillis();
        long lastBackupTime = destination.lastModified();
        long elapsedBackupTime = currentTime - lastBackupTime;
        return elapsedBackupTime < BACKUP_INTERVAL_MILLIS;
    }
}
