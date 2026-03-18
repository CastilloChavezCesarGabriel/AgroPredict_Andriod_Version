package com.agropredict.infrastructure.persistence;

import android.database.sqlite.SQLiteDatabase;

public final class SchemaCreator {

    public void create(SQLiteDatabase database) {
        createCatalogs(database);
        createUserTable(database);
        createCropTable(database);
        createImageTable(database);
        createDiagnosticTable(database);
        createReportTable(database);
        createSupportTables(database);
        createDiagnosticSummaryView(database);
    }

    private void createCatalogs(SQLiteDatabase database) {
        createOccupationTable(database);
        createSoilTypeTable(database);
        createPhenologicalStageTable(database);
        createProblemTypeTable(database);
        createSupportResourceTable(database);
    }

    private void createOccupationTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS occupation ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "name TEXT NOT NULL UNIQUE)"
        );
    }

    private void createSoilTypeTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS soil_type ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "name TEXT NOT NULL UNIQUE)"
        );
    }

    private void createPhenologicalStageTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS phenological_stage ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "name TEXT NOT NULL UNIQUE)"
        );
    }

    private void createProblemTypeTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS catalog_problem_type ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "name TEXT NOT NULL UNIQUE)"
        );
    }

    private void createSupportResourceTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS support_resource ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "title TEXT NOT NULL, "
            + "resource_type TEXT NOT NULL CHECK (resource_type IN ('pdf', 'link')), "
            + "path_or_url TEXT NOT NULL, "
            + "description TEXT, "
            + "position INTEGER DEFAULT 0)"
        );
    }

    private void createUserTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS user ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "occupation_id TEXT REFERENCES occupation(id) ON DELETE SET NULL, "
            + "full_name TEXT NOT NULL, "
            + "username TEXT NOT NULL UNIQUE, "
            + "email TEXT NOT NULL UNIQUE, "
            + "password_hash TEXT NOT NULL, "
            + "phone_number TEXT, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "updated_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "is_active INTEGER DEFAULT 1 CHECK (is_active IN (0,1)))"
        );
        database.execSQL(
            "CREATE TRIGGER IF NOT EXISTS user_updated "
            + "AFTER UPDATE ON user FOR EACH ROW BEGIN "
            + "UPDATE user SET updated_at = CURRENT_TIMESTAMP WHERE id = NEW.id; END"
        );
    }

    private void createCropTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS crop ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "user_id TEXT NOT NULL REFERENCES user(id) ON DELETE CASCADE, "
            + "soil_type_id TEXT REFERENCES soil_type(id) ON DELETE SET NULL, "
            + "phenological_stage_id TEXT REFERENCES phenological_stage(id) ON DELETE SET NULL, "
            + "field_name TEXT, "
            + "crop_type TEXT NOT NULL, "
            + "location TEXT, "
            + "area REAL, "
            + "planting_date TEXT, "
            + "is_active INTEGER DEFAULT 1 CHECK (is_active IN (0,1)), "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "updated_at TEXT DEFAULT CURRENT_TIMESTAMP)"
        );
        database.execSQL(
            "CREATE TRIGGER IF NOT EXISTS crop_updated "
            + "AFTER UPDATE ON crop FOR EACH ROW BEGIN "
            + "UPDATE crop SET updated_at = CURRENT_TIMESTAMP WHERE id = NEW.id; END"
        );
        database.execSQL("CREATE INDEX IF NOT EXISTS index_crop_user ON crop(user_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_crop_type ON crop(crop_type)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS crop_history ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "crop_id TEXT NOT NULL REFERENCES crop(id) ON DELETE CASCADE, "
            + "field_modified TEXT, "
            + "old_value TEXT, "
            + "new_value TEXT, "
            + "modified_at TEXT DEFAULT CURRENT_TIMESTAMP)"
        );
    }

    private void createImageTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS image ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "crop_id TEXT REFERENCES crop(id) ON DELETE SET NULL, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "file_path TEXT NOT NULL, "
            + "format TEXT CHECK (format IN ('jpg','png')), "
            + "size_in_megabytes REAL, "
            + "captured_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)"
        );
        database.execSQL("CREATE INDEX IF NOT EXISTS index_image_crop ON image(crop_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_image_user ON image(user_id)");
    }

    private void createDiagnosticTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS diagnostic ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "crop_id TEXT REFERENCES crop(id) ON DELETE SET NULL, "
            + "image_id TEXT REFERENCES image(id) ON DELETE SET NULL, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "problem_type_id TEXT REFERENCES catalog_problem_type(id) ON DELETE SET NULL, "
            + "predicted_crop TEXT, "
            + "confidence REAL, "
            + "severity TEXT CHECK (severity IN ('low','moderate','high')), "
            + "temperature REAL, "
            + "humidity REAL, "
            + "recommendation_text TEXT, "
            + "short_summary TEXT, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)"
        );
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_crop ON diagnostic(crop_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_user ON diagnostic(user_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_created ON diagnostic(created_at)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ai_question ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "key TEXT NOT NULL UNIQUE, "
            + "text TEXT NOT NULL, "
            + "position INTEGER DEFAULT 0, "
            + "answer_type TEXT NOT NULL DEFAULT 'single' "
            + "CHECK (answer_type IN ('single','multiple','numeric','date')))"
        );
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ai_option ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "question_id TEXT NOT NULL REFERENCES ai_question(id) ON DELETE CASCADE, "
            + "option_text TEXT NOT NULL, "
            + "option_value TEXT)"
        );
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ai_user_response ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "diagnostic_id TEXT NOT NULL REFERENCES diagnostic(id) ON DELETE CASCADE, "
            + "question_id TEXT NOT NULL REFERENCES ai_question(id) ON DELETE CASCADE, "
            + "option_id TEXT REFERENCES ai_option(id) ON DELETE SET NULL, "
            + "text_value TEXT, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)"
        );
    }

    private void createReportTable(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS report ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "diagnostic_id TEXT REFERENCES diagnostic(id) ON DELETE SET NULL, "
            + "crop_id TEXT REFERENCES crop(id) ON DELETE SET NULL, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "format TEXT NOT NULL CHECK (format IN ('pdf','csv')), "
            + "file_path TEXT, "
            + "generated_at TEXT DEFAULT CURRENT_TIMESTAMP)"
        );
        database.execSQL("CREATE INDEX IF NOT EXISTS index_report_user ON report(user_id)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS report_sharing ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "report_id TEXT NOT NULL REFERENCES report(id) ON DELETE CASCADE, "
            + "qr_code TEXT NOT NULL UNIQUE, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "expiration TEXT)"
        );
    }

    private void createSupportTables(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS sync_pending ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "table_name TEXT NOT NULL, "
            + "operation TEXT NOT NULL CHECK (operation IN ('INSERT','UPDATE','DELETE')), "
            + "json_data TEXT NOT NULL, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)"
        );
        database.execSQL("CREATE INDEX IF NOT EXISTS index_sync_user ON sync_pending(user_id)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS log_entry ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "action TEXT NOT NULL, "
            + "description TEXT, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)"
        );
        database.execSQL("CREATE INDEX IF NOT EXISTS index_log_user ON log_entry(user_id)");
    }

    private void createDiagnosticSummaryView(SQLiteDatabase database) {
        database.execSQL(
            "CREATE VIEW IF NOT EXISTS diagnostic_summary AS "
            + "SELECT diagnostic.id AS diagnostic_id, diagnostic.crop_id, "
            + "crop.crop_type, diagnostic.predicted_crop, diagnostic.confidence, "
            + "diagnostic.short_summary, diagnostic.created_at, "
            + "user.id AS user_id, user.username "
            + "FROM diagnostic "
            + "LEFT JOIN crop ON diagnostic.crop_id = crop.id "
            + "LEFT JOIN user ON diagnostic.user_id = user.id "
            + "ORDER BY diagnostic.created_at DESC"
        );
    }
}
