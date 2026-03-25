package com.agropredict.presentation.user_interface;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public void notify(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void navigate(Class<? extends AppCompatActivity> target) {
        startActivity(new Intent(this, target));
    }

    protected void redirect(Class<? extends AppCompatActivity> target) {
        Intent intent = new Intent(this, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}