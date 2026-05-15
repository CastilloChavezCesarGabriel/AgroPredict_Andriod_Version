package com.agropredict.domain.user;

import com.agropredict.domain.authentication.session.ISessionBuilder;
import com.agropredict.domain.authentication.session.ISessionConsumer;
import com.agropredict.domain.user.occupation.IOccupationListener;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import java.util.Objects;

public abstract class Occupation {
    protected final String value;

    protected Occupation(String value) {
        this.value = Objects.requireNonNull(value, "occupation requires a value");
    }

    public abstract void accept(IOccupationListener listener);

    public final void report(ISessionConsumer consumer, String userIdentifier) {
        consumer.report(userIdentifier, value);
    }

    public void classify(IOccupationConsumer consumer) {
        consumer.classify(value);
    }

    public final void expose(ISessionBuilder builder, String userIdentifier) {
        builder.build(userIdentifier, value);
    }
}