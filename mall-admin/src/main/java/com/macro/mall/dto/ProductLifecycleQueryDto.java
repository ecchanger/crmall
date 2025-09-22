package com.macro.mall.dto;

import com.macro.mall.enums.ProductLifecycleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品生命周期查询参数DTO
 * Created for Product Management Module Enhancement
 */
@Data
public class ProductLifecycleQueryDto {

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "商品名称（模糊查询）")
    private String productName;

    @ApiModelProperty(value = "当前状态列表")
    private List<String> currentStates;

    @ApiModelProperty(value = "审核员ID")
    private Long reviewerId;

    @ApiModelProperty(value = "状态变更开始时间")
    private Date stateChangedFrom;

    @ApiModelProperty(value = "状态变更结束时间")
    private Date stateChangedTo;

    @ApiModelProperty(value = "是否只显示待审核")
    private Boolean pendingReviewOnly;

    @ApiModelProperty(value = "是否只显示已过期审核")
    private Boolean expiredReviewOnly;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String sortBy = "stateChangedAt";

    @ApiModelProperty(value = "排序方向")
    private String sortDirection = "DESC";

    // Business methods
    public List<ProductLifecycleStatus> getCurrentStatusEnums() {
        if (currentStates == null || currentStates.isEmpty()) {
            return null;
        }
        return currentStates.stream()
                .map(ProductLifecycleStatus::fromCode)
                .collect(java.util.stream.Collectors.toList());
    }

    public void setCurrentStatusEnums(List<ProductLifecycleStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            this.currentStates = null;
            return;
        }
        this.currentStates = statuses.stream()
                .map(ProductLifecycleStatus::getCode)
                .collect(java.util.stream.Collectors.toList());
    }
}