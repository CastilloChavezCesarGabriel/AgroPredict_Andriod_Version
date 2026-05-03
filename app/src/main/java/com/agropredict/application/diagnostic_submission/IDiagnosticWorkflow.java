package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;

public interface IDiagnosticWorkflow {
    void persist(SubmissionRequest request, StampedDiagnostic stamped);
}