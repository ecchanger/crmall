package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品查询参数
 */
@Data
@EqualsAndHashCode
public class GmsGoodsQueryParam {
    @ApiModelProperty("上架状态")
    private Integer publishStatus;
    @ApiModelProperty("审核状态")
    private Integer verifyStatus;
    @ApiModelProperty("商品名称模糊关键字")
    private String keyword;
    @ApiModelProperty("商品编码")
    private String goodsSn;
    @ApiModelProperty("商品分类ID")
    private Long categoryId;
    @ApiModelProperty("品牌ID")
    private Long brandId;
}
