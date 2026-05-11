package com.agropredict.application.request.diagnostic_submission;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.diagnostic_submission.workflow.CropDossier;
import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.application.diagnostic_submission.request.Cultivation;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.crop.Crop;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.CapturingPhotographRepository;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.visitor.CropTypeCapturingVisitor;
import com.agropredict.visitor.StageCapturingVisitor;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public final class CultivationTest {

    private CropRegistry arrange(ICatalogRepository catalog) {
        CropDossier dossier = new CropDossier(new CapturingCropRepository(), new CapturingPhotographRepository());
        return new CropRegistry(dossier, catalog);
    }

    @Test
    public void testStageNameResolvesToCatalogIdentifier() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_abc123");
        CropRegistry registry = arrange(new FixedCatalogRepository(mapping));

        Crop crop = new Cultivation("rice", "Vegetative").produce("crop_test_1", registry);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.track(visitor);
        assertTrue(visitor.recordedStage("stage_abc123"));
    }

    @Test
    public void testStageNameDoesNotLeakIntoIdentifierColumn() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_abc123");
        CropRegistry registry = arrange(new FixedCatalogRepository(mapping));

        Crop crop = new Cultivation("rice", "Vegetative").produce("crop_test_2", registry);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.track(visitor);
        assertFalse(visitor.recordedStage("Vegetative"));
    }

    @Test
    public void testUnknownStageNameRecordsNoStageIdentifier() {
        CropRegistry registry = arrange(new FixedCatalogRepository(new HashMap<>()));

        Crop crop = new Cultivation("rice", "MysteryStage").produce("crop_test_3", registry);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.track(visitor);
        assertFalse(visitor.recordedStage("MysteryStage"));
    }

    @Test
    public void testNullStageNameRecordsNoStageIdentifier() {
        CropRegistry registry = arrange(new FixedCatalogRepository(new HashMap<>()));

        Crop crop = new Cultivation("rice", null).produce("crop_test_4", registry);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        crop.track(visitor);
        assertFalse(visitor.recordedStage("anything"));
    }

    @Test
    public void testPredictedCropPropagatesToEntity() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Flowering", "stage_xyz");
        CropRegistry registry = arrange(new FixedCatalogRepository(mapping));

        Crop crop = new Cultivation("tomato", "Flowering").produce("crop_test_5", registry);

        CropTypeCapturingVisitor visitor = new CropTypeCapturingVisitor();
        crop.describe(visitor);
        assertTrue(visitor.wasClassifiedAs("tomato"));
    }
}
