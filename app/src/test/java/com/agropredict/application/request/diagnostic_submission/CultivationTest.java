package com.agropredict.application.request.diagnostic_submission;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.diagnostic_submission.Cropland;
import com.agropredict.application.diagnostic_submission.FieldStorage;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.entity.Crop;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.CapturingPhotographRepository;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.visitor.CropTypeCapturingVisitor;
import com.agropredict.visitor.StageCapturingVisitor;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public final class CultivationTest {

    private Cropland arrange(ICatalogRepository catalog) {
        FieldStorage storage = new FieldStorage(new CapturingCropRepository(), new CapturingPhotographRepository());
        return new Cropland(storage, catalog);
    }

    @Test
    public void testStageNameResolvesToCatalogIdentifier() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_abc123");
        Cropland cropland = arrange(new FixedCatalogRepository(mapping));

        Crop crop = new Cultivation("rice", "Vegetative").cultivate("crop_test_1", cropland);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.accept(visitor);
        assertTrue(visitor.recordedStage("stage_abc123"));
    }

    @Test
    public void testStageNameDoesNotLeakIntoIdentifierColumn() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_abc123");
        Cropland cropland = arrange(new FixedCatalogRepository(mapping));

        Crop crop = new Cultivation("rice", "Vegetative").cultivate("crop_test_2", cropland);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.accept(visitor);
        assertFalse(visitor.recordedStage("Vegetative"));
    }

    @Test
    public void testUnknownStageNameRecordsNoStageIdentifier() {
        Cropland cropland = arrange(new FixedCatalogRepository(new HashMap<>()));

        Crop crop = new Cultivation("rice", "MysteryStage").cultivate("crop_test_3", cropland);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.accept(visitor);
        assertFalse(visitor.recordedStage("MysteryStage"));
    }

    @Test
    public void testNullStageNameRecordsNoStageIdentifier() {
        Cropland cropland = arrange(new FixedCatalogRepository(new HashMap<>()));

        Crop crop = new Cultivation("rice", null).cultivate("crop_test_4", cropland);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.accept(visitor);
        assertFalse(visitor.recordedStage("anything"));
    }

    @Test
    public void testPredictedCropPropagatesToEntity() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Flowering", "stage_xyz");
        Cropland cropland = arrange(new FixedCatalogRepository(mapping));

        Crop crop = new Cultivation("tomato", "Flowering").cultivate("crop_test_5", cropland);

        CropTypeCapturingVisitor visitor = new CropTypeCapturingVisitor();
        crop.accept(visitor);
        assertTrue(visitor.wasClassifiedAs("tomato"));
    }
}
