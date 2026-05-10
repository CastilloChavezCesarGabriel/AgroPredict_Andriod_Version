package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class DiagnosticWorkflow {
    private final CropRegistry registry;
    private final DiagnosticArchive archive;

    public DiagnosticWorkflow(CropRegistry registry, DiagnosticArchive archive) {
        this.registry = Objects.requireNonNull(registry, "diagnostic workflow requires a crop registry");
        this.archive = Objects.requireNonNull(archive, "diagnostic workflow requires a diagnostic archive");
    }

    public void persist(SubmissionRequest request, Diagnostic diagnostic) {
        SubmissionIdentity identity = SubmissionIdentity.generate();
        request.store(registry, identity);
        identity.link(diagnostic);
        archive.preserve(diagnostic, request);
    }
}