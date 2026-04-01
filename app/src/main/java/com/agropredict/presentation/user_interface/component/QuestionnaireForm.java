package com.agropredict.presentation.user_interface.component;

import com.agropredict.presentation.user_interface.component.input.CatalogInput;
import com.agropredict.presentation.user_interface.component.input.EnvironmentInput;
import com.agropredict.presentation.user_interface.component.input.ManagementInput;
import com.agropredict.presentation.user_interface.component.input.SoilInput;
import com.agropredict.presentation.user_interface.component.input.SymptomInput;
import android.app.Activity;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.request.ai_questionnaire.Condition;
import com.agropredict.application.request.ai_questionnaire.CropCare;
import com.agropredict.application.request.diagnostic_submission.Field;
import com.agropredict.application.request.diagnostic_submission.Classification;
import com.agropredict.application.request.ai_questionnaire.Questionnaire;
import com.agropredict.application.request.diagnostic_submission.Cultivation;
import com.agropredict.application.request.diagnostic_submission.Submission;
import com.agropredict.application.request.diagnostic_submission.PhotographInput;
import com.agropredict.presentation.user_interface.component.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.component.input.StageCatalog;

public final class QuestionnaireForm {
    private final CatalogInput catalogGroup;
    private final EnvironmentInput environmentGroup;
    private final SoilInput soilGroup;
    private final ManagementInput managementGroup;
    private final SymptomInput symptomGroup;

    public QuestionnaireForm(Activity activity) {
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

    public SubmissionRequest assemble(Classification prediction, PhotographInput image) {
        Cultivation crop = prediction.cultivate(catalogGroup.extract());
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