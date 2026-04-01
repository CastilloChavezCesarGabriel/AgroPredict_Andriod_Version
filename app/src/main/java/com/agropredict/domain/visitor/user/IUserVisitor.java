package com.agropredict.domain.visitor.user;

public interface IUserVisitor {
    void visitIdentity(String identifier, String fullName);
    void visitCredential(String email, String passwordHash);
    void visitUsername(String username);
    void visitPhone(String phoneNumber);
    void visitOccupation(String occupationIdentifier);
}