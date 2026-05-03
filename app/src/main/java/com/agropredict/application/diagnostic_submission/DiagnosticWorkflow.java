package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.Identifier;

public final class DiagnosticWorkflow implements IDiagnosticWorkflow {
    private final FieldRecorder fieldRecorder;
    private final Archival archival;

    public DiagnosticWorkflow(FieldRecorder fieldRecorder, Archival archival) {
        this.fieldRecorder = fieldRecorder;
        this.archival = archival;
    }

    @Override
    public void persist(SubmissionRequest request, StampedDiagnostic stamped) {
        Allocation allocation = new Allocation(
                Identifier.generate("crop"), Identifier.generate("image"));
        fieldRecorder.record(request, allocation);
        stamped.attribute(allocation);
        stamped.persist(archival, request);
    }
}