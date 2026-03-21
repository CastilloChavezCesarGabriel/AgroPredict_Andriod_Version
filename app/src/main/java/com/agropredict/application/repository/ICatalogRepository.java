package com.agropredict.application.repository;

import java.util.List;

public interface ICatalogRepository {
    List<String> list();
    String resolve(String name);
}