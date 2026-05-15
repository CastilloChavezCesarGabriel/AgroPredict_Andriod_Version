package com.agropredict.presentation.user_interface.form;

import android.app.Activity;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Condition;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.CropCare;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Questionnaire;
import com.agropredict.presentation.user_interface.questionnaire_input.EnvironmentInput;
import com.agropredict.presentation.user_interface.questionnaire_input.ManagementInput;
import com.agropredict.presentation.user_interface.questionnaire_input.SoilInput;
import com.agropredict.presentation.user_interface.questionnaire_input.SymptomInput;

public final class QuestionnaireForm {
    private final EnvironmentInput environmentGroup;
    private final SoilInput soilGroup;
    private final ManagementInput managementGroup;
    private final SymptomInput symptomGroup;

    public QuestionnaireForm(Activity activity) {
        this.environmentGroup = new EnvironmentInput(activity);
        this.soilGroup = new SoilInput(activity);
        this.managementGroup = new ManagementInput(activity);
        this.symptomGroup = new SymptomInput(activity);
    }

    public Questionnaire assemble() {
        Condition condition = new Condition(environmentGroup.collect(), soilGroup.collect());
        CropCare cropCare = new CropCare(managementGroup.collect(), symptomGroup.collect());
        return new Questionnaire(condition, cropCare);
    }
}
