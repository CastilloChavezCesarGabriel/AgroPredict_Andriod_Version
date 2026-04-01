package com.agropredict.application.service;

public interface IPasswordHasher {
    String hash(String password);
    boolean verify(String password, String stored);
}