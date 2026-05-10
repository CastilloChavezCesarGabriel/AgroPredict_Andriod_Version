package com.agropredict.application.repository;

import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.domain.user.ISessionSubject;
import com.agropredict.domain.user.User;

public interface IUserRepository {
    ISessionSubject authenticate(String email, String password);
    void register(RegistrationRequest request, ICatalogRepository catalog);
    boolean reset(String email, String newPasswordHash);
    User find(String userIdentifier);
}