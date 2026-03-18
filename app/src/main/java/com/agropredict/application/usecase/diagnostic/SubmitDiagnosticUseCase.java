package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.result.OperationResult;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.CropImage;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.value.diagnostic.DiagnosticData;
import com.agropredict.domain.value.ISubmissionContextVisitor;
import com.agropredict.domain.value.SubmissionContext;
import com.agropredict.domain.visitor.IDiagnosticVisitor;

public final class SubmitDiagnosticUseCase implements IDiagnosticVisitor,
        ISubmissionContextVisitor {

    private final IRepositoryFactory factory;
    private OperationResult operationResult;

    public SubmitDiagnosticUseCase(IRepositoryFactory factory) {
        this.factory = factory;
    }

    public OperationResult submit(Diagnostic diagnostic, SubmissionContext context) {
        try {
            context.accept(this);
            Diagnostic enriched = factory.createApiService().submit(diagnostic);
            factory.createDiagnosticRepository().store(enriched);
            enriched.accept(this);
            return operationResult;
        } catch (RuntimeException exception) {
            return OperationResult.fail();
        }
    }

    @Override
    public void visit(Crop crop, CropImage image) {
        if (crop != null) {
            factory.createCropRepository().store(crop);
        }
        if (image != null) {
            factory.createCropImageRepository().store(image);
        }
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        this.operationResult = OperationResult.succeed(identifier);
    }
}
