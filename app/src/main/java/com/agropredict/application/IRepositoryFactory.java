package com.agropredict.application;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageService;
import com.agropredict.application.service.IReportService;

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
    IImageService createImageService();
    IDiagnosticApiService createApiService();
    IReportService createPdfReportGenerator();
    IReportService createCsvReportGenerator();
    IDiagnosticWorkflow createDiagnosticWorkflow();
}