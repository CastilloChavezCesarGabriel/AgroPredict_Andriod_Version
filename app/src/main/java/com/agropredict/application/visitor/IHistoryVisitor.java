package com.agropredict.application.visitor;

import com.agropredict.application.operation_result.HistoryTransition;
import com.agropredict.application.operation_result.Modification;

public interface IHistoryVisitor {
    void visit(Modification modification, HistoryTransition transition);
}