package com.agropredict.application.service;

import com.agropredict.application.result.OperationResult;
import java.util.Map;

public interface IReportGeneratorService {
    OperationResult generate(Map<String, Object> reportData);
}