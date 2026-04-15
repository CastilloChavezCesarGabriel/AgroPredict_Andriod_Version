package com.agropredict.domain;

import com.agropredict.domain.visitor.session.ISessionVisitor;
import java.util.Set;

public abstract class Occupation {
    private static final Set<String> ADVANCED = Set.of("Agronomist", "Specialist", "Researcher");

    protected final String raw;

    protected Occupation(String raw) {
        this.raw = raw;
    }

    public abstract void accept(IOccupationHandler handler);

    public final void accept(ISessionVisitor visitor, String userIdentifier) {
        visitor.visit(userIdentifier, raw);
    }

    public static Occupation of(String raw) {
        if (raw == null) return new BasicOccupation(null);
        return ADVANCED.contains(raw) ? new AdvancedOccupation(raw) : new BasicOccupation(raw);
    }
}