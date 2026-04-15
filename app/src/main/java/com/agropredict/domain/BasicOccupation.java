package com.agropredict.domain;

public final class BasicOccupation extends Occupation {
    public BasicOccupation(String raw) {
        super(raw);
    }

    @Override
    public void accept(IOccupationHandler handler) {
        handler.onBasic();
    }
}