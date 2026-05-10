package com.agropredict.presentation.user_interface.screen;

import android.view.View;
import com.agropredict.domain.user.IOccupationListener;

public final class AdvancedReportCardReveal implements IOccupationListener {
    private final View reportCard;

    public AdvancedReportCardReveal(View reportCard) {
        this.reportCard = reportCard;
    }

    @Override
    public void onElevate(String label) {
        reportCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLimit(String label) {}
}