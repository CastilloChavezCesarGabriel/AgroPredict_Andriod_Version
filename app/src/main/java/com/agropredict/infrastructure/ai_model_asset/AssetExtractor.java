package com.agropredict.infrastructure.ai_model_asset;

import android.content.Context;
import com.agropredict.application.service.IAssetService;
import com.agropredict.infrastructure.database_backup.FileCopier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class AssetExtractor implements IAssetService {
    private final Context context;

    public AssetExtractor(Context context) {
        this.context = context;
    }

    @Override
    public String extract(String path) throws IOException {
        String fileName = resolve(path);
        File outputFile = new File(context.getCacheDir(), fileName);
        if (!outputFile.exists()) copy(path, outputFile);
        return outputFile.getAbsolutePath();
    }

    private String resolve(String path) {
        int separator = path.lastIndexOf('/');
        return separator >= 0 ? path.substring(separator + 1) : path;
    }

    private void copy(String path, File destination) throws IOException {
        try (InputStream input = context.getAssets().open(path);
             FileOutputStream output = new FileOutputStream(destination)) {
            FileCopier.copy(input, output);
        }
    }
}