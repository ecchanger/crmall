package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品高级查询参数
 * Created by macro on 2024/1/1.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PmsProductAdvancedQueryParam extends PmsProductQueryParam {
    
    @ApiModelProperty("最低价格")
    private BigDecimal minPrice;
    
    @ApiModelProperty("最高价格")
    private BigDecimal maxPrice;
    
    @ApiModelProperty("最低库存")
    private Integer minStock;
    
    @ApiModelProperty("最高库存")
    private Integer maxStock;
    
    @ApiModelProperty("推荐状态")
    private Integer recommendStatus;
    
    @ApiModelProperty("新品状态")
    private Integer newStatus;
    
    @ApiModelProperty("删除状态")
    private Integer deleteStatus;
    
    @ApiModelProperty("创建开始时间")
    private Date createTimeStart;
    
    @ApiModelProperty("创建结束时间")
    private Date createTimeEnd;
    
    @ApiModelProperty("排序字段")
    private String sortField;
    
    @ApiModelProperty("排序方向：asc/desc")
    private String sortOrder;
    
    @ApiModelProperty("商品标签")
    private String tags;
    
    @ApiModelProperty("商品重量范围-最小值")
    private BigDecimal minWeight;
    
    @ApiModelProperty("商品重量范围-最大值")
    private BigDecimal maxWeight;
    
    @ApiModelProperty("是否包含SKU信息")
    private Boolean includeSku;
    
    @ApiModelProperty("是否包含属性信息")
    private Boolean includeAttributes;
    
    @ApiModelProperty("销量范围-最小值")
    private Integer minSale;
    
    @ApiModelProperty("销量范围-最大值")
    private Integer maxSale;
}
