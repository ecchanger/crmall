package com.macro.mall.config;

/**
 * Performance threshold configuration for monitoring
 * Defines acceptable performance limits and SLA targets
 */
public class PerformanceThresholds {

    // Response time thresholds (in milliseconds)
    private long maxSearchResponseTime = 500;
    private long maxBatchOperationTime = 30000; // 30 seconds
    private long maxLifecycleTransitionTime = 1000;
    private long maxInventoryUpdateTime = 200;
    private long maxValidationTime = 100;

    // Success rate thresholds (as percentage)
    private double minSuccessRate = 99.0;
    private double minCacheHitRate = 80.0;
    private double maxErrorRate = 1.0;

    // Throughput thresholds
    private int maxConcurrentBatchOperations = 5;
    private int maxQueuedOperations = 100;
    private int maxSearchQueriesPerSecond = 50;

    // Resource utilization thresholds
    private double maxMemoryUsage = 85.0; // 85%
    private double maxCpuUsage = 80.0; // 80%
    private long maxDatabaseConnectionTime = 1000; // 1 second

    // Business metric thresholds
    private int maxPendingApprovals = 100;
    private int maxActiveAlerts = 50;
    private int maxStaleDrafts = 200;

    // Getters and setters
    public long getMaxSearchResponseTime() {
        return maxSearchResponseTime;
    }

    public void setMaxSearchResponseTime(long maxSearchResponseTime) {
        this.maxSearchResponseTime = maxSearchResponseTime;
    }

    public long getMaxBatchOperationTime() {
        return maxBatchOperationTime;
    }

    public void setMaxBatchOperationTime(long maxBatchOperationTime) {
        this.maxBatchOperationTime = maxBatchOperationTime;
    }

    public long getMaxLifecycleTransitionTime() {
        return maxLifecycleTransitionTime;
    }

    public void setMaxLifecycleTransitionTime(long maxLifecycleTransitionTime) {
        this.maxLifecycleTransitionTime = maxLifecycleTransitionTime;
    }

    public long getMaxInventoryUpdateTime() {
        return maxInventoryUpdateTime;
    }

    public void setMaxInventoryUpdateTime(long maxInventoryUpdateTime) {
        this.maxInventoryUpdateTime = maxInventoryUpdateTime;
    }

    public long getMaxValidationTime() {
        return maxValidationTime;
    }

    public void setMaxValidationTime(long maxValidationTime) {
        this.maxValidationTime = maxValidationTime;
    }

    public double getMinSuccessRate() {
        return minSuccessRate;
    }

    public void setMinSuccessRate(double minSuccessRate) {
        this.minSuccessRate = minSuccessRate;
    }

    public double getMinCacheHitRate() {
        return minCacheHitRate;
    }

    public void setMinCacheHitRate(double minCacheHitRate) {
        this.minCacheHitRate = minCacheHitRate;
    }

    public double getMaxErrorRate() {
        return maxErrorRate;
    }

    public void setMaxErrorRate(double maxErrorRate) {
        this.maxErrorRate = maxErrorRate;
    }

    public int getMaxConcurrentBatchOperations() {
        return maxConcurrentBatchOperations;
    }

    public void setMaxConcurrentBatchOperations(int maxConcurrentBatchOperations) {
        this.maxConcurrentBatchOperations = maxConcurrentBatchOperations;
    }

    public int getMaxQueuedOperations() {
        return maxQueuedOperations;
    }

    public void setMaxQueuedOperations(int maxQueuedOperations) {
        this.maxQueuedOperations = maxQueuedOperations;
    }

    public int getMaxSearchQueriesPerSecond() {
        return maxSearchQueriesPerSecond;
    }

    public void setMaxSearchQueriesPerSecond(int maxSearchQueriesPerSecond) {
        this.maxSearchQueriesPerSecond = maxSearchQueriesPerSecond;
    }

    public double getMaxMemoryUsage() {
        return maxMemoryUsage;
    }

    public void setMaxMemoryUsage(double maxMemoryUsage) {
        this.maxMemoryUsage = maxMemoryUsage;
    }

    public double getMaxCpuUsage() {
        return maxCpuUsage;
    }

    public void setMaxCpuUsage(double maxCpuUsage) {
        this.maxCpuUsage = maxCpuUsage;
    }

    public long getMaxDatabaseConnectionTime() {
        return maxDatabaseConnectionTime;
    }

    public void setMaxDatabaseConnectionTime(long maxDatabaseConnectionTime) {
        this.maxDatabaseConnectionTime = maxDatabaseConnectionTime;
    }

    public int getMaxPendingApprovals() {
        return maxPendingApprovals;
    }

    public void setMaxPendingApprovals(int maxPendingApprovals) {
        this.maxPendingApprovals = maxPendingApprovals;
    }

    public int getMaxActiveAlerts() {
        return maxActiveAlerts;
    }

    public void setMaxActiveAlerts(int maxActiveAlerts) {
        this.maxActiveAlerts = maxActiveAlerts;
    }

    public int getMaxStaleDrafts() {
        return maxStaleDrafts;
    }

    public void setMaxStaleDrafts(int maxStaleDrafts) {
        this.maxStaleDrafts = maxStaleDrafts;
    }

    /**
     * Check if response time is within acceptable limits
     */
    public boolean isResponseTimeAcceptable(String operationType, long responseTime) {
        switch (operationType.toLowerCase()) {
            case "search":
                return responseTime <= maxSearchResponseTime;
            case "batch":
                return responseTime <= maxBatchOperationTime;
            case "lifecycle":
                return responseTime <= maxLifecycleTransitionTime;
            case "inventory":
                return responseTime <= maxInventoryUpdateTime;
            case "validation":
                return responseTime <= maxValidationTime;
            case "database":
                return responseTime <= maxDatabaseConnectionTime;
            default:
                return responseTime <= maxSearchResponseTime; // Default threshold
        }
    }

    /**
     * Check if success rate meets minimum requirements
     */
    public boolean isSuccessRateAcceptable(double successRate) {
        return successRate >= minSuccessRate;
    }

    /**
     * Check if cache hit rate meets minimum requirements
     */
    public boolean isCacheHitRateAcceptable(double cacheHitRate) {
        return cacheHitRate >= minCacheHitRate;
    }

    /**
     * Check if error rate is within acceptable limits
     */
    public boolean isErrorRateAcceptable(double errorRate) {
        return errorRate <= maxErrorRate;
    }

    /**
     * Check if resource usage is within acceptable limits
     */
    public boolean isResourceUsageAcceptable(String resourceType, double usage) {
        switch (resourceType.toLowerCase()) {
            case "memory":
                return usage <= maxMemoryUsage;
            case "cpu":
                return usage <= maxCpuUsage;
            default:
                return true; // Unknown resource type, assume acceptable
        }
    }

    /**
     * Check if business metrics are within acceptable limits
     */
    public boolean isBusinessMetricAcceptable(String metricType, int value) {
        switch (metricType.toLowerCase()) {
            case "pending_approvals":
                return value <= maxPendingApprovals;
            case "active_alerts":
                return value <= maxActiveAlerts;
            case "stale_drafts":
                return value <= maxStaleDrafts;
            case "queued_operations":
                return value <= maxQueuedOperations;
            default:
                return true; // Unknown metric type, assume acceptable
        }
    }

    /**
     * Get warning threshold (80% of max threshold)
     */
    public long getWarningThreshold(String operationType) {
        long maxThreshold = getMaxThreshold(operationType);
        return (long) (maxThreshold * 0.8);
    }

    /**
     * Get maximum threshold for operation type
     */
    public long getMaxThreshold(String operationType) {
        switch (operationType.toLowerCase()) {
            case "search":
                return maxSearchResponseTime;
            case "batch":
                return maxBatchOperationTime;
            case "lifecycle":
                return maxLifecycleTransitionTime;
            case "inventory":
                return maxInventoryUpdateTime;
            case "validation":
                return maxValidationTime;
            case "database":
                return maxDatabaseConnectionTime;
            default:
                return maxSearchResponseTime;
        }
    }

    @Override
    public String toString() {
        return "PerformanceThresholds{" +
                "maxSearchResponseTime=" + maxSearchResponseTime +
                ", maxBatchOperationTime=" + maxBatchOperationTime +
                ", maxLifecycleTransitionTime=" + maxLifecycleTransitionTime +
                ", maxInventoryUpdateTime=" + maxInventoryUpdateTime +
                ", maxValidationTime=" + maxValidationTime +
                ", minSuccessRate=" + minSuccessRate +
                ", minCacheHitRate=" + minCacheHitRate +
                ", maxErrorRate=" + maxErrorRate +
                ", maxConcurrentBatchOperations=" + maxConcurrentBatchOperations +
                ", maxQueuedOperations=" + maxQueuedOperations +
                ", maxSearchQueriesPerSecond=" + maxSearchQueriesPerSecond +
                ", maxMemoryUsage=" + maxMemoryUsage +
                ", maxCpuUsage=" + maxCpuUsage +
                ", maxDatabaseConnectionTime=" + maxDatabaseConnectionTime +
                ", maxPendingApprovals=" + maxPendingApprovals +
                ", maxActiveAlerts=" + maxActiveAlerts +
                ", maxStaleDrafts=" + maxStaleDrafts +
                '}';
    }
}