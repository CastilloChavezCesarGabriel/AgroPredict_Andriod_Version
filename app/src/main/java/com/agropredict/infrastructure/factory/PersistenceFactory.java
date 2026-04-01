package com.agropredict.infrastructure.factory;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.infrastructure.persistence.AuditLogger;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.diagnostic_submission.DiagnosticWorkflow;
import com.agropredict.infrastructure.persistence.diagnostic_submission.SubmissionRecorder;
import com.agropredict.infrastructure.persistence.repository.SqliteCatalog;
import com.agropredict.infrastructure.persistence.repository.SqlitePhotographRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteQuestionnaireRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteReportRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteUserRepository;
import com.agropredict.infrastructure.persistence.diagnostic_submission.Submission;

public final class PersistenceFactory {
    private final Database database;
    private final ISessionRepository sessionRepository;

    public PersistenceFactory(Database database, ISessionRepository sessionRepository) {
        this.database = database;
        this.sessionRepository = sessionRepository;
    }

    public IUserRepository createUserRepository() {
        return new SqliteUserRepository(database, createCatalog("occupation"));
    }

    public ICropRepository createCropRepository() {
        return new SqliteCropRepository(database, sessionRepository);
    }

    public IDiagnosticRepository createDiagnosticRepository() {
        return new SqliteDiagnosticRepository(database);
    }

    public IPhotographRepository createPhotographRepository() {
        return new SqlitePhotographRepository(database, sessionRepository);
    }

    public IReportRepository createReportRepository() {
        return new SqliteReportRepository(database);
    }

    private IQuestionnaireRepository createQuestionnaireRepository() {
        return new SqliteQuestionnaireRepository(database);
    }

    public ICatalogRepository createCatalog(String tableName) {
        return new SqliteCatalog(database, tableName);
    }

    public IAuditLogger createAuditLogger() {
        return new AuditLogger(database);
    }

    public IDiagnosticWorkflow createDiagnosticWorkflow() {
        Submission submission = new Submission(createCropRepository(), createPhotographRepository());
        SubmissionRecorder recorder = new SubmissionRecorder(createDiagnosticRepository(), createQuestionnaireRepository());
        return new DiagnosticWorkflow(submission, recorder);
    }
}