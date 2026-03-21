package com.agropredict.application.repository;

import com.agropredict.application.request.data.Questionnaire;

public interface IQuestionnaireRepository {
    void store(String diagnosticIdentifier, Questionnaire questionnaire);
}