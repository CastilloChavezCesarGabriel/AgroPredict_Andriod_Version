package com.agropredict.core;

import android.content.Context;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.factory.IPredictionFactory;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.domain.diagnostic.SeverityClassifier;
import com.agropredict.infrastructure.api_integration.GravitySeverityFactory;
import com.agropredict.infrastructure.database_backup.DatabaseBackupSchedule;
import com.agropredict.infrastructure.factory.AndroidAccessFactory;
import com.agropredict.infrastructure.factory.AndroidCatalogFactory;
import com.agropredict.infrastructure.factory.AndroidDashboardFactory;
import com.agropredict.infrastructure.factory.AndroidPredictionFactory;
import com.agropredict.infrastructure.factory.AndroidReportingFactory;
import com.agropredict.infrastructure.factory.AndroidReviewFactory;
import com.agropredict.infrastructure.persistence.database.Database;
import java.util.List;

public final class Configuration {
    private final Database database;
    private final Context context;
    private final ISeverityFactory severityFactory;

    public Configuration(Context context) {
        this.database = new Database(context);
        this.context = context;
        this.severityFactory = createSeverityFactory();
    }

    public IAccessFactory createAccess() {
        return new AndroidAccessFactory(database, context);
    }

    public IDashboardFactory createDashboard() {
        return new AndroidDashboardFactory(context);
    }

    public ICatalogFactory createCatalog() {
        return new AndroidCatalogFactory(database, context);
    }

    public IReviewFactory createReview() {
        return new AndroidReviewFactory(database, context, severityFactory);
    }

    public IPredictionFactory createPrediction() {
        return new AndroidPredictionFactory(database, context, severityFactory);
    }

    public IReportingFactory createReporting() {
        return new AndroidReportingFactory(database, context, severityFactory);
    }

    public DatabaseBackupSchedule createBackup() {
        return new DatabaseBackupSchedule(database, context);
    }

    private ISeverityFactory createSeverityFactory() {
        Severity healthyLabel = new Severity("low", "Healthy", 0);
        Severity moderateLabel = new Severity("moderate", "Moderate issue", 1);
        Severity severeLabel = new Severity("high", "Severe issue", 2);
        Severity unknown = new Severity(null, "Analysis complete", 0);
        SeverityClassifier healthy = new SeverityClassifier(List.of("bajo", "low"), healthyLabel);
        SeverityClassifier moderate = new SeverityClassifier(List.of("moderado", "moderate"), moderateLabel);
        SeverityClassifier severe = new SeverityClassifier(List.of("alto", "high", "critico", "critical"), severeLabel);
        return new GravitySeverityFactory(List.of(healthy, moderate, severe), unknown);
    }
}
