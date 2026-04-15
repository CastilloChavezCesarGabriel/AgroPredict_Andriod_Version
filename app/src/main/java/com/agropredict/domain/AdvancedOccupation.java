package com.agropredict.domain;

public final class AdvancedOccupation extends Occupation {
    public AdvancedOccupation(String raw) {
        super(raw);
    }

    @Override
    public void accept(IOccupationHandler handler) {
        handler.onAdvanced();
    }
}