package com.agropredict.infrastructure.ai_model_asset;

import android.content.Context;
import com.agropredict.application.service.IAssetService;
import com.agropredict.infrastructure.database_backup.FileCopier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class AssetExtractor implements IAssetService {
    private final Context context;

    public AssetExtractor(Context context) {
        this.context = Objects.requireNonNull(context, "asset extractor requires a context");
    }

    @Override
    public String extract(String path) throws IOException {
        File outputFile = new File(context.getCacheDir(), new File(path).getName());
        if (!outputFile.exists()) {
            copy(path, outputFile);
        }
        return outputFile.getAbsolutePath();
    }

    private void copy(String path, File destination) throws IOException {
        try (InputStream input = context.getAssets().open(path);
             FileOutputStream output = new FileOutputStream(destination)) {
            FileCopier.copy(input, output);
        }
    }
}