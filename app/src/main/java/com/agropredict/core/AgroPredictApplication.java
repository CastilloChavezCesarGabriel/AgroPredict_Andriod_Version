package com.agropredict.core;

import android.app.Application;
import com.agropredict.application.factory.IImageRejectionFactory;
import com.agropredict.application.factory.ISeverityFactory;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticWorkflow;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.factory.IDiagnosticApiFactory;
import com.agropredict.application.factory.IDiagnosticWorkflowFactory;
import com.agropredict.application.factory.IImageClassificationFactory;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IAssetService;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.service.IImageValidator;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportServiceCatalog;
import com.agropredict.application.service.ReportFormat;
import com.agropredict.presentation.viewmodel.diagnostic_history.AndroidSeverityFactory;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.AndroidImageRejectionFactory;
import com.google.android.material.color.DynamicColors;

public final class AgroPredictApplication extends Application implements
        IAccessFactory, IDashboardFactory, ICatalogFactory,
        IReviewFactory, IImageClassificationFactory, IDiagnosticApiFactory,
        IDiagnosticWorkflowFactory, IReportingFactory, IReportServiceCatalog {
    private Configuration configuration;
    private IImageRejectionFactory imageRejectionFactory;
    private ISeverityFactory severityFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
        severityFactory = new AndroidSeverityFactory(this);
        configuration = new Configuration(this, severityFactory);
        imageRejectionFactory = new AndroidImageRejectionFactory(this);
        configuration.createBackup().backup();
    }

    @Override
    public IUserRepository createUserRepository() {
        return configuration.createAccess().createUserRepository();
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return configuration.createAccess().createSessionRepository();
    }

    @Override
    public IPasswordHasher createPasswordHasher() {
        return configuration.createAccess().createPasswordHasher();
    }

    @Override
    public IAuditLogger createAuditLogger() {
        return configuration.createAccess().createAuditLogger();
    }

    @Override
    public ICatalogRepository createOccupationCatalog() {
        return configuration.createAccess().createOccupationCatalog();
    }

    @Override
    public IAssetService createAssetService() {
        return configuration.createDashboard().createAssetService();
    }

    @Override
    public ICropRepository createCropRepository() {
        return configuration.createCatalog().createCropRepository();
    }

    @Override
    public ICatalogRepository createSoilTypeCatalog() {
        return configuration.createCatalog().createSoilTypeCatalog();
    }

    @Override
    public ICatalogRepository createStageCatalog() {
        return configuration.createCatalog().createStageCatalog();
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        return configuration.createReview(severityFactory).createDiagnosticRepository();
    }

    @Override
    public IPhotographRepository createPhotographRepository() {
        return configuration.createReview(severityFactory).createPhotographRepository();
    }

    @Override
    public IImageClassifier createImageClassifier() {
        return configuration.createImageClassification(imageRejectionFactory).createImageClassifier();
    }

    @Override
    public IImageCompressor createImageCompressor() {
        return configuration.createImageClassification(imageRejectionFactory).createImageCompressor();
    }

    @Override
    public IImageValidator createImageValidator() {
        return configuration.createImageClassification(imageRejectionFactory).createImageValidator();
    }

    @Override
    public IDiagnosticApiService createApiService() {
        return configuration.createDiagnosticApi().createApiService();
    }

    @Override
    public DiagnosticWorkflow createDiagnosticWorkflow() {
        return configuration.createDiagnosticWorkflow(severityFactory).createDiagnosticWorkflow();
    }

    @Override
    public IReportRepository createReportRepository() {
        return configuration.createReporting().createReportRepository();
    }

    @Override
    public IReportService select(ReportFormat format) {
        return configuration.createReportServiceCatalog().select(format);
    }
}
