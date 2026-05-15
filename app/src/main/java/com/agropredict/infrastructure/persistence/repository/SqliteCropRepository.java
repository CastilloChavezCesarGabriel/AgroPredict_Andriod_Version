package com.agropredict.infrastructure.persistence.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.application.crop_management.request.CropUpdateRequest;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.history.HistoryTransition;
import com.agropredict.domain.history.FieldModification;
import com.agropredict.domain.history.ChangeMoment;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.Field;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.crop.Soil;
import com.agropredict.domain.crop.Crop;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.database.SqliteRowStore;
import com.agropredict.infrastructure.persistence.visitor.CropPersistenceVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SqliteCropRepository implements ICropRepository, IRecordEraser {
    private static final String SELECT_CROP = "SELECT c.id, c.crop_type, c.field_name, "
            + "c.location, c.planting_date, c.area, c.user_id, "
            + "c.soil_type_id, c.phenological_stage_id, "
            + "st.name AS soil_type, ps.name AS stage "
            + "FROM crop c "
            + "LEFT JOIN soil_type st ON c.soil_type_id = st.id "
            + "LEFT JOIN phenological_stage ps ON c.phenological_stage_id = ps.id ";

    private final Database database;
    private final SqliteRowStore store;
    private final ISessionRepository sessionRepository;
    private final SqliteRowFactory rowFactory;
    private final SqliteCropHistory history;

    public SqliteCropRepository(Database database, ISessionRepository sessionRepository, SqliteRowFactory rowFactory) {
        this.database = Objects.requireNonNull(database, "crop repository requires a database");
        this.sessionRepository = Objects.requireNonNull(sessionRepository, "crop repository requires a session repository");
        this.rowFactory = Objects.requireNonNull(rowFactory, "crop repository requires a row factory");
        this.store = new SqliteRowStore(database);
        this.history = new SqliteCropHistory(database, rowFactory);
    }

    @Override
    public void store(Crop crop) {
        SqliteRow row = rowFactory.open();
        new CropPersistenceVisitor(row, sessionRepository.recall()).persist(crop);
        row.stamp("created_at");
        row.stamp("updated_at");
        row.mark("is_active", 1);
        row.flush("crop");
    }

    @Override
    public void update(CropUpdateRequest request) {
        SqliteRow row = rowFactory.open();
        CropPersistenceVisitor visitor = new CropPersistenceVisitor(row, sessionRepository.recall());
        request.identify(visitor);
        request.locate(visitor);
        request.analyze(visitor);
        request.track(visitor);
        request.identify(identifier -> {
            Map<String, String> previous = history.snapshot(identifier);
            row.stamp("updated_at");
            row.overwrite("crop", "id");
            history.track(identifier, previous);
        });
    }

    @Override
    public List<Crop> list(String userIdentifier) {
        return store.fetch(SELECT_CROP
                + "WHERE c.user_id = ? AND c.is_active = 1 ORDER BY c.created_at DESC",
                new String[]{userIdentifier}, this::recover);
    }

    @Override
    public List<HistoryRecord> trace(String cropIdentifier) {
        SQLiteDatabase readable = database.getReadableDatabase();
        String query = "SELECT field_modified, old_value, new_value, modified_at "
                + "FROM crop_history WHERE crop_id = ? ORDER BY modified_at DESC";
        Cursor cursor = readable.rawQuery(query, new String[]{cropIdentifier});
        List<HistoryRecord> records = new ArrayList<>();
        while (cursor.moveToNext()) {
            records.add(rebuild(cursor));
        }
        cursor.close();
        return records;
    }

    private HistoryRecord rebuild(Cursor cursor) {
        FieldModification modification = new FieldModification(cursor.getString(0));
        HistoryTransition transition = new HistoryTransition(cursor.getString(1), cursor.getString(2));
        ChangeMoment moment = new ChangeMoment(cursor.getString(3));
        return new HistoryRecord(modification, transition, moment);
    }

    @Override
    public Crop find(String cropIdentifier) {
        return store.locate(SELECT_CROP + "WHERE c.id = ?", cropIdentifier, this::recover);
    }

    @Override
    public void erase(String cropIdentifier) {
        SQLiteDatabase writable = database.getWritableDatabase();
        ContentValues deactivation = new ContentValues();
        deactivation.put("is_active", 0);
        String[] cropArgs = new String[]{cropIdentifier};
        writable.beginTransaction();
        try {
            writable.update("diagnostic", deactivation, "crop_id = ?", cropArgs);
            writable.update("image", deactivation, "crop_id = ?", cropArgs);
            writable.update("report", deactivation, "crop_id = ?", cropArgs);
            writable.update("crop", deactivation, "id = ?", cropArgs);
            writable.setTransactionSuccessful();
        } finally {
            writable.endTransaction();
        }
    }

    private Crop recover(Cursor cursor) {
        Field field = new Field(
                cursor.getString(cursor.getColumnIndexOrThrow("field_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("location")));
        Soil soil = new Soil(
                cursor.getString(cursor.getColumnIndexOrThrow("soil_type_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("area")));
        GrowthCycle cycle = new GrowthCycle(
                cursor.getString(cursor.getColumnIndexOrThrow("planting_date")),
                cursor.getString(cursor.getColumnIndexOrThrow("phenological_stage_id")));
        return new Crop(
                cursor.getString(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("crop_type")),
                new CropProfile(new Plot(field, soil), cycle));
    }
}
