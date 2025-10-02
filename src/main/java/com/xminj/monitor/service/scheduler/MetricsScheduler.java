package com.xminj.monitor.service.scheduler;

import com.xminj.monitor.config.MonitorAgentProperties;
import com.xminj.monitor.domain.HealthReport;
import com.xminj.monitor.domain.Network;
import com.xminj.monitor.repository.HealthReportRepository;
import com.xminj.monitor.service.metrics.CpuMetrics;
import com.xminj.monitor.util.JsonUtil;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.core.util.StringUtils;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.TaskScheduler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class MetricsScheduler {

    private static final Logger log = LoggerFactory.getLogger(MetricsScheduler.class);
    private static final long MEMORY_GB = 1024L * 1024L * 1024L;
    private static final long DISK_GB = 1000L * 1000 * 1000;

    // 保存当前任务，用于取消
    private final AtomicReference<ScheduledFuture<?>> currentTask = new AtomicReference<>();
    // 硬件指标采集相关
    private volatile SystemInfo systemInfo;
    private volatile HardwareAbstractionLayer hardware;
    private volatile OperatingSystem os;


    @Inject
    private TaskScheduler taskScheduler;
    @Inject
    private MonitorAgentProperties monitorAgentProperties;
    @Inject
    private HealthReportRepository healthReportRepository;

    @EventListener
    public void init(StartupEvent event) {
        this.systemInfo = new SystemInfo();
        this.hardware = systemInfo.getHardware();
        this.os = systemInfo.getOperatingSystem();
        CpuMetrics.setPervTicks(hardware.getProcessor());
        scheduleCollectionTask();
    }


    /**
     * 动态修改执行时间间隔
     *
     * @param seconds 时间间隔  秒
     */
    public void updateCollectionInterval(Long seconds) {
        log.info("== 修改定时采集硬件指标时间为：{}秒", seconds);
        monitorAgentProperties.setCollectionIntervalSeconds(seconds);
        scheduleCollectionTask();
    }


    /**
     * 动态采集硬件指标
     */
    private void scheduleCollectionTask() {
        ScheduledFuture<?> oldTask = currentTask.getAndSet(null);
        if (Objects.nonNull(oldTask)) {
            log.info("== 取消历史的采集硬件指标任务");
            oldTask.cancel(true);
        }

        Long interval = monitorAgentProperties.getCollectionIntervalSeconds();
        log.info("== 采集硬件指标时间间隔： {} 秒", interval);

        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(
                Duration.ofSeconds(interval),  // 首次延迟（可为 null，表示立即执行），这里表示启动后延迟 interval 秒后执行
                Duration.ofSeconds(interval),  // 执行间隔
                this::collectAndStoreLocally); // 任务

        currentTask.set(scheduledFuture);
    }


    private void collectAndStoreLocally() {
        HealthReport report = new HealthReport();

        safeCollect("系统基本信息采集", () -> {
            report.setTimestamp(System.currentTimeMillis());
            report.setHostname(os.getNetworkParams().getHostName());
            report.setOsName(os.toString());
        });

        safeCollect("CPU指标采集", () -> {
            // CPU 使用率
            double cpuUsage = CpuMetrics.getCpuUsage(hardware.getProcessor());
            report.setCpuUsage(BigDecimal.valueOf(cpuUsage).setScale(3, RoundingMode.HALF_DOWN).doubleValue());
        });

        safeCollect("内存指标采集", () -> {
            GlobalMemory memory = hardware.getMemory();
            long memoryTotal = memory.getTotal() / MEMORY_GB;
            long memoryUsed = memoryTotal - memory.getAvailable() / MEMORY_GB;
            report.setMemoryTotal(memoryTotal);
            report.setMemoryUsed(memoryUsed);
        });

        safeCollect("磁盘指标采集", () -> {
            OSFileStore osFileStore = os.getFileSystem().getFileStores().getFirst();

            long totalSpace = osFileStore.getTotalSpace() / DISK_GB;
            long usableSpace = osFileStore.getUsableSpace() / DISK_GB;

            report.setDiskTotal(totalSpace);
            report.setDiskUsed(totalSpace - usableSpace);

        });

        safeCollect("网络指标采集", () -> {
            List<Network> networks = new ArrayList<>();
            for (NetworkIF networkIF : hardware.getNetworkIFs()) {
                String ipv4Addr = Arrays.toString(networkIF.getIPv4addr()).replaceAll("[\\[\\]]","");

                if (StringUtils.hasText(ipv4Addr)) {
                    Network network = new Network();
                    network.setNetworkInterface(networkIF.getName());
                    network.setiPv4addr(ipv4Addr);
                    network.setNetworkRecv(networkIF.getBytesRecv());
                    network.setNetworkSent(networkIF.getBytesSent());
                    networks.add(network);
                }
            }
            report.setNetwork(JsonUtil.toJsonString(networks));
        });

        try {
            healthReportRepository.save(report);
            log.info("采集数据写库成功 {}", JsonUtil.toJsonString(report));
        } catch (Exception e) {
            log.error("保存采集数据出错： {}  , {}", JsonUtil.toJsonString(report), e.getMessage());
        }

    }


    private void safeCollect(String metricName, Runnable collector) {
        try {
            log.debug(metricName);
            collector.run();
        } catch (Exception e) {
            log.error("采集 {} 指标失败, message:  {}", metricName, e.getMessage());
        }
    }
}