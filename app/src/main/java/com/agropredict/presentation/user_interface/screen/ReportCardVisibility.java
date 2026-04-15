package com.agropredict.presentation.user_interface.screen;

import android.view.View;
import com.agropredict.domain.IOccupationHandler;

public final class ReportCardVisibility implements IOccupationHandler {
    private final View reportCard;

    public ReportCardVisibility(View reportCard) {
        this.reportCard = reportCard;
    }

    @Override
    public void onAdvanced() {
        reportCard.setVisibility(View.VISIBLE);
    }
}