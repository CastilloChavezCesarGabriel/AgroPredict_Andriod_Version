package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.result.HistoryRecord;
import com.agropredict.application.result.HistoryTransition;
import com.agropredict.application.result.Modification;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.component.crop.CropContent;
import com.agropredict.domain.component.crop.CropData;
import com.agropredict.domain.component.crop.CropDetail;
import com.agropredict.domain.component.crop.CropEnvironment;
import com.agropredict.domain.component.crop.CropLocation;
import com.agropredict.domain.component.crop.CropOwnership;
import com.agropredict.domain.component.crop.CropSoil;
import com.agropredict.infrastructure.persistence.Database;
import com.agropredict.infrastructure.persistence.SqliteRow;
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
    private static final int COLUMN_IDENTIFIER = 0;
    private static final int COLUMN_CROP_TYPE = 1;
    private static final int COLUMN_FIELD_NAME = 2;
    private static final int COLUMN_LOCATION = 3;
    private static final int COLUMN_PLANTING_DATE = 4;
    private static final int COLUMN_AREA = 5;
    private static final int COLUMN_USER_ID = 6;
    private static final int COLUMN_SOIL_TYPE_ID = 7;
    private static final int COLUMN_STAGE_ID = 8;

    public SqliteCropRepository(Database database) {
        super(database, "crop");
    }

    @Override
    protected void persist(Crop crop, SqliteRow row) {
        crop.accept(new CropPersistenceVisitor(row));
    }

    @Override
    protected Crop restore(Cursor cursor) {
        CropDetail detail = new CropDetail(
                cursor.getString(COLUMN_CROP_TYPE), cursor.getString(COLUMN_FIELD_NAME));
        CropLocation location = new CropLocation(
                cursor.getString(COLUMN_LOCATION), cursor.getString(COLUMN_PLANTING_DATE));
        CropSoil soil = new CropSoil(
                cursor.getString(COLUMN_SOIL_TYPE_ID), cursor.getString(COLUMN_AREA));
        CropEnvironment environment = new CropEnvironment(location, soil);
        CropOwnership ownership = new CropOwnership(
                cursor.getString(COLUMN_USER_ID), cursor.getString(COLUMN_STAGE_ID));
        CropContent content = new CropContent(environment, ownership);
        CropData data = new CropData(detail, content);
        return Crop.create(cursor.getString(COLUMN_IDENTIFIER), data);
    }

    @Override
    public void update(Crop crop) {
        SqliteRow sqliteRow = write(crop);
        sqliteRow.overwrite("crop", "id");
    }

    @Override
    public List<Crop> list(String userIdentifier) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = SELECT_CROP
                + "WHERE c.user_id = ? AND c.is_active = 1 ORDER BY c.created_at DESC";
        Cursor cursor = database.rawQuery(query, new String[]{userIdentifier});
        List<Crop> crops = read(cursor);
        cursor.close();
        return crops;
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
        SQLiteDatabase database = this.database.getReadableDatabase();
        String query = SELECT_CROP + "WHERE c.id = ?";
        Cursor cursor = database.rawQuery(query, new String[]{cropIdentifier});
        Crop crop = cursor.moveToFirst() ? restore(cursor) : null;
        cursor.close();
        return crop;
    }
}