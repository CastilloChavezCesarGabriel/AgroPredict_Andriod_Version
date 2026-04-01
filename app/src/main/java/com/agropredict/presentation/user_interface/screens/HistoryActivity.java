package com.agropredict.presentation.user_interface.screens;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.application.facade.DiagnosticHistoryFacade;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.crop.ListCropUseCase;
import com.agropredict.application.usecase.diagnostic.DeleteDiagnosticUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticUseCase;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.viewmodel.history.HistoryViewModel;
import com.agropredict.presentation.viewmodel.history.IHistoryView;
import com.agropredict.presentation.user_interface.component.CropSelection;
import com.agropredict.presentation.user_interface.component.DiagnosticHistory;
import java.util.List;

public final class HistoryActivity extends BaseActivity implements IHistoryView {
    private HistoryViewModel viewModel;
    private DiagnosticHistory diagnosticHistory;
    private CropSelection cropSelection;
    private ListCropUseCase cropUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        diagnosticHistory = new DiagnosticHistory(this);
        diagnosticHistory.listen(this::inspect);
        diagnosticHistory.observe(this::confirm);
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            ListDiagnosticUseCase listUseCase = new ListDiagnosticUseCase(factory.createDiagnosticRepository());
            DeleteDiagnosticUseCase deleteUseCase = new DeleteDiagnosticUseCase(factory.createDiagnosticRepository());
            CheckSessionUseCase sessionUseCase = new CheckSessionUseCase(factory.createSessionRepository());
            cropUseCase = new ListCropUseCase(factory.createCropRepository());
            DiagnosticHistoryFacade facade = new DiagnosticHistoryFacade(listUseCase, deleteUseCase);
            viewModel = new HistoryViewModel(facade, this);
            cropSelection = new CropSelection(findViewById(R.id.spnCropFilter), viewModel::filter);
            sessionUseCase.check((identifier, occupation) -> start(identifier));
        });
    }

    private void start(String identifier) {
        if (identifier == null) return;
        viewModel.load(identifier);
        cropSelection.populate(cropUseCase.list(identifier));
    }

    private void confirm(String diagnosticIdentifier) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_diagnostic)
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(R.string.confirm,
                (dialog, which) -> viewModel.delete(diagnosticIdentifier))
            .setNegativeButton(R.string.cancel, null)
            .show();
    }

    @Override
    public void display(List<Diagnostic> diagnostics) {
        diagnosticHistory.display(diagnostics);
    }

    @Override
    public void inspect(String diagnosticIdentifier) {
        Intent intent = new Intent(this, FieldDetailActivity.class);
        intent.putExtra("diagnostic_identifier", diagnosticIdentifier);
        startActivity(intent);
    }

    @Override
    public void empty() {
        diagnosticHistory.empty();
    }
}
