package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import com.macro.mall.model.PmsBulkOperation;

/**
 * 批量操作结果
 * Created by macro on 2023/12/22.
 */
public class PmsBulkOperationResult extends PmsBulkOperation {
    @ApiModelProperty(value = "执行耗时（毫秒）")
    private Long executionTime;

    @ApiModelProperty(value = "错误详情列表")
    private java.util.List<String> errorDetails;

    @ApiModelProperty(value = "成功处理的商品ID列表")
    private java.util.List<Long> successProductIds;

    @ApiModelProperty(value = "失败处理的商品ID列表")
    private java.util.List<Long> failureProductIds;

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public java.util.List<String> getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(java.util.List<String> errorDetails) {
        this.errorDetails = errorDetails;
    }

    public java.util.List<Long> getSuccessProductIds() {
        return successProductIds;
    }

    public void setSuccessProductIds(java.util.List<Long> successProductIds) {
        this.successProductIds = successProductIds;
    }

    public java.util.List<Long> getFailureProductIds() {
        return failureProductIds;
    }

    public void setFailureProductIds(java.util.List<Long> failureProductIds) {
        this.failureProductIds = failureProductIds;
    }
}