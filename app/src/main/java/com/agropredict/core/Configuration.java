package com.agropredict.core;

import android.content.Context;
import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.IFactoryConsumer;
import com.agropredict.infrastructure.factory.RepositoryFactory;
import com.agropredict.infrastructure.persistence.Database;

public final class Configuration {
    private final IRepositoryFactory repositoryFactory;

    public Configuration(Context context) {
        Database database = new Database(context);
        this.repositoryFactory = new RepositoryFactory(database, context);
    }

    public void provide(IFactoryConsumer consumer) {
        consumer.accept(repositoryFactory);
    }
}
