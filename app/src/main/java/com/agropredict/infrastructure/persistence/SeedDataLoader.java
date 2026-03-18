package com.agropredict.infrastructure.persistence;

import android.database.sqlite.SQLiteDatabase;

public final class SeedDataLoader {

    public void load(SQLiteDatabase database) {
        seedSoilTypes(database);
        seedPhenologicalStages(database);
        seedOccupations(database);
    }

    private void seedSoilTypes(SQLiteDatabase database) {
        String[] soilTypes = {"Arcilloso", "Arenoso", "Franco", "Limoso", "Pedregoso"};
        for (String soilType : soilTypes) {
            database.execSQL(
                "INSERT OR IGNORE INTO soil_type (name) VALUES (?)",
                new Object[]{soilType}
            );
        }
    }

    private void seedPhenologicalStages(SQLiteDatabase database) {
        String[] stages = {
            "Germinaci\u00f3n", "Vegetativo", "Floraci\u00f3n",
            "Fructificaci\u00f3n", "Madurez"
        };
        for (String stage : stages) {
            database.execSQL(
                "INSERT OR IGNORE INTO phenological_stage (name) VALUES (?)",
                new Object[]{stage}
            );
        }
    }

    private void seedOccupations(SQLiteDatabase database) {
        String[] occupations = {
            "Agricultor", "Agr\u00f3nomo", "Licenciado", "T\u00e9cnico",
            "Ingeniero", "Especialista", "Investigador", "Otro"
        };
        for (String occupation : occupations) {
            database.execSQL(
                "INSERT OR IGNORE INTO occupation (name) VALUES (?)",
                new Object[]{occupation}
            );
        }
    }
}
