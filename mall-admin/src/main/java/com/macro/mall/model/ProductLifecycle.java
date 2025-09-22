package com.macro.mall.model;

import com.macro.mall.enums.ProductLifecycleStatus;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品生命周期管理实体
 * Created for Product Management Module Enhancement
 */
public class ProductLifecycle implements Serializable {
    
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "当前生命周期状态")
    private String currentState;

    @ApiModelProperty(value = "前一个状态")
    private String previousState;

    @ApiModelProperty(value = "状态变更时间")
    private Date stateChangedAt;

    @ApiModelProperty(value = "状态变更操作人ID")
    private Long stateChangedBy;

    @ApiModelProperty(value = "分配的审核员ID")
    private Long reviewerId;

    @ApiModelProperty(value = "审核备注")
    private String reviewNotes;

    @ApiModelProperty(value = "计划发布时间")
    private Date scheduledPublishDate;

    @ApiModelProperty(value = "实际发布时间")
    private Date actualPublishDate;

    @ApiModelProperty(value = "下次审核时间")
    private Date nextReviewDate;

    @ApiModelProperty(value = "最后审核时间")
    private Date lastReviewedDate;

    @ApiModelProperty(value = "状态持续时间（分钟）")
    private Integer stateDurationMinutes;

    @ApiModelProperty(value = "操作备注")
    private String operationNotes;

    @ApiModelProperty(value = "自动处理标识")
    private Boolean autoProcessed;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    private static final long serialVersionUID = 1L;

    // Constructors
    public ProductLifecycle() {}

    public ProductLifecycle(Long productId, ProductLifecycleStatus status, Long operatorId) {
        this.productId = productId;
        this.currentState = status.getCode();
        this.stateChangedBy = operatorId;
        this.stateChangedAt = new Date();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.autoProcessed = false;
        this.version = 1;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getPreviousState() {
        return previousState;
    }

    public void setPreviousState(String previousState) {
        this.previousState = previousState;
    }

    public Date getStateChangedAt() {
        return stateChangedAt;
    }

    public void setStateChangedAt(Date stateChangedAt) {
        this.stateChangedAt = stateChangedAt;
    }

    public Long getStateChangedBy() {
        return stateChangedBy;
    }

    public void setStateChangedBy(Long stateChangedBy) {
        this.stateChangedBy = stateChangedBy;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public Date getScheduledPublishDate() {
        return scheduledPublishDate;
    }

    public void setScheduledPublishDate(Date scheduledPublishDate) {
        this.scheduledPublishDate = scheduledPublishDate;
    }

    public Date getActualPublishDate() {
        return actualPublishDate;
    }

    public void setActualPublishDate(Date actualPublishDate) {
        this.actualPublishDate = actualPublishDate;
    }

    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public Date getLastReviewedDate() {
        return lastReviewedDate;
    }

    public void setLastReviewedDate(Date lastReviewedDate) {
        this.lastReviewedDate = lastReviewedDate;
    }

    public Integer getStateDurationMinutes() {
        return stateDurationMinutes;
    }

    public void setStateDurationMinutes(Integer stateDurationMinutes) {
        this.stateDurationMinutes = stateDurationMinutes;
    }

    public String getOperationNotes() {
        return operationNotes;
    }

    public void setOperationNotes(String operationNotes) {
        this.operationNotes = operationNotes;
    }

    public Boolean getAutoProcessed() {
        return autoProcessed;
    }

    public void setAutoProcessed(Boolean autoProcessed) {
        this.autoProcessed = autoProcessed;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Business methods
    public ProductLifecycleStatus getCurrentStatus() {
        return ProductLifecycleStatus.fromCode(this.currentState);
    }

    public void setCurrentStatus(ProductLifecycleStatus status) {
        this.currentState = status.getCode();
    }

    public ProductLifecycleStatus getPreviousStatus() {
        return this.previousState != null ? ProductLifecycleStatus.fromCode(this.previousState) : null;
    }

    public void setPreviousStatus(ProductLifecycleStatus status) {
        this.previousState = status != null ? status.getCode() : null;
    }

    public boolean canTransitionTo(ProductLifecycleStatus targetStatus) {
        return getCurrentStatus().canTransitionTo(targetStatus);
    }

    public void transitionTo(ProductLifecycleStatus targetStatus, Long operatorId, String notes) {
        if (!canTransitionTo(targetStatus)) {
            throw new IllegalStateException(
                String.format("无法从状态 %s 转换到 %s", getCurrentStatus().getName(), targetStatus.getName())
            );
        }
        
        this.previousState = this.currentState;
        this.currentState = targetStatus.getCode();
        this.stateChangedBy = operatorId;
        this.stateChangedAt = new Date();
        this.operationNotes = notes;
        this.updatedAt = new Date();
        this.version++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", currentState=").append(currentState);
        sb.append(", previousState=").append(previousState);
        sb.append(", stateChangedAt=").append(stateChangedAt);
        sb.append(", stateChangedBy=").append(stateChangedBy);
        sb.append(", reviewerId=").append(reviewerId);
        sb.append(", reviewNotes=").append(reviewNotes);
        sb.append(", scheduledPublishDate=").append(scheduledPublishDate);
        sb.append(", actualPublishDate=").append(actualPublishDate);
        sb.append(", nextReviewDate=").append(nextReviewDate);
        sb.append(", lastReviewedDate=").append(lastReviewedDate);
        sb.append(", stateDurationMinutes=").append(stateDurationMinutes);
        sb.append(", operationNotes=").append(operationNotes);
        sb.append(", autoProcessed=").append(autoProcessed);
        sb.append(", version=").append(version);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}