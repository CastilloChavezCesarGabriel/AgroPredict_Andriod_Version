package com.agropredict.usecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.crop.CropCleanup;
import com.agropredict.application.usecase.crop.RemoveCropUseCase;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.CapturingDiagnosticRepository;
import com.agropredict.repository.CapturingPhotographRepository;
import com.agropredict.repository.CapturingReportRepository;
import com.agropredict.visitor.TestOperationResultVisitor;

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
        CropCleanup cleanup = new CropCleanup(diagnostics, new CropCleanup(photographs, reports));
        RemoveCropUseCase useCase = new RemoveCropUseCase(crops, cleanup);

        OperationResult result = useCase.remove("crop_42");

        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        result.accept(visitor);
        assertTrue(visitor.isCompleted("crop_42"));
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
        CropCleanup cleanup = new CropCleanup(diagnostics, new CropCleanup(photographs, reports));
        RemoveCropUseCase useCase = new RemoveCropUseCase(crops, cleanup);

        useCase.remove("crop_42");

        assertFalse(diagnostics.remainsFor("crop_42"));
        assertTrue(diagnostics.remainsFor("crop_99"));
        assertFalse(photographs.remainsFor("crop_42"));
        assertTrue(photographs.remainsFor("crop_99"));
        assertFalse(reports.remainsFor("crop_42"));
        assertTrue(reports.remainsFor("crop_99"));
    }
}
