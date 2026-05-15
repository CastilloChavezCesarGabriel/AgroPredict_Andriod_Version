package com.agropredict.infrastructure.factory;

import com.agropredict.application.crop_management.usecase.RegisterCropUseCase;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticArchive;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticWorkflow;
import com.agropredict.application.factory.IDiagnosticWorkflowFactory;
import java.util.Objects;

public final class AndroidDiagnosticWorkflow implements IDiagnosticWorkflowFactory {
    private final CropPersistence cropPersistence;
    private final DiagnosticPersistence diagnosticPersistence;

    public AndroidDiagnosticWorkflow(CropPersistence cropPersistence,
                                     DiagnosticPersistence diagnosticPersistence) {
        this.cropPersistence = Objects.requireNonNull(cropPersistence,
                "android diagnostic workflow requires a crop persistence");
        this.diagnosticPersistence = Objects.requireNonNull(diagnosticPersistence,
                "android diagnostic workflow requires a diagnostic persistence");
    }

    @Override
    public DiagnosticWorkflow createDiagnosticWorkflow() {
        RegisterCropUseCase registerCropUseCase = new RegisterCropUseCase(cropPersistence.createCropRepository());
        DiagnosticArchive archive = new DiagnosticArchive(
                diagnosticPersistence.createDiagnosticRepository(),
                diagnosticPersistence.createQuestionnaireRepository());
        return new DiagnosticWorkflow(registerCropUseCase, cropPersistence.createPhotographRepository(), archive);
    }
}
