package com.agropredict.application.repository;

import com.agropredict.application.request.ai_questionnaire.Questionnaire;

public interface IQuestionnaireRepository {
    void store(String diagnosticIdentifier, Questionnaire questionnaire);
}