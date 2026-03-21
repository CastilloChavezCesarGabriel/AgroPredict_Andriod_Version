package com.agropredict.infrastructure.persistence;

import com.agropredict.domain.visitor.crop.ICropImageVisitor;

public final class CropImageRecorder implements ICropImageVisitor {
    private final IRecord record;

    public CropImageRecorder(IRecord record) {
        this.record = record;
    }

    @Override
    public void visit(String identifier, String filePath) {
        record.record("id", identifier);
        record.record("file_path", filePath);
    }
}
