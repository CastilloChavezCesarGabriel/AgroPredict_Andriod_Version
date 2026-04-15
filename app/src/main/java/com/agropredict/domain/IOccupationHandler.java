package com.agropredict.domain;

public interface IOccupationHandler {
    default void onAdvanced() {}
    default void onBasic() {}
}