package com.agropredict.domain.user.occupation;

import com.agropredict.domain.user.Occupation;

import java.util.Set;

public final class AdvancedOccupationKind implements IOccupationKind {
    private static final Set<String> RECOGNIZED = Set.of("Agronomist", "Specialist", "Researcher");

    @Override
    public Occupation classify(String value) {
        return RECOGNIZED.contains(value) ? new AdvancedOccupation(value) : null;
    }
}
