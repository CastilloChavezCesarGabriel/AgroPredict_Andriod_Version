package com.agropredict.infrastructure.persistence.schema;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.AnswerKey;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public final class AssetSeedLoader {
    private static final String SEED_ASSET = "seed.json";
    private final SQLiteDatabase database;
    private final AssetManager assets;

    public AssetSeedLoader(SQLiteDatabase database, AssetManager assets) {
        this.database = Objects.requireNonNull(database, "seed loader requires a database");
        this.assets = Objects.requireNonNull(assets, "seed loader requires an asset manager");
    }

    public void load() {
        try {
            JSONObject root = read();
            populate(root.getJSONObject("catalogs"));
            register(root.getJSONArray("questions"));
        } catch (IOException | JSONException failure) {
            throw new IllegalStateException("seed asset load failed", failure);
        }
    }

    private JSONObject read() throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(assets.open(SEED_ASSET)))) {
            String line;
            while ((line = reader.readLine()) != null) builder.append(line);
        }
        return new JSONObject(builder.toString());
    }

    private void populate(JSONObject catalogs) throws JSONException {
        for (CatalogName catalog : CatalogName.values()) {
            catalog.populate(database, decode(catalogs.getJSONArray(catalog.name())));
        }
    }

    private void register(JSONArray questions) throws JSONException {
        for (int index = 0; index < questions.length(); index++) {
            JSONObject question = questions.getJSONObject(index);
            AnswerKey key = AnswerKey.valueOf(question.getString("key"));
            String text = question.getString("text");
            int position = index + 1;
            String[] options = decode(question.getJSONArray("options"));
            key.expose(identifier -> new QuestionSeed(identifier, text, position).load(database));
            key.expose(identifier -> new OptionSeed(identifier, options).load(database));
        }
    }

    private String[] decode(JSONArray array) throws JSONException {
        String[] values = new String[array.length()];
        for (int index = 0; index < array.length(); index++) {
            values[index] = array.getString(index);
        }
        return values;
    }
}