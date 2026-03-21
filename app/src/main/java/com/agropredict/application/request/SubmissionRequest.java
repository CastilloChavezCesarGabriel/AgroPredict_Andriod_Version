package com.agropredict.application.request;

import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.request.data.Questionnaire;
import com.agropredict.application.request.input.Submission;
import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.CropImage;
import com.agropredict.domain.entity.Diagnostic;

public final class SubmissionRequest {
    private final Submission diagnostic;
    private final Questionnaire questionnaire;

    public SubmissionRequest(Submission diagnostic, Questionnaire questionnaire) {
        this.diagnostic = diagnostic;
        this.questionnaire = questionnaire;
    }

    public Diagnostic diagnose() {
        return diagnostic.diagnose();
    }

    public Crop cultivate() {
        return diagnostic.cultivate();
    }

    public CropImage capture() {
        return diagnostic.capture();
    }

    public void record(IQuestionnaireRepository repository, String identifier) {
        repository.store(identifier, questionnaire);
    }

    public void accept(ISubmissionVisitor visitor) {
        diagnostic.accept(visitor);
        questionnaire.accept(visitor);
    }
}