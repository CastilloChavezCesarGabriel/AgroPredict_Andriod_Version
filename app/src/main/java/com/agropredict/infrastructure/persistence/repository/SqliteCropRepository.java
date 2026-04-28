package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.history.HistoryTransition;
import com.agropredict.domain.history.Modification;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.component.crop.CropProfile;
import com.agropredict.domain.component.crop.Field;
import com.agropredict.domain.component.crop.GrowthCycle;
import com.agropredict.domain.component.crop.Soil;
import com.agropredict.domain.entity.Crop;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.CropPersistenceVisitor;
import java.util.ArrayList;
import java.util.List;

public final class SqliteCropRepository extends SqliteRepository<Crop> implements ICropRepository {
    private static final String SELECT_CROP = "SELECT c.id, c.crop_type, c.field_name, "
            + "c.location, c.planting_date, c.area, c.user_id, "
            + "c.soil_type_id, c.phenological_stage_id, "
            + "st.name AS soil_type, ps.name AS stage "
            + "FROM crop c "
            + "LEFT JOIN soil_type st ON c.soil_type_id = st.id "
            + "LEFT JOIN phenological_stage ps ON c.phenological_stage_id = ps.id ";

    private final ISessionRepository sessionRepository;

    public SqliteCropRepository(Database database, ISessionRepository sessionRepository) {
        super(database, "crop");
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected void persist(Crop crop, SqliteRow row) {
        CropPersistenceVisitor visitor = new CropPersistenceVisitor(row, sessionRepository.recall());
        crop.accept(visitor);
    }

    @Override
    protected Crop restore(Cursor cursor) {
        Field field = new Field(
                cursor.getString(cursor.getColumnIndexOrThrow("field_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("location")));
        Soil soil = new Soil(
                cursor.getString(cursor.getColumnIndexOrThrow("soil_type_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("area")));
        GrowthCycle growth = new GrowthCycle(
                cursor.getString(cursor.getColumnIndexOrThrow("planting_date")),
                cursor.getString(cursor.getColumnIndexOrThrow("phenological_stage_id")));
        CropProfile profile = new CropProfile(field, soil, growth);
        return new Crop(
                cursor.getString(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("crop_type")),
                profile);
    }

    @Override
    public void update(CropUpdateRequest request) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        request.accept(new CropPersistenceVisitor(row, sessionRepository.recall()));
        row.overwrite("crop", "id");
    }

    @Override
    public List<Crop> list(String userIdentifier) {
        return fetch(SELECT_CROP
                + "WHERE c.user_id = ? AND c.is_active = 1 ORDER BY c.created_at DESC", new String[]{userIdentifier});
    }

    @Override
    public List<HistoryRecord> trace(String cropIdentifier) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = "SELECT field_modified, old_value, new_value, modified_at "
                + "FROM crop_history WHERE crop_id = ? ORDER BY modified_at DESC";
        Cursor cursor = database.rawQuery(query, new String[]{cropIdentifier});
        List<HistoryRecord> records = new ArrayList<>();
        while (cursor.moveToNext()) {
            records.add(recover(cursor));
        }
        cursor.close();
        return records;
    }

    private HistoryRecord recover(Cursor cursor) {
        Modification modification = new Modification(cursor.getString(0), cursor.getString(3));
        HistoryTransition transition = new HistoryTransition(cursor.getString(1), cursor.getString(2));
        return new HistoryRecord(modification, transition);
    }

    @Override
    public Crop find(String cropIdentifier) {
        return locate(SELECT_CROP + "WHERE c.id = ?", cropIdentifier);
    }

    @Override
    public void delete(String cropIdentifier) {
        deactivate(cropIdentifier);
    }
}