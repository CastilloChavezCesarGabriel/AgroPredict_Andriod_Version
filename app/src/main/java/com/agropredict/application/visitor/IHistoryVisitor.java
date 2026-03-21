package com.agropredict.application.visitor;

import com.agropredict.application.result.HistoryTransition;
import com.agropredict.application.result.Modification;

public interface IHistoryVisitor {
    void visit(Modification modification, HistoryTransition transition);
}