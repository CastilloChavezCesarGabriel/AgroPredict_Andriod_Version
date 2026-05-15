package com.agropredict.domain.authentication.session;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.user.Occupation;
import com.agropredict.domain.user.occupation.IOccupationListener;
import com.agropredict.domain.user.occupation.OccupationCatalog;

public final class Session implements ISession {
    private final String userIdentifier;
    private final Occupation occupation;

    public Session(String userIdentifier, String occupation) {
        this.userIdentifier = ArgumentPrecondition.validate(userIdentifier, "session user identifier");
        this.occupation = new OccupationCatalog().classify(occupation);
    }

    @Override
    public void report(ISessionConsumer consumer) {
        occupation.report(consumer, userIdentifier);
    }

    @Override
    public void observe(IOccupationListener listener) {
        occupation.accept(listener);
    }
}
