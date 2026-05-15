package com.agropredict.domain.user.occupation;

import com.agropredict.domain.user.Occupation;
import java.util.List;

public final class OccupationCatalog {
    private final List<IOccupationKind> kinds;

    public OccupationCatalog() {
        this.kinds = List.of(
                new AdvancedOccupationKind(),
                new StandardOccupationKind());
    }

    public Occupation classify(String value) {
        if (value == null || value.isBlank()) return new NoOccupation();
        for (IOccupationKind kind : kinds) {
            Occupation match = kind.classify(value);
            if (match != null) return match;
        }
        return new StandardOccupation(value);
    }
}