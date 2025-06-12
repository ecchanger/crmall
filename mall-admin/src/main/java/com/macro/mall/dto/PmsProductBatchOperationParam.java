package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品批量操作参数
 * Created by macro on 2024/1/1.
 */
@Data
public class PmsProductBatchOperationParam {
    
    @NotEmpty(message = "商品ID列表不能为空")
    @ApiModelProperty(value = "商品ID列表", required = true)
    private List<Long> productIds;
    
    @NotNull(message = "操作类型不能为空")
    @ApiModelProperty(value = "操作类型：1-上架，2-下架，3-删除，4-审核通过，5-审核拒绝，6-设为推荐，7-取消推荐，8-设为新品，9-取消新品，10-价格调整", required = true)
    private Integer operationType;
    
    @ApiModelProperty("操作备注")
    private String remark;
    
    @ApiModelProperty("审核详情（审核操作时使用）")
    private String verifyDetail;
    
    @ApiModelProperty("价格调整值（价格调整操作时使用）")
    private BigDecimal priceAdjustment;
    
    @ApiModelProperty("价格调整类型：0-销售价格，1-市场价格（价格调整操作时使用）")
    private Integer priceType;
    
    @ApiModelProperty("价格调整方式：0-设置为固定值，1-增加，2-减少，3-按比例调整（价格调整操作时使用）")
    private Integer adjustmentType;
    
    @ApiModelProperty("调整比例（按比例调整时使用，如0.1表示增加10%）")
    private BigDecimal adjustmentRatio;
}
