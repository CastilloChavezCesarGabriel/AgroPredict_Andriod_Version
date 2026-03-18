package com.agropredict.application.repository;

import com.agropredict.domain.entity.Report;

public interface IReportRepository {
    void store(Report report);
}