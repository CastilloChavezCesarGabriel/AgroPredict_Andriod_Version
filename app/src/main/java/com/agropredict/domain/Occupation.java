package com.agropredict.domain;

import com.agropredict.domain.visitor.session.ISessionVisitor;
import java.util.Set;

public abstract class Occupation {
    private static final Set<String> ADVANCED = Set.of("Agronomist", "Specialist", "Researcher");

    protected final String value;

    protected Occupation(String value) {
        this.value = value;
    }

    public abstract void accept(IOccupationVisitor visitor);

    public final void accept(ISessionVisitor visitor, String userIdentifier) {
        visitor.visit(userIdentifier, value);
    }

    public static Occupation classify(String value) {
        if (value == null) return new BasicOccupation(null);
        return ADVANCED.contains(value) ? new AdvancedOccupation(value) : new BasicOccupation(value);
    }
}