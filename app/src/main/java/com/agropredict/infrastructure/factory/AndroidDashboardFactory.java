package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IAssetService;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.infrastructure.ai_model_asset.AssetExtractor;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;

public final class AndroidDashboardFactory implements IDashboardFactory {
    private final Context context;

    public AndroidDashboardFactory(Context context) {
        this.context = context;
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return new SessionRepository(context);
    }

    @Override
    public IAssetService createAssetService() {
        return new AssetExtractor(context);
    }
}