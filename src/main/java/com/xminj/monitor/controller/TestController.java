package com.xminj.monitor.controller;

import com.xminj.monitor.domain.HardwareMetrics;
import com.xminj.monitor.service.HardwareMetricsService;
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
    private HardwareMetricsService hardwareMetricsService;

    @Get("/queryAll")
    public String queryAll() {
        List<HardwareMetrics> hardwareMetrics = hardwareMetricsService.queryAll();
        return JsonUtil.toJsonString(hardwareMetrics);
    }

    @Get("/updateCollectionInterval/{seconds}")
    public void updateCollectionInterval(@PathVariable("seconds") Long seconds) {
        metricsScheduler.updateCollectionInterval(seconds);
    }
}
