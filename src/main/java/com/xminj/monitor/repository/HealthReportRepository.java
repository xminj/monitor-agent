package com.xminj.monitor.repository;

import com.xminj.monitor.domain.HealthReport;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.transaction.annotation.Transactional;

import java.util.List;


@JdbcRepository(dialect = Dialect.H2)
public interface HealthReportRepository extends CrudRepository<HealthReport,Long> {

    @Query(value = "SELECT * FROM agent_health_report WHERE uploaded = false ORDER BY id DESC",
            countQuery = "SELECT count(*) FROM agent_health_report WHERE uploaded = false",nativeQuery = true)
    Page<HealthReport> pageReportData(Pageable pageable);



    @Transactional
    @Query(value = "UPDATE agent_health_report SET uploaded = true WHERE id IN (:ids)", nativeQuery = true)
    int markAsUploadedByIds(List<Long> ids);
}
