package com.agropredict.presentation.user_interface.form;

import com.agropredict.presentation.user_interface.catalog_input.CatalogInput;
import com.agropredict.presentation.user_interface.questionnaire_input.EnvironmentInput;
import com.agropredict.presentation.user_interface.questionnaire_input.ManagementInput;
import com.agropredict.presentation.user_interface.questionnaire_input.SoilInput;
import com.agropredict.presentation.user_interface.questionnaire_input.SymptomInput;
import android.app.Activity;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.request.ai_questionnaire.Condition;
import com.agropredict.application.request.ai_questionnaire.CropCare;
import com.agropredict.application.request.diagnostic_submission.ImagePrediction;
import com.agropredict.application.request.ai_questionnaire.Questionnaire;
import com.agropredict.application.request.diagnostic_submission.Cultivation;
import com.agropredict.application.request.diagnostic_submission.Submission;
import com.agropredict.application.request.diagnostic_submission.PhotographInput;
import com.agropredict.application.request.diagnostic_submission.DiagnosticSubject;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;

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

    public void populate(SoilTypeOption soilTypeOption) {
        catalogGroup.populate(soilTypeOption);
    }

    public void populate(StageOption stageOption) {
        catalogGroup.populate(stageOption);
    }

    public SubmissionRequest assemble(ImagePrediction prediction, PhotographInput image) {
        Cultivation crop = prediction.produce(catalogGroup.extract());
        DiagnosticSubject subject = new DiagnosticSubject(crop, image);
        Submission diagnostic = new Submission(prediction, subject);
        return new SubmissionRequest(diagnostic, collect());
    }

    private Questionnaire collect() {
        Condition condition = new Condition(environmentGroup.collect(), soilGroup.collect());
        CropCare cropCare = new CropCare(managementGroup.collect(), symptomGroup.collect());
        return new Questionnaire(condition, cropCare);
    }
}