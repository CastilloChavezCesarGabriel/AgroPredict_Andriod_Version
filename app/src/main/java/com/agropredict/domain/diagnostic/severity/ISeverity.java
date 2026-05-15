package com.agropredict.domain.diagnostic.severity;

import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;

public interface ISeverity {
    void label(ISeverityConsumer consumer);
    void review(ISeverityLevelConsumer consumer);
}
