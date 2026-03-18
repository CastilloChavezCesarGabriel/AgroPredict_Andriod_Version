package com.agropredict.presentation.viewmodel.report;

import com.agropredict.application.consumer.IOperationResultConsumer;

public final class ReportResultStrategy implements IOperationResultConsumer {

    private final IReportView view;

    public ReportResultStrategy(IReportView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (completed) {
            view.notify("Reporte generado exitosamente");
            view.showShareOption(resultIdentifier);
        } else {
            view.notify("Error al generar el reporte");
        }
    }
}
