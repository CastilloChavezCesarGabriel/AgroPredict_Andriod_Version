package com.agropredict.application.service;

public interface IAuditLogger {
    void log(String userIdentifier, String action);
}