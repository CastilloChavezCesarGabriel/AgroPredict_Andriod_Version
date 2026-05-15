package com.agropredict.infrastructure.persistence;

import com.agropredict.application.service.IAuditLogger;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import java.util.Objects;

public final class AuditLogger implements IAuditLogger {
    private final SqliteRowFactory rowFactory;

    public AuditLogger(SqliteRowFactory rowFactory) {
        this.rowFactory = Objects.requireNonNull(rowFactory, "audit logger requires a row factory");
    }

    @Override
    public void log(String userIdentifier, String action) {
        SqliteRow row = rowFactory.open();
        row.record("id", IdentifierFactory.generate("log"));
        row.record("user_id", userIdentifier);
        row.record("log_action", action);
        row.record("description", action);
        row.stamp("created_at");
        row.flush("log_entry");
    }
}
