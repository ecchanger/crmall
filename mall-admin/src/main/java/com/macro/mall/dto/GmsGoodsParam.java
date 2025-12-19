package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品请求参数
 */
@Data
@EqualsAndHashCode
public class GmsGoodsParam {
    @NotEmpty
    @ApiModelProperty(value = "商品名称", required = true)
    private String name;

    @ApiModelProperty(value = "商品编码")
    private String goodsSn;

    @ApiModelProperty(value = "商品分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "商品分类名称")
    private String categoryName;

    @ApiModelProperty(value = "品牌ID")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "商品相册，多个图片用逗号分隔")
    private String albumPics;

    @NotNull
    @Min(value = 0)
    @ApiModelProperty(value = "商品价格", required = true)
    private BigDecimal price;

    @ApiModelProperty(value = "市场价")
    private BigDecimal marketPrice;

    @Min(value = 0)
    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "库存预警值")
    private Integer lowStock;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "商品重量，单位克")
    private BigDecimal weight;

    @Min(value = 0)
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    private Integer publishStatus;

    @ApiModelProperty(value = "新品状态：0->不是新品；1->新品")
    private Integer newStatus;

    @ApiModelProperty(value = "推荐状态：0->不推荐；1->推荐")
    private Integer recommendStatus;

    @ApiModelProperty(value = "商品简介")
    private String subTitle;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "商品详情")
    private String detailHtml;

    @ApiModelProperty(value = "移动端商品详情")
    private String detailMobileHtml;

    @ApiModelProperty(value = "商品关键词")
    private String keywords;

    @ApiModelProperty(value = "备注")
    private String note;
}
