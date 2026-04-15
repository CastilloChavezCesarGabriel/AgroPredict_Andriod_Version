package com.agropredict.domain.component.diagnostic;

public interface ISeverityHandler {
    default void onPending() {}
    default void onHealthy() {}
    default void onModerate() {}
    default void onSevere() {}
    default void onUnknown() {}
}