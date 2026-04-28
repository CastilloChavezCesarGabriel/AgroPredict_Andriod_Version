package com.agropredict.domain.visitor.user;

public interface IUserVisitor {
    default void visitIdentity(String identifier, String fullName) {}
    default void visitCredential(String email, String passwordHash) {}
    default void visitUsername(String username) {}
    default void visitPhone(String phoneNumber) {}
    default void visitOccupation(String occupationIdentifier) {}
}