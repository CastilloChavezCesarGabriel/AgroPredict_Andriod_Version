package com.agropredict.application.service;

public interface IPasswordHasher {
    String hash(String password);
    boolean isVerified(String password, String stored);
}
