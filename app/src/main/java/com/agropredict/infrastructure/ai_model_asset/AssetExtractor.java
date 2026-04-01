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
    public String extract(String assetPath) throws IOException {
        String fileName = resolve(assetPath);
        File outputFile = new File(context.getCacheDir(), fileName);
        if (!outputFile.exists()) copy(assetPath, outputFile);
        return outputFile.getAbsolutePath();
    }

    private String resolve(String assetPath) {
        int separator = assetPath.lastIndexOf('/');
        return separator >= 0 ? assetPath.substring(separator + 1) : assetPath;
    }

    private void copy(String assetPath, File destination) throws IOException {
        try (InputStream input = context.getAssets().open(assetPath);
             FileOutputStream output = new FileOutputStream(destination)) {
            FileCopier.copy(input, output);
        }
    }
}