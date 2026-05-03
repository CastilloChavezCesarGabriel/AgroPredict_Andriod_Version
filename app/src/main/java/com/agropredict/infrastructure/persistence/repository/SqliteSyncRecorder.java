package com.agropredict.infrastructure.persistence.repository;

import android.content.ContentValues;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.Identifier;
import com.agropredict.domain.Session;
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.database.Database;

public final class SqliteSyncRecorder {
    private final Database database;
    private final ISessionRepository sessionRepository;

    public SqliteSyncRecorder(Database database, ISessionRepository sessionRepository) {
        this.database = database;
        this.sessionRepository = sessionRepository;
    }

    public void insert(String table, String identifier) {
        write(table, "INSERT", identifier);
    }

    public void update(String table, String identifier) {
        write(table, "UPDATE", identifier);
    }

    public void delete(String table, String identifier) {
        write(table, "DELETE", identifier);
    }

    private void write(String table, String operation, String identifier) {
        ContentValues values = new ContentValues();
        values.put("id", Identifier.generate("sync"));
        Session session = sessionRepository.recall();
        if (session != null) session.accept((userIdentifier, occupation) -> values.put("user_id", userIdentifier));
        values.put("table_name", table);
        values.put("operation", operation);
        values.put("json_data", "{\"id\":\"" + identifier + "\"}");
        values.put("created_at", Clock.read());
        database.getWritableDatabase().insert("sync_pending", null, values);
    }
}
