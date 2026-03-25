package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticOwnershipVisitor;

public final class DiagnosticOwnership {
    private final String userIdentifier;
    private final String recommendationText;

    public DiagnosticOwnership(String userIdentifier, String recommendationText) {
        this.userIdentifier = userIdentifier;
        this.recommendationText = recommendationText;
    }

    public void accept(IDiagnosticOwnershipVisitor visitor) {
        visitor.visitOwnership(userIdentifier, recommendationText);
    }
}