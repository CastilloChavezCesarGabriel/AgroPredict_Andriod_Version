package com.agropredict.presentation.user_interface.holder;
import com.agropredict.presentation.user_interface.spinner.CatalogSpinnerGroup;
import com.agropredict.presentation.user_interface.spinner.EnvironmentSpinnerGroup;
import com.agropredict.presentation.user_interface.spinner.ManagementSpinnerGroup;
import com.agropredict.presentation.user_interface.spinner.SoilSpinnerGroup;
import com.agropredict.presentation.user_interface.spinner.SymptomSpinnerGroup;

import android.app.Activity;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.application.request.data.Condition;
import com.agropredict.application.request.data.CropCare;
import com.agropredict.application.request.data.Field;
import com.agropredict.application.request.data.Classification;
import com.agropredict.application.request.data.Questionnaire;
import com.agropredict.application.request.input.Cultivation;
import com.agropredict.application.request.input.Submission;
import com.agropredict.application.request.input.Photograph;
import java.util.List;

public final class QuestionnaireHolder {
    private final CatalogSpinnerGroup catalogGroup;
    private final EnvironmentSpinnerGroup environmentGroup;
    private final SoilSpinnerGroup soilGroup;
    private final ManagementSpinnerGroup managementGroup;
    private final SymptomSpinnerGroup symptomGroup;

    public QuestionnaireHolder(Activity activity) {
        this.catalogGroup = new CatalogSpinnerGroup(activity);
        this.environmentGroup = new EnvironmentSpinnerGroup(activity);
        this.soilGroup = new SoilSpinnerGroup(activity);
        this.managementGroup = new ManagementSpinnerGroup(activity);
        this.symptomGroup = new SymptomSpinnerGroup(activity);
    }

    public void populateSoilTypes(List<String> soilTypes) {
        catalogGroup.populateSoilTypes(soilTypes);
    }

    public void populateStages(List<String> stages) {
        catalogGroup.populateStages(stages);
    }

    public SubmissionRequest assemble(Classification prediction, Photograph image) {
        Cultivation crop = prediction.cultivate(catalogGroup.stage());
        Field field = new Field(crop, image);
        Submission diagnostic = new Submission(prediction, field);
        return new SubmissionRequest(diagnostic, collect());
    }

    private Questionnaire collect() {
        Condition condition = new Condition(environmentGroup.collect(), soilGroup.collect());
        CropCare cropCare = new CropCare(managementGroup.collect(), symptomGroup.collect());
        return new Questionnaire(condition, cropCare);
    }
}
