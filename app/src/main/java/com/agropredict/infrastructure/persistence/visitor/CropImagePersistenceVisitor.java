package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.visitor.crop.ICropImageVisitor;
import com.agropredict.infrastructure.persistence.IRow;

public final class CropImagePersistenceVisitor implements ICropImageVisitor {
    private final IRow row;

    public CropImagePersistenceVisitor(IRow row) {
        this.row = row;
    }

    @Override
    public void visit(String identifier, String filePath) {
        row.record("id", identifier);
        row.record("file_path", filePath);
    }
}