package com.agropredict.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import com.agropredict.application.diagnostic_submission.workflow.CropDossier;
import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.diagnostic_submission.request.Cultivation;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.domain.authentication.Session;
import com.agropredict.domain.crop.Crop;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqlitePhotographRepository;
import com.agropredict.infrastructure.persistence.schema.CatalogName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public final class RealDatabaseTest {
    private Database database;
    private Context context;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase("agro_diagnostic.db");
        database = new Database(context);
        database.getWritableDatabase();
    }

    @After
    public void teardown() {
        if (database != null) database.close();
        context.deleteDatabase("agro_diagnostic.db");
    }

    private String enroll(SQLiteDatabase raw, String username) {
        String identifier = IdentifierFactory.generate("user");
        String now = "2026-01-01 00:00:00";
        ContentValues user = new ContentValues();
        user.put("id", identifier);
        user.put("full_name", "Test User");
        user.put("username", username);
        user.put("email", username + "@example.com");
        user.put("password_hash", "abcd:efgh");
        user.put("created_at", now);
        user.put("updated_at", now);
        user.put("is_active", 1);
        long result = raw.insertOrThrow("user", null, user);
        assertFalse("user insert returned -1", result < 0);
        return identifier;
    }

    @Test
    public void testForeignKeysPragmaIsEnabled() {
        SQLiteDatabase raw = database.getReadableDatabase();
        Cursor cursor = raw.rawQuery("PRAGMA foreign_keys", null);
        assertTrue(cursor.moveToFirst());
        assertEquals(1, cursor.getInt(0));
        cursor.close();
    }

    @Test
    public void testCatalogSeedPopulatesPhenologicalStageRows() {
        ICatalogRepository stageCatalog = CatalogName.PHENOLOGICAL_STAGE.open(database);
        assertEquals(5, stageCatalog.list().size());
        assertNotNull(stageCatalog.resolve("Vegetative"));
        assertNotNull(stageCatalog.resolve("Flowering"));
        assertNotNull(stageCatalog.resolve("Maturity"));
        assertNull(stageCatalog.resolve("MysteryStage"));
    }

    @Test
    public void testCatalogSeedPopulatesSoilTypeRows() {
        ICatalogRepository soilCatalog = CatalogName.SOIL_TYPE.open(database);
        assertEquals(5, soilCatalog.list().size());
        assertNotNull(soilCatalog.resolve("Clay"));
        assertNull(soilCatalog.resolve("Quicksand"));
    }

    @Test
    public void testCatalogSeedPopulatesOccupationRows() {
        ICatalogRepository occupationCatalog = CatalogName.OCCUPATION.open(database);
        assertEquals(7, occupationCatalog.list().size());
        assertNotNull(occupationCatalog.resolve("Farmer"));
    }

    @Test
    public void testCropInsertWithBogusStageIdFailsForeignKey() {
        SQLiteDatabase raw = database.getWritableDatabase();
        String userIdentifier = enroll(raw, "rooted_user");

        ContentValues row = new ContentValues();
        row.put("id", IdentifierFactory.generate("crop"));
        row.put("user_id", userIdentifier);
        row.put("crop_type", "rice");
        row.put("phenological_stage_id", "Vegetative");

        try {
            raw.insertOrThrow("crop", null, row);
            fail("Expected SQLiteConstraintException for bogus phenological_stage_id");
        } catch (SQLiteConstraintException expected) {
        }
    }

    @Test
    public void testCropInsertWithResolvedStageIdSucceeds() {
        SQLiteDatabase raw = database.getWritableDatabase();
        String userIdentifier = enroll(raw, "good_user");
        ICatalogRepository stageCatalog = CatalogName.PHENOLOGICAL_STAGE.open(database);
        String stageIdentifier = stageCatalog.resolve("Vegetative");
        assertNotNull(stageIdentifier);

        ContentValues row = new ContentValues();
        String now = "2026-01-01 00:00:00";
        row.put("id", IdentifierFactory.generate("crop"));
        row.put("user_id", userIdentifier);
        row.put("crop_type", "rice");
        row.put("phenological_stage_id", stageIdentifier);
        row.put("created_at", now);
        row.put("updated_at", now);
        row.put("is_active", 1);

        long inserted = raw.insertOrThrow("crop", null, row);
        assertTrue(inserted > 0);
    }

    @Test
    public void testFullSubmissionPipelineInsertsCropWithResolvedStage() {
        SQLiteDatabase raw = database.getWritableDatabase();
        String userIdentifier = enroll(raw, "pipeline_user");

        ICatalogRepository stageCatalog = CatalogName.PHENOLOGICAL_STAGE.open(database);
        ISessionRepository sessionRepository = stub(userIdentifier);
        ICropRepository cropRepository = new SqliteCropRepository(database, sessionRepository);
        CropDossier dossier = new CropDossier(cropRepository, new SqlitePhotographRepository(database, sessionRepository));
        CropRegistry registry = new CropRegistry(dossier, stageCatalog);

        Crop crop = new Cultivation("rice", "Vegetative").produce("crop_pipeline_1", registry);
        cropRepository.store(crop);

        Cursor cursor = raw.rawQuery(
                "SELECT phenological_stage_id, crop_type, user_id FROM crop WHERE crop_type = ?",
                new String[]{"rice"});
        assertTrue(cursor.moveToFirst());
        String storedStageId = cursor.getString(0);
        String storedCropType = cursor.getString(1);
        String storedUserId = cursor.getString(2);
        cursor.close();

        assertEquals(stageCatalog.resolve("Vegetative"), storedStageId);
        assertEquals("rice", storedCropType);
        assertEquals(userIdentifier, storedUserId);
    }

    @Test
    public void testFullSubmissionPipelineWithUnknownStageStoresNullForeignKey() {
        SQLiteDatabase raw = database.getWritableDatabase();
        String userIdentifier = enroll(raw, "unknown_stage_user");

        ICatalogRepository stageCatalog = CatalogName.PHENOLOGICAL_STAGE.open(database);
        ISessionRepository sessionRepository = stub(userIdentifier);
        ICropRepository cropRepository = new SqliteCropRepository(database, sessionRepository);
        CropDossier dossier = new CropDossier(cropRepository, new SqlitePhotographRepository(database, sessionRepository));
        CropRegistry registry = new CropRegistry(dossier, stageCatalog);

        Crop crop = new Cultivation("tomato", "MysteryStage").produce("crop_pipeline_2", registry);
        cropRepository.store(crop);

        Cursor cursor = raw.rawQuery(
                "SELECT phenological_stage_id FROM crop WHERE crop_type = ?",
                new String[]{"tomato"});
        assertTrue(cursor.moveToFirst());
        boolean isNull = cursor.isNull(0);
        cursor.close();
        assertTrue("phenological_stage_id should be NULL when name does not resolve", isNull);
    }

    @Test
    public void testSchemaIsStructuralWithNoTriggers() {
        SQLiteDatabase raw = database.getReadableDatabase();
        Cursor tables = raw.rawQuery(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'ai_user_response'",
                null);
        assertTrue("ai_user_response table must exist", tables.moveToFirst());
        tables.close();

        Cursor triggers = raw.rawQuery(
                "SELECT name FROM sqlite_master WHERE type = 'trigger'",
                null);
        int triggerCount = 0;
        while (triggers.moveToNext()) triggerCount++;
        triggers.close();
        assertEquals("schema must have zero triggers", 0, triggerCount);
    }

    @Test
    public void testLogEntryHasLogActionColumnAfterMigration() {
        SQLiteDatabase raw = database.getReadableDatabase();
        Cursor cursor = raw.rawQuery("PRAGMA table_info(log_entry)", null);
        boolean hasLogAction = false;
        while (cursor.moveToNext()) {
            if ("log_action".equals(cursor.getString(1))) {
                hasLogAction = true;
                break;
            }
        }
        cursor.close();
        assertTrue("log_action column must be present", hasLogAction);
    }

    private ISessionRepository stub(String userIdentifier) {
        return new ISessionRepository() {
            @Override public void save(Session session) {}
            @Override public Session recall() { return new Session(userIdentifier, "Farmer"); }
            @Override public void clear() {}
        };
    }
}