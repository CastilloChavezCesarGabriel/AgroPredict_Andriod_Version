package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public enum QuestionKey {
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

    QuestionKey(String identifier) {
        this.identifier = identifier;
    }

    public void define(SQLiteDatabase database, String questionText) {
        new QuestionSeed(identifier, questionText).load(database);
    }

    public void fill(SQLiteDatabase database, String[] options) {
        new OptionSeed(identifier, options).load(database);
    }

    public void pair(IKeyConsumer consumer, String value) {
        consumer.accept(identifier, value);
    }
}