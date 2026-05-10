package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.application.request.CropUpdateRequest;
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
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowStore;
import com.agropredict.infrastructure.persistence.visitor.CropPersistenceVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final SqliteCropHistory history;

    public SqliteCropRepository(Database database, ISessionRepository sessionRepository) {
        this.database = database;
        this.store = new SqliteRowStore(database);
        this.sessionRepository = sessionRepository;
        this.history = new SqliteCropHistory(database);
    }

    @Override
    public void store(Crop crop) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        new CropPersistenceVisitor(row, sessionRepository.recall()).persist(crop);
        String now = Clock.read();
        row.record("created_at", now);
        row.record("updated_at", now);
        row.mark("is_active", 1);
        row.flush("crop");
    }

    @Override
    public void update(CropUpdateRequest request) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        CropPersistenceVisitor visitor = new CropPersistenceVisitor(row, sessionRepository.recall());
        request.identify(visitor);
        request.locate(visitor);
        request.analyze(visitor);
        request.track(visitor);
        String identifier = row.lookup("id");
        Map<String, String> previous = history.snapshot(identifier);
        row.record("updated_at", Clock.read());
        row.overwrite("crop", "id");
        history.track(identifier, previous);
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
        ChangeMoment timestamp = new ChangeMoment(cursor.getString(3));
        return new HistoryRecord(modification, transition, timestamp);
    }

    @Override
    public Crop find(String cropIdentifier) {
        return store.locate(SELECT_CROP + "WHERE c.id = ?", cropIdentifier, this::recover);
    }

    @Override
    public void erase(String cropIdentifier) {
        store.deactivate("crop", cropIdentifier);
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