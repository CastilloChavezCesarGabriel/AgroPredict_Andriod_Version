package com.agropredict.application.request.data;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Weather {
    private final String temperature;
    private final String humidity;
    private final String precipitation;

    public Weather(String temperature, String humidity, String precipitation) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitEnvironment(temperature, humidity);
        visitor.visitRain(precipitation);
    }
}