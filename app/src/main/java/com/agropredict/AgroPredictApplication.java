package com.agropredict;

import android.app.Application;
import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.IRepositoryFactoryConsumer;
import com.agropredict.infrastructure.RepositoryFactory;
import com.agropredict.infrastructure.persistence.DatabaseHelper;

public final class AgroPredictApplication extends Application {
    private IRepositoryFactory repositoryFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        repositoryFactory = new RepositoryFactory(databaseHelper, this);
    }

    public void provide(IRepositoryFactoryConsumer consumer) {
        consumer.accept(repositoryFactory);
    }
}