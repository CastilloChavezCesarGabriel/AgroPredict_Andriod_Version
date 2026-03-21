package com.agropredict.application.repository;

public interface ISessionRepository {
    void save(String userIdentifier);
    String recall();
    void clear();
}