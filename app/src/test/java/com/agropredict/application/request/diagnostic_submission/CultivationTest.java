package com.agropredict.application.request.diagnostic_submission;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.entity.Crop;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.visitor.CropTypeCapturingVisitor;
import com.agropredict.visitor.StageCapturingVisitor;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public final class CultivationTest {

    @Test
    public void testStageNameResolvesToCatalogIdentifier() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_abc123");
        ICatalogRepository catalog = new FixedCatalogRepository(mapping);

        Crop crop = new Cultivation("rice", "Vegetative").cultivate(catalog);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.accept(visitor);
        assertTrue(visitor.recordedStage("stage_abc123"));
    }

    @Test
    public void testStageNameDoesNotLeakIntoIdentifierColumn() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_abc123");
        ICatalogRepository catalog = new FixedCatalogRepository(mapping);

        Crop crop = new Cultivation("rice", "Vegetative").cultivate(catalog);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.accept(visitor);
        assertFalse(visitor.recordedStage("Vegetative"));
    }

    @Test
    public void testUnknownStageNameRecordsNoStageIdentifier() {
        ICatalogRepository catalog = new FixedCatalogRepository(new HashMap<>());

        Crop crop = new Cultivation("rice", "MysteryStage").cultivate(catalog);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.accept(visitor);
        assertFalse(visitor.recordedStage("MysteryStage"));
    }

    @Test
    public void testNullStageNameRecordsNoStageIdentifier() {
        ICatalogRepository catalog = new FixedCatalogRepository(new HashMap<>());

        Crop crop = new Cultivation("rice", null).cultivate(catalog);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.accept(visitor);
        assertFalse(visitor.recordedStage("anything"));
    }

    @Test
    public void testPredictedCropPropagatesToEntity() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Flowering", "stage_xyz");
        ICatalogRepository catalog = new FixedCatalogRepository(mapping);

        Crop crop = new Cultivation("tomato", "Flowering").cultivate(catalog);

        CropTypeCapturingVisitor visitor = new CropTypeCapturingVisitor();
        crop.accept(visitor);
        assertTrue(visitor.wasClassifiedAs("tomato"));
    }
}
