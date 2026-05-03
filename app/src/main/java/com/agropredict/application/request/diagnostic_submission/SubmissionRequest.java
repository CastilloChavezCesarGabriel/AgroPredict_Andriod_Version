package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.diagnostic_submission.Allocation;
import com.agropredict.application.diagnostic_submission.Cropland;
import com.agropredict.application.diagnostic_submission.IDiagnosticWorkflow;
import com.agropredict.application.diagnostic_submission.StampedDiagnostic;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.request.ai_questionnaire.Questionnaire;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.domain.Identifier;
import com.agropredict.domain.entity.Diagnostic;

public final class SubmissionRequest {
    private final Submission submission;
    private final Questionnaire questionnaire;

    public SubmissionRequest(Submission submission, Questionnaire questionnaire) {
        this.submission = submission;
        this.questionnaire = questionnaire;
    }

    public String submit(IDiagnosticApiService apiService, IDiagnosticWorkflow workflow) {
        String identifier = Identifier.generate("diagnosis");
        Diagnostic diagnostic = submission.diagnose(identifier);
        Diagnostic enriched = apiService.submit(diagnostic, this);
        workflow.persist(this, new StampedDiagnostic(identifier, enriched));
        return identifier;
    }

    public void store(Cropland cropland, Allocation allocation) {
        submission.store(cropland, allocation);
    }

    public void record(IQuestionnaireRepository repository, String identifier) {
        repository.store(identifier, questionnaire);
    }

    public void accept(ISubmissionVisitor visitor) {
        submission.accept(visitor);
        questionnaire.accept(visitor);
    }
}
