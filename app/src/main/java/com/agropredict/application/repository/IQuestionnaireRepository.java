package com.agropredict.application.repository;

import com.agropredict.application.diagnostic_submission.ai_questionnaire.Questionnaire;

public interface IQuestionnaireRepository {
    void store(String diagnosticIdentifier, Questionnaire questionnaire);
}