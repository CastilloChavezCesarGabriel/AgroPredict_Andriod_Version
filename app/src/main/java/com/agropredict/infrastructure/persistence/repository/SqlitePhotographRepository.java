package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.PhotographPersistenceVisitor;

public final class SqlitePhotographRepository implements IPhotographRepository {
    private final Database database;
    private final ISessionRepository sessionRepository;

    public SqlitePhotographRepository(Database database, ISessionRepository sessionRepository) {
        this.database = database;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void store(Photograph photograph, Crop crop) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        PhotographPersistenceVisitor visitor = new PhotographPersistenceVisitor(row, sessionRepository.recall());
        photograph.accept(visitor);
        crop.accept(visitor);
        row.flush("image");
    }
}
