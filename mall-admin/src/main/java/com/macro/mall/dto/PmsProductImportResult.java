package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 商品导入结果
 * Created by macro on 2023/12/22.
 */
public class PmsProductImportResult {
    @ApiModelProperty(value = "导入ID")
    private Long importId;

    @ApiModelProperty(value = "导入状态：0->待处理；1->处理中；2->成功；3->失败；4->部分成功")
    private Integer status;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "总记录数")
    private Integer totalCount;

    @ApiModelProperty(value = "成功导入数")
    private Integer successCount;

    @ApiModelProperty(value = "失败数")
    private Integer failureCount;

    @ApiModelProperty(value = "跳过数")
    private Integer skipCount;

    @ApiModelProperty(value = "处理进度百分比")
    private Integer progress;

    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "错误详情列表")
    private List<String> errorDetails;

    @ApiModelProperty(value = "警告信息列表")
    private List<String> warnings;

    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @ApiModelProperty(value = "完成时间")
    private java.util.Date completeTime;

    @ApiModelProperty(value = "执行耗时（毫秒）")
    private Long executionTime;

    @ApiModelProperty(value = "是否可以重试")
    private Boolean canRetry;

    public Long getImportId() {
        return importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public Integer getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(Integer skipCount) {
        this.skipCount = skipCount;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(List<String> errorDetails) {
        this.errorDetails = errorDetails;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(java.util.Date completeTime) {
        this.completeTime = completeTime;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Boolean getCanRetry() {
        return canRetry;
    }

    public void setCanRetry(Boolean canRetry) {
        this.canRetry = canRetry;
    }
}