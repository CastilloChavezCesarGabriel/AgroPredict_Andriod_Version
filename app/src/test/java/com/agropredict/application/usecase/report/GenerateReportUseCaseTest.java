package com.agropredict.application.usecase.report;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.service.IReportService;
import com.agropredict.domain.component.crop.CropProfile;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.visitor.TestOperationResultVisitor;

import org.junit.Test;

public final class GenerateReportUseCaseTest {

    private IReportService fakeReport(boolean success) {
        return (crop, diagnostic) -> success ? OperationResult.succeed("report_1") : OperationResult.fail();
    }

    @Test
    public void testGenerateSuccess() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new GenerateReportUseCase(fakeReport(true))
            .generate(new Crop("c1", "wheat", new CropProfile(null, null, null)), new Diagnostic("d1", new Prediction("wheat", 0.85))).accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testGenerateFailure() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new GenerateReportUseCase(fakeReport(false))
            .generate(new Crop("c1", "wheat", new CropProfile(null, null, null)), new Diagnostic("d1", new Prediction("wheat", 0.85))).accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}