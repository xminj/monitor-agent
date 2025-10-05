package com.xminj.monitor.repository;

import com.xminj.monitor.domain.HardwareMetrics;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.transaction.annotation.Transactional;

import java.util.List;


@JdbcRepository(dialect = Dialect.H2)
public interface HardwareMetricsRepository extends CrudRepository<HardwareMetrics,Long> {

    @Query(value = "SELECT * FROM hardware_metrics WHERE uploaded = false ORDER BY id DESC",
            countQuery = "SELECT count(*) FROM hardware_metrics WHERE uploaded = false",nativeQuery = true)
    Page<HardwareMetrics> pageReportData(Pageable pageable);

    @Transactional
    @Query(value = "UPDATE hardware_metrics SET uploaded = true WHERE id IN (:ids)", nativeQuery = true)
    int markAsUploadedByIds(List<Long> ids);

    @Transactional
    @Query(value = "delete from hardware_metrics where uploaded = true and timestamp <= :timestamp", nativeQuery = true)
    int markAsDeleteOldMetrics(Long timestamp);
}
