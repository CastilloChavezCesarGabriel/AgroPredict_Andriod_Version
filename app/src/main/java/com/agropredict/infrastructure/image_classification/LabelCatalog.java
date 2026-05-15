package com.agropredict.infrastructure.image_classification;

import android.content.res.AssetManager;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public final class LabelCatalog {
    private final AssetManager assetManager;

    public LabelCatalog(AssetManager assetManager) {
        this.assetManager = Objects.requireNonNull(assetManager, "label catalog requires an asset manager");
    }

    public String[] load(String labelsAsset) {
        try {
            return parse(read(labelsAsset));
        } catch (IOException | JSONException loadFailure) {
            throw new IllegalStateException("Failed to load labels asset '" + labelsAsset
                    + "'. Classifier cannot operate without class names.", loadFailure);
        }
    }

    private String read(String labelsAsset) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(assetManager.open(labelsAsset)))) {
            String line;
            while ((line = reader.readLine()) != null) builder.append(line);
        }
        return builder.toString();
    }

    private String[] parse(String json) throws JSONException {
        JSONArray array = new JSONArray(json);
        String[] labels = new String[array.length()];
        for (int index = 0; index < array.length(); index++) {
            labels[index] = array.getString(index);
        }
        return labels;
    }
}