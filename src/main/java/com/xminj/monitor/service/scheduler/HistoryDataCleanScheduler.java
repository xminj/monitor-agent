package com.xminj.monitor.service.scheduler;

import com.xminj.monitor.config.MonitorAgentProperties;
import com.xminj.monitor.repository.HardwareMetricsRepository;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.core.annotation.Order;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.TaskScheduler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;


@Singleton
public class HistoryDataCleanScheduler {
    private static final Logger log = LoggerFactory.getLogger(HistoryDataCleanScheduler.class);

    // 历史数据删除任务
    private final AtomicReference<ScheduledFuture<?>> dataCleanTask = new AtomicReference<>();

    @Inject
    private TaskScheduler taskScheduler;
    @Inject
    private MonitorAgentProperties monitorAgentProperties;
    @Inject
    private HardwareMetricsRepository hardwareMetricsRepository;

    @EventListener
    @Order(Integer.MAX_VALUE - 2)
    public void init(StartupEvent event) {
        historyCleanDataSchedule();
    }


    public void updateHistoryDataCleanTaskCron(String cron) {
        monitorAgentProperties.setDataCleanCron(cron);
        historyCleanDataSchedule();
    }


    private void historyCleanDataSchedule() {
        ScheduledFuture<?> oldTask = dataCleanTask.getAndSet(null);
        if (Objects.nonNull(oldTask)) {
            log.info("== 取消历史定时清理任务");
            oldTask.cancel(true);
        }
        String cron = monitorAgentProperties.getDataCleanCron();
        log.info("== 历史数据清理时间间隔： {}", cron);
        
        ScheduledFuture<?> schedule = taskScheduler.schedule(cron, this::historyCleanDataTask);
        dataCleanTask.set(schedule);
    }

    private void historyCleanDataTask() {
        safeClean("hardware_metrics 表", () -> {
            int number = hardwareMetricsRepository.markAsDeleteOldMetrics(System.currentTimeMillis());
            log.info("成功清理 {} 条数据", number);
        });
        safeClean("", () -> {
        });
    }

    private void safeClean(String logName, Runnable clean) {
        try {
            log.info("开始 {} 的清理", logName);
            clean.run();
        } catch (Exception e) {
            log.error("{} 清理失败：{}", logName, e.getMessage());
        }
    }


}