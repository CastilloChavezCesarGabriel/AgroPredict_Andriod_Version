package com.agropredict.core;

import android.app.Application;
import com.agropredict.application.diagnostic_submission.IDiagnosticWorkflow;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.factory.IPredictionFactory;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
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
        return configuration.access().createUserRepository();
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return configuration.access().createSessionRepository();
    }

    @Override
    public IPasswordHasher createPasswordHasher() {
        return configuration.access().createPasswordHasher();
    }

    @Override
    public IAuditLogger createAuditLogger() {
        return configuration.access().createAuditLogger();
    }

    @Override
    public ICatalogRepository createOccupationCatalog() {
        return configuration.access().createOccupationCatalog();
    }

    @Override
    public IAssetService createAssetService() {
        return configuration.dashboard().createAssetService();
    }

    @Override
    public ICropRepository createCropRepository() {
        return configuration.catalog().createCropRepository();
    }

    @Override
    public ICatalogRepository createSoilTypeCatalog() {
        return configuration.catalog().createSoilTypeCatalog();
    }

    @Override
    public ICatalogRepository createStageCatalog() {
        return configuration.catalog().createStageCatalog();
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        return configuration.review().createDiagnosticRepository();
    }

    @Override
    public IImageClassifier createImageClassifier() {
        return configuration.prediction().createImageClassifier();
    }

    @Override
    public IImageCompressor createImageCompressor() {
        return configuration.prediction().createImageCompressor();
    }

    @Override
    public IDiagnosticApiService createApiService() {
        return configuration.prediction().createApiService();
    }

    @Override
    public IDiagnosticWorkflow createDiagnosticWorkflow() {
        return configuration.prediction().createDiagnosticWorkflow();
    }

    @Override
    public IReportRepository createReportRepository() {
        return configuration.reporting().createReportRepository();
    }

    @Override
    public IReportService createReportService(String format) {
        return configuration.reporting().createReportService(format);
    }
}