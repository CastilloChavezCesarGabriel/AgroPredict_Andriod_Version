package com.agropredict.application.repository;

import java.util.List;

public interface ICatalogRepository {
    List<String> list();
    String findIdentifier(String name);
}