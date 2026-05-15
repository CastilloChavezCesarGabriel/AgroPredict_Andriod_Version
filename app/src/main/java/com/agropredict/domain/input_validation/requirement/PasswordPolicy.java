package com.agropredict.domain.input_validation.requirement;

import java.util.List;
import java.util.Objects;

public final class PasswordPolicy {
    private final List<ICharacterRequirement> requirements;

    public PasswordPolicy(List<ICharacterRequirement> requirements) {
        this.requirements = Objects.requireNonNull(requirements, "password policy requires a list of requirements");
    }

    public boolean accepts(String password) {
        for (ICharacterRequirement requirement : requirements) {
            if (!requirement.accepts(password)) return false;
        }
        return true;
    }
}