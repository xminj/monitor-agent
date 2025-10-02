package com.xminj.monitor.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.inject.Singleton;

@Singleton
@ConfigurationProperties("monitor.agent.scheduler")
public class MonitorAgentProperties {
    // 默认30秒
    /**
     * 硬件数据采集时间间隔
     */
    private Long collectionIntervalSeconds = 30L;

    /**
     * 上报地址
     */
    private String uploadedUrl;

    private Long uploadedIntervalSeconds = 5L;

    public String getUploadedUrl() {
        return uploadedUrl;
    }

    public void setUploadedUrl(String uploadedUrl) {
        this.uploadedUrl = uploadedUrl;
    }

    public Long getUploadedIntervalSeconds() {
        return uploadedIntervalSeconds;
    }

    public void setUploadedIntervalSeconds(Long uploadedIntervalSeconds) {
        if (uploadedIntervalSeconds < 5) {
            // 最小5秒，防止刷爆
            this.uploadedIntervalSeconds = 5L;
            return;
        }

        if (uploadedIntervalSeconds > 1200) {
            this.uploadedIntervalSeconds = 1200L; // 最大30分
            return;
        }

        this.uploadedIntervalSeconds = uploadedIntervalSeconds;
    }

    public Long getCollectionIntervalSeconds() {
        return collectionIntervalSeconds;
    }

    public void setCollectionIntervalSeconds(Long collectionIntervalSeconds) {
        if (collectionIntervalSeconds < 5) {
            // 最小5秒，防止刷爆
            this.collectionIntervalSeconds = 5L;
            return;
        }
        if (collectionIntervalSeconds > 3600) {
            this.collectionIntervalSeconds = 3600L; // 最大1小时
            return;
        }
        this.collectionIntervalSeconds = collectionIntervalSeconds;
    }
}
