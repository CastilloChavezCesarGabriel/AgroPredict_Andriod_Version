package com.agropredict.domain;

public interface IOccupationVisitor {
    void visit(String label, boolean advanced);
}