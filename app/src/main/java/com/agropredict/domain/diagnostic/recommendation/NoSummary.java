package com.agropredict.domain.diagnostic.recommendation;

import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public final class NoSummary implements ISummary {
    @Override
    public void summarize(ISummaryConsumer consumer) {}
}