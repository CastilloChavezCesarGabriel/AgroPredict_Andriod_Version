package com.agropredict.application.factory;

import com.agropredict.application.diagnostic_submission.IDiagnosticWorkflow;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;

public interface IPredictionFactory {
    IImageClassifier createImageClassifier();
    IImageCompressor createImageCompressor();
    IDiagnosticApiService createApiService();
    IDiagnosticWorkflow createDiagnosticWorkflow();
    ICatalogRepository createSoilTypeCatalog();
    ICatalogRepository createStageCatalog();
}