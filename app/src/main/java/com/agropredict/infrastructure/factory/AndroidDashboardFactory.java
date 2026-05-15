package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IAssetService;
import com.agropredict.infrastructure.ai_model_asset.AssetExtractor;
import java.util.Objects;

public final class AndroidDashboardFactory implements IDashboardFactory {
    private final SessionPersistence sessionPersistence;
    private final Context context;

    public AndroidDashboardFactory(SessionPersistence sessionPersistence, Context context) {
        this.sessionPersistence = Objects.requireNonNull(sessionPersistence,
                "android dashboard factory requires a session persistence");
        this.context = Objects.requireNonNull(context,
                "android dashboard factory requires a context");
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return sessionPersistence.createSessionRepository();
    }

    @Override
    public IAssetService createAssetService() {
        return new AssetExtractor(context);
    }
}