package com.agropredict.application.repository;

import com.agropredict.domain.entity.User;

public interface IUserRepository {
    String authenticate(String email, String password);
    void store(User user);
    boolean isRegistered(String email);
    boolean isTaken(String username);
    boolean reset(String email, String newPasswordHash);
}