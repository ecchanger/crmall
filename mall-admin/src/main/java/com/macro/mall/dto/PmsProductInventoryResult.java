package com.macro.mall.dto;

import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductInventoryLog;
import com.macro.mall.model.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品库存查询结果
 */
@Data
public class PmsProductInventoryResult {
    @ApiModelProperty("商品信息")
    private PmsProduct product;

    @ApiModelProperty("SKU库存信息")
    private List<PmsSkuStock> skuStockList;

    @ApiModelProperty("库存变动记录")
    private List<PmsProductInventoryLog> inventoryLogList;
}