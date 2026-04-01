package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.Session;
import com.agropredict.domain.visitor.session.ISessionVisitor;
import com.agropredict.domain.visitor.crop.ICropVisitor;
import com.agropredict.domain.visitor.crop.IPhotographVisitor;
import com.agropredict.infrastructure.persistence.database.IRow;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class PhotographPersistenceVisitor implements IPhotographVisitor, ICropVisitor, ISessionVisitor {
    private final IRow row;

    public PhotographPersistenceVisitor(IRow row, Session session) {
        this.row = row;
        session.accept(this);
    }

    @Override
    public void visitImage(String identifier, String filePath) {
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
    public void visitIdentity(String identifier, String cropType) {
        row.record("crop_id", identifier);
    }

    @Override
    public void visitField(String name, String location) {}

    @Override
    public void visitSoil(String typeIdentifier, String area) {}

    @Override
    public void visitPlanting(String date, String stageIdentifier) {}

    @Override
    public void visit(String userIdentifier, String occupation) {
        row.record("user_id", userIdentifier);
    }
}