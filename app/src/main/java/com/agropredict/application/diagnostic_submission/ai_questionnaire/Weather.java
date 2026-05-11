package com.agropredict.application.diagnostic_submission.ai_questionnaire;

import com.agropredict.application.IAnswerConsumer;
import java.util.Objects;

public final class Weather {
    private final String temperature;
    private final String humidity;
    private final Rainfall rainfall;

    public Weather(String temperature, String humidity, Rainfall rainfall) {
        this.temperature = Objects.requireNonNull(temperature, "weather requires a temperature answer");
        this.humidity = Objects.requireNonNull(humidity, "weather requires a humidity answer");
        this.rainfall = Objects.requireNonNull(rainfall, "weather requires a rainfall observation");
    }

    public void accept(IAnswerConsumer consumer) {
        AnswerKey.TEMPERATURE.pair(consumer, temperature);
        AnswerKey.HUMIDITY.pair(consumer, humidity);
        rainfall.accept(consumer);
    }
}