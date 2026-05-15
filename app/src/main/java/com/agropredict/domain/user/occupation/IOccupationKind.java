package com.agropredict.domain.user.occupation;

import com.agropredict.domain.user.Occupation;

public interface IOccupationKind {
    Occupation classify(String value);
}
