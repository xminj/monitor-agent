package com.xminj.monitor.repository;

import com.xminj.monitor.domain.HealthReport;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.H2)
public interface HealthReportRepository extends CrudRepository<HealthReport,Long> {
}
