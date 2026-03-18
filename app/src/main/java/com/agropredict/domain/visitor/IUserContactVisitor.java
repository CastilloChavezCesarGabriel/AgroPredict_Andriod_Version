package com.agropredict.domain.visitor;

public interface IUserContactVisitor {
    void visitContact(String username, String phoneNumber);
}