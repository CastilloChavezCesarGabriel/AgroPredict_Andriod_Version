package com.agropredict.domain.user.occupation;

import com.agropredict.domain.user.Occupation;
import com.agropredict.domain.user.visitor.IOccupationConsumer;

public final class NoOccupation extends Occupation {
    public NoOccupation() {
        super("");
    }

    @Override
    public void accept(IOccupationListener listener) {}

    @Override
    public void classify(IOccupationConsumer consumer) {}
}
