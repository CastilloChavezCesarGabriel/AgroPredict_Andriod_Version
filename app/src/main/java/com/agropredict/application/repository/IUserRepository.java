package com.agropredict.application.repository;

import com.agropredict.domain.entity.User;

public interface IUserRepository {
    User authenticate(String email, String passwordHash);
    void store(User user);
    boolean isRegistered(String email);
    boolean isTaken(String username);
}