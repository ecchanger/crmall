package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 批量操作记录实体类
 * Created by macro on 2023/12/22.
 */
public class PmsBulkOperation implements Serializable {
    private Long id;

    @ApiModelProperty(value = "操作类型：1->批量更新；2->批量状态变更；3->批量删除；4->批量导入；5->批量导出")
    private Integer operationType;

    @ApiModelProperty(value = "操作名称")
    private String operationName;

    @ApiModelProperty(value = "操作描述")
    private String description;

    @ApiModelProperty(value = "操作状态：0->待执行；1->执行中；2->执行成功；3->执行失败；4->部分成功")
    private Integer status;

    @ApiModelProperty(value = "总记录数")
    private Integer totalCount;

    @ApiModelProperty(value = "成功处理数")
    private Integer successCount;

    @ApiModelProperty(value = "失败处理数")
    private Integer failureCount;

    @ApiModelProperty(value = "处理进度百分比")
    private Integer progress;

    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "操作参数JSON")
    private String parameters;

    @ApiModelProperty(value = "结果文件路径")
    private String resultFilePath;

    @ApiModelProperty(value = "创建人ID")
    private Long createdBy;

    @ApiModelProperty(value = "创建人姓名")
    private String createdByName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "开始执行时间")
    private Date startTime;

    @ApiModelProperty(value = "完成时间")
    private Date completeTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operationType=").append(operationType);
        sb.append(", operationName=").append(operationName);
        sb.append(", description=").append(description);
        sb.append(", status=").append(status);
        sb.append(", totalCount=").append(totalCount);
        sb.append(", successCount=").append(successCount);
        sb.append(", failureCount=").append(failureCount);
        sb.append(", progress=").append(progress);
        sb.append(", errorMessage=").append(errorMessage);
        sb.append(", parameters=").append(parameters);
        sb.append(", resultFilePath=").append(resultFilePath);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", createdByName=").append(createdByName);
        sb.append(", createTime=").append(createTime);
        sb.append(", startTime=").append(startTime);
        sb.append(", completeTime=").append(completeTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}