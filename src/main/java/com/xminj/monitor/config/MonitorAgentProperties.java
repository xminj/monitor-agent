package com.xminj.monitor.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.inject.Singleton;

@Singleton
@ConfigurationProperties("monitor.agent.scheduler")
public class MonitorAgentProperties {
    // 默认30秒
    private Long collectionIntervalSeconds = 30L;

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
