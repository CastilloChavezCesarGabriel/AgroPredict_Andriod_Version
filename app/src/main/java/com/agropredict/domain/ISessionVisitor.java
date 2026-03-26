package com.agropredict.domain;

public interface ISessionVisitor {
    void visit(String userIdentifier, String occupation);
}
