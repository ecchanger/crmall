package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 批量操作结果DTO
 * Created for Product Management Module Enhancement
 */
@Data
public class BatchOperationResultDto {

    @ApiModelProperty(value = "操作ID")
    private String operationId;

    @ApiModelProperty(value = "操作类型")
    private String operationType;

    @ApiModelProperty(value = "操作状态：PENDING->待执行；RUNNING->执行中；SUCCESS->成功；FAILED->失败；PARTIAL->部分成功")
    private String status;

    @ApiModelProperty(value = "操作开始时间")
    private Date startTime;

    @ApiModelProperty(value = "操作结束时间")
    private Date endTime;

    @ApiModelProperty(value = "总数量")
    private Integer totalCount;

    @ApiModelProperty(value = "成功数量")
    private Integer successCount;

    @ApiModelProperty(value = "失败数量")
    private Integer failedCount;

    @ApiModelProperty(value = "跳过数量")
    private Integer skippedCount;

    @ApiModelProperty(value = "处理进度百分比")
    private Double progressPercentage;

    @ApiModelProperty(value = "执行时长（秒）")
    private Long executionTimeSeconds;

    @ApiModelProperty(value = "操作摘要")
    private String summary;

    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "成功处理的商品ID列表")
    private List<Long> successfulProductIds;

    @ApiModelProperty(value = "失败处理的商品ID列表")
    private List<Long> failedProductIds;

    @ApiModelProperty(value = "详细结果列表")
    private List<ProductOperationResult> detailResults;

    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "是否可以重试")
    private Boolean canRetry;

    @ApiModelProperty(value = "是否可以回滚")
    private Boolean canRollback;

    @ApiModelProperty(value = "回滚操作ID")
    private String rollbackOperationId;

    // Business methods
    public boolean isCompleted() {
        return "SUCCESS".equals(status) || "FAILED".equals(status) || "PARTIAL".equals(status);
    }

    public boolean isRunning() {
        return "RUNNING".equals(status);
    }

    public boolean isSuccess() {
        return "SUCCESS".equals(status);
    }

    public boolean isFailed() {
        return "FAILED".equals(status);
    }

    public boolean isPartialSuccess() {
        return "PARTIAL".equals(status);
    }

    public double getSuccessRate() {
        if (totalCount == null || totalCount == 0) return 0.0;
        return (successCount != null ? successCount.doubleValue() : 0.0) / totalCount.doubleValue() * 100.0;
    }

    public double getFailureRate() {
        if (totalCount == null || totalCount == 0) return 0.0;
        return (failedCount != null ? failedCount.doubleValue() : 0.0) / totalCount.doubleValue() * 100.0;
    }

    public String getStatusText() {
        switch (status != null ? status : "UNKNOWN") {
            case "PENDING": return "待执行";
            case "RUNNING": return "执行中";
            case "SUCCESS": return "成功";
            case "FAILED": return "失败";
            case "PARTIAL": return "部分成功";
            default: return "未知";
        }
    }

    /**
     * 单个商品操作结果
     */
    @Data
    public static class ProductOperationResult {
        @ApiModelProperty(value = "商品ID")
        private Long productId;

        @ApiModelProperty(value = "商品名称")
        private String productName;

        @ApiModelProperty(value = "操作状态：SUCCESS->成功；FAILED->失败；SKIPPED->跳过")
        private String status;

        @ApiModelProperty(value = "错误消息")
        private String errorMessage;

        @ApiModelProperty(value = "操作前值")
        private String beforeValue;

        @ApiModelProperty(value = "操作后值")
        private String afterValue;

        @ApiModelProperty(value = "处理时间")
        private Date processedAt;

        public boolean isSuccess() {
            return "SUCCESS".equals(status);
        }

        public boolean isFailed() {
            return "FAILED".equals(status);
        }

        public boolean isSkipped() {
            return "SKIPPED".equals(status);
        }
    }
}