package com.agropredict.presentation.user_interface.screen;

import android.content.Intent;

public enum IntentExtra {
    CROP_IDENTIFIER("crop_identifier"),
    DIAGNOSTIC_IDENTIFIER("diagnostic_identifier");

    private final String key;

    IntentExtra(String key) {
        this.key = key;
    }

    public void attach(Intent intent, String value) {
        intent.putExtra(key, value);
    }

    public String read(Intent intent) {
        return intent.getStringExtra(key);
    }
}