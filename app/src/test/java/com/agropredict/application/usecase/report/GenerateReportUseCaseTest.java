package com.agropredict.application.usecase.report;

import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.report_generation.usecase.GenerateReportUseCase;
import com.agropredict.application.service.IReportService;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.Field;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.crop.Soil;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.classification.Prediction;
import com.agropredict.domain.diagnostic.severity.PendingSeverity;
import com.agropredict.visitor.FailExpecter;
import com.agropredict.visitor.SucceedExpecter;
import org.junit.Test;

public final class GenerateReportUseCaseTest {
    private IReportService fakeReport(boolean success) {
        return (crop, diagnostic) -> success ? new SuccessfulOperation("report_1") : new FailedOperation();
    }

    private Crop newCrop() {
        Field field = new Field("Field A", "Lima");
        Soil soil = new Soil("Clay", "1ha");
        Plot plot = new Plot(field, soil);
        GrowthCycle cycle = new GrowthCycle("2026-01-01", "Vegetative");
        return new Crop("c1", "wheat", new CropProfile(plot, cycle));
    }

    private Diagnostic newDiagnostic() {
        return Diagnostic.begin("d1", new Prediction("wheat", 0.85), new PendingSeverity(() -> "Pending"));
    }

    @Test
    public void testGenerateSuccess() {
        new GenerateReportUseCase(fakeReport(true))
            .generate(newCrop(), newDiagnostic()).accept(new SucceedExpecter(null));
    }

    @Test
    public void testGenerateFailure() {
        new GenerateReportUseCase(fakeReport(false))
            .generate(newCrop(), newDiagnostic()).accept(new FailExpecter());
    }
}
