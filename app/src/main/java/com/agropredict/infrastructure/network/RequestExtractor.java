package com.agropredict.infrastructure.network;

import com.agropredict.domain.value.diagnostic.DiagnosticContent;
import com.agropredict.domain.value.diagnostic.DiagnosticData;
import com.agropredict.domain.value.diagnostic.Prediction;
import com.agropredict.domain.visitor.IDiagnosticDataVisitor;
import com.agropredict.domain.visitor.IDiagnosticVisitor;
import com.agropredict.domain.visitor.IPredictionVisitor;
import org.json.JSONException;
import org.json.JSONObject;

public final class RequestExtractor implements IDiagnosticVisitor,
        IDiagnosticDataVisitor, IPredictionVisitor {

    private String predictedCrop;
    private double confidence;

    @Override
    public void visit(String identifier, DiagnosticData data) {
        data.accept(this);
    }

    @Override
    public void visit(Prediction prediction, DiagnosticContent content) {
        if (prediction != null) prediction.accept(this);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
    }

    public JSONObject buildBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("cultivo_detectado", predictedCrop);
        body.put("confianza", confidence);
        return body;
    }
}
