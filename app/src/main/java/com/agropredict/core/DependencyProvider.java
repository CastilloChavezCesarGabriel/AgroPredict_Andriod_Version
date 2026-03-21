package com.agropredict.core;

import android.content.Context;
import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.IFactoryConsumer;
import com.agropredict.infrastructure.RepositoryFactory;
import com.agropredict.infrastructure.persistence.DatabaseHelper;

public final class DependencyProvider {
    private final IRepositoryFactory repositoryFactory;

    public DependencyProvider(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        this.repositoryFactory = new RepositoryFactory(databaseHelper, context);
    }

    public void provide(IFactoryConsumer consumer) {
        consumer.accept(repositoryFactory);
    }
}
