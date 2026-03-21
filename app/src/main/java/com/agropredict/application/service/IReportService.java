package com.agropredict.application.service;

import com.agropredict.application.result.OperationResult;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Diagnostic;

public interface IReportService {
    OperationResult generate(Crop crop, Diagnostic diagnostic);
}