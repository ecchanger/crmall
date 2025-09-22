package com.macro.mall.config;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Gauge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuator.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Performance monitoring configuration for Product Management Module
 * Provides comprehensive metrics collection and monitoring capabilities
 */
@Configuration
@EnableAspectJAutoProxy
public class MonitoringConfig {

    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * Customize meter registry with application-specific tags
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "mall-admin", "module", "product-management");
    }

    /**
     * Product lifecycle operation counter
     */
    @Bean
    public Counter lifecycleTransitionCounter() {
        return Counter.builder("product.lifecycle.transitions")
                .description("Number of product lifecycle transitions")
                .tag("type", "transition")
                .register(meterRegistry);
    }

    /**
     * Batch operation counter
     */
    @Bean
    public Counter batchOperationCounter() {
        return Counter.builder("product.batch.operations")
                .description("Number of batch operations executed")
                .tag("type", "batch")
                .register(meterRegistry);
    }

    /**
     * Inventory alert counter
     */
    @Bean
    public Counter inventoryAlertCounter() {
        return Counter.builder("inventory.alerts.generated")
                .description("Number of inventory alerts generated")
                .tag("type", "alert")
                .register(meterRegistry);
    }

    /**
     * Product search operation timer
     */
    @Bean
    public Timer searchOperationTimer() {
        return Timer.builder("product.search.duration")
                .description("Time taken for product search operations")
                .tag("operation", "search")
                .register(meterRegistry);
    }

    /**
     * Database operation timer
     */
    @Bean
    public Timer databaseOperationTimer() {
        return Timer.builder("product.database.duration")
                .description("Time taken for database operations")
                .tag("operation", "database")
                .register(meterRegistry);
    }

    /**
     * Active lifecycle gauge
     */
    @Bean
    public Gauge activeLifecycleGauge() {
        return Gauge.builder("product.lifecycle.active")
                .description("Number of products in active lifecycle states")
                .tag("state", "active")
                .register(meterRegistry, this, MonitoringConfig::getActiveLifecycleCount);
    }

    /**
     * Low stock products gauge
     */
    @Bean
    public Gauge lowStockProductsGauge() {
        return Gauge.builder("inventory.lowstock.count")
                .description("Number of products with low stock")
                .tag("type", "lowstock")
                .register(meterRegistry, this, MonitoringConfig::getLowStockCount);
    }

    /**
     * Cache hit rate gauge
     */
    @Bean
    public Gauge cacheHitRateGauge() {
        return Gauge.builder("product.cache.hitrate")
                .description("Cache hit rate for product operations")
                .tag("cache", "product")
                .register(meterRegistry, this, MonitoringConfig::getCacheHitRate);
    }

    /**
     * API response time percentile timer
     */
    @Bean
    public Timer apiResponseTimer() {
        return Timer.builder("product.api.response.time")
                .description("API response time for product operations")
                .publishPercentiles(0.5, 0.75, 0.90, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);
    }

    /**
     * Error rate counter
     */
    @Bean
    public Counter errorRateCounter() {
        return Counter.builder("product.errors.count")
                .description("Number of errors in product operations")
                .tag("type", "error")
                .register(meterRegistry);
    }

    /**
     * Validation failure counter
     */
    @Bean
    public Counter validationFailureCounter() {
        return Counter.builder("product.validation.failures")
                .description("Number of validation failures")
                .tag("type", "validation")
                .register(meterRegistry);
    }

    /**
     * Performance monitoring aspects configuration
     */
    @Bean
    public PerformanceMonitoringAspect performanceMonitoringAspect() {
        return new PerformanceMonitoringAspect(meterRegistry);
    }

    /**
     * Health indicators for product management
     */
    @Bean
    public ProductManagementHealthIndicator productHealthIndicator() {
        return new ProductManagementHealthIndicator();
    }

    // Private methods for gauge calculations

    private Double getActiveLifecycleCount(MonitoringConfig config) {
        // This would integrate with actual service to get real-time count
        // For now, return a placeholder value
        return 0.0;
    }

    private Double getLowStockCount(MonitoringConfig config) {
        // This would integrate with inventory service to get real-time count
        return 0.0;
    }

    private Double getCacheHitRate(MonitoringConfig config) {
        // This would integrate with cache metrics to get hit rate
        return 0.85; // 85% hit rate placeholder
    }

    /**
     * Custom metrics for business KPIs
     */
    @Bean
    public CustomMetricsCollector customMetricsCollector() {
        return new CustomMetricsCollector(meterRegistry);
    }

    /**
     * Performance threshold configuration
     */
    @Bean
    public PerformanceThresholds performanceThresholds() {
        PerformanceThresholds thresholds = new PerformanceThresholds();
        thresholds.setMaxSearchResponseTime(500); // 500ms
        thresholds.setMaxBatchOperationTime(30000); // 30 seconds
        thresholds.setMaxLifecycleTransitionTime(1000); // 1 second
        thresholds.setMinCacheHitRate(0.80); // 80%
        thresholds.setMaxErrorRate(0.01); // 1%
        return thresholds;
    }

    /**
     * SLA monitoring configuration
     */
    @Bean
    public SLAMonitor slaMonitor() {
        return new SLAMonitor(meterRegistry, performanceThresholds());
    }

    /**
     * Alert threshold configuration
     */
    @Bean
    public AlertThresholdConfig alertThresholdConfig() {
        AlertThresholdConfig config = new AlertThresholdConfig();
        config.setCriticalResponseTime(1000); // 1 second
        config.setWarningResponseTime(500); // 500ms
        config.setCriticalErrorRate(0.05); // 5%
        config.setWarningErrorRate(0.02); // 2%
        config.setMinCacheHitRate(0.70); // 70%
        return config;
    }

    /**
     * Performance degradation detector
     */
    @Bean
    public PerformanceDegradationDetector performanceDegradationDetector() {
        return new PerformanceDegradationDetector(meterRegistry, alertThresholdConfig());
    }

    /**
     * Resource utilization monitor
     */
    @Bean
    public ResourceUtilizationMonitor resourceUtilizationMonitor() {
        return new ResourceUtilizationMonitor(meterRegistry);
    }
}