package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public final class StampedDiagnostic {
    private final String identifier;
    private final Diagnostic diagnostic;

    public StampedDiagnostic(String identifier, Diagnostic diagnostic) {
        this.identifier = identifier;
        this.diagnostic = diagnostic;
    }

    public void attribute(Allocation allocation) {
        allocation.expose(diagnostic::attribute);
    }

    public void persist(Archival archival, SubmissionRequest request) {
        archival.archive(diagnostic);
        archival.record(request, identifier);
    }
}
