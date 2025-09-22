package com.macro.mall.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SLA (Service Level Agreement) monitoring component
 * Tracks and reports on performance against defined service level objectives
 */
@Component
public class SLAMonitor {

    private static final Logger logger = LoggerFactory.getLogger(SLAMonitor.class);

    private final MeterRegistry meterRegistry;
    private final PerformanceThresholds performanceThresholds;
    
    // SLA tracking data structures
    private final ConcurrentHashMap<String, AtomicLong> operationCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> violationCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> totalResponseTimes = new ConcurrentHashMap<>();
    
    // SLA targets (percentage)
    private static final double SEARCH_SLA_TARGET = 95.0; // 95% of searches under 500ms
    private static final double BATCH_SLA_TARGET = 90.0; // 90% of batch ops under 30s
    private static final double LIFECYCLE_SLA_TARGET = 98.0; // 98% of transitions under 1s
    private static final double INVENTORY_SLA_TARGET = 99.0; // 99% of inventory updates under 200ms
    private static final double OVERALL_AVAILABILITY_TARGET = 99.9; // 99.9% uptime

    public SLAMonitor(MeterRegistry meterRegistry, PerformanceThresholds performanceThresholds) {
        this.meterRegistry = meterRegistry;
        this.performanceThresholds = performanceThresholds;
        initializeMetrics();
    }

    /**
     * Initialize SLA metrics
     */
    private void initializeMetrics() {
        // Initialize operation types
        initializeOperationType("search");
        initializeOperationType("batch");
        initializeOperationType("lifecycle");
        initializeOperationType("inventory");
        initializeOperationType("validation");
    }

    /**
     * Initialize metrics for a specific operation type
     */
    private void initializeOperationType(String operationType) {
        operationCounts.put(operationType, new AtomicLong(0));
        violationCounts.put(operationType, new AtomicLong(0));
        totalResponseTimes.put(operationType, new AtomicLong(0));
    }

    /**
     * Record an operation execution
     */
    public void recordOperation(String operationType, long responseTime) {
        operationCounts.computeIfAbsent(operationType, k -> new AtomicLong(0)).incrementAndGet();
        totalResponseTimes.computeIfAbsent(operationType, k -> new AtomicLong(0)).addAndGet(responseTime);

        // Check if operation violates SLA
        if (!performanceThresholds.isResponseTimeAcceptable(operationType, responseTime)) {
            violationCounts.computeIfAbsent(operationType, k -> new AtomicLong(0)).incrementAndGet();
            
            // Log SLA violation
            logger.warn("SLA violation detected - Operation: {}, Response Time: {}ms, Threshold: {}ms",
                    operationType, responseTime, performanceThresholds.getMaxThreshold(operationType));
            
            // Record metric
            meterRegistry.counter("sla.violations.total", "operation", operationType).increment();
        }

        // Record response time metric
        meterRegistry.timer("sla.response.time", "operation", operationType)
                .record(responseTime, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    /**
     * Get current SLA compliance percentage for an operation type
     */
    public double getSLACompliance(String operationType) {
        AtomicLong totalOps = operationCounts.get(operationType);
        AtomicLong violations = violationCounts.get(operationType);
        
        if (totalOps == null || totalOps.get() == 0) {
            return 100.0; // No operations recorded, assume 100% compliance
        }
        
        long violationCount = violations != null ? violations.get() : 0;
        return ((double) (totalOps.get() - violationCount) / totalOps.get()) * 100.0;
    }

    /**
     * Get average response time for an operation type
     */
    public double getAverageResponseTime(String operationType) {
        AtomicLong totalOps = operationCounts.get(operationType);
        AtomicLong totalTime = totalResponseTimes.get(operationType);
        
        if (totalOps == null || totalOps.get() == 0) {
            return 0.0;
        }
        
        return (double) totalTime.get() / totalOps.get();
    }

    /**
     * Get SLA target for operation type
     */
    public double getSLATarget(String operationType) {
        switch (operationType.toLowerCase()) {
            case "search":
                return SEARCH_SLA_TARGET;
            case "batch":
                return BATCH_SLA_TARGET;
            case "lifecycle":
                return LIFECYCLE_SLA_TARGET;
            case "inventory":
                return INVENTORY_SLA_TARGET;
            default:
                return 95.0; // Default target
        }
    }

    /**
     * Check if SLA target is being met
     */
    public boolean isSLATargetMet(String operationType) {
        double compliance = getSLACompliance(operationType);
        double target = getSLATarget(operationType);
        return compliance >= target;
    }

    /**
     * Get overall system SLA compliance
     */
    public double getOverallSLACompliance() {
        double totalWeightedCompliance = 0.0;
        double totalWeight = 0.0;
        
        // Weight different operations based on importance
        double searchWeight = 0.3;
        double batchWeight = 0.2;
        double lifecycleWeight = 0.3;
        double inventoryWeight = 0.2;
        
        totalWeightedCompliance += getSLACompliance("search") * searchWeight;
        totalWeightedCompliance += getSLACompliance("batch") * batchWeight;
        totalWeightedCompliance += getSLACompliance("lifecycle") * lifecycleWeight;
        totalWeightedCompliance += getSLACompliance("inventory") * inventoryWeight;
        
        totalWeight = searchWeight + batchWeight + lifecycleWeight + inventoryWeight;
        
        return totalWeight > 0 ? totalWeightedCompliance / totalWeight : 100.0;
    }

    /**
     * Generate SLA report
     */
    public SLAReport generateSLAReport() {
        SLAReport report = new SLAReport();
        report.setGeneratedAt(LocalDateTime.now());
        report.setOverallCompliance(getOverallSLACompliance());
        
        // Add operation-specific data
        for (String operationType : operationCounts.keySet()) {
            SLAReport.OperationSLA operationSLA = new SLAReport.OperationSLA();
            operationSLA.setOperationType(operationType);
            operationSLA.setTotalOperations(operationCounts.get(operationType).get());
            operationSLA.setViolations(violationCounts.getOrDefault(operationType, new AtomicLong(0)).get());
            operationSLA.setCompliancePercentage(getSLACompliance(operationType));
            operationSLA.setAverageResponseTime(getAverageResponseTime(operationType));
            operationSLA.setTargetPercentage(getSLATarget(operationType));
            operationSLA.setTargetMet(isSLATargetMet(operationType));
            
            report.addOperationSLA(operationSLA);
        }
        
        return report;
    }

    /**
     * Scheduled task to check SLA compliance and log warnings
     */
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void checkSLACompliance() {
        logger.info("SLA Compliance Check - {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        boolean overallCompliant = true;
        
        for (String operationType : operationCounts.keySet()) {
            double compliance = getSLACompliance(operationType);
            double target = getSLATarget(operationType);
            boolean targetMet = compliance >= target;
            
            if (!targetMet) {
                overallCompliant = false;
                logger.warn("SLA target not met - Operation: {}, Compliance: {:.2f}%, Target: {:.2f}%",
                        operationType, compliance, target);
            } else {
                logger.debug("SLA target met - Operation: {}, Compliance: {:.2f}%, Target: {:.2f}%",
                        operationType, compliance, target);
            }
            
            // Record compliance metric
            meterRegistry.gauge("sla.compliance.percentage", 
                    io.micrometer.core.instrument.Tags.of("operation", operationType), compliance);
        }
        
        double overallCompliance = getOverallSLACompliance();
        logger.info("Overall SLA Compliance: {:.2f}%", overallCompliance);
        
        if (!overallCompliant || overallCompliance < OVERALL_AVAILABILITY_TARGET) {
            logger.error("CRITICAL: Overall SLA compliance below target - Current: {:.2f}%, Target: {:.2f}%",
                    overallCompliance, OVERALL_AVAILABILITY_TARGET);
        }
        
        // Record overall compliance metric
        meterRegistry.gauge("sla.overall.compliance", overallCompliance);
    }

    /**
     * Scheduled task to generate detailed SLA reports
     */
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void generateHourlySLAReport() {
        SLAReport report = generateSLAReport();
        logger.info("Hourly SLA Report Generated:\n{}", report.toString());
        
        // Reset hourly counters (optional, depending on requirements)
        // resetCounters();
    }

    /**
     * Reset all counters (use carefully)
     */
    public void resetCounters() {
        operationCounts.values().forEach(counter -> counter.set(0));
        violationCounts.values().forEach(counter -> counter.set(0));
        totalResponseTimes.values().forEach(counter -> counter.set(0));
        logger.info("SLA counters reset");
    }

    /**
     * Get SLA status summary
     */
    public String getSLAStatusSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("SLA Status Summary:\n");
        summary.append("Overall Compliance: ").append(String.format("%.2f%%", getOverallSLACompliance())).append("\n");
        
        for (String operationType : operationCounts.keySet()) {
            summary.append(String.format("- %s: %.2f%% (Target: %.2f%%) - %s\n",
                    operationType,
                    getSLACompliance(operationType),
                    getSLATarget(operationType),
                    isSLATargetMet(operationType) ? "✓" : "✗"));
        }
        
        return summary.toString();
    }

    // Getters for external access
    public ConcurrentHashMap<String, AtomicLong> getOperationCounts() {
        return operationCounts;
    }

    public ConcurrentHashMap<String, AtomicLong> getViolationCounts() {
        return violationCounts;
    }
}