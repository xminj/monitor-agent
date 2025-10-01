package com.xminj.monitor.service;

import com.xminj.monitor.domain.HealthReport;
import com.xminj.monitor.repository.HealthReportRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class HealthReportService {

    @Inject
    private HealthReportRepository healthReportRepository;

    public List<HealthReport> queryAll() {
        return healthReportRepository.findAll();
    }

}