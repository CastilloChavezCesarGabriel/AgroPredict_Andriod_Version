package com.agropredict.application.factory;

import com.agropredict.application.repository.ICropRecord;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ISessionRepository;
import java.util.List;

public interface IReviewFactory {
    IDiagnosticRepository createDiagnosticRepository();
    ICropRepository createCropRepository();
    IPhotographRepository createPhotographRepository();
    ISessionRepository createSessionRepository();
    List<ICropRecord> createCropRecord();
}
