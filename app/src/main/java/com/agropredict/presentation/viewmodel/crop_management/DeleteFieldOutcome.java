package com.agropredict.presentation.viewmodel.crop_management;

import android.app.Activity;
import android.widget.Toast;
import com.agropredict.R;
import com.agropredict.application.operation_result.IOperationResult;
import java.util.Objects;

public final class DeleteFieldOutcome implements IOperationResult {
    private final Activity activity;

    public DeleteFieldOutcome(Activity activity) {
        this.activity = Objects.requireNonNull(activity, "delete field outcome requires an activity");
    }

    @Override
    public void onSucceed(String value) {
        Toast.makeText(activity, R.string.field_deleted, Toast.LENGTH_SHORT).show();
        activity.finish();
    }

    @Override
    public void onFail() {
        Toast.makeText(activity, R.string.error_general, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReject(String reason) {
        Toast.makeText(activity, R.string.error_general, Toast.LENGTH_SHORT).show();
    }
}
