package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.authentication.session.ISession;
import com.agropredict.domain.photograph.IPhotographConsumer;
import com.agropredict.domain.authentication.session.ISessionConsumer;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import java.io.File;

public final class PhotographPersistenceVisitor implements IPhotographConsumer, ISessionConsumer {
    private final SqliteRow row;

    public PhotographPersistenceVisitor(SqliteRow row, ISession session) {
        this.row = row;
        session.report(this);
    }

    public void link(String cropIdentifier) {
        row.record("crop_id", cropIdentifier);
    }

    @Override
    public void expose(String identifier, String filePath) {
        row.record("id", identifier);
        row.record("file_path", filePath);
        measure(filePath);
    }

    private void measure(String filePath) {
        if (filePath == null) return;
        File file = new File(filePath);
        if (!file.exists()) return;
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot > 0) row.record("format", name.substring(dot + 1).toLowerCase());
        double megabytes = file.length() / (1024.0 * 1024.0);
        row.record("size_in_megabytes", String.valueOf(megabytes));
        row.imprint("captured_at", file.lastModified());
    }

    @Override
    public void report(String userIdentifier, String occupation) {
        row.record("user_id", userIdentifier);
    }
}
