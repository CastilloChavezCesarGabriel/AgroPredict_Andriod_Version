package com.agropredict.application.diagnostic_submission.request;

import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticWorkflow;
import com.agropredict.application.diagnostic_submission.workflow.SubmissionIdentity;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Questionnaire;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.IAnswerConsumer;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class SubmissionRequest {
    private final Submission submission;
    private final Questionnaire questionnaire;

    public SubmissionRequest(Submission submission, Questionnaire questionnaire) {
        this.submission = Objects.requireNonNull(submission, "submission request requires a submission");
        this.questionnaire = Objects.requireNonNull(questionnaire, "submission request requires a questionnaire");
    }

    public String submit(IDiagnosticApiService apiService, DiagnosticWorkflow workflow) {
        String identifier = IdentifierFactory.generate("diagnosis");
        Diagnostic diagnostic = submission.diagnose(identifier);
        Diagnostic enriched = apiService.submit(diagnostic, this);
        workflow.persist(this, enriched);
        return identifier;
    }

    public void store(CropRegistry registry, SubmissionIdentity identity) {
        submission.store(registry, identity);
    }

    public void record(IQuestionnaireRepository repository, String identifier) {
        repository.store(identifier, questionnaire);
    }

    public void dispatch(IPredictionConsumer consumer) {
        submission.dispatch(consumer);
    }

    public void accept(IAnswerConsumer consumer) {
        questionnaire.accept(consumer);
    }
}