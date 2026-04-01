package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class SeedData {
    public void load(SQLiteDatabase database) {
        populate(database);
        register(database);
        fill(database);
    }

    private void populate(SQLiteDatabase database) {
        new Seed("soil_type",
                new String[]{"Clay", "Sandy", "Loam", "Silt", "Rocky"}).load(database);
        new Seed("phenological_stage",
                new String[]{"Germination", "Vegetative", "Flowering", "Fruiting", "Maturity"}).load(database);
        new Seed("occupation",
                new String[]{"Farmer", "Agronomist", "Technician", "Engineer", "Specialist", "Researcher", "Other"}).load(database);
        new Seed("catalog_problem_type",
                new String[]{"Disease", "Pest", "Nutrient deficiency", "Water stress", "Environmental damage", "Unknown"}).load(database);
    }

    private void register(SQLiteDatabase database) {
        new QuestionSeed("temperature", "What is the temperature?").load(database);
        new QuestionSeed("humidity", "What is the humidity?").load(database);
        new QuestionSeed("rain", "When did it last rain?").load(database);
        new QuestionSeed("soilMoisture", "What is the soil moisture?").load(database);
        new QuestionSeed("ph", "What is the soil pH?").load(database);
        new QuestionSeed("irrigation", "How often is the crop irrigated?").load(database);
        new QuestionSeed("fertilization", "When was it last fertilized?").load(database);
        new QuestionSeed("spraying", "Has the crop been sprayed?").load(database);
        new QuestionSeed("weeds", "How much weed presence?").load(database);
        new QuestionSeed("symptom", "What symptoms are visible?").load(database);
        new QuestionSeed("severity", "How severe are the symptoms?").load(database);
        new QuestionSeed("insects", "What insects are present?").load(database);
        new QuestionSeed("animals", "What animals cause damage?").load(database);
    }

    private void fill(SQLiteDatabase database) {
        new OptionSeed("temperature",
                new String[]{"<15°C", "15–25°C", "26–32°C", ">32°C"}).load(database);
        new OptionSeed("humidity",
                new String[]{"20–40%", "40–60%", "60–80%", ">80%", "Don't know"}).load(database);
        new OptionSeed("rain",
                new String[]{"Today", "This week", "1 week ago", ">2 weeks ago", "No rain"}).load(database);
        new OptionSeed("soilMoisture",
                new String[]{"Very dry", "Dry", "Moderate", "Moist", "Waterlogged"}).load(database);
        new OptionSeed("ph",
                new String[]{"<5.5", "5.5–7", "7–8", ">8", "Don't know"}).load(database);
        new OptionSeed("irrigation",
                new String[]{"Daily", "Every 2–3 days", "Weekly", "Very little", "No recent irrigation"}).load(database);
        new OptionSeed("fertilization",
                new String[]{"<1 week", "1–2 weeks", "3 weeks", "Not fertilized", "Don't know"}).load(database);
        new OptionSeed("spraying",
                new String[]{"Yes (last 7 days)", "Yes (last 14 days)", "No", "Don't know"}).load(database);
        new OptionSeed("weeds",
                new String[]{"Heavy", "Moderate", "Light", "None"}).load(database);
        new OptionSeed("symptom",
                new String[]{"Yellow leaves", "Brown spots", "Dry tips", "Weak stems", "Visible fungi", "Insect presence", "Looks normal"}).load(database);
        new OptionSeed("severity",
                new String[]{"Mild", "Moderate", "Severe", "Very severe"}).load(database);
        new OptionSeed("insects",
                new String[]{"Whitefly", "Aphid", "Leafminer", "Thrips", "Caterpillar", "None", "Don't know"}).load(database);
        new OptionSeed("animals",
                new String[]{"Rodents", "Birds", "Large animals", "None", "Don't know"}).load(database);
    }
}