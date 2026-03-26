package com.agropredict.infrastructure.factory;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.infrastructure.persistence.AuditLogger;
import com.agropredict.infrastructure.persistence.Database;
import com.agropredict.infrastructure.persistence.DiagnosticWorkflow;
import com.agropredict.infrastructure.persistence.SubmissionRecorder;
import com.agropredict.infrastructure.persistence.SqliteCatalog;
import com.agropredict.infrastructure.persistence.repository.SqliteCropImageRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteQuestionnaireRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteReportRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteUserRepository;
import com.agropredict.infrastructure.persistence.Submission;

public final class PersistenceFactory {
    private final Database database;

    public PersistenceFactory(Database database) {
        this.database = database;
    }

    public IUserRepository createUserRepository() {
        return new SqliteUserRepository(database);
    }

    public ICropRepository createCropRepository() {
        return new SqliteCropRepository(database);
    }

    public IDiagnosticRepository createDiagnosticRepository() {
        return new SqliteDiagnosticRepository(database);
    }

    public ICropImageRepository createCropImageRepository() {
        return new SqliteCropImageRepository(database);
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
        Submission submission = new Submission(createCropRepository(), createCropImageRepository());
        SubmissionRecorder result = new SubmissionRecorder(createDiagnosticRepository(), createQuestionnaireRepository());
        return new DiagnosticWorkflow(submission, result);
    }
}