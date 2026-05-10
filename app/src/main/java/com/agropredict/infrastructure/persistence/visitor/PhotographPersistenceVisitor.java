package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.authentication.ISession;
import com.agropredict.domain.crop.visitor.ICropIdentityConsumer;
import com.agropredict.domain.photograph.IPhotographConsumer;
import com.agropredict.domain.authentication.ISessionConsumer;
import com.agropredict.infrastructure.persistence.database.IRow;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class PhotographPersistenceVisitor implements IPhotographConsumer, ICropIdentityConsumer, ISessionConsumer {
    private final IRow row;

    public PhotographPersistenceVisitor(IRow row, ISession session) {
        this.row = row;
        session.report(this);
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
        String captured = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date(file.lastModified()));
        row.record("captured_at", captured);
    }

    @Override
    public void describe(String identifier, String cropType) {
        row.record("crop_id", identifier);
    }

    @Override
    public void report(String userIdentifier, String occupation) {
        row.record("user_id", userIdentifier);
    }
}
