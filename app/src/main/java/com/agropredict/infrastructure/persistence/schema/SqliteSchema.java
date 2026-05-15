package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SqliteSchema {
    private final List<ITable> tables;

    public SqliteSchema() {
        List<ITable> ordered = new ArrayList<>();
        Collections.addAll(ordered, CatalogName.values());
        ordered.add(new UserTable());
        ordered.add(new CropTable());
        ordered.add(new ImageTable());
        ordered.add(new QuestionnaireTable());
        ordered.add(new DiagnosticTable());
        ordered.add(new ReportTable());
        ordered.add(new SupportTable());
        this.tables = List.copyOf(ordered);
    }

    public void create(SQLiteDatabase database) {
        for (ITable table : tables) table.create(database);
    }

    public void drop(SQLiteDatabase database) {
        for (int index = tables.size() - 1; index >= 0; index--) {
            tables.get(index).drop(database);
        }
    }
}