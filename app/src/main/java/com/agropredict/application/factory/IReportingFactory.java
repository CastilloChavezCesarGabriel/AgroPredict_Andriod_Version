package com.agropredict.application.factory;

import com.agropredict.application.repository.IReportRepository;

public interface IReportingFactory {
    IReportRepository createReportRepository();
}