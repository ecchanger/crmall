package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 商品库存修改参数
 */
@Data
public class PmsProductInventoryParam {
    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty(value = "商品ID", required = true)
    private Long productId;

    @ApiModelProperty(value = "SKU ID")
    private Long skuId;

    @NotNull(message = "库存变动数量不能为空")
    @ApiModelProperty(value = "库存变动数量", required = true)
    private Integer changeStock;

    @NotNull(message = "操作类型不能为空")
    @ApiModelProperty(value = "操作类型：1->入库；2->出库；3->调整", required = true)
    private Integer operateType;

    @ApiModelProperty(value = "操作人")
    private String operateMan;

    @ApiModelProperty(value = "操作备注")
    private String note;
}