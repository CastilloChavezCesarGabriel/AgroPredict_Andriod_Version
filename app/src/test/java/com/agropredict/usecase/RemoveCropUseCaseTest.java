package com.agropredict.usecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.usecase.crop.RemoveCropUseCase;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.CapturingDiagnosticRepository;
import com.agropredict.repository.CapturingPhotographRepository;
import com.agropredict.repository.CapturingReportRepository;
import com.agropredict.visitor.SucceedExpecter;

import java.util.List;
import org.junit.Test;

public final class RemoveCropUseCaseTest {

    @Test
    public void testRemovingACropClearsItsDiagnosticsPhotosAndReportsAndDeactivatesTheCrop() {
        CapturingCropRepository crops = new CapturingCropRepository();
        CapturingDiagnosticRepository diagnostics = new CapturingDiagnosticRepository();
        CapturingPhotographRepository photographs = new CapturingPhotographRepository();
        CapturingReportRepository reports = new CapturingReportRepository();
        diagnostics.enroll("crop_42", new Diagnostic("d_1", new Prediction("rice", 0.9)));
        diagnostics.enroll("crop_42", new Diagnostic("d_2", new Prediction("rice", 0.8)));
        photographs.enroll("crop_42");
        photographs.enroll("crop_42");
        reports.enroll("crop_42");
        RemoveCropUseCase useCase = new RemoveCropUseCase(crops, List.of(diagnostics, photographs, reports));

        useCase.remove("crop_42").accept(new SucceedExpecter("crop_42"));

        assertTrue(diagnostics.clearedFor("crop_42"));
        assertFalse(diagnostics.remainsFor("crop_42"));
        assertTrue(photographs.clearedFor("crop_42"));
        assertFalse(photographs.remainsFor("crop_42"));
        assertTrue(reports.clearedFor("crop_42"));
        assertFalse(reports.remainsFor("crop_42"));
        assertTrue(crops.deletedAccordingTo("crop_42"));
    }

    @Test
    public void testRemovingOneCropDoesNotAffectAnother() {
        CapturingCropRepository crops = new CapturingCropRepository();
        CapturingDiagnosticRepository diagnostics = new CapturingDiagnosticRepository();
        CapturingPhotographRepository photographs = new CapturingPhotographRepository();
        CapturingReportRepository reports = new CapturingReportRepository();
        diagnostics.enroll("crop_42", new Diagnostic("d_1", new Prediction("rice", 0.9)));
        diagnostics.enroll("crop_99", new Diagnostic("d_2", new Prediction("maize", 0.85)));
        photographs.enroll("crop_42");
        photographs.enroll("crop_99");
        reports.enroll("crop_42");
        reports.enroll("crop_99");
        RemoveCropUseCase useCase = new RemoveCropUseCase(crops, List.of(diagnostics, photographs, reports));

        useCase.remove("crop_42");

        assertFalse(diagnostics.remainsFor("crop_42"));
        assertTrue(diagnostics.remainsFor("crop_99"));
        assertFalse(photographs.remainsFor("crop_42"));
        assertTrue(photographs.remainsFor("crop_99"));
        assertFalse(reports.remainsFor("crop_42"));
        assertTrue(reports.remainsFor("crop_99"));
    }
}
