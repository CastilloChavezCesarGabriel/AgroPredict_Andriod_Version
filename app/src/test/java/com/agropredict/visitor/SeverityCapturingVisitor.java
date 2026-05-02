package com.agropredict.visitor;

import com.agropredict.domain.component.diagnostic.ISeverityVisitor;

public final class SeverityCapturingVisitor implements ISeverityVisitor {
    private String name;
    private int urgency = -1;

    @Override
    public void visit(String name, int urgency) {
        this.name = name;
        this.urgency = urgency;
    }

    public boolean recordedSevere() {
        return urgency == 2 && "Severe issue".equals(name);
    }

    public boolean recordedModerate() {
        return urgency == 1 && "Moderate issue".equals(name);
    }

    public boolean recordedHealthy() {
        return urgency == 0 && "Healthy".equals(name);
    }

    public boolean recordedPending() {
        return urgency == 0 && "Pending".equals(name);
    }
}