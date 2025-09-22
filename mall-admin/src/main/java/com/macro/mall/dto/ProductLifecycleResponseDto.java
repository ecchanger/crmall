package com.macro.mall.dto;

import com.macro.mall.enums.ProductLifecycleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品生命周期响应DTO
 * Created for Product Management Module Enhancement
 */
@Data
public class ProductLifecycleResponseDto {

    @ApiModelProperty(value = "生命周期ID")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品编号")
    private String productSn;

    @ApiModelProperty(value = "当前状态代码")
    private String currentState;

    @ApiModelProperty(value = "当前状态名称")
    private String currentStateName;

    @ApiModelProperty(value = "当前状态描述")
    private String currentStateDescription;

    @ApiModelProperty(value = "前一个状态代码")
    private String previousState;

    @ApiModelProperty(value = "前一个状态名称")
    private String previousStateName;

    @ApiModelProperty(value = "状态变更时间")
    private Date stateChangedAt;

    @ApiModelProperty(value = "状态变更操作人ID")
    private Long stateChangedBy;

    @ApiModelProperty(value = "状态变更操作人名称")
    private String stateChangedByName;

    @ApiModelProperty(value = "审核员ID")
    private Long reviewerId;

    @ApiModelProperty(value = "审核员名称")
    private String reviewerName;

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

    @ApiModelProperty(value = "状态持续时间（可读格式）")
    private String stateDurationText;

    @ApiModelProperty(value = "操作备注")
    private String operationNotes;

    @ApiModelProperty(value = "下一个可能的状态列表")
    private List<StatusOptionDto> nextPossibleStates;

    @ApiModelProperty(value = "是否可以编辑")
    private Boolean canEdit;

    @ApiModelProperty(value = "是否对用户可见")
    private Boolean visibleToUsers;

    @ApiModelProperty(value = "是否需要立即处理")
    private Boolean requiresImmediateAction;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    // Business methods
    public ProductLifecycleStatus getCurrentStatusEnum() {
        return ProductLifecycleStatus.fromCode(this.currentState);
    }

    public ProductLifecycleStatus getPreviousStatusEnum() {
        return this.previousState != null ? ProductLifecycleStatus.fromCode(this.previousState) : null;
    }

    /**
     * 状态选项DTO
     */
    @Data
    public static class StatusOptionDto {
        @ApiModelProperty(value = "状态代码")
        private String code;

        @ApiModelProperty(value = "状态名称")
        private String name;

        @ApiModelProperty(value = "状态描述")
        private String description;

        @ApiModelProperty(value = "是否需要审核员")
        private Boolean requiresReviewer;

        @ApiModelProperty(value = "是否高风险操作")
        private Boolean highRisk;

        public StatusOptionDto(ProductLifecycleStatus status) {
            this.code = status.getCode();
            this.name = status.getName();
            this.description = status.getDescription();
            this.requiresReviewer = (status == ProductLifecycleStatus.UNDER_REVIEW);
            this.highRisk = (status == ProductLifecycleStatus.DISCONTINUED || 
                           status == ProductLifecycleStatus.ARCHIVED);
        }
    }
}