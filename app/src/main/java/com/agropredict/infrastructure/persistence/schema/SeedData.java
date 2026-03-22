package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class SeedData {
    public void load(SQLiteDatabase database) {
        new Seed("soil_type",
                new String[]{"Arcilloso", "Arenoso", "Franco", "Limoso", "Pedregoso"}).load(database);
        new Seed("phenological_stage",
                new String[]{"Germinación", "Vegetativo", "Floración", "Fructificación", "Madurez"}).load(database);
        new Seed("occupation",
                new String[]{"Agricultor", "Agrónomo", "Licenciado", "Técnico", "Ingeniero", "Especialista", "Investigador", "Otro"}).load(database);
    }
}