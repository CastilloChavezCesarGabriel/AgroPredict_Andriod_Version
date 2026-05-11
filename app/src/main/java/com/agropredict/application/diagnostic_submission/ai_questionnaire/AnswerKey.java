package com.agropredict.application.diagnostic_submission.ai_questionnaire;

import com.agropredict.application.IAnswerConsumer;
import com.agropredict.domain.identifier.IIdentifierConsumer;

public enum AnswerKey {
    TEMPERATURE("temperature"),
    HUMIDITY("humidity"),
    RAIN("rain"),
    SOIL_MOISTURE("soilMoisture"),
    PH("ph"),
    IRRIGATION("irrigation"),
    FERTILIZATION("fertilization"),
    SPRAYING("spraying"),
    WEEDS("weeds"),
    SYMPTOM("symptom"),
    SEVERITY("severity"),
    INSECTS("insects"),
    ANIMALS("animals");
    private final String identifier;

    AnswerKey(String identifier) {
        this.identifier = identifier;
    }

    public void pair(IAnswerConsumer consumer, String value) {
        consumer.record(identifier, value);
    }

    public void expose(IIdentifierConsumer consumer) {
        consumer.identify(identifier);
    }
}