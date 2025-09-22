package com.macro.mall.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * SLA Report data structure for comprehensive SLA monitoring
 */
public class SLAReport {

    private LocalDateTime generatedAt;
    private double overallCompliance;
    private List<OperationSLA> operationSLAs;
    private String reportPeriod = "Hourly";

    public SLAReport() {
        this.operationSLAs = new ArrayList<>();
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public double getOverallCompliance() {
        return overallCompliance;
    }

    public void setOverallCompliance(double overallCompliance) {
        this.overallCompliance = overallCompliance;
    }

    public List<OperationSLA> getOperationSLAs() {
        return operationSLAs;
    }

    public void setOperationSLAs(List<OperationSLA> operationSLAs) {
        this.operationSLAs = operationSLAs;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public void addOperationSLA(OperationSLA operationSLA) {
        this.operationSLAs.add(operationSLA);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== SLA REPORT (").append(reportPeriod).append(") ==========\n");
        sb.append("Generated: ").append(generatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n");
        sb.append("Overall Compliance: ").append(String.format("%.2f%%", overallCompliance)).append("\n");
        sb.append("-----------------------------------------------\n");
        
        for (OperationSLA operationSLA : operationSLAs) {
            sb.append(operationSLA.toString()).append("\n");
        }
        
        sb.append("===============================================");
        return sb.toString();
    }

    /**
     * Operation-specific SLA data
     */
    public static class OperationSLA {
        private String operationType;
        private long totalOperations;
        private long violations;
        private double compliancePercentage;
        private double averageResponseTime;
        private double targetPercentage;
        private boolean targetMet;

        // Getters and setters
        public String getOperationType() {
            return operationType;
        }

        public void setOperationType(String operationType) {
            this.operationType = operationType;
        }

        public long getTotalOperations() {
            return totalOperations;
        }

        public void setTotalOperations(long totalOperations) {
            this.totalOperations = totalOperations;
        }

        public long getViolations() {
            return violations;
        }

        public void setViolations(long violations) {
            this.violations = violations;
        }

        public double getCompliancePercentage() {
            return compliancePercentage;
        }

        public void setCompliancePercentage(double compliancePercentage) {
            this.compliancePercentage = compliancePercentage;
        }

        public double getAverageResponseTime() {
            return averageResponseTime;
        }

        public void setAverageResponseTime(double averageResponseTime) {
            this.averageResponseTime = averageResponseTime;
        }

        public double getTargetPercentage() {
            return targetPercentage;
        }

        public void setTargetPercentage(double targetPercentage) {
            this.targetPercentage = targetPercentage;
        }

        public boolean isTargetMet() {
            return targetMet;
        }

        public void setTargetMet(boolean targetMet) {
            this.targetMet = targetMet;
        }

        @Override
        public String toString() {
            return String.format("%s: %.2f%% (Target: %.2f%%) | Ops: %d | Violations: %d | Avg Time: %.2fms %s",
                    operationType.toUpperCase(),
                    compliancePercentage,
                    targetPercentage,
                    totalOperations,
                    violations,
                    averageResponseTime,
                    targetMet ? "✓" : "✗");
        }
    }
}