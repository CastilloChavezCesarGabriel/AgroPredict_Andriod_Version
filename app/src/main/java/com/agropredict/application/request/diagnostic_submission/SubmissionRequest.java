package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.diagnostic_submission.IDiagnosticWorkflow;
import com.agropredict.application.repository.IPhotographRepository;
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
        workflow.persist(this, enriched, identifier);
        return identifier;
    }

    public void store(ICropRepository cropRepository, IPhotographRepository photoRepository, ICatalogRepository stageCatalog) {
        submission.store(cropRepository, photoRepository, stageCatalog);
    }

    public void record(IQuestionnaireRepository repository, String identifier) {
        repository.store(identifier, questionnaire);
    }

    public void accept(ISubmissionVisitor visitor) {
        submission.accept(visitor);
        questionnaire.accept(visitor);
    }
}