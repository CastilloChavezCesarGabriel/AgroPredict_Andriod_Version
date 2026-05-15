package com.agropredict.application.diagnostic_submission.workflow;

import com.agropredict.application.crop_management.usecase.RegisterCropUseCase;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class DiagnosticWorkflow {
    private final RegisterCropUseCase registerCropUseCase;
    private final IPhotographRepository photographRepository;
    private final DiagnosticArchive diagnosticArchive;

    public DiagnosticWorkflow(RegisterCropUseCase registerCropUseCase,
                              IPhotographRepository photographRepository,
                              DiagnosticArchive diagnosticArchive) {
        this.registerCropUseCase = Objects.requireNonNull(registerCropUseCase, 
            "diagnostic workflow requires a register crop use case");
        this.photographRepository = Objects.requireNonNull(photographRepository, 
            "diagnostic workflow requires a photograph repository");
        this.diagnosticArchive = Objects.requireNonNull(diagnosticArchive, 
            "diagnostic workflow requires a diagnostic archive");
    }

    public void persist(SubmissionRequest request, Diagnostic diagnostic) {
        request.establish(registerCropUseCase, cropIdentifier -> {
            Diagnostic linked = attach(cropIdentifier, request, diagnostic);
            diagnosticArchive.preserve(linked, request);
        });
    }

    private Diagnostic attach(String cropIdentifier, SubmissionRequest request, Diagnostic diagnostic) {
        SubmissionIdentity identity = SubmissionIdentity.bind(cropIdentifier);
        request.archive(photographRepository, identity);
        return identity.link(diagnostic);
    }
}