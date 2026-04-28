package com.agropredict.presentation.user_interface.screen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import com.agropredict.R;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.usecase.DeleteUseCase;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.crop.ListCropUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticUseCase;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.user_interface.display.DiagnosticHistory;
import com.agropredict.presentation.user_interface.selector.CropSelection;
import com.agropredict.presentation.viewmodel.diagnostic_history.HistoryViewModel;
import com.agropredict.presentation.viewmodel.diagnostic_history.IHistoryView;
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
        bind();
        initialize();
        listen();
    }

    private void bind() {
        diagnosticHistory = new DiagnosticHistory(this);
    }

    private void initialize() {
        IReviewFactory factory = (IReviewFactory) getApplication();
        ListDiagnosticUseCase listUseCase = new ListDiagnosticUseCase(factory.createDiagnosticRepository());
        DeleteUseCase deleteUseCase = new DeleteUseCase(factory.createDiagnosticRepository());
        CheckSessionUseCase sessionUseCase = new CheckSessionUseCase(factory.createSessionRepository());
        cropUseCase = new ListCropUseCase(factory.createCropRepository());
        viewModel = new HistoryViewModel(listUseCase, deleteUseCase, this);
        cropSelection = new CropSelection(findViewById(R.id.spnCropFilter), viewModel::filter);
        sessionUseCase.check((identifier, occupation) -> start(identifier));
    }

    private void listen() {
        diagnosticHistory.listen(this::inspect);
        diagnosticHistory.observe(this::confirm);
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
        IntentExtra.DIAGNOSTIC_IDENTIFIER.attach(intent, diagnosticIdentifier);
        startActivity(intent);
    }
}