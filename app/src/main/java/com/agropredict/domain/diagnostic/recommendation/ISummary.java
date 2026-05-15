package com.agropredict.domain.diagnostic.recommendation;

import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public interface ISummary {
    void summarize(ISummaryConsumer consumer);
}
