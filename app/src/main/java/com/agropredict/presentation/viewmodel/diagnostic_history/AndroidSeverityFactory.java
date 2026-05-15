package com.agropredict.presentation.viewmodel.diagnostic_history;

import android.content.Context;
import com.agropredict.R;
import com.agropredict.application.factory.ISeverityFactory;
import com.agropredict.domain.diagnostic.severity.HealthySeverity;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.severity.ModerateSeverity;
import com.agropredict.domain.diagnostic.severity.PendingSeverity;
import com.agropredict.domain.diagnostic.severity.SevereSeverity;
import com.agropredict.domain.diagnostic.severity.UnknownSeverity;
import java.util.Objects;

public final class AndroidSeverityFactory implements ISeverityFactory {
    private final Context context;

    public AndroidSeverityFactory(Context context) {
        this.context = Objects.requireNonNull(context, "android severity factory requires a context");
    }

    @Override
    public ISeverity createHealthy() {
        return new HealthySeverity(() -> context.getString(R.string.severity_label_low));
    }

    @Override
    public ISeverity createModerate() {
        return new ModerateSeverity(() -> context.getString(R.string.severity_label_moderate));
    }

    @Override
    public ISeverity createSevere() {
        return new SevereSeverity(() -> context.getString(R.string.severity_label_high));
    }

    @Override
    public ISeverity createUnknown() {
        return new UnknownSeverity(() -> context.getString(R.string.severity_label_unknown));
    }

    @Override
    public ISeverity createPending() {
        return new PendingSeverity(() -> context.getString(R.string.severity_label_pending));
    }
}
