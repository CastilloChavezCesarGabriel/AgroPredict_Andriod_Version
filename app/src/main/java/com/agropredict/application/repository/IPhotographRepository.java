package com.agropredict.application.repository;

import com.agropredict.domain.photograph.Photograph;

public interface IPhotographRepository {
    void store(Photograph photograph, String cropIdentifier);
    Photograph find(String diagnosticIdentifier);
}