package com.agropredict.infrastructure.database_backup;

import com.agropredict.application.service.IBackupService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class DatabaseBackup implements IBackupService {
    private final File source;
    private final File destination;

    public DatabaseBackup(File source, File destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void backup() {
        if (!source.exists()) return;
        File parent = destination.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) return;
        copy();
    }

    private void copy() {
        try (FileInputStream input = new FileInputStream(source);
             FileOutputStream output = new FileOutputStream(destination)) {
            FileCopier.copy(input, output);
        } catch (IOException ignored) {
        }
    }
}