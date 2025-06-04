package com.macro.mall.dto;

import com.macro.mall.model.ProductManagement;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class ProductManagementResult extends ProductManagement {
    @Getter
    @Setter
    @ApiModelProperty("关联商品名称")
    private String productName;

    @Getter
    @Setter
    @ApiModelProperty("关联商品货号")
    private String productSn;

    @Getter
    @Setter
    @ApiModelProperty("关联商品品牌名称")
    private String brandName;

    @Getter
    @Setter
    @ApiModelProperty("关联商品分类名称")
    private String categoryName;
}
