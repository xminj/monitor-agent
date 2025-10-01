package com.xminj.monitor.controller;

import com.xminj.monitor.domain.HealthReport;
import com.xminj.monitor.service.HealthReportService;
import com.xminj.monitor.service.scheduler.MetricsScheduler;
import com.xminj.monitor.util.JsonUtil;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/test")
public class TestController {
    @Inject
    private MetricsScheduler metricsScheduler;
    @Inject
    private HealthReportService healthReportService;

    @Get("/queryAll")
    public String queryAll() {
        List<HealthReport> healthReports = healthReportService.queryAll();
        return JsonUtil.toJsonString(healthReports);
    }

    @Get("/updateCollectionInterval/{seconds}")
    public void updateCollectionInterval(@PathVariable("seconds") Long seconds) {
        metricsScheduler.updateCollectionInterval(seconds);
    }
}
