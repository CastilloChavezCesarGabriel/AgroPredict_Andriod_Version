package com.agropredict.application.factory;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IAssetService;

public interface IDashboardFactory {
    ISessionRepository createSessionRepository();
    IAssetService createAssetService();
}