package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class SeedData {
    public void load(SQLiteDatabase database) {
        new Seed("soil_type",
                new String[]{"Clay", "Sandy", "Loam", "Silt", "Rocky"}).load(database);
        new Seed("phenological_stage",
                new String[]{"Germination", "Vegetative", "Flowering", "Fruiting", "Maturity"}).load(database);
        new Seed("occupation",
                new String[]{"Farmer", "Agronomist", "Technician", "Engineer", "Specialist", "Researcher", "Other"}).load(database);
    }
}