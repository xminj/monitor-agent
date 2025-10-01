package com.xminj.monitor.service.metrics;

import oshi.hardware.CentralProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * CPU的指标
 */
public class CpuMetrics {
    private static long[] prevTicks = null;

    /**
     * 设置tick 数据
     *
     * @param processor 处理
     */
    public static void  setPervTicks(CentralProcessor processor) {
        if (Objects.isNull(processor)) {
            throw new RuntimeException("CentralProcessor is null");
        }
        prevTicks = processor.getSystemCpuLoadTicks();
    }


    /**
     * 处理CPU指标
     */
    public synchronized static double getCpuUsage(CentralProcessor processor) {
        // 获取当前tick数据
        long[] ticks = processor.getSystemCpuLoadTicks();

        // 用户级应用程序执行时的CPU利用率
        int userIndex = CentralProcessor.TickType.USER.getIndex();
        long user = ticks[userIndex] - prevTicks[userIndex];

        //
        int niceIndex = CentralProcessor.TickType.NICE.getIndex();
        long nice = ticks[niceIndex] - prevTicks[niceIndex];

        //
        int systemIndex = CentralProcessor.TickType.SYSTEM.getIndex();
        long system = ticks[systemIndex] - prevTicks[systemIndex];

        // CPU空闲时间
        int idleIndex = CentralProcessor.TickType.IDLE.getIndex();
        long idle = ticks[idleIndex] - prevTicks[idleIndex];

        // 在系统有未完成的磁盘I/O请求期间，CPU或多个CPU处于空闲状态的时间。
        int iowaitIndex = CentralProcessor.TickType.IOWAIT.getIndex();
        long iowait = ticks[iowaitIndex] - prevTicks[iowaitIndex];

        //
        int irqIndex = CentralProcessor.TickType.IRQ.getIndex();
        long irq = ticks[irqIndex] - prevTicks[irqIndex];

        int softirqIndex = CentralProcessor.TickType.SOFTIRQ.getIndex();
        long softirq = ticks[softirqIndex] - prevTicks[softirqIndex];

        int stealIndex = CentralProcessor.TickType.STEAL.getIndex();
        long steal = ticks[stealIndex] - prevTicks[stealIndex];

        // 计算总时间和非空闲时间
        long totalCpu = user + nice + system + idle + iowait + irq + softirq + steal;
        long cpuUsed = user + nice + system + irq + softirq + steal;

        // // 将当前 tick 数据保存为下一次的 "prev"
        prevTicks = ticks.clone();

        return totalCpu > 0 ?
                (double) cpuUsed / totalCpu
                : 0.0;
    }
}
