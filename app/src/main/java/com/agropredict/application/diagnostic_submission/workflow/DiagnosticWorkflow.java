package com.agropredict.application.diagnostic_submission.workflow;

import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.service.INotificationService;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class DiagnosticWorkflow {
    private final CropRegistry registry;
    private final DiagnosticArchive archive;
    private final INotificationService notificationService;

    public DiagnosticWorkflow(CropRegistry registry, DiagnosticArchive archive) {
        this(registry, archive, null);
    }

    public DiagnosticWorkflow(CropRegistry registry, DiagnosticArchive archive, INotificationService notificationService) {
        this.registry = Objects.requireNonNull(registry, "diagnostic workflow requires a crop registry");
        this.archive = Objects.requireNonNull(archive, "diagnostic workflow requires a diagnostic archive");
        this.notificationService = notificationService;
    }

    public void persist(SubmissionRequest request, Diagnostic diagnostic) {
        SubmissionIdentity identity = SubmissionIdentity.generate();
        request.store(registry, identity);
        identity.link(diagnostic);
        archive.preserve(diagnostic, request);
        alertIfSevere(diagnostic);
    }

    private void alertIfSevere(Diagnostic diagnostic) {
        if (notificationService == null) return;
        diagnostic.label((text, urgency) -> {
            if (urgency >= 2) notificationService.alertSevereDiagnosis();
        });
    }
}