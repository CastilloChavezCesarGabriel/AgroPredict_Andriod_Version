package com.agropredict.notification;

import com.agropredict.application.service.INotificationService;

public final class CapturingNotificationService implements INotificationService {
    private int alertCount = 0;

    @Override
    public void alertSevereDiagnosis() {
        alertCount++;
    }

    public boolean wasAlerted() {
        return alertCount > 0;
    }

    public int alertCount() {
        return alertCount;
    }
}
