package com.agropredict.domain.diagnostic;

public interface ISeverityFactory {
    Severity classify(String gravity);
}