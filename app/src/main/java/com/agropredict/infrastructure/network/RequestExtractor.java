package com.agropredict.infrastructure.network;

import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.application.request.SubmissionRequest;
import org.json.JSONException;
import org.json.JSONObject;

public final class RequestExtractor implements ISubmissionVisitor {
    private final JSONObject body;

    public RequestExtractor() {
        this.body = new JSONObject();
    }

    public JSONObject extract(SubmissionRequest request) {
        request.accept(this);
        return body;
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        record("cultivo_detectado", predictedCrop);
        record("confianza", String.valueOf(confidence));
    }

    @Override
    public void visitEnvironment(String temperature, String humidity) {
        record("temperature", temperature);
        record("humidity", humidity);
    }

    @Override
    public void visitRain(String precipitation) {
        record("rain", precipitation);
    }

    @Override
    public void visitSoil(String moisture, String acidity) {
        record("soilMoisture", moisture);
        record("ph", acidity);
    }

    @Override
    public void visitIrrigation(String irrigation, String fertilization) {
        record("irrigation", irrigation);
        record("fertilization", fertilization);
    }

    @Override
    public void visitPestControl(String spraying, String weeds) {
        record("spraying", spraying);
        record("weeds", weeds);
    }

    @Override
    public void visitSymptom(String symptomType, String severity) {
        record("symptom", symptomType);
        record("severity", severity);
    }

    @Override
    public void visitPest(String insects, String animals) {
        record("insects", insects);
        record("animals", animals);
    }

    private void record(String key, String value) {
        try {
            body.put(key, value);
        } catch (JSONException ignored) {
        }
    }
}
