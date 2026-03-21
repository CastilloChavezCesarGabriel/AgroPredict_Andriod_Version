package com.agropredict;

import android.app.Application;
import com.agropredict.application.IFactoryConsumer;
import com.agropredict.core.DependencyProvider;

public final class AgroPredictApplication extends Application {
    private DependencyProvider dependencyProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        dependencyProvider = new DependencyProvider(this);
    }

    public void provide(IFactoryConsumer consumer) {
        dependencyProvider.provide(consumer);
    }
}
