package com.agropredict.infrastructure.factory;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqlitePhotographRepository;
import com.agropredict.infrastructure.persistence.sync.SqliteSyncRecorder;
import com.agropredict.infrastructure.persistence.sync.SyncingCropRepository;
import com.agropredict.infrastructure.persistence.sync.SyncingPhotographRepository;
import java.util.Objects;

public final class CropPersistence {
    private final Database database;
    private final SqliteRowFactory rowFactory;
    private final ISessionRepository session;

    public CropPersistence(Database database, SqliteRowFactory rowFactory, ISessionRepository session) {
        this.database = Objects.requireNonNull(database, "crop persistence requires a database");
        this.rowFactory = Objects.requireNonNull(rowFactory, "crop persistence requires a row factory");
        this.session = Objects.requireNonNull(session, "crop persistence requires a session");
    }

    public ICropRepository createCropRepository() {
        return new SyncingCropRepository(
                new SqliteCropRepository(database, session, rowFactory),
                new SqliteSyncRecorder(session, rowFactory));
    }

    public IPhotographRepository createPhotographRepository() {
        return new SyncingPhotographRepository(
                new SqlitePhotographRepository(database, session, rowFactory),
                new SqliteSyncRecorder(session, rowFactory));
    }
}