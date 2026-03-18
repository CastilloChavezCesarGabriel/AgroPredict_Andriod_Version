package com.agropredict.application;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageClassifierService;
import com.agropredict.application.service.IImageValidatorService;
import com.agropredict.application.service.IReportGeneratorService;

public interface IRepositoryFactory {
    IUserRepository createUserRepository();
    ICropRepository createCropRepository();
    IDiagnosticRepository createDiagnosticRepository();
    ICropImageRepository createCropImageRepository();
    IReportRepository createReportRepository();
    ICatalogRepository createSoilTypeCatalog();
    ICatalogRepository createStageCatalog();
    ICatalogRepository createOccupationCatalog();
    ISessionRepository createSessionRepository();
    IImageClassifierService createClassifierService();
    IDiagnosticApiService createApiService();
    IImageValidatorService createImageValidatorService();
    IReportGeneratorService createPdfReportGenerator();
    IReportGeneratorService createCsvReportGenerator();
}