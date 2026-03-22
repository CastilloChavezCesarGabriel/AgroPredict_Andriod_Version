package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageService;
import com.agropredict.application.service.IReportService;
import com.agropredict.infrastructure.persistence.Database;

public final class RepositoryFactory implements IRepositoryFactory {
    private final PersistenceFactory persistence;
    private final ServiceFactory services;

    public RepositoryFactory(Database database, Context applicationContext) {
        this.persistence = new PersistenceFactory(database);
        this.services = new ServiceFactory(applicationContext);
    }

    @Override
    public IUserRepository createUserRepository() {
        return persistence.createUserRepository();
    }

    @Override
    public ICropRepository createCropRepository() {
        return persistence.createCropRepository();
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        return persistence.createDiagnosticRepository();
    }

    @Override
    public ICropImageRepository createCropImageRepository() {
        return persistence.createCropImageRepository();
    }

    @Override
    public IReportRepository createReportRepository() {
        return persistence.createReportRepository();
    }

    @Override
    public ICatalogRepository createSoilTypeCatalog() {
        return persistence.createCatalog("soil_type");
    }

    @Override
    public ICatalogRepository createStageCatalog() {
        return persistence.createCatalog("phenological_stage");
    }

    @Override
    public ICatalogRepository createOccupationCatalog() {
        return persistence.createCatalog("occupation");
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return services.createSessionRepository();
    }

    @Override
    public IImageService createImageService() {
        return services.createImageService();
    }

    @Override
    public IDiagnosticApiService createApiService() {
        return services.createApiService();
    }

    @Override
    public IReportService createPdfReportGenerator() {
        return services.createPdfReportGenerator();
    }

    @Override
    public IReportService createCsvReportGenerator() {
        return services.createCsvReportGenerator();
    }

    @Override
    public IDiagnosticWorkflow createDiagnosticWorkflow() {
        return persistence.createDiagnosticWorkflow();
    }
}