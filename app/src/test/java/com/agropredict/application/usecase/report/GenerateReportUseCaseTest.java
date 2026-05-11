package com.agropredict.application.usecase.report;

import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.report_generation.usecase.GenerateReportUseCase;
import com.agropredict.application.service.IReportService;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.visitor.FailExpecter;
import com.agropredict.visitor.SucceedExpecter;

import org.junit.Test;

public final class GenerateReportUseCaseTest {
    private IReportService fakeReport(boolean success) {
        return (crop, diagnostic) -> success ? new SuccessfulOperation("report_1") : new FailedOperation();
    }

    @Test
    public void testGenerateSuccess() {
        new GenerateReportUseCase(fakeReport(true))
            .generate(new Crop("c1", "wheat", new CropProfile(new Plot(null, null), new GrowthCycle(null, null))), new Diagnostic("d1", new Prediction("wheat", 0.85))).accept(new SucceedExpecter(null));
    }

    @Test
    public void testGenerateFailure() {
        new GenerateReportUseCase(fakeReport(false))
            .generate(new Crop("c1", "wheat", new CropProfile(new Plot(null, null), new GrowthCycle(null, null))), new Diagnostic("d1", new Prediction("wheat", 0.85))).accept(new FailExpecter());
    }
}