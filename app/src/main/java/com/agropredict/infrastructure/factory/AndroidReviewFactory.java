package com.agropredict.infrastructure.factory;

import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ISessionRepository;
import java.util.Objects;

public final class AndroidReviewFactory implements IReviewFactory {
    private final DiagnosticPersistence diagnosticPersistence;
    private final CropPersistence cropPersistence;
    private final SessionPersistence sessionPersistence;

    public AndroidReviewFactory(DiagnosticPersistence diagnosticPersistence,
                                CropPersistence cropPersistence,
                                SessionPersistence sessionPersistence) {
        this.diagnosticPersistence = Objects.requireNonNull(diagnosticPersistence,
                "android review factory requires a diagnostic persistence");
        this.cropPersistence = Objects.requireNonNull(cropPersistence,
                "android review factory requires a crop persistence");
        this.sessionPersistence = Objects.requireNonNull(sessionPersistence,
                "android review factory requires a session persistence");
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        return diagnosticPersistence.createDiagnosticRepository();
    }

    @Override
    public ICropRepository createCropRepository() {
        return cropPersistence.createCropRepository();
    }

    @Override
    public IPhotographRepository createPhotographRepository() {
        return cropPersistence.createPhotographRepository();
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return sessionPersistence.createSessionRepository();
    }
}