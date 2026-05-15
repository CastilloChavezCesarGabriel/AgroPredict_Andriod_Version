package com.agropredict.presentation.user_interface.form;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.agropredict.R;
import com.agropredict.application.diagnostic_submission.request.ExistingCropReference;
import com.agropredict.application.diagnostic_submission.request.ICropReference;
import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.application.diagnostic_submission.request.NewCropReference;
import com.agropredict.application.diagnostic_submission.request.PhotographInput;
import com.agropredict.application.diagnostic_submission.request.Submission;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.Field;
import com.agropredict.domain.crop.GrowthCycle;
import com.agropredict.domain.crop.Plot;
import com.agropredict.domain.crop.Soil;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;
import com.agropredict.presentation.user_interface.selector.CropChoiceSelection;
import java.util.List;
import java.util.Locale;

public final class PredictionForm {
    private final Activity activity;
    private final CropChoiceSelection cropChoiceSelection;
    private final ICatalogRepository soilTypes;
    private final ICatalogRepository stages;
    private final QuestionnaireForm questionnaire;

    public PredictionForm(Activity activity, ICatalogRepository soilTypes, ICatalogRepository stages) {
        this.activity = activity;
        this.soilTypes = soilTypes;
        this.stages = stages;
        Spinner spinner = activity.findViewById(R.id.spnCropChoice);
        this.cropChoiceSelection = new CropChoiceSelection(spinner, this::toggle);
        this.questionnaire = new QuestionnaireForm(activity);
    }

    public void load() {
        ProgressBar bar = activity.findViewById(R.id.progressLoading);
        bar.setVisibility(View.VISIBLE);
    }

    public void rest() {
        ProgressBar bar = activity.findViewById(R.id.progressLoading);
        bar.setVisibility(View.GONE);
    }

    public void classify(String cropName, double confidence) {
        TextView label = activity.findViewById(R.id.etCropType);
        String formatted = String.format(Locale.getDefault(), "%.0f%%", confidence * 100);
        label.setText(activity.getString(R.string.classification_result, cropName, formatted));
        label.setVisibility(View.VISIBLE);
    }

    public void stamp(String date) {
        EditText input = activity.findViewById(R.id.etPlantingDate);
        input.setText(date);
    }

    public void preview(Uri imageUri) {
        ImageView preview = activity.findViewById(R.id.ivCropPhoto);
        preview.setImageURI(imageUri);
    }

    public void furnish(SoilTypeOption option) {
        option.populate(activity.findViewById(R.id.spnSoilType));
    }

    public void arrange(StageOption option) {
        option.populate(activity.findViewById(R.id.spnStage));
    }

    public void offer(List<Crop> crops) {
        cropChoiceSelection.populate(crops);
    }

    public SubmissionRequest collect(ImagePrediction prediction, PhotographInput image) {
        String existingId = cropChoiceSelection.resolve();
        ICropReference reference = existingId != null ? new ExistingCropReference(existingId) : produce();
        return new SubmissionRequest(new Submission(prediction, image), questionnaire.assemble(), reference);
    }

    private ICropReference produce() {
        String cropId = IdentifierFactory.generate("crop");
        return new NewCropReference(cropId, build(cropId));
    }

    private Crop build(String cropId) {
        Field field = new Field(read(R.id.etFieldName), read(R.id.etLocation));
        Soil soil = new Soil(soilTypes.resolve(pick(R.id.spnSoilType)), read(R.id.etArea));
        GrowthCycle cycle = new GrowthCycle(read(R.id.etPlantingDate), stages.resolve(pick(R.id.spnStage)));
        return new Crop(cropId, read(R.id.etCropType), new CropProfile(new Plot(field, soil), cycle));
    }

    private String read(int viewId) {
        EditText input = activity.findViewById(viewId);
        return input.getText().toString().trim();
    }

    private String pick(int spinnerId) {
        Spinner spinner = activity.findViewById(spinnerId);
        return spinner.getSelectedItem().toString();
    }

    private void toggle(String selectedIdentifier) {
        View group = activity.findViewById(R.id.groupNewCropFields);
        group.setVisibility(selectedIdentifier == null ? View.VISIBLE : View.GONE);
    }
}
