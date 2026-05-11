package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.AnswerKey;

public final class SeedLoader {
    private final SQLiteDatabase database;

    public SeedLoader(SQLiteDatabase database) {
        this.database = database;
    }

    public void load() {
        populate();
        register();
        enumerate();
    }

    private void populate() {
        CatalogName.SOIL_TYPE.populate(database,
                new String[]{"Clay", "Sandy", "Loam", "Silt", "Rocky"});
        CatalogName.PHENOLOGICAL_STAGE.populate(database,
                new String[]{"Germination", "Vegetative", "Flowering", "Fruiting", "Maturity"});
        CatalogName.OCCUPATION.populate(database,
                new String[]{"Farmer", "Agronomist", "Technician", "Engineer", "Specialist", "Researcher", "Other"});
        CatalogName.PROBLEM_TYPE.populate(database,
                new String[]{"Disease", "Pest", "Nutrient deficiency", "Water stress", "Environmental damage", "Unknown"});
    }

    private void register() {
        define(AnswerKey.TEMPERATURE, "What is the temperature?");
        define(AnswerKey.HUMIDITY, "What is the humidity?");
        define(AnswerKey.RAIN, "When did it last rain?");
        define(AnswerKey.SOIL_MOISTURE, "What is the soil moisture?");
        define(AnswerKey.PH, "What is the soil pH?");
        define(AnswerKey.IRRIGATION, "How often is the crop irrigated?");
        define(AnswerKey.FERTILIZATION, "When was it last fertilized?");
        define(AnswerKey.SPRAYING, "Has the crop been sprayed?");
        define(AnswerKey.WEEDS, "How much weed presence?");
        define(AnswerKey.SYMPTOM, "What symptoms are visible?");
        define(AnswerKey.SEVERITY, "How severe are the symptoms?");
        define(AnswerKey.INSECTS, "What insects are present?");
        define(AnswerKey.ANIMALS, "What animals cause damage?");
    }

    private void define(AnswerKey key, String text) {
        key.expose(identifier -> new QuestionSeed(identifier, text).load(database));
    }

    private void enumerate() {
        supply(AnswerKey.TEMPERATURE,
                new String[]{"<15°C", "15–25°C", "26–32°C", ">32°C"});
        supply(AnswerKey.HUMIDITY,
                new String[]{"20–40%", "40–60%", "60–80%", ">80%", "Don't know"});
        supply(AnswerKey.RAIN,
                new String[]{"Today", "This week", "1 week ago", ">2 weeks ago", "No rain"});
        supply(AnswerKey.SOIL_MOISTURE,
                new String[]{"Very dry", "Dry", "Moderate", "Moist", "Waterlogged"});
        supply(AnswerKey.PH,
                new String[]{"<5.5", "5.5–7", "7–8", ">8", "Don't know"});
        supply(AnswerKey.IRRIGATION,
                new String[]{"Daily", "Every 2–3 days", "Weekly", "Very little", "No recent irrigation"});
        supply(AnswerKey.FERTILIZATION,
                new String[]{"<1 week", "1–2 weeks", "3 weeks", "Not fertilized", "Don't know"});
        supply(AnswerKey.SPRAYING,
                new String[]{"Yes (last 7 days)", "Yes (last 14 days)", "No", "Don't know"});
        supply(AnswerKey.WEEDS,
                new String[]{"Heavy", "Moderate", "Light", "None"});
        supply(AnswerKey.SYMPTOM,
                new String[]{"Yellow leaves", "Brown spots", "Dry tips", "Weak stems", "Visible fungi", "Insect presence", "Looks normal"});
        supply(AnswerKey.SEVERITY,
                new String[]{"Mild", "Moderate", "Severe", "Very severe"});
        supply(AnswerKey.INSECTS,
                new String[]{"Whitefly", "Aphid", "Leafminer", "Thrips", "Caterpillar", "None", "Don't know"});
        supply(AnswerKey.ANIMALS,
                new String[]{"Rodents", "Birds", "Large animals", "None", "Don't know"});
    }

    private void supply(AnswerKey key, String[] options) {
        key.expose(identifier -> new OptionSeed(identifier, options).load(database));
    }
}
