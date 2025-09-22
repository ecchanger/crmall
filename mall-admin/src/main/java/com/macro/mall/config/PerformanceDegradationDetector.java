package com.macro.mall.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Performance degradation detector for Product Management Module
 * Monitors performance trends and detects degradation patterns
 */
@Component
public class PerformanceDegradationDetector {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceDegradationDetector.class);

    private final MeterRegistry meterRegistry;
    private final AlertThresholdConfig alertThresholdConfig;
    
    // Historical performance data
    private final ConcurrentHashMap<String, PerformanceWindow> performanceWindows = new ConcurrentHashMap<>();
    
    // Detection configuration
    private static final int WINDOW_SIZE = 10; // Number of measurements to keep
    private static final double DEGRADATION_THRESHOLD = 20.0; // 20% degradation threshold
    private static final long MEASUREMENT_INTERVAL = 60000; // 1 minute

    public PerformanceDegradationDetector(MeterRegistry meterRegistry, AlertThresholdConfig alertThresholdConfig) {
        this.meterRegistry = meterRegistry;
        this.alertThresholdConfig = alertThresholdConfig;
        initializePerformanceWindows();
    }

    /**
     * Initialize performance tracking windows for different operations
     */
    private void initializePerformanceWindows() {
        performanceWindows.put("search", new PerformanceWindow("search"));
        performanceWindows.put("batch", new PerformanceWindow("batch"));
        performanceWindows.put("lifecycle", new PerformanceWindow("lifecycle"));
        performanceWindows.put("inventory", new PerformanceWindow("inventory"));
        performanceWindows.put("validation", new PerformanceWindow("validation"));
    }

    /**
     * Record performance measurement
     */
    public void recordPerformance(String operationType, long responseTime, double throughput, double errorRate) {
        PerformanceWindow window = performanceWindows.get(operationType);
        if (window != null) {
            window.addMeasurement(responseTime, throughput, errorRate);
            
            // Check for degradation
            checkForDegradation(operationType, window);
        }
    }

    /**
     * Check for performance degradation
     */
    private void checkForDegradation(String operationType, PerformanceWindow window) {
        if (!window.hasEnoughData()) {
            return; // Not enough data for analysis
        }

        // Check response time degradation
        double responseTimeDegradation = window.getResponseTimeDegradation();
        if (responseTimeDegradation > DEGRADATION_THRESHOLD) {
            AlertThresholdConfig.AlertLevel alertLevel = responseTimeDegradation > 50.0 ? 
                    AlertThresholdConfig.AlertLevel.CRITICAL : AlertThresholdConfig.AlertLevel.WARNING;
            
            reportDegradation(operationType, "response_time", responseTimeDegradation, alertLevel);
        }

        // Check throughput degradation
        double throughputDegradation = window.getThroughputDegradation();
        if (throughputDegradation > DEGRADATION_THRESHOLD) {
            AlertThresholdConfig.AlertLevel alertLevel = throughputDegradation > 50.0 ? 
                    AlertThresholdConfig.AlertLevel.CRITICAL : AlertThresholdConfig.AlertLevel.WARNING;
            
            reportDegradation(operationType, "throughput", throughputDegradation, alertLevel);
        }

        // Check error rate increase
        double errorRateIncrease = window.getErrorRateIncrease();
        if (errorRateIncrease > 50.0) { // 50% increase in error rate
            AlertThresholdConfig.AlertLevel alertLevel = errorRateIncrease > 100.0 ? 
                    AlertThresholdConfig.AlertLevel.CRITICAL : AlertThresholdConfig.AlertLevel.WARNING;
            
            reportDegradation(operationType, "error_rate", errorRateIncrease, alertLevel);
        }
    }

    /**
     * Report performance degradation
     */
    private void reportDegradation(String operationType, String metricType, double degradationPercentage, 
                                   AlertThresholdConfig.AlertLevel alertLevel) {
        String logLevel = alertLevel == AlertThresholdConfig.AlertLevel.CRITICAL ? "CRITICAL" : "WARNING";
        
        logger.warn("{}: Performance degradation detected - Operation: {}, Metric: {}, Degradation: {:.2f}%",
                logLevel, operationType, metricType, degradationPercentage);
        
        // Record metric
        meterRegistry.counter("performance.degradation.detected",
                "operation", operationType,
                "metric", metricType,
                "severity", alertLevel.name().toLowerCase()).increment();
        
        // Update degradation gauge
        meterRegistry.gauge("performance.degradation.percentage",
                io.micrometer.core.instrument.Tags.of(
                        "operation", operationType,
                        "metric", metricType),
                degradationPercentage);
    }

    /**
     * Get current performance status
     */
    public PerformanceStatus getPerformanceStatus(String operationType) {
        PerformanceWindow window = performanceWindows.get(operationType);
        if (window == null || !window.hasEnoughData()) {
            return new PerformanceStatus(operationType, PerformanceStatus.Status.UNKNOWN);
        }

        double maxDegradation = Math.max(
                Math.max(window.getResponseTimeDegradation(), window.getThroughputDegradation()),
                window.getErrorRateIncrease());

        PerformanceStatus.Status status;
        if (maxDegradation > 50.0) {
            status = PerformanceStatus.Status.CRITICAL;
        } else if (maxDegradation > DEGRADATION_THRESHOLD) {
            status = PerformanceStatus.Status.WARNING;
        } else {
            status = PerformanceStatus.Status.HEALTHY;
        }

        return new PerformanceStatus(operationType, status, window.getAverageResponseTime(),
                window.getAverageThroughput(), window.getAverageErrorRate());
    }

    /**
     * Get overall system performance status
     */
    public PerformanceStatus getOverallPerformanceStatus() {
        PerformanceStatus.Status worstStatus = PerformanceStatus.Status.HEALTHY;
        double avgResponseTime = 0.0;
        double avgThroughput = 0.0;
        double avgErrorRate = 0.0;
        int validWindows = 0;

        for (String operationType : performanceWindows.keySet()) {
            PerformanceStatus status = getPerformanceStatus(operationType);
            
            if (status.getStatus() != PerformanceStatus.Status.UNKNOWN) {
                validWindows++;
                avgResponseTime += status.getAverageResponseTime();
                avgThroughput += status.getAverageThroughput();
                avgErrorRate += status.getAverageErrorRate();
                
                if (status.getStatus().ordinal() > worstStatus.ordinal()) {
                    worstStatus = status.getStatus();
                }
            }
        }

        if (validWindows > 0) {
            avgResponseTime /= validWindows;
            avgThroughput /= validWindows;
            avgErrorRate /= validWindows;
        }

        return new PerformanceStatus("overall", worstStatus, avgResponseTime, avgThroughput, avgErrorRate);
    }

    /**
     * Scheduled task to check for performance degradation
     */
    @Scheduled(fixedRate = 60000) // Every minute
    public void performDegradationCheck() {
        logger.debug("Running performance degradation check...");
        
        for (String operationType : performanceWindows.keySet()) {
            PerformanceStatus status = getPerformanceStatus(operationType);
            
            if (status.getStatus() == PerformanceStatus.Status.CRITICAL ||
                status.getStatus() == PerformanceStatus.Status.WARNING) {
                
                logger.warn("Performance issue detected for {}: {}", operationType, status);
            }
        }
        
        // Log overall status
        PerformanceStatus overallStatus = getOverallPerformanceStatus();
        logger.info("Overall performance status: {}", overallStatus);
    }

    /**
     * Performance window for tracking historical data
     */
    private static class PerformanceWindow {
        private final String operationType;
        private final CircularBuffer<Long> responseTimes;
        private final CircularBuffer<Double> throughputValues;
        private final CircularBuffer<Double> errorRates;

        public PerformanceWindow(String operationType) {
            this.operationType = operationType;
            this.responseTimes = new CircularBuffer<>(WINDOW_SIZE);
            this.throughputValues = new CircularBuffer<>(WINDOW_SIZE);
            this.errorRates = new CircularBuffer<>(WINDOW_SIZE);
        }

        public void addMeasurement(long responseTime, double throughput, double errorRate) {
            responseTimes.add(responseTime);
            throughputValues.add(throughput);
            errorRates.add(errorRate);
        }

        public boolean hasEnoughData() {
            return responseTimes.size() >= WINDOW_SIZE / 2; // At least half the window
        }

        public double getResponseTimeDegradation() {
            if (responseTimes.size() < 4) return 0.0;
            
            double recentAvg = responseTimes.getRecentAverage(responseTimes.size() / 2);
            double historicalAvg = responseTimes.getHistoricalAverage(responseTimes.size() / 2);
            
            return historicalAvg > 0 ? ((recentAvg - historicalAvg) / historicalAvg) * 100 : 0.0;
        }

        public double getThroughputDegradation() {
            if (throughputValues.size() < 4) return 0.0;
            
            double recentAvg = throughputValues.getRecentAverage(throughputValues.size() / 2);
            double historicalAvg = throughputValues.getHistoricalAverage(throughputValues.size() / 2);
            
            return historicalAvg > 0 ? ((historicalAvg - recentAvg) / historicalAvg) * 100 : 0.0;
        }

        public double getErrorRateIncrease() {
            if (errorRates.size() < 4) return 0.0;
            
            double recentAvg = errorRates.getRecentAverage(errorRates.size() / 2);
            double historicalAvg = errorRates.getHistoricalAverage(errorRates.size() / 2);
            
            return historicalAvg > 0 ? ((recentAvg - historicalAvg) / historicalAvg) * 100 : 0.0;
        }

        public double getAverageResponseTime() {
            return responseTimes.getOverallAverage();
        }

        public double getAverageThroughput() {
            return throughputValues.getOverallAverage();
        }

        public double getAverageErrorRate() {
            return errorRates.getOverallAverage();
        }
    }

    /**
     * Circular buffer for storing performance measurements
     */
    private static class CircularBuffer<T extends Number> {
        private final T[] buffer;
        private final int capacity;
        private int size = 0;
        private int head = 0;

        @SuppressWarnings("unchecked")
        public CircularBuffer(int capacity) {
            this.capacity = capacity;
            this.buffer = (T[]) new Number[capacity];
        }

        public void add(T value) {
            buffer[head] = value;
            head = (head + 1) % capacity;
            if (size < capacity) {
                size++;
            }
        }

        public int size() {
            return size;
        }

        public double getRecentAverage(int count) {
            if (size == 0) return 0.0;
            count = Math.min(count, size);
            
            double sum = 0.0;
            int index = (head - 1 + capacity) % capacity;
            
            for (int i = 0; i < count; i++) {
                sum += buffer[index].doubleValue();
                index = (index - 1 + capacity) % capacity;
            }
            
            return sum / count;
        }

        public double getHistoricalAverage(int count) {
            if (size <= count) return 0.0;
            
            double sum = 0.0;
            int startIndex = (head - size + capacity) % capacity;
            
            for (int i = 0; i < count; i++) {
                sum += buffer[(startIndex + i) % capacity].doubleValue();
            }
            
            return sum / count;
        }

        public double getOverallAverage() {
            if (size == 0) return 0.0;
            
            double sum = 0.0;
            for (int i = 0; i < size; i++) {
                sum += buffer[i].doubleValue();
            }
            
            return sum / size;
        }
    }

    /**
     * Performance status data structure
     */
    public static class PerformanceStatus {
        public enum Status {
            UNKNOWN, HEALTHY, WARNING, CRITICAL
        }

        private final String operationType;
        private final Status status;
        private final double averageResponseTime;
        private final double averageThroughput;
        private final double averageErrorRate;

        public PerformanceStatus(String operationType, Status status) {
            this(operationType, status, 0.0, 0.0, 0.0);
        }

        public PerformanceStatus(String operationType, Status status, double averageResponseTime,
                                double averageThroughput, double averageErrorRate) {
            this.operationType = operationType;
            this.status = status;
            this.averageResponseTime = averageResponseTime;
            this.averageThroughput = averageThroughput;
            this.averageErrorRate = averageErrorRate;
        }

        // Getters
        public String getOperationType() { return operationType; }
        public Status getStatus() { return status; }
        public double getAverageResponseTime() { return averageResponseTime; }
        public double getAverageThroughput() { return averageThroughput; }
        public double getAverageErrorRate() { return averageErrorRate; }

        @Override
        public String toString() {
            return String.format("%s: %s (RT: %.2fms, TP: %.2f, ER: %.2f%%)",
                    operationType, status, averageResponseTime, averageThroughput, averageErrorRate);
        }
    }
}