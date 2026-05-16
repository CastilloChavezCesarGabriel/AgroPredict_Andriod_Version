package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.factory.ISeverityFactory;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.authentication.session.ISession;
import com.agropredict.domain.diagnostic.severity.ISeverityResolver;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.sync.SqliteSyncRecorder;

import java.util.Objects;

public final class DiagnosticPersistenceContext {
    private final ISessionRepository sessionRepository;
    private final ISeverityResolver severityResolver;
    private final ISeverityFactory severityFactory;

    public DiagnosticPersistenceContext(ISessionRepository sessionRepository, ISeverityResolver severityResolver, ISeverityFactory severityFactory) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository, "diagnostic context requires a session repository");
        this.severityResolver = Objects.requireNonNull(severityResolver, "diagnostic context requires a severity resolver");
        this.severityFactory = Objects.requireNonNull(severityFactory, "diagnostic context requires a severity factory");
    }

    public ISession recall() {
        return sessionRepository.recall();
    }

    public ISeverity classify(String gravity) {
        return severityResolver.classify(gravity);
    }

    public ISeverity createPending() {
        return severityFactory.createPending();
    }

    public SqliteSyncRecorder track(SqliteRowFactory rowFactory) {
        return new SqliteSyncRecorder(sessionRepository, rowFactory);
    }
}