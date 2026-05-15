package com.agropredict.application.repository;

import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.domain.user.ISessionSubject;
import com.agropredict.domain.user.IUser;

public interface IUserRepository {
    ISessionSubject authenticate(String email, String password);
    void register(RegistrationRequest request, ICatalogRepository catalog);
    boolean reset(String email, String newPasswordHash);
    IUser find(String userIdentifier);
}