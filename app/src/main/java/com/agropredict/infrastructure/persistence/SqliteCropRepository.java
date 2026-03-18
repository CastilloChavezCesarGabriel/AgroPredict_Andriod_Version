package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.value.crop.CropContent;
import com.agropredict.domain.value.crop.CropData;
import com.agropredict.domain.value.crop.CropDetail;
import com.agropredict.domain.value.crop.CropEnvironment;
import com.agropredict.domain.value.crop.CropLocation;
import com.agropredict.domain.value.crop.CropOwnership;
import com.agropredict.domain.value.crop.CropSoil;
import java.util.ArrayList;
import java.util.List;

public final class SqliteCropRepository implements ICropRepository {
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

    private final DatabaseHelper databaseHelper;

    public SqliteCropRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void store(Crop crop) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = record(crop);
        database.insert("crop", null, values);
    }

    @Override
    public void update(Crop crop) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = record(crop);
        String identifier = values.getAsString("id");
        database.update("crop", values, "id = ?", new String[]{identifier});
    }

    @Override
    public List<Crop> list(String userIdentifier) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String query = SELECT_CROP
                + "WHERE c.user_id = ? AND c.is_active = 1 ORDER BY c.created_at DESC";
        Cursor cursor = database.rawQuery(query, new String[]{userIdentifier});
        List<Crop> crops = extractCrops(cursor);
        cursor.close();
        return crops;
    }

    @Override
    public Crop load(String cropIdentifier) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String query = SELECT_CROP + "WHERE c.id = ?";
        Cursor cursor = database.rawQuery(query, new String[]{cropIdentifier});
        Crop crop = extractSingle(cursor);
        cursor.close();
        return crop;
    }

    private ContentValues record(Crop crop) {
        ContentValues values = new ContentValues();
        crop.accept(new CropRecorder(values));
        return values;
    }

    private List<Crop> extractCrops(Cursor cursor) {
        List<Crop> crops = new ArrayList<>();
        while (cursor.moveToNext()) {
            crops.add(build(cursor));
        }
        return crops;
    }

    private Crop extractSingle(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        return build(cursor);
    }

    private Crop build(Cursor cursor) {
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
}