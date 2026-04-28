package com.agropredict.application.factory;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.ISessionRepository;

public interface IReviewFactory {
    IDiagnosticRepository createDiagnosticRepository();
    ICropRepository createCropRepository();
    ISessionRepository createSessionRepository();
}