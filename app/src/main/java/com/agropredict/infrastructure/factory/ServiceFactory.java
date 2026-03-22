package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.service.IImageService;
import com.agropredict.application.service.IReportService;
import com.agropredict.infrastructure.export.CsvReportService;
import com.agropredict.infrastructure.export.PdfReportService;
import com.agropredict.infrastructure.image.ImageService;
import com.agropredict.infrastructure.network.DiagnosticApiService;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import java.io.File;

public final class ServiceFactory {
    private final Context applicationContext;

    public ServiceFactory(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ISessionRepository createSessionRepository() {
        return new SessionRepository(applicationContext);
    }

    public IImageService createImageService() {
        return new ImageService(applicationContext);
    }

    public IDiagnosticApiService createApiService() {
        return new DiagnosticApiService();
    }

    public IReportService createPdfReportGenerator() {
        return new PdfReportService(resolve());
    }

    public IReportService createCsvReportGenerator() {
        return new CsvReportService(resolve());
    }

    private File resolve() {
        File directory = new File(applicationContext.getExternalFilesDir(null), "reports");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException("Cannot create reports directory");
        }
        return directory;
    }
}