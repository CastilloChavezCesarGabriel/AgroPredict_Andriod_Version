package com.agropredict.core;

import android.content.Context;
import com.agropredict.application.factory.IImageRejectionFactory;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.factory.IDiagnosticApiFactory;
import com.agropredict.application.factory.IDiagnosticWorkflowFactory;
import com.agropredict.application.factory.IImageClassificationFactory;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.factory.ISeverityFactory;
import com.agropredict.application.backup.BackupPolicy;
import com.agropredict.application.backup.BackupSchedule;
import com.agropredict.application.backup.IBackup;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IClock;
import com.agropredict.application.service.IReportServiceCatalog;
import com.agropredict.application.service.ReportFormat;
import com.agropredict.domain.diagnostic.severity.GravitySeverityResolver;
import com.agropredict.domain.diagnostic.severity.ISeverityResolver;
import com.agropredict.domain.diagnostic.severity.SeverityClassifier;
import com.agropredict.infrastructure.database_backup.DatabaseBackup;
import com.agropredict.infrastructure.factory.AndroidAccessFactory;
import com.agropredict.infrastructure.factory.AndroidCatalogFactory;
import com.agropredict.infrastructure.factory.AndroidDashboardFactory;
import com.agropredict.infrastructure.factory.AndroidDiagnosticApi;
import com.agropredict.infrastructure.factory.AndroidDiagnosticWorkflow;
import com.agropredict.infrastructure.factory.AndroidImageClassification;
import com.agropredict.infrastructure.factory.AndroidReportingFactory;
import com.agropredict.infrastructure.factory.AndroidReviewFactory;
import com.agropredict.infrastructure.factory.CatalogPersistence;
import com.agropredict.infrastructure.factory.CropPersistence;
import com.agropredict.infrastructure.factory.DiagnosticPersistence;
import com.agropredict.infrastructure.factory.ReportPersistence;
import com.agropredict.infrastructure.factory.SessionPersistence;
import com.agropredict.infrastructure.factory.UserPersistence;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.database.SystemClock;
import com.agropredict.infrastructure.persistence.repository.DiagnosticPersistenceContext;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import com.agropredict.infrastructure.report_export.AndroidReportServiceCatalog;
import com.agropredict.infrastructure.report_export.CsvReportService;
import com.agropredict.infrastructure.report_export.PdfReportService;
import java.io.File;
import java.util.List;
import java.util.Map;

public final class Configuration {
    private static final String DATABASE_NAME = "agro_diagnostic.db";
    private final Context context;
    private final Database database;
    private final IClock clock;
    private final ISeverityResolver severityResolver;
    private final ISessionRepository session;

    public Configuration(Context context, ISeverityFactory severityFactory) {
        this.context = context;
        this.database = new Database(context, DATABASE_NAME);
        this.clock = new SystemClock();
        this.severityResolver = createSeverityResolver(severityFactory);
        this.session = new SessionRepository(context);
    }

    public IAccessFactory createAccess() {
        return new AndroidAccessFactory(
                new UserPersistence(database, createRowFactory(), session),
                new SessionPersistence(context),
                new CatalogPersistence(database));
    }

    public IDashboardFactory createDashboard() {
        return new AndroidDashboardFactory(new SessionPersistence(context), context);
    }

    public ICatalogFactory createCatalog() {
        SqliteRowFactory rowFactory = createRowFactory();
        return new AndroidCatalogFactory(
                new CropPersistence(database, rowFactory, session),
                new CatalogPersistence(database));
    }

    public IReviewFactory createReview(ISeverityFactory severityFactory) {
        SqliteRowFactory rowFactory = createRowFactory();
        DiagnosticPersistenceContext diagnosticContext = new DiagnosticPersistenceContext(session, severityResolver, severityFactory);
        return new AndroidReviewFactory(
                new DiagnosticPersistence(database, rowFactory, diagnosticContext),
                new CropPersistence(database, rowFactory, session),
                new SessionPersistence(context));
    }

    public IImageClassificationFactory createImageClassification(IImageRejectionFactory rejectionFactory) {
        return new AndroidImageClassification(context, rejectionFactory);
    }

    public IDiagnosticApiFactory createDiagnosticApi() {
        return new AndroidDiagnosticApi(severityResolver);
    }

    public IDiagnosticWorkflowFactory createDiagnosticWorkflow(ISeverityFactory severityFactory) {
        SqliteRowFactory rowFactory = createRowFactory();
        DiagnosticPersistenceContext diagnosticContext = new DiagnosticPersistenceContext(session, severityResolver, severityFactory);
        return new AndroidDiagnosticWorkflow(
                new CropPersistence(database, rowFactory, session),
                new DiagnosticPersistence(database, rowFactory, diagnosticContext));
    }

    public IReportingFactory createReporting() {
        return new AndroidReportingFactory(new ReportPersistence(database, createRowFactory(), session));
    }

    public IReportServiceCatalog createReportServiceCatalog() {
        File reportsDirectory = new File(context.getExternalFilesDir(null), "reports");
        if (!reportsDirectory.exists() && !reportsDirectory.mkdirs()) {
            throw new IllegalStateException("cannot create reports directory: " + reportsDirectory.getAbsolutePath());
        }
        return new AndroidReportServiceCatalog(Map.of(
                ReportFormat.CSV, new CsvReportService(reportsDirectory, clock),
                ReportFormat.PDF, new PdfReportService(reportsDirectory, clock)));
    }

    public IBackup createBackup() {
        long backupIntervalMillis = 24L * 60 * 60 * 1000;
        String backupSubdirectory = "backups";
        BackupSchedule schedule = new BackupSchedule(new BackupPolicy(backupIntervalMillis), clock);
        File source = context.getDatabasePath(DATABASE_NAME);
        File destination = new File(context.getExternalFilesDir(null), backupSubdirectory + File.separator + DATABASE_NAME);
        DatabaseBackup databaseBackup = new DatabaseBackup(source, destination);
        return () -> databaseBackup.backup(schedule);
    }

    private SqliteRowFactory createRowFactory() {
        return new SqliteRowFactory(database, clock);
    }

    private ISeverityResolver createSeverityResolver(ISeverityFactory severityFactory) {
        SeverityClassifier healthy = new SeverityClassifier(List.of("bajo", "low"), severityFactory.createHealthy());
        SeverityClassifier moderate = new SeverityClassifier(List.of("moderado", "moderate"), severityFactory.createModerate());
        SeverityClassifier severe = new SeverityClassifier(List.of("alto", "high", "critico", "critical"), severityFactory.createSevere());
        return new GravitySeverityResolver(List.of(healthy, moderate, severe), severityFactory.createUnknown());
    }
}