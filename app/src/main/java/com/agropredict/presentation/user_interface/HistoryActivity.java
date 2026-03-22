package com.agropredict.presentation.user_interface;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.application.DiagnosticHistoryFacade;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.diagnostic.DeleteDiagnosticUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticsUseCase;
import com.agropredict.presentation.viewmodel.history.HistoryViewModel;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.viewmodel.history.IHistoryView;
import com.agropredict.presentation.user_interface.holder.HistoryViewHolder;
import java.util.List;

public final class HistoryActivity extends BaseActivity implements IHistoryView {
    private HistoryViewModel viewModel;
    private HistoryViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        holder = new HistoryViewHolder(this);
        holder.listen(this::inspect);
        holder.observe(this::confirm);
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            ListDiagnosticsUseCase listUseCase = new ListDiagnosticsUseCase(factory.createDiagnosticRepository());
            DeleteDiagnosticUseCase deleteUseCase = new DeleteDiagnosticUseCase(factory.createDiagnosticRepository());
            CheckSessionUseCase sessionUseCase = new CheckSessionUseCase(factory.createSessionRepository());
            DiagnosticHistoryFacade facade = new DiagnosticHistoryFacade(listUseCase, deleteUseCase);
            viewModel = new HistoryViewModel(facade, this);
            sessionUseCase.check(this::start);
        });
    }

    private void start(boolean hasSession, String identifier) {
        viewModel.load(identifier);
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
        holder.display(diagnostics);
    }

    @Override
    public void inspect(String diagnosticIdentifier) {
        Intent intent = new Intent(this, FieldDetailActivity.class);
        intent.putExtra("diagnostic_identifier", diagnosticIdentifier);
        startActivity(intent);
    }

    @Override
    public void empty() {
        holder.empty();
    }
}
