package com.xminj.monitor.domain;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.DataType;

@Introspected
@MappedEntity("agent_health_report")
public class HealthReport {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 时间
     */
    @MappedProperty("timestamp")
    private Long timestamp;

    /**
     * 主机制造商
     */
    @MappedProperty("system_manufacturer")
    private String systemManufacturer;

    /**
     * 主机型号
     */
    @MappedProperty("system_mode")
    private String systemMode;


    /**
     * 主机序列号
     */
    @MappedProperty("serial_number")
    private String serialNumber;


    /**
     * cpu使用
     */
    @MappedProperty("cpu_usage")
    private Double cpuUsage;


    /**
     * 内存使用，GB
     */
    @MappedProperty("memory_used")
    private Long memoryUsed;

    /**
     * 内存总量 GB
     */
    @MappedProperty("memory_total")
    private Long memoryTotal;

    /**
     * 磁盘使用 GB
     */
    @MappedProperty("disk_used")
    private Long diskUsed;

    /**
     * 磁盘总量 GB
     */
    @MappedProperty("disk_total")
    private Long diskTotal;

    /**
     * 网络情况 JSON
     */
    @MappedProperty(value = "network",type = DataType.JSON)
    private String network;

    /**
     * 是否已上报
     */
    @MappedProperty(value = "uploaded")
    private Boolean uploaded = false;


    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Long getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(Long diskTotal) {
        this.diskTotal = diskTotal;
    }

    public Long getDiskUsed() {
        return diskUsed;
    }

    public void setDiskUsed(Long diskUsed) {
        this.diskUsed = diskUsed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(Long memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public Long getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Long memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSystemManufacturer() {
        return systemManufacturer;
    }

    public void setSystemManufacturer(String systemManufacturer) {
        this.systemManufacturer = systemManufacturer;
    }

    public String getSystemMode() {
        return systemMode;
    }

    public void setSystemMode(String systemMode) {
        this.systemMode = systemMode;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getUploaded() {
        return uploaded;
    }

    public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }
}
