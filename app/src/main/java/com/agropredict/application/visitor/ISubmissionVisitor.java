package com.agropredict.application.visitor;

public interface ISubmissionVisitor extends IQuestionnaireVisitor {
    void visitPrediction(String predictedCrop, double confidence);
}