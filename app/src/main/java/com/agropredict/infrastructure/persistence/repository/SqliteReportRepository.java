package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.domain.entity.Report;
import com.agropredict.infrastructure.persistence.Database;
import com.agropredict.infrastructure.persistence.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.ReportPersistenceVisitor;

public final class SqliteReportRepository extends SqliteRepository<Report> implements IReportRepository {
    public SqliteReportRepository(Database database) {
        super(database, "report");
    }

    @Override
    protected void persist(Report report, SqliteRow row) {
        report.accept(new ReportPersistenceVisitor(row));
    }
}