package com.agropredict.application.repository;

public interface ISessionRepository {
    void save(String userIdentifier);
    String load();
    void clear();
}