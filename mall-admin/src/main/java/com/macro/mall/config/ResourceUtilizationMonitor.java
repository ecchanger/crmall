package com.macro.mall.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

/**
 * Resource utilization monitor for Product Management Module
 * Tracks system resources and provides alerts for resource exhaustion
 */
@Component
public class ResourceUtilizationMonitor {

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtilizationMonitor.class);

    private final MeterRegistry meterRegistry;
    private final MemoryMXBean memoryMXBean;
    private final OperatingSystemMXBean osMXBean;
    private final RuntimeMXBean runtimeMXBean;

    // Resource utilization thresholds
    private static final double WARNING_MEMORY_THRESHOLD = 75.0; // 75%
    private static final double CRITICAL_MEMORY_THRESHOLD = 90.0; // 90%
    private static final double WARNING_CPU_THRESHOLD = 70.0; // 70%
    private static final double CRITICAL_CPU_THRESHOLD = 85.0; // 85%

    // Historical tracking
    private double previousCpuUsage = 0.0;
    private long previousSystemTime = System.currentTimeMillis();

    public ResourceUtilizationMonitor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.osMXBean = ManagementFactory.getOperatingSystemMXBean();
        this.runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        
        initializeGauges();
    }

    /**
     * Initialize resource monitoring gauges
     */
    private void initializeGauges() {
        // Memory utilization gauges
        Gauge.builder("system.memory.used.bytes")
                .description("Used memory in bytes")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getUsedMemoryBytes);

        Gauge.builder("system.memory.max.bytes")
                .description("Maximum memory in bytes")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getMaxMemoryBytes);

        Gauge.builder("system.memory.utilization.percentage")
                .description("Memory utilization percentage")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getMemoryUtilizationPercentage);

        // CPU utilization gauge
        Gauge.builder("system.cpu.utilization.percentage")
                .description("CPU utilization percentage")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getCpuUtilizationPercentage);

        // JVM specific gauges
        Gauge.builder("jvm.heap.used.bytes")
                .description("Used heap memory in bytes")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getHeapUsedBytes);

        Gauge.builder("jvm.heap.max.bytes")
                .description("Maximum heap memory in bytes")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getHeapMaxBytes);

        Gauge.builder("jvm.nonheap.used.bytes")
                .description("Used non-heap memory in bytes")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getNonHeapUsedBytes);

        // System information gauges
        Gauge.builder("system.processors.available")
                .description("Number of available processors")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getAvailableProcessors);

        Gauge.builder("jvm.uptime.milliseconds")
                .description("JVM uptime in milliseconds")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getJvmUptimeMilliseconds);

        // Resource health score
        Gauge.builder("system.resource.health.score")
                .description("Overall resource health score (0-100)")
                .register(meterRegistry, this, ResourceUtilizationMonitor::getResourceHealthScore);
    }

    /**
     * Get used memory in bytes
     */
    public double getUsedMemoryBytes(ResourceUtilizationMonitor monitor) {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Get maximum memory in bytes
     */
    public double getMaxMemoryBytes(ResourceUtilizationMonitor monitor) {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * Get memory utilization percentage
     */
    public double getMemoryUtilizationPercentage(ResourceUtilizationMonitor monitor) {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        return maxMemory > 0 ? (double) usedMemory / maxMemory * 100.0 : 0.0;
    }

    /**
     * Get CPU utilization percentage (simplified calculation)
     */
    public double getCpuUtilizationPercentage(ResourceUtilizationMonitor monitor) {
        double systemLoadAverage = osMXBean.getSystemLoadAverage();
        int availableProcessors = osMXBean.getAvailableProcessors();
        
        if (systemLoadAverage >= 0 && availableProcessors > 0) {
            return Math.min(100.0, (systemLoadAverage / availableProcessors) * 100.0);
        }
        
        // Fallback calculation if system load average is not available
        return calculateCpuUsageAlternative();
    }

    /**
     * Alternative CPU usage calculation
     */
    private double calculateCpuUsageAlternative() {
        // This is a simplified calculation - in production, you might want to use
        // more sophisticated methods or external libraries
        long currentTime = System.currentTimeMillis();
        double currentCpuTime = getCpuTime();
        
        if (previousSystemTime != 0) {
            double timeDiff = currentTime - previousSystemTime;
            double cpuDiff = currentCpuTime - previousCpuUsage;
            
            if (timeDiff > 0) {
                double cpuUsage = (cpuDiff / timeDiff) * 100.0;
                previousCpuUsage = currentCpuTime;
                previousSystemTime = currentTime;
                return Math.min(100.0, Math.max(0.0, cpuUsage));
            }
        }
        
        previousCpuUsage = currentCpuTime;
        previousSystemTime = currentTime;
        return 0.0;
    }

    /**
     * Get CPU time (simplified)
     */
    private double getCpuTime() {
        // This is a placeholder - actual implementation would depend on platform
        return System.nanoTime() / 1000000.0; // Convert to milliseconds
    }

    /**
     * Get heap used bytes
     */
    public double getHeapUsedBytes(ResourceUtilizationMonitor monitor) {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    /**
     * Get heap max bytes
     */
    public double getHeapMaxBytes(ResourceUtilizationMonitor monitor) {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }

    /**
     * Get non-heap used bytes
     */
    public double getNonHeapUsedBytes(ResourceUtilizationMonitor monitor) {
        return memoryMXBean.getNonHeapMemoryUsage().getUsed();
    }

    /**
     * Get available processors
     */
    public double getAvailableProcessors(ResourceUtilizationMonitor monitor) {
        return osMXBean.getAvailableProcessors();
    }

    /**
     * Get JVM uptime in milliseconds
     */
    public double getJvmUptimeMilliseconds(ResourceUtilizationMonitor monitor) {
        return runtimeMXBean.getUptime();
    }

    /**
     * Calculate overall resource health score
     */
    public double getResourceHealthScore(ResourceUtilizationMonitor monitor) {
        double memoryUtilization = getMemoryUtilizationPercentage(monitor);
        double cpuUtilization = getCpuUtilizationPercentage(monitor);
        
        // Calculate health score based on resource utilization
        double memoryScore = Math.max(0.0, 100.0 - memoryUtilization);
        double cpuScore = Math.max(0.0, 100.0 - cpuUtilization);
        
        // Weight the scores (memory is slightly more important for this application)
        double healthScore = (memoryScore * 0.6) + (cpuScore * 0.4);
        
        return Math.max(0.0, Math.min(100.0, healthScore));
    }

    /**
     * Check if system is under resource pressure
     */
    public boolean isUnderResourcePressure() {
        double memoryUtilization = getMemoryUtilizationPercentage(this);
        double cpuUtilization = getCpuUtilizationPercentage(this);
        
        return memoryUtilization > WARNING_MEMORY_THRESHOLD || 
               cpuUtilization > WARNING_CPU_THRESHOLD;
    }

    /**
     * Check if system is in critical resource state
     */
    public boolean isInCriticalResourceState() {
        double memoryUtilization = getMemoryUtilizationPercentage(this);
        double cpuUtilization = getCpuUtilizationPercentage(this);
        
        return memoryUtilization > CRITICAL_MEMORY_THRESHOLD || 
               cpuUtilization > CRITICAL_CPU_THRESHOLD;
    }

    /**
     * Get resource status
     */
    public ResourceStatus getResourceStatus() {
        double memoryUtilization = getMemoryUtilizationPercentage(this);
        double cpuUtilization = getCpuUtilizationPercentage(this);
        double healthScore = getResourceHealthScore(this);
        
        ResourceStatus.Status status;
        if (isInCriticalResourceState()) {
            status = ResourceStatus.Status.CRITICAL;
        } else if (isUnderResourcePressure()) {
            status = ResourceStatus.Status.WARNING;
        } else {
            status = ResourceStatus.Status.HEALTHY;
        }
        
        return new ResourceStatus(status, memoryUtilization, cpuUtilization, healthScore);
    }

    /**
     * Scheduled task to monitor resource utilization
     */
    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void monitorResourceUtilization() {
        ResourceStatus status = getResourceStatus();
        
        if (status.getStatus() == ResourceStatus.Status.CRITICAL) {
            logger.error("CRITICAL: System resources under severe pressure - Memory: {:.2f}%, CPU: {:.2f}%",
                    status.getMemoryUtilization(), status.getCpuUtilization());
            
            meterRegistry.counter("system.resource.alerts", "level", "critical").increment();
            
        } else if (status.getStatus() == ResourceStatus.Status.WARNING) {
            logger.warn("WARNING: System resources under pressure - Memory: {:.2f}%, CPU: {:.2f}%",
                    status.getMemoryUtilization(), status.getCpuUtilization());
            
            meterRegistry.counter("system.resource.alerts", "level", "warning").increment();
            
        } else {
            logger.debug("Resource status healthy - Memory: {:.2f}%, CPU: {:.2f}%, Health Score: {:.2f}",
                    status.getMemoryUtilization(), status.getCpuUtilization(), status.getHealthScore());
        }
    }

    /**
     * Scheduled task to log detailed resource information
     */
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void logDetailedResourceInfo() {
        Runtime runtime = Runtime.getRuntime();
        
        logger.info("Detailed Resource Information:");
        logger.info("- Memory: Used={} MB, Free={} MB, Total={} MB, Max={} MB",
                (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024,
                runtime.freeMemory() / 1024 / 1024,
                runtime.totalMemory() / 1024 / 1024,
                runtime.maxMemory() / 1024 / 1024);
        
        logger.info("- Heap: Used={} MB, Max={} MB",
                memoryMXBean.getHeapMemoryUsage().getUsed() / 1024 / 1024,
                memoryMXBean.getHeapMemoryUsage().getMax() / 1024 / 1024);
        
        logger.info("- Non-Heap: Used={} MB, Max={} MB",
                memoryMXBean.getNonHeapMemoryUsage().getUsed() / 1024 / 1024,
                memoryMXBean.getNonHeapMemoryUsage().getMax() / 1024 / 1024);
        
        logger.info("- CPU: Utilization={:.2f}%, Available Processors={}",
                getCpuUtilizationPercentage(this), osMXBean.getAvailableProcessors());
        
        logger.info("- JVM: Uptime={} minutes", runtimeMXBean.getUptime() / 60000);
        
        logger.info("- Resource Health Score: {:.2f}/100", getResourceHealthScore(this));
    }

    /**
     * Resource status data structure
     */
    public static class ResourceStatus {
        public enum Status {
            HEALTHY, WARNING, CRITICAL
        }

        private final Status status;
        private final double memoryUtilization;
        private final double cpuUtilization;
        private final double healthScore;

        public ResourceStatus(Status status, double memoryUtilization, double cpuUtilization, double healthScore) {
            this.status = status;
            this.memoryUtilization = memoryUtilization;
            this.cpuUtilization = cpuUtilization;
            this.healthScore = healthScore;
        }

        // Getters
        public Status getStatus() { return status; }
        public double getMemoryUtilization() { return memoryUtilization; }
        public double getCpuUtilization() { return cpuUtilization; }
        public double getHealthScore() { return healthScore; }

        @Override
        public String toString() {
            return String.format("ResourceStatus{status=%s, memory=%.2f%%, cpu=%.2f%%, health=%.2f}",
                    status, memoryUtilization, cpuUtilization, healthScore);
        }
    }
}