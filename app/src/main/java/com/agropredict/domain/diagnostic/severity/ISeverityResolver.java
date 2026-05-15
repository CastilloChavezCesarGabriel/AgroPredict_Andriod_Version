package com.agropredict.domain.diagnostic.severity;

public interface ISeverityResolver {
    ISeverity classify(String gravity);
}
