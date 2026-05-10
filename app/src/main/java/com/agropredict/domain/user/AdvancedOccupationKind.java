package com.agropredict.domain.user;

import java.util.Set;

public final class AdvancedOccupationKind implements IOccupationKind {
    private static final Set<String> RECOGNIZED = Set.of("Agronomist", "Specialist", "Researcher");

    @Override
    public Occupation classify(String value) {
        return RECOGNIZED.contains(value) ? new AdvancedOccupation(value) : null;
    }
}
