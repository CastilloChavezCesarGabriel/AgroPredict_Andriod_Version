package com.agropredict.domain.authentication;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.user.AdvancedOccupationKind;
import com.agropredict.domain.user.IOccupationListener;
import com.agropredict.domain.user.Occupation;
import com.agropredict.domain.user.OccupationCatalog;
import com.agropredict.domain.user.StandardOccupationKind;
import java.util.List;

public final class Session implements ISession {
    private static final OccupationCatalog CATALOG = new OccupationCatalog(List.of(
            new AdvancedOccupationKind(),
            new StandardOccupationKind()));
    private final String userIdentifier;
    private final Occupation occupation;

    public Session(String userIdentifier, String occupation) {
        this.userIdentifier = ArgumentPrecondition.validate(userIdentifier, "session user identifier");
        this.occupation = CATALOG.classify(occupation);
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