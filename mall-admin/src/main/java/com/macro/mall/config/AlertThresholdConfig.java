package com.macro.mall.config;

/**
 * Alert threshold configuration for monitoring and alerting
 * Defines warning and critical thresholds for various metrics
 */
public class AlertThresholdConfig {

    // Response time thresholds (in milliseconds)
    private long warningResponseTime = 500;
    private long criticalResponseTime = 1000;
    
    // Error rate thresholds (as percentage)
    private double warningErrorRate = 2.0; // 2%
    private double criticalErrorRate = 5.0; // 5%
    
    // Cache performance thresholds
    private double minCacheHitRate = 70.0; // 70%
    private double criticalCacheHitRate = 50.0; // 50%
    
    // Resource utilization thresholds
    private double warningMemoryUsage = 75.0; // 75%
    private double criticalMemoryUsage = 90.0; // 90%
    private double warningCpuUsage = 70.0; // 70%
    private double criticalCpuUsage = 85.0; // 85%
    
    // Business metric thresholds
    private int warningPendingApprovals = 50;
    private int criticalPendingApprovals = 100;
    private int warningActiveAlerts = 25;
    private int criticalActiveAlerts = 50;
    private int warningStaleDrafts = 100;
    private int criticalStaleDrafts = 200;
    
    // Throughput thresholds
    private int warningBatchQueueSize = 50;
    private int criticalBatchQueueSize = 100;
    private double warningThroughputDrop = 20.0; // 20% drop
    private double criticalThroughputDrop = 50.0; // 50% drop
    
    // SLA compliance thresholds
    private double warningSLACompliance = 95.0; // 95%
    private double criticalSLACompliance = 90.0; // 90%

    // Getters and setters
    public long getWarningResponseTime() {
        return warningResponseTime;
    }

    public void setWarningResponseTime(long warningResponseTime) {
        this.warningResponseTime = warningResponseTime;
    }

    public long getCriticalResponseTime() {
        return criticalResponseTime;
    }

    public void setCriticalResponseTime(long criticalResponseTime) {
        this.criticalResponseTime = criticalResponseTime;
    }

    public double getWarningErrorRate() {
        return warningErrorRate;
    }

    public void setWarningErrorRate(double warningErrorRate) {
        this.warningErrorRate = warningErrorRate;
    }

    public double getCriticalErrorRate() {
        return criticalErrorRate;
    }

    public void setCriticalErrorRate(double criticalErrorRate) {
        this.criticalErrorRate = criticalErrorRate;
    }

    public double getMinCacheHitRate() {
        return minCacheHitRate;
    }

    public void setMinCacheHitRate(double minCacheHitRate) {
        this.minCacheHitRate = minCacheHitRate;
    }

    public double getCriticalCacheHitRate() {
        return criticalCacheHitRate;
    }

    public void setCriticalCacheHitRate(double criticalCacheHitRate) {
        this.criticalCacheHitRate = criticalCacheHitRate;
    }

    public double getWarningMemoryUsage() {
        return warningMemoryUsage;
    }

    public void setWarningMemoryUsage(double warningMemoryUsage) {
        this.warningMemoryUsage = warningMemoryUsage;
    }

    public double getCriticalMemoryUsage() {
        return criticalMemoryUsage;
    }

    public void setCriticalMemoryUsage(double criticalMemoryUsage) {
        this.criticalMemoryUsage = criticalMemoryUsage;
    }

    public double getWarningCpuUsage() {
        return warningCpuUsage;
    }

    public void setWarningCpuUsage(double warningCpuUsage) {
        this.warningCpuUsage = warningCpuUsage;
    }

    public double getCriticalCpuUsage() {
        return criticalCpuUsage;
    }

    public void setCriticalCpuUsage(double criticalCpuUsage) {
        this.criticalCpuUsage = criticalCpuUsage;
    }

    public int getWarningPendingApprovals() {
        return warningPendingApprovals;
    }

    public void setWarningPendingApprovals(int warningPendingApprovals) {
        this.warningPendingApprovals = warningPendingApprovals;
    }

    public int getCriticalPendingApprovals() {
        return criticalPendingApprovals;
    }

    public void setCriticalPendingApprovals(int criticalPendingApprovals) {
        this.criticalPendingApprovals = criticalPendingApprovals;
    }

    public int getWarningActiveAlerts() {
        return warningActiveAlerts;
    }

    public void setWarningActiveAlerts(int warningActiveAlerts) {
        this.warningActiveAlerts = warningActiveAlerts;
    }

    public int getCriticalActiveAlerts() {
        return criticalActiveAlerts;
    }

    public void setCriticalActiveAlerts(int criticalActiveAlerts) {
        this.criticalActiveAlerts = criticalActiveAlerts;
    }

    public int getWarningStaleDrafts() {
        return warningStaleDrafts;
    }

    public void setWarningStaleDrafts(int warningStaleDrafts) {
        this.warningStaleDrafts = warningStaleDrafts;
    }

    public int getCriticalStaleDrafts() {
        return criticalStaleDrafts;
    }

    public void setCriticalStaleDrafts(int criticalStaleDrafts) {
        this.criticalStaleDrafts = criticalStaleDrafts;
    }

    public int getWarningBatchQueueSize() {
        return warningBatchQueueSize;
    }

    public void setWarningBatchQueueSize(int warningBatchQueueSize) {
        this.warningBatchQueueSize = warningBatchQueueSize;
    }

    public int getCriticalBatchQueueSize() {
        return criticalBatchQueueSize;
    }

    public void setCriticalBatchQueueSize(int criticalBatchQueueSize) {
        this.criticalBatchQueueSize = criticalBatchQueueSize;
    }

    public double getWarningThroughputDrop() {
        return warningThroughputDrop;
    }

    public void setWarningThroughputDrop(double warningThroughputDrop) {
        this.warningThroughputDrop = warningThroughputDrop;
    }

    public double getCriticalThroughputDrop() {
        return criticalThroughputDrop;
    }

    public void setCriticalThroughputDrop(double criticalThroughputDrop) {
        this.criticalThroughputDrop = criticalThroughputDrop;
    }

    public double getWarningSLACompliance() {
        return warningSLACompliance;
    }

    public void setWarningSLACompliance(double warningSLACompliance) {
        this.warningSLACompliance = warningSLACompliance;
    }

    public double getCriticalSLACompliance() {
        return criticalSLACompliance;
    }

    public void setCriticalSLACompliance(double criticalSLACompliance) {
        this.criticalSLACompliance = criticalSLACompliance;
    }

    /**
     * Check alert level for response time
     */
    public AlertLevel getResponseTimeAlertLevel(long responseTime) {
        if (responseTime >= criticalResponseTime) {
            return AlertLevel.CRITICAL;
        } else if (responseTime >= warningResponseTime) {
            return AlertLevel.WARNING;
        }
        return AlertLevel.OK;
    }

    /**
     * Check alert level for error rate
     */
    public AlertLevel getErrorRateAlertLevel(double errorRate) {
        if (errorRate >= criticalErrorRate) {
            return AlertLevel.CRITICAL;
        } else if (errorRate >= warningErrorRate) {
            return AlertLevel.WARNING;
        }
        return AlertLevel.OK;
    }

    /**
     * Check alert level for cache hit rate
     */
    public AlertLevel getCacheHitRateAlertLevel(double cacheHitRate) {
        if (cacheHitRate <= criticalCacheHitRate) {
            return AlertLevel.CRITICAL;
        } else if (cacheHitRate <= minCacheHitRate) {
            return AlertLevel.WARNING;
        }
        return AlertLevel.OK;
    }

    /**
     * Check alert level for memory usage
     */
    public AlertLevel getMemoryUsageAlertLevel(double memoryUsage) {
        if (memoryUsage >= criticalMemoryUsage) {
            return AlertLevel.CRITICAL;
        } else if (memoryUsage >= warningMemoryUsage) {
            return AlertLevel.WARNING;
        }
        return AlertLevel.OK;
    }

    /**
     * Check alert level for CPU usage
     */
    public AlertLevel getCpuUsageAlertLevel(double cpuUsage) {
        if (cpuUsage >= criticalCpuUsage) {
            return AlertLevel.CRITICAL;
        } else if (cpuUsage >= warningCpuUsage) {
            return AlertLevel.WARNING;
        }
        return AlertLevel.OK;
    }

    /**
     * Check alert level for business metrics
     */
    public AlertLevel getBusinessMetricAlertLevel(String metricType, int value) {
        switch (metricType.toLowerCase()) {
            case "pending_approvals":
                if (value >= criticalPendingApprovals) return AlertLevel.CRITICAL;
                if (value >= warningPendingApprovals) return AlertLevel.WARNING;
                break;
            case "active_alerts":
                if (value >= criticalActiveAlerts) return AlertLevel.CRITICAL;
                if (value >= warningActiveAlerts) return AlertLevel.WARNING;
                break;
            case "stale_drafts":
                if (value >= criticalStaleDrafts) return AlertLevel.CRITICAL;
                if (value >= warningStaleDrafts) return AlertLevel.WARNING;
                break;
            case "batch_queue_size":
                if (value >= criticalBatchQueueSize) return AlertLevel.CRITICAL;
                if (value >= warningBatchQueueSize) return AlertLevel.WARNING;
                break;
        }
        return AlertLevel.OK;
    }

    /**
     * Check alert level for SLA compliance
     */
    public AlertLevel getSLAComplianceAlertLevel(double slaCompliance) {
        if (slaCompliance <= criticalSLACompliance) {
            return AlertLevel.CRITICAL;
        } else if (slaCompliance <= warningSLACompliance) {
            return AlertLevel.WARNING;
        }
        return AlertLevel.OK;
    }

    /**
     * Get threshold value for specific metric and alert level
     */
    public double getThreshold(String metricType, AlertLevel alertLevel) {
        switch (metricType.toLowerCase()) {
            case "response_time":
                return alertLevel == AlertLevel.CRITICAL ? criticalResponseTime : warningResponseTime;
            case "error_rate":
                return alertLevel == AlertLevel.CRITICAL ? criticalErrorRate : warningErrorRate;
            case "cache_hit_rate":
                return alertLevel == AlertLevel.CRITICAL ? criticalCacheHitRate : minCacheHitRate;
            case "memory_usage":
                return alertLevel == AlertLevel.CRITICAL ? criticalMemoryUsage : warningMemoryUsage;
            case "cpu_usage":
                return alertLevel == AlertLevel.CRITICAL ? criticalCpuUsage : warningCpuUsage;
            case "sla_compliance":
                return alertLevel == AlertLevel.CRITICAL ? criticalSLACompliance : warningSLACompliance;
            default:
                return 0.0;
        }
    }

    /**
     * Alert severity levels
     */
    public enum AlertLevel {
        OK,
        WARNING,
        CRITICAL
    }

    @Override
    public String toString() {
        return "AlertThresholdConfig{" +
                "warningResponseTime=" + warningResponseTime +
                ", criticalResponseTime=" + criticalResponseTime +
                ", warningErrorRate=" + warningErrorRate +
                ", criticalErrorRate=" + criticalErrorRate +
                ", minCacheHitRate=" + minCacheHitRate +
                ", criticalCacheHitRate=" + criticalCacheHitRate +
                ", warningMemoryUsage=" + warningMemoryUsage +
                ", criticalMemoryUsage=" + criticalMemoryUsage +
                ", warningCpuUsage=" + warningCpuUsage +
                ", criticalCpuUsage=" + criticalCpuUsage +
                ", warningSLACompliance=" + warningSLACompliance +
                ", criticalSLACompliance=" + criticalSLACompliance +
                '}';
    }
}