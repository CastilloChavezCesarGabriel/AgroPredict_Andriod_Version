package com.agropredict.presentation.user_interface;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.result.HistoryRecord;
import com.agropredict.application.result.HistoryTransition;
import com.agropredict.application.visitor.IHistoryVisitor;
import com.agropredict.application.result.Modification;
import com.agropredict.application.usecase.crop.TraceCropHistoryUseCase;
import com.agropredict.presentation.viewmodel.field.CropHistoryViewModel;
import com.agropredict.presentation.viewmodel.field.ICropHistoryView;
import java.util.ArrayList;
import java.util.List;

public final class CropHistoryActivity extends BaseActivity
        implements ICropHistoryView, IHistoryVisitor {
    private CropHistoryViewModel viewModel;
    private ListView historyListView;
    private TextView emptyLabel;
    private final List<String> labels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_history);
        historyListView = findViewById(R.id.lvHistory);
        emptyLabel = findViewById(R.id.tvEmpty);
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            TraceCropHistoryUseCase useCase = new TraceCropHistoryUseCase(factory.createCropRepository());
            viewModel = new CropHistoryViewModel(useCase, this);
        });
        String identifier = getIntent().getStringExtra("crop_identifier");
        if (identifier != null) viewModel.load(identifier);
    }

    @Override
    public void display(List<HistoryRecord> records) {
        labels.clear();
        for (HistoryRecord record : records) {
            record.accept(this);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, labels);
        historyListView.setAdapter(adapter);
    }

    @Override
    public void visit(Modification modification, HistoryTransition transition) {
        StringBuilder label = new StringBuilder();
        modification.describe(label);
        label.append(": ");
        transition.format(label);
        label.append(" (");
        modification.date(label);
        label.append(")");
        labels.add(label.toString());
    }

    @Override
    public void empty() {
        historyListView.setVisibility(View.GONE);
        emptyLabel.setVisibility(View.VISIBLE);
    }
}