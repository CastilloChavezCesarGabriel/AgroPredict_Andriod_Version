package com.agropredict.application.visitor;

public interface IQuestionnaireVisitor {
    void visitEnvironment(String temperature, String humidity);
    void visitRain(String precipitation);
    void visitSoil(String moisture, String acidity);
    void visitIrrigation(String irrigation, String fertilization);
    void visitPestControl(String spraying, String weeds);
    void visitSymptom(String symptomType, String severity);
    void visitPest(String insects, String animals);
}
