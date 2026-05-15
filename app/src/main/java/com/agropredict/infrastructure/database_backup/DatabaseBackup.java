package com.agropredict.infrastructure.database_backup;

import android.util.Log;
import com.agropredict.application.backup.BackupSchedule;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public final class DatabaseBackup {
    private static final String TAG = "DatabaseBackup";
    private final File source;
    private final File destination;

    public DatabaseBackup(File source, File destination) {
        this.source = Objects.requireNonNull(source, "database backup requires a source");
        this.destination = Objects.requireNonNull(destination, "database backup requires a destination");
    }

    public void backup(BackupSchedule schedule) {
        if (!isStale(schedule)) return;
        if (!source.exists()) return;
        File parent = destination.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) return;
        transfer();
    }

    private boolean isStale(BackupSchedule schedule) {
        if (!destination.exists()) return true;
        return schedule.permits(destination.lastModified());
    }

    private void transfer() {
        try (FileInputStream input = new FileInputStream(source);
             FileOutputStream output = new FileOutputStream(destination)) {
            new FileCopier().copy(input, output);
        } catch (IOException ioFailure) {
            Log.e(TAG, "Database backup copy failed from "
                    + source.getAbsolutePath() + " to " + destination.getAbsolutePath(), ioFailure);
        }
    }
}
