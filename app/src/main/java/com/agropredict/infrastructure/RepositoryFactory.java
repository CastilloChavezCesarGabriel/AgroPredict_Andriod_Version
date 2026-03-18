package com.agropredict.infrastructure;

import android.content.Context;
import com.agropredict.application.IRepositoryFactory;
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
import com.agropredict.infrastructure.classifier.ImageClassifierService;
import com.agropredict.infrastructure.export.CsvReportGeneratorService;
import com.agropredict.infrastructure.export.PdfReportGeneratorService;
import com.agropredict.infrastructure.network.HttpDiagnosticApiService;
import com.agropredict.infrastructure.persistence.DatabaseHelper;
import com.agropredict.infrastructure.persistence.SharedPreferencesSessionRepository;
import com.agropredict.infrastructure.persistence.SqliteCatalog;
import com.agropredict.infrastructure.persistence.SqliteCropImageRepository;
import com.agropredict.infrastructure.persistence.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.SqliteReportRepository;
import com.agropredict.infrastructure.persistence.SqliteUserRepository;
import com.agropredict.infrastructure.validation.AndroidImageValidatorService;

import java.io.File;

public final class RepositoryFactory implements IRepositoryFactory {
    private final DatabaseHelper databaseHelper;
    private final Context applicationContext;

    public RepositoryFactory(DatabaseHelper databaseHelper, Context applicationContext) {
        this.databaseHelper = databaseHelper;
        this.applicationContext = applicationContext;
    }

    @Override
    public IUserRepository createUserRepository() {
        return new SqliteUserRepository(databaseHelper);
    }

    @Override
    public ICropRepository createCropRepository() {
        return new SqliteCropRepository(databaseHelper);
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        return new SqliteDiagnosticRepository(databaseHelper);
    }

    @Override
    public ICropImageRepository createCropImageRepository() {
        return new SqliteCropImageRepository(databaseHelper);
    }

    @Override
    public IReportRepository createReportRepository() {
        return new SqliteReportRepository(databaseHelper);
    }

    @Override
    public ICatalogRepository createSoilTypeCatalog() {
        return new SqliteCatalog(databaseHelper, "soil_type");
    }

    @Override
    public ICatalogRepository createStageCatalog() {
        return new SqliteCatalog(databaseHelper, "phenological_stage");
    }

    @Override
    public ICatalogRepository createOccupationCatalog() {
        return new SqliteCatalog(databaseHelper, "occupation");
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return new SharedPreferencesSessionRepository(applicationContext);
    }

    @Override
    public IImageClassifierService createClassifierService() {
        return new ImageClassifierService(applicationContext.getAssets());
    }

    @Override
    public IDiagnosticApiService createApiService() {
        return new HttpDiagnosticApiService();
    }

    @Override
    public IImageValidatorService createImageValidatorService() {
        return new AndroidImageValidatorService();
    }

    @Override
    public IReportGeneratorService createPdfReportGenerator() {
        return new PdfReportGeneratorService(resolveReportsDirectory());
    }

    @Override
    public IReportGeneratorService createCsvReportGenerator() {
        return new CsvReportGeneratorService(resolveReportsDirectory());
    }

    private File resolveReportsDirectory() {
        File directory = new File(applicationContext.getExternalFilesDir(null), "reports");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }
}