package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class SeedData {
    public void load(SQLiteDatabase database) {
        populate(database);
        register(database);
        fill(database);
    }

    private void populate(SQLiteDatabase database) {
        CatalogName.SOIL_TYPE.populate(database,
                new String[]{"Clay", "Sandy", "Loam", "Silt", "Rocky"});
        CatalogName.PHENOLOGICAL_STAGE.populate(database,
                new String[]{"Germination", "Vegetative", "Flowering", "Fruiting", "Maturity"});
        CatalogName.OCCUPATION.populate(database,
                new String[]{"Farmer", "Agronomist", "Technician", "Engineer", "Specialist", "Researcher", "Other"});
        CatalogName.PROBLEM_TYPE.populate(database,
                new String[]{"Disease", "Pest", "Nutrient deficiency", "Water stress", "Environmental damage", "Unknown"});
    }

    private void register(SQLiteDatabase database) {
        QuestionKey.TEMPERATURE.define(database, "What is the temperature?");
        QuestionKey.HUMIDITY.define(database, "What is the humidity?");
        QuestionKey.RAIN.define(database, "When did it last rain?");
        QuestionKey.SOIL_MOISTURE.define(database, "What is the soil moisture?");
        QuestionKey.PH.define(database, "What is the soil pH?");
        QuestionKey.IRRIGATION.define(database, "How often is the crop irrigated?");
        QuestionKey.FERTILIZATION.define(database, "When was it last fertilized?");
        QuestionKey.SPRAYING.define(database, "Has the crop been sprayed?");
        QuestionKey.WEEDS.define(database, "How much weed presence?");
        QuestionKey.SYMPTOM.define(database, "What symptoms are visible?");
        QuestionKey.SEVERITY.define(database, "How severe are the symptoms?");
        QuestionKey.INSECTS.define(database, "What insects are present?");
        QuestionKey.ANIMALS.define(database, "What animals cause damage?");
    }

    private void fill(SQLiteDatabase database) {
        QuestionKey.TEMPERATURE.fill(database,
                new String[]{"<15°C", "15–25°C", "26–32°C", ">32°C"});
        QuestionKey.HUMIDITY.fill(database,
                new String[]{"20–40%", "40–60%", "60–80%", ">80%", "Don't know"});
        QuestionKey.RAIN.fill(database,
                new String[]{"Today", "This week", "1 week ago", ">2 weeks ago", "No rain"});
        QuestionKey.SOIL_MOISTURE.fill(database,
                new String[]{"Very dry", "Dry", "Moderate", "Moist", "Waterlogged"});
        QuestionKey.PH.fill(database,
                new String[]{"<5.5", "5.5–7", "7–8", ">8", "Don't know"});
        QuestionKey.IRRIGATION.fill(database,
                new String[]{"Daily", "Every 2–3 days", "Weekly", "Very little", "No recent irrigation"});
        QuestionKey.FERTILIZATION.fill(database,
                new String[]{"<1 week", "1–2 weeks", "3 weeks", "Not fertilized", "Don't know"});
        QuestionKey.SPRAYING.fill(database,
                new String[]{"Yes (last 7 days)", "Yes (last 14 days)", "No", "Don't know"});
        QuestionKey.WEEDS.fill(database,
                new String[]{"Heavy", "Moderate", "Light", "None"});
        QuestionKey.SYMPTOM.fill(database,
                new String[]{"Yellow leaves", "Brown spots", "Dry tips", "Weak stems", "Visible fungi", "Insect presence", "Looks normal"});
        QuestionKey.SEVERITY.fill(database,
                new String[]{"Mild", "Moderate", "Severe", "Very severe"});
        QuestionKey.INSECTS.fill(database,
                new String[]{"Whitefly", "Aphid", "Leafminer", "Thrips", "Caterpillar", "None", "Don't know"});
        QuestionKey.ANIMALS.fill(database,
                new String[]{"Rodents", "Birds", "Large animals", "None", "Don't know"});
    }
}