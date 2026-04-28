package com.agropredict.visitor;

import com.agropredict.domain.IOccupationVisitor;

public final class TestOccupationVisitor implements IOccupationVisitor {
    private boolean advanced;
    private boolean basic;

    @Override
    public void visit(String label, boolean advanced) {
        if (advanced) this.advanced = true;
        else this.basic = true;
    }

    public boolean sawAdvanced() {
        return advanced;
    }

    public boolean sawBasic() {
        return basic;
    }
}