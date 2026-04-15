package com.agropredict.visitor;

import com.agropredict.domain.IOccupationHandler;

public final class TestOccupationHandler implements IOccupationHandler {
    private boolean advanced;
    private boolean basic;

    @Override
    public void onAdvanced() {
        this.advanced = true;
    }

    @Override
    public void onBasic() {
        this.basic = true;
    }

    public boolean sawAdvanced() {
        return advanced;
    }

    public boolean sawBasic() {
        return basic;
    }
}