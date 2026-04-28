package com.agropredict.application.usecase.diagnostic;

import static org.junit.Assert.assertFalse;

import com.agropredict.application.diagnostic_submission.IDiagnosticWorkflow;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.visitor.TestOperationResultVisitor;

import org.junit.Test;

public final class SubmitDiagnosticUseCaseTest {

    private IDiagnosticApiService fakeApi(boolean throwError) {
        return (diagnostic, request) -> {
            if (throwError) throw new RuntimeException("API down");
            return diagnostic;
        };
    }

    private IDiagnosticWorkflow fakeWorkflow() {
        return (request, enriched, identifier) -> {};
    }

    @Test
    public void testSubmitNullRequestFails() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new SubmitDiagnosticUseCase(fakeApi(false), fakeWorkflow())
            .submit(new SubmissionRequest(null, null)).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testSubmitApiFailure() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new SubmitDiagnosticUseCase(fakeApi(true), fakeWorkflow())
            .submit(new SubmissionRequest(null, null)).accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}