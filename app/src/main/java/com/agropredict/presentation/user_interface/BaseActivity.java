package com.agropredict.presentation.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
    public void notify(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void navigate(Class<? extends Activity> target) {
        startActivity(new Intent(this, target));
    }

    protected void redirect(Class<? extends Activity> target) {
        Intent intent = new Intent(this, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}