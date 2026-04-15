package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IAssetService;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.application.service.IReportService;
import com.agropredict.infrastructure.ai_model_asset.AssetExtractor;
import com.agropredict.infrastructure.api_integration.DiagnosticApiService;
import com.agropredict.infrastructure.image_classification.BitmapCompressor;
import com.agropredict.infrastructure.image_classification.ImagePreprocessor;
import com.agropredict.infrastructure.image_classification.ImageValidator;
import com.agropredict.infrastructure.image_classification.InterpreterLoader;
import com.agropredict.infrastructure.image_classification.LabelCatalog;
import com.agropredict.infrastructure.image_classification.TFLiteClassifier;
import com.agropredict.infrastructure.image_classification.TFLiteModel;
import com.agropredict.infrastructure.persistence.AuditLogger;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.diagnostic_submission.AnswerArchive;
import com.agropredict.infrastructure.persistence.diagnostic_submission.DiagnosticArchive;
import com.agropredict.infrastructure.persistence.diagnostic_submission.DiagnosticWorkflow;
import com.agropredict.infrastructure.persistence.diagnostic_submission.Submission;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteCatalog;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SqlitePhotographRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteQuestionnaireRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteReportRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteUserRepository;
import com.agropredict.infrastructure.report_export.CsvReportService;
import com.agropredict.infrastructure.report_export.PdfReportService;
import com.agropredict.infrastructure.security.PasswordHasher;
import java.io.File;

public final class RepositoryFactory implements IRepositoryFactory {
    private final Database database;
    private final Context applicationContext;
    private final ISessionRepository sessionRepository;

    public RepositoryFactory(Database database, Context applicationContext) {
        this.database = database;
        this.applicationContext = applicationContext;
        this.sessionRepository = new SessionRepository(applicationContext);
    }

    @Override
    public IUserRepository createUserRepository() {
        return new SqliteUserRepository(database, createCatalog("occupation"));
    }

    @Override
    public ICropRepository createCropRepository() {
        return new SqliteCropRepository(database, sessionRepository);
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        return new SqliteDiagnosticRepository(database);
    }

    @Override
    public IReportRepository createReportRepository() {
        return new SqliteReportRepository(database);
    }

    @Override
    public ICatalogRepository createSoilTypeCatalog() {
        return createCatalog("soil_type");
    }

    @Override
    public ICatalogRepository createStageCatalog() {
        return createCatalog("phenological_stage");
    }

    @Override
    public ICatalogRepository createOccupationCatalog() {
        return createCatalog("occupation");
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return sessionRepository;
    }

    @Override
    public IImageClassifier createImageClassifier() {
        InterpreterLoader loader = new InterpreterLoader(applicationContext.getAssets());
        LabelCatalog labelCatalog = new LabelCatalog(applicationContext.getAssets());
        TFLiteModel model = new TFLiteModel(
                loader.load("models/cultivo_model.tflite"),
                labelCatalog.load("models/classes.json"));
        return new TFLiteClassifier(model, new ImageValidator(), new ImagePreprocessor());
    }

    @Override
    public IImageCompressor createImageCompressor() {
        return new BitmapCompressor(applicationContext);
    }

    @Override
    public IDiagnosticApiService createApiService() {
        return new DiagnosticApiService();
    }

    @Override
    public IReportService createReportService(String format) {
        File reportsDirectory = resolveReportsDirectory();
        return "csv".equals(format)
                ? new CsvReportService(reportsDirectory)
                : new PdfReportService(reportsDirectory);
    }

    @Override
    public IDiagnosticWorkflow createDiagnosticWorkflow() {
        IPhotographRepository photoRepo = new SqlitePhotographRepository(database, sessionRepository);
        Submission submission = new Submission(createCropRepository(), photoRepo);
        IQuestionnaireRepository questionnaireRepo = new SqliteQuestionnaireRepository(database);
        DiagnosticArchive diagnosticArchive = new DiagnosticArchive(createDiagnosticRepository());
        AnswerArchive answerArchive = new AnswerArchive(questionnaireRepo);
        return new DiagnosticWorkflow(submission, diagnosticArchive, answerArchive);
    }

    @Override
    public IAssetService createAssetService() {
        return new AssetExtractor(applicationContext);
    }

    @Override
    public IAuditLogger createAuditLogger() {
        return new AuditLogger(database);
    }

    @Override
    public IPasswordHasher createPasswordHasher() {
        return new PasswordHasher();
    }

    private ICatalogRepository createCatalog(String tableName) {
        return new SqliteCatalog(database, tableName);
    }

    private File resolveReportsDirectory() {
        File directory = new File(applicationContext.getExternalFilesDir(null), "reports");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException("Cannot create reports directory");
        }
        return directory;
    }
}
