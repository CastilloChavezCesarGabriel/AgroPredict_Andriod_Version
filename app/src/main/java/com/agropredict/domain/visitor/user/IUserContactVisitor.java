package com.agropredict.domain.visitor.user;

public interface IUserContactVisitor {
    void visitContact(String username, String phoneNumber);
}