package com.agropredict;

import android.app.Application;
import com.agropredict.application.IFactoryConsumer;
import com.agropredict.core.Configuration;

public final class AgroPredictApplication extends Application {
    private Configuration configuration;

    @Override
    public void onCreate() {
        super.onCreate();
        configuration = new Configuration(this);
    }

    public void provide(IFactoryConsumer consumer) {
        configuration.provide(consumer);
    }
}