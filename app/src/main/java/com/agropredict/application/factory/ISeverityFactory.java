package com.agropredict.application.factory;

import com.agropredict.domain.diagnostic.severity.ISeverity;

public interface ISeverityFactory {
    ISeverity createHealthy();
    ISeverity createModerate();
    ISeverity createSevere();
    ISeverity createUnknown();
    ISeverity createPending();
}