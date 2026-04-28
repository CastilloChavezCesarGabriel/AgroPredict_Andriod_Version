package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;

public final class AnswerArchive {
    private final IQuestionnaireRepository questionnaireRepository;

    public AnswerArchive(IQuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    public void archive(SubmissionRequest request, String identifier) {
        request.record(questionnaireRepository, identifier);
    }
}