package com.macro.mall.demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product {
    @ApiModelProperty(value = "商品ID")
    private Long id;

    @NotEmpty(message = "商品名称不能为空")
    @ApiModelProperty(value = "商品名称", required = true)
    private String name;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @NotNull(message = "商品价格不能为空")
    @ApiModelProperty(value = "商品价格", required = true)
    private BigDecimal price;

    @NotNull(message = "商品库存不能为空")
    @ApiModelProperty(value = "商品库存", required = true)
    private Integer stock;

    @ApiModelProperty(value = "商品分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "商品图片")
    private String pic;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    private Integer publishStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
