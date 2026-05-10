package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.authentication.ISession;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Severity;

public final class DiagnosticContext {
    private final ISessionRepository sessionRepository;
    private final ISeverityFactory severityFactory;

    public DiagnosticContext(ISessionRepository sessionRepository, ISeverityFactory severityFactory) {
        this.sessionRepository = sessionRepository;
        this.severityFactory = severityFactory;
    }

    public ISession recall() {
        return sessionRepository.recall();
    }

    public Severity classify(String gravity) {
        return severityFactory.classify(gravity);
    }
}
