package com.agropredict.presentation.user_interface.holder;

import com.agropredict.presentation.user_interface.input.CatalogInput;
import com.agropredict.presentation.user_interface.input.EnvironmentInput;
import com.agropredict.presentation.user_interface.input.ManagementInput;
import com.agropredict.presentation.user_interface.input.SoilInput;
import com.agropredict.presentation.user_interface.input.SymptomInput;
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
import com.agropredict.presentation.user_interface.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.input.StageCatalog;

public final class QuestionnaireHolder {
    private final CatalogInput catalogGroup;
    private final EnvironmentInput environmentGroup;
    private final SoilInput soilGroup;
    private final ManagementInput managementGroup;
    private final SymptomInput symptomGroup;

    public QuestionnaireHolder(Activity activity) {
        this.catalogGroup = new CatalogInput(activity);
        this.environmentGroup = new EnvironmentInput(activity);
        this.soilGroup = new SoilInput(activity);
        this.managementGroup = new ManagementInput(activity);
        this.symptomGroup = new SymptomInput(activity);
    }

    public void populate(SoilTypeCatalog soilTypeOption) {
        catalogGroup.populate(soilTypeOption);
    }

    public void populate(StageCatalog stageOption) {
        catalogGroup.populate(stageOption);
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