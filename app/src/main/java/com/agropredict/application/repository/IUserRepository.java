package com.agropredict.application.repository;

import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.Session;

public interface IUserRepository {
    Session authenticate(String email, String password);
    void register(RegistrationRequest request, IPasswordHasher hasher);
    boolean reset(String email, String newPasswordHash);
}