package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Weather {
    private final String temperature;
    private final String humidity;
    private Rainfall rainfall;

    public Weather(String temperature, String humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public void rain(String precipitation) {
        this.rainfall = new Rainfall(precipitation);
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitEnvironment(temperature, humidity);
        if (rainfall != null) rainfall.accept(visitor);
    }
}