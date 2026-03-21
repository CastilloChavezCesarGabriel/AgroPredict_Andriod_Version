package com.agropredict.infrastructure.factory;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.infrastructure.persistence.DatabaseHelper;
import com.agropredict.infrastructure.persistence.DiagnosticWorkflow;
import com.agropredict.infrastructure.persistence.ResultRecorder;
import com.agropredict.infrastructure.persistence.SqliteCatalog;
import com.agropredict.infrastructure.persistence.SqliteCropImageRepository;
import com.agropredict.infrastructure.persistence.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.SqliteQuestionnaireRepository;
import com.agropredict.infrastructure.persistence.SqliteReportRepository;
import com.agropredict.infrastructure.persistence.SqliteUserRepository;
import com.agropredict.infrastructure.persistence.SubmissionRecorder;

public final class PersistenceFactory {
    private final DatabaseHelper databaseHelper;

    public PersistenceFactory(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public IUserRepository createUserRepository() {
        return new SqliteUserRepository(databaseHelper);
    }

    public ICropRepository createCropRepository() {
        return new SqliteCropRepository(databaseHelper);
    }

    public IDiagnosticRepository createDiagnosticRepository() {
        return new SqliteDiagnosticRepository(databaseHelper);
    }

    public ICropImageRepository createCropImageRepository() {
        return new SqliteCropImageRepository(databaseHelper);
    }

    public IReportRepository createReportRepository() {
        return new SqliteReportRepository(databaseHelper);
    }

    public IQuestionnaireRepository createQuestionnaireRepository() {
        return new SqliteQuestionnaireRepository(databaseHelper);
    }

    public ICatalogRepository createCatalog(String tableName) {
        return new SqliteCatalog(databaseHelper, tableName);
    }

    public IDiagnosticWorkflow createDiagnosticWorkflow() {
        SubmissionRecorder submission = new SubmissionRecorder(createCropRepository(), createCropImageRepository());
        ResultRecorder result = new ResultRecorder(createDiagnosticRepository(), createQuestionnaireRepository());
        return new DiagnosticWorkflow(submission, result);
    }
}
