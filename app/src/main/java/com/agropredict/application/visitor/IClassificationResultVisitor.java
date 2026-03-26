package com.agropredict.application.visitor;

import com.agropredict.domain.visitor.diagnostic.IPredictionVisitor;

public interface IClassificationResultVisitor extends IPredictionVisitor {
    void reject(String errorMessage);
}