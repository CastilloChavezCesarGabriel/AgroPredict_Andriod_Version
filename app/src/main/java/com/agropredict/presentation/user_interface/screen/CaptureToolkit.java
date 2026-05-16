package com.agropredict.presentation.user_interface.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.util.Objects;

public final class CaptureToolkit {
    private final ComponentActivity activity;
    private final ActivityResultLauncher<Intent> cameraLauncher;
    private final ActivityResultLauncher<Intent> galleryLauncher;
    private final ActivityResultLauncher<String> cameraPermissionLauncher;

    public CaptureToolkit(ComponentActivity activity, IUriConsumer onUri) {
        this.activity = Objects.requireNonNull(activity, "capture toolkit requires an activity");
        Objects.requireNonNull(onUri, "capture toolkit requires a uri consumer");
        this.cameraLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> deliver(result, onUri));
        this.galleryLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> deliver(result, onUri));
        this.cameraPermissionLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), granted -> {
                    if (granted) fire();
                });
    }

    public void capture() {
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            return;
        }
        fire();
    }

    public void browse() {
        galleryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }

    private void fire() {
        cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    private void deliver(ActivityResult result, IUriConsumer onUri) {
        if (result.getResultCode() != ComponentActivity.RESULT_OK) return;
        Intent intent = result.getData();
        if (intent == null) return;
        Uri uri = intent.getData();
        if (uri == null) return;
        onUri.accept(uri);
    }
}
