package com.agropredict.application.factory;

import com.agropredict.application.diagnostic_submission.workflow.DiagnosticWorkflow;

public interface IDiagnosticWorkflowFactory {
    DiagnosticWorkflow createDiagnosticWorkflow();
}