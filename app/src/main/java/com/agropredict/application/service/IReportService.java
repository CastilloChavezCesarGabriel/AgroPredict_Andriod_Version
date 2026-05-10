package com.agropredict.application.service;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;

public interface IReportService {
    IUseCaseResult generate(Crop crop, Diagnostic diagnostic);
}