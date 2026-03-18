package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import com.agropredict.domain.visitor.ICropImageVisitor;

public final class CropImageRecorder implements ICropImageVisitor {
    private final ContentValues values;

    public CropImageRecorder(ContentValues values) {
        this.values = values;
    }

    @Override
    public void visit(String identifier, String filePath) {
        values.put("id", identifier);
        values.put("file_path", filePath);
    }
}
