package com.agropredict.presentation.user_interface.holder;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.agropredict.R;
import java.io.File;
import java.util.Locale;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.domain.component.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.component.diagnostic.DiagnosticConditions;
import com.agropredict.domain.component.diagnostic.DiagnosticContent;
import com.agropredict.domain.component.diagnostic.DiagnosticContext;
import com.agropredict.domain.component.diagnostic.DiagnosticData;
import com.agropredict.domain.component.diagnostic.DiagnosticEnvironment;
import com.agropredict.domain.component.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.component.diagnostic.DiagnosticSummary;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.entity.CropImage;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.visitor.crop.ICropImageVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticAssessmentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticConditionsVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticContentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticContextVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticDataVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticOwnershipVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticSummaryVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import com.agropredict.domain.visitor.diagnostic.IPredictionVisitor;

public final class FieldDetailViewHolder implements IDiagnosticVisitor,
        IDiagnosticDataVisitor, IDiagnosticContentVisitor,
        IDiagnosticConditionsVisitor, IDiagnosticContextVisitor,
        IDiagnosticAssessmentVisitor, IPredictionVisitor,
        IDiagnosticSummaryVisitor, IDiagnosticOwnershipVisitor,
        ICropImageVisitor {

    private final TextView cropNameLabel;
    private final TextView confidenceLabel;
    private final TextView severityLabel;
    private final TextView recommendationsLabel;
    private final ImageView cropPhotoView;
    private final ICropImageRepository imageRepository;

    public FieldDetailViewHolder(Activity activity, ICropImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        cropNameLabel = activity.findViewById(R.id.tvCropType);
        confidenceLabel = activity.findViewById(R.id.tvConfidence);
        severityLabel = activity.findViewById(R.id.tvSeverity);
        recommendationsLabel = activity.findViewById(R.id.tvRecommendations);
        cropPhotoView = activity.findViewById(R.id.ivCropPhoto);
    }

    public void display(Diagnostic diagnostic) {
        diagnostic.accept(this);
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        data.accept(this);
    }

    @Override
    public void visit(Prediction prediction, DiagnosticContent content) {
        if (prediction != null) prediction.accept(this);
        if (content != null) content.accept(this);
    }

    @Override
    public void visit(DiagnosticConditions conditions, DiagnosticAssessment assessment) {
        if (conditions != null) conditions.accept(this);
        if (assessment != null) assessment.accept(this);
    }

    @Override
    public void visit(DiagnosticContext context, DiagnosticEnvironment environment) {
        if (context != null) context.accept(this);
    }

    @Override
    public void visitContext(String cropIdentifier, String imageIdentifier) {
        if (imageIdentifier == null) return;
        CropImage image = imageRepository.find(imageIdentifier);
        if (image != null) image.accept(this);
    }

    @Override
    public void visit(String identifier, String filePath) {
        if (filePath == null) return;
        File file = new File(filePath);
        if (file.exists()) {
            cropPhotoView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            cropPhotoView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void visit(DiagnosticSummary summary, DiagnosticOwnership ownership) {
        if (summary != null) summary.accept(this);
        if (ownership != null) ownership.accept(this);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        cropNameLabel.setText(predictedCrop);
        confidenceLabel.setText(String.format(Locale.getDefault(), "%.0f%%", confidence * 100));
    }

    @Override
    public void visitSummary(String severity, String shortSummary) {
        severityLabel.setText(severity);
    }

    @Override
    public void visitOwnership(String userIdentifier, String recommendationText) {
        recommendationsLabel.setText(recommendationText);
    }
}
