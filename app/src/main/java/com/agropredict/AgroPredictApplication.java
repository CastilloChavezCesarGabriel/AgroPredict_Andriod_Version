package com.agropredict;

import android.app.Application;
import com.agropredict.application.IFactoryConsumer;
import com.agropredict.core.Configuration;
import com.google.android.material.color.DynamicColors;

public final class AgroPredictApplication extends Application {
    private Configuration configuration;

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
        configuration = new Configuration(this);
    }

    public void provide(IFactoryConsumer consumer) {
        configuration.provide(consumer);
    }
}