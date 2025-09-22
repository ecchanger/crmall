package com.macro.mall.dto;

import com.macro.mall.enums.ProductLifecycleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 商品生命周期状态转换请求DTO
 * Created for Product Management Module Enhancement
 */
@Data
public class ProductLifecycleTransitionDto {

    @ApiModelProperty(value = "商品ID", required = true)
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @ApiModelProperty(value = "目标状态", required = true)
    @NotNull(message = "目标状态不能为空")
    private String targetStatus;

    @ApiModelProperty(value = "操作备注")
    @Size(max = 500, message = "操作备注不能超过500个字符")
    private String notes;

    @ApiModelProperty(value = "审核员ID（审核状态时必填）")
    private Long reviewerId;

    @ApiModelProperty(value = "计划发布时间")
    private Date scheduledPublishDate;

    @ApiModelProperty(value = "下次审核时间")
    private Date nextReviewDate;

    @ApiModelProperty(value = "是否强制转换（跳过验证）")
    private Boolean forceTransition = false;

    // Business methods
    public ProductLifecycleStatus getTargetStatusEnum() {
        return ProductLifecycleStatus.fromCode(this.targetStatus);
    }

    public void setTargetStatusEnum(ProductLifecycleStatus status) {
        this.targetStatus = status.getCode();
    }
}