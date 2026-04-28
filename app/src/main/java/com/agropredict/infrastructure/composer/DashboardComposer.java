package com.agropredict.infrastructure.composer;

import android.content.Context;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IAssetService;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.infrastructure.ai_model_asset.AssetExtractor;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;

public final class DashboardComposer implements IDashboardFactory {
    private final Context context;

    public DashboardComposer(Context context) {
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