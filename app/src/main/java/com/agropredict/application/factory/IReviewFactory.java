package com.agropredict.application.factory;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.usecase.crop.CropCleanup;

public interface IReviewFactory {
    IDiagnosticRepository createDiagnosticRepository();
    ICropRepository createCropRepository();
    IPhotographRepository createPhotographRepository();
    ISessionRepository createSessionRepository();
    CropCleanup createCropCleanup();
}
