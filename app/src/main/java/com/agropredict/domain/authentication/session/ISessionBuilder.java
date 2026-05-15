package com.agropredict.domain.authentication.session;

public interface ISessionBuilder {
    void build(String identifier, String occupation);
}