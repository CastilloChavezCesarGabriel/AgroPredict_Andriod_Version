package com.agropredict.core;

import android.app.Application;
import com.agropredict.application.diagnostic_submission.DiagnosticWorkflow;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.factory.IPredictionFactory;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRecord;
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
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.application.service.IReportService;
import com.google.android.material.color.DynamicColors;
import java.util.List;

public final class AgroPredictApplication extends Application implements
        IAccessFactory, IDashboardFactory, ICatalogFactory,
        IReviewFactory, IPredictionFactory, IReportingFactory {

    private Configuration configuration;

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
        configuration = new Configuration(this);
        configuration.backup();
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
        return configuration.createReview().createDiagnosticRepository();
    }

    @Override
    public IPhotographRepository createPhotographRepository() {
        return configuration.createReview().createPhotographRepository();
    }

    @Override
    public List<ICropRecord> createCropRecord() {
        return configuration.createReview().createCropRecord();
    }

    @Override
    public IImageClassifier createImageClassifier() {
        return configuration.createPrediction().createImageClassifier();
    }

    @Override
    public IImageCompressor createImageCompressor() {
        return configuration.createPrediction().createImageCompressor();
    }

    @Override
    public IDiagnosticApiService createApiService() {
        return configuration.createPrediction().createApiService();
    }

    @Override
    public DiagnosticWorkflow createDiagnosticWorkflow() {
        return configuration.createPrediction().createDiagnosticWorkflow();
    }

    @Override
    public IReportRepository createReportRepository() {
        return configuration.createReporting().createReportRepository();
    }

    @Override
    public IReportService createReportService(String format) {
        return configuration.createReporting().createReportService(format);
    }
}