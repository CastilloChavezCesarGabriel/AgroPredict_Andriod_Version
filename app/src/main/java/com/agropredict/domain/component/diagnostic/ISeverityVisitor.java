package com.agropredict.domain.component.diagnostic;

public interface ISeverityVisitor {
    void visit(String name, int urgency);
}