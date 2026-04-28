package com.agropredict.presentation.user_interface.screen;

import android.view.View;
import com.agropredict.domain.IOccupationVisitor;

public final class ReportCardVisibility implements IOccupationVisitor {
    private final View reportCard;

    public ReportCardVisibility(View reportCard) {
        this.reportCard = reportCard;
    }

    @Override
    public void visit(String label, boolean advanced) {
        if (advanced) reportCard.setVisibility(View.VISIBLE);
    }
}