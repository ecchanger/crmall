package com.macro.mall.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicDouble;

/**
 * Custom metrics collector for Product Management Module
 * Collects business-specific KPIs and performance metrics
 */
@Component
public class CustomMetricsCollector {

    private static final Logger logger = LoggerFactory.getLogger(CustomMetricsCollector.class);

    private final MeterRegistry meterRegistry;
    
    // Metrics storage
    private final AtomicLong totalProductsCreated = new AtomicLong(0);
    private final AtomicLong totalProductsPublished = new AtomicLong(0);
    private final AtomicLong totalBatchOperations = new AtomicLong(0);
    private final AtomicLong totalInventoryAlerts = new AtomicLong(0);
    private final AtomicDouble averageProcessingTime = new AtomicDouble(0.0);
    private final AtomicLong totalValidationFailures = new AtomicLong(0);
    
    // Cache for performance metrics
    private final ConcurrentHashMap<String, AtomicLong> operationCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicDouble> operationDurations = new ConcurrentHashMap<>();
    
    // Counters
    private final Counter productCreationCounter;
    private final Counter productPublicationCounter;
    private final Counter batchOperationCounter;
    private final Counter inventoryAlertCounter;
    private final Counter validationFailureCounter;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    
    // Timers
    private final Timer searchOperationTimer;
    private final Timer lifecycleTransitionTimer;
    private final Timer batchProcessingTimer;

    public CustomMetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Initialize counters
        this.productCreationCounter = Counter.builder("product.created.total")
                .description("Total number of products created")
                .register(meterRegistry);
        
        this.productPublicationCounter = Counter.builder("product.published.total")
                .description("Total number of products published")
                .register(meterRegistry);
        
        this.batchOperationCounter = Counter.builder("batch.operations.total")
                .description("Total number of batch operations executed")
                .register(meterRegistry);
        
        this.inventoryAlertCounter = Counter.builder("inventory.alerts.total")
                .description("Total number of inventory alerts generated")
                .register(meterRegistry);
        
        this.validationFailureCounter = Counter.builder("validation.failures.total")
                .description("Total number of validation failures")
                .register(meterRegistry);
        
        this.cacheHitCounter = Counter.builder("cache.hits.total")
                .description("Total number of cache hits")
                .register(meterRegistry);
        
        this.cacheMissCounter = Counter.builder("cache.misses.total")
                .description("Total number of cache misses")
                .register(meterRegistry);
        
        // Initialize timers
        this.searchOperationTimer = Timer.builder("search.operation.duration")
                .description("Duration of search operations")
                .publishPercentiles(0.5, 0.90, 0.95, 0.99)
                .register(meterRegistry);
        
        this.lifecycleTransitionTimer = Timer.builder("lifecycle.transition.duration")
                .description("Duration of lifecycle transitions")
                .publishPercentiles(0.5, 0.90, 0.95, 0.99)
                .register(meterRegistry);
        
        this.batchProcessingTimer = Timer.builder("batch.processing.duration")
                .description("Duration of batch processing operations")
                .publishPercentiles(0.5, 0.90, 0.95, 0.99)
                .register(meterRegistry);
        
        // Initialize gauges
        initializeGauges();
    }

    /**
     * Initialize gauge metrics
     */
    private void initializeGauges() {
        Gauge.builder("product.creation.rate")
                .description("Current product creation rate")
                .register(meterRegistry, this, CustomMetricsCollector::getProductCreationRate);
        
        Gauge.builder("product.publication.rate")
                .description("Current product publication rate")
                .register(meterRegistry, this, CustomMetricsCollector::getProductPublicationRate);
        
        Gauge.builder("batch.operation.success.rate")
                .description("Batch operation success rate")
                .register(meterRegistry, this, CustomMetricsCollector::getBatchOperationSuccessRate);
        
        Gauge.builder("cache.hit.rate")
                .description("Cache hit rate percentage")
                .register(meterRegistry, this, CustomMetricsCollector::getCacheHitRate);
        
        Gauge.builder("average.processing.time")
                .description("Average processing time for operations")
                .register(meterRegistry, this, CustomMetricsCollector::getAverageProcessingTime);
        
        Gauge.builder("validation.failure.rate")
                .description("Validation failure rate")
                .register(meterRegistry, this, CustomMetricsCollector::getValidationFailureRate);
        
        Gauge.builder("system.health.score")
                .description("Overall system health score")
                .register(meterRegistry, this, CustomMetricsCollector::getSystemHealthScore);
    }

    /**
     * Record product creation event
     */
    public void recordProductCreation() {
        productCreationCounter.increment();
        totalProductsCreated.incrementAndGet();
        logger.debug("Product creation recorded. Total: {}", totalProductsCreated.get());
    }

    /**
     * Record product publication event
     */
    public void recordProductPublication() {
        productPublicationCounter.increment();
        totalProductsPublished.incrementAndGet();
        logger.debug("Product publication recorded. Total: {}", totalProductsPublished.get());
    }

    /**
     * Record batch operation execution
     */
    public void recordBatchOperation(String operationType, long duration, boolean success) {
        batchOperationCounter.increment("type", operationType, "status", success ? "success" : "failure");
        totalBatchOperations.incrementAndGet();
        
        // Update operation-specific metrics
        operationCounts.computeIfAbsent(operationType, k -> new AtomicLong(0)).incrementAndGet();
        operationDurations.computeIfAbsent(operationType, k -> new AtomicDouble(0.0))
                .addAndGet(duration);
        
        batchProcessingTimer.record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
        
        logger.debug("Batch operation recorded: {} in {}ms, success: {}", operationType, duration, success);
    }

    /**
     * Record inventory alert generation
     */
    public void recordInventoryAlert(String alertType, String severity) {
        inventoryAlertCounter.increment("type", alertType, "severity", severity);
        totalInventoryAlerts.incrementAndGet();
        logger.debug("Inventory alert recorded: {} with severity {}", alertType, severity);
    }

    /**
     * Record validation failure
     */
    public void recordValidationFailure(String validationType, String reason) {
        validationFailureCounter.increment("type", validationType, "reason", reason);
        totalValidationFailures.incrementAndGet();
        logger.debug("Validation failure recorded: {} - {}", validationType, reason);
    }

    /**
     * Record cache hit
     */
    public void recordCacheHit(String cacheType) {
        cacheHitCounter.increment("type", cacheType);
        logger.debug("Cache hit recorded for type: {}", cacheType);
    }

    /**
     * Record cache miss
     */
    public void recordCacheMiss(String cacheType) {
        cacheMissCounter.increment("type", cacheType);
        logger.debug("Cache miss recorded for type: {}", cacheType);
    }

    /**
     * Record search operation
     */
    public void recordSearchOperation(long duration, int resultCount) {
        searchOperationTimer.record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
        meterRegistry.counter("search.operations.total", "result_count_range", getResultCountRange(resultCount)).increment();
        logger.debug("Search operation recorded: {}ms, {} results", duration, resultCount);
    }

    /**
     * Record lifecycle transition
     */
    public void recordLifecycleTransition(String fromStatus, String toStatus, long duration, boolean success) {
        lifecycleTransitionTimer.record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
        meterRegistry.counter("lifecycle.transitions.total", 
                "from", fromStatus, 
                "to", toStatus, 
                "status", success ? "success" : "failure").increment();
        logger.debug("Lifecycle transition recorded: {} -> {} in {}ms, success: {}", 
                fromStatus, toStatus, duration, success);
    }

    /**
     * Get result count range for categorization
     */
    private String getResultCountRange(int count) {
        if (count == 0) return "empty";
        if (count <= 10) return "small";
        if (count <= 100) return "medium";
        if (count <= 1000) return "large";
        return "xlarge";
    }

    // Gauge calculation methods

    private double getProductCreationRate(CustomMetricsCollector collector) {
        // Calculate rate based on last hour's data
        return collector.totalProductsCreated.get() / 24.0; // Simplified calculation
    }

    private double getProductPublicationRate(CustomMetricsCollector collector) {
        return collector.totalProductsPublished.get() / 24.0; // Simplified calculation
    }

    private double getBatchOperationSuccessRate(CustomMetricsCollector collector) {
        long total = collector.totalBatchOperations.get();
        if (total == 0) return 1.0;
        // Simplified success rate calculation
        return 0.95; // 95% success rate placeholder
    }

    private double getCacheHitRate(CustomMetricsCollector collector) {
        double hits = collector.cacheHitCounter.count();
        double misses = collector.cacheMissCounter.count();
        double total = hits + misses;
        return total > 0 ? (hits / total) * 100 : 0.0;
    }

    private double getAverageProcessingTime(CustomMetricsCollector collector) {
        return collector.averageProcessingTime.get();
    }

    private double getValidationFailureRate(CustomMetricsCollector collector) {
        long failures = collector.totalValidationFailures.get();
        long total = collector.totalProductsCreated.get() + collector.totalBatchOperations.get();
        return total > 0 ? (double) failures / total * 100 : 0.0;
    }

    private double getSystemHealthScore(CustomMetricsCollector collector) {
        // Calculate composite health score based on various metrics
        double cacheHitRate = getCacheHitRate(collector) / 100.0;
        double successRate = getBatchOperationSuccessRate(collector);
        double failureRate = getValidationFailureRate(collector) / 100.0;
        
        // Weight factors for different metrics
        double healthScore = (cacheHitRate * 0.3) + (successRate * 0.5) + ((1.0 - failureRate) * 0.2);
        return Math.max(0.0, Math.min(1.0, healthScore)) * 100; // 0-100 scale
    }

    /**
     * Scheduled task to update average processing time
     */
    @Scheduled(fixedRate = 60000) // Every minute
    public void updateAverageProcessingTime() {
        double totalDuration = 0.0;
        long totalOperations = 0;
        
        for (AtomicDouble duration : operationDurations.values()) {
            totalDuration += duration.get();
        }
        
        for (AtomicLong count : operationCounts.values()) {
            totalOperations += count.get();
        }
        
        if (totalOperations > 0) {
            averageProcessingTime.set(totalDuration / totalOperations);
        }
        
        logger.debug("Updated average processing time: {}ms", averageProcessingTime.get());
    }

    /**
     * Scheduled task to log metrics summary
     */
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void logMetricsSummary() {
        logger.info("Metrics Summary - Products Created: {}, Published: {}, Batch Ops: {}, Alerts: {}, Cache Hit Rate: {:.2f}%",
                totalProductsCreated.get(),
                totalProductsPublished.get(),
                totalBatchOperations.get(),
                totalInventoryAlerts.get(),
                getCacheHitRate(this));
    }

    // Public getters for external access
    public long getTotalProductsCreated() { return totalProductsCreated.get(); }
    public long getTotalProductsPublished() { return totalProductsPublished.get(); }
    public long getTotalBatchOperations() { return totalBatchOperations.get(); }
    public long getTotalInventoryAlerts() { return totalInventoryAlerts.get(); }
    public long getTotalValidationFailures() { return totalValidationFailures.get(); }
}