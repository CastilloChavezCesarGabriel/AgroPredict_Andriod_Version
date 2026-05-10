package com.agropredict.domain.user;

import java.util.List;
import java.util.Objects;

public final class OccupationCatalog {
    private final List<IOccupationKind> kinds;

    public OccupationCatalog(List<IOccupationKind> kinds) {
        this.kinds = Objects.requireNonNull(kinds, "occupation catalog requires a list of kinds");
    }

    public Occupation classify(String value) {
        if (value == null) return new StandardOccupation(null);
        for (IOccupationKind kind : kinds) {
            Occupation match = kind.classify(value);
            if (match != null) return match;
        }
        return new StandardOccupation(value);
    }
}
