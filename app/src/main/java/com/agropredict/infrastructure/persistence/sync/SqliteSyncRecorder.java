package com.agropredict.infrastructure.persistence.sync;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.domain.authentication.session.ISession;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import java.util.Objects;

public final class SqliteSyncRecorder {
    private final ISessionRepository sessionRepository;
    private final SqliteRowFactory rowFactory;

    public SqliteSyncRecorder(ISessionRepository sessionRepository, SqliteRowFactory rowFactory) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository, "sync recorder requires a session repository");
        this.rowFactory = Objects.requireNonNull(rowFactory, "sync recorder requires a row factory");
    }

    public void insert(String table, String identifier) {
        write(table, SyncOperation.INSERT, identifier);
    }

    public void update(String table, String identifier) {
        write(table, SyncOperation.UPDATE, identifier);
    }

    public void delete(String table, String identifier) {
        write(table, SyncOperation.DELETE, identifier);
    }

    private void write(String table, SyncOperation operation, String identifier) {
        SqliteRow row = rowFactory.open();
        row.record("id", IdentifierFactory.generate("sync"));
        ISession session = sessionRepository.recall();
        session.report((userIdentifier, occupation) -> row.record("user_id", userIdentifier));
        row.record("table_name", table);
        operation.record(row);
        row.record("json_data", "{\"id\":\"" + identifier + "\"}");
        row.stamp("created_at");
        row.flush("sync_pending");
    }
}
