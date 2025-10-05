package com.xminj.monitor.service;

import com.xminj.monitor.domain.HardwareMetrics;
import com.xminj.monitor.repository.HardwareMetricsRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class HardwareMetricsService {

    @Inject
    private HardwareMetricsRepository hardwareMetricsRepository;

    public List<HardwareMetrics> queryAll() {
        return hardwareMetricsRepository.findAll();
    }

}