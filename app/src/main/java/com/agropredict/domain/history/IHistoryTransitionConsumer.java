package com.agropredict.domain.history;

public interface IHistoryTransitionConsumer {
    void link(String previousValue, String currentValue);
}