package com.macro.mall.dto;

import com.macro.mall.enums.BatchOperationType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 批量操作请求DTO
 * Created for Product Management Module Enhancement
 */
@Data
public class BatchOperationDto {

    @ApiModelProperty(value = "操作类型", required = true)
    @NotNull(message = "操作类型不能为空")
    private String operationType;

    @ApiModelProperty(value = "商品ID列表", required = true)
    @NotEmpty(message = "商品ID列表不能为空")
    @Size(max = 1000, message = "批量操作商品数量不能超过1000")
    private List<Long> productIds;

    @ApiModelProperty(value = "操作参数")
    private Map<String, Object> operationParams;

    @ApiModelProperty(value = "操作备注")
    @Size(max = 500, message = "操作备注不能超过500个字符")
    private String notes;

    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "是否异步执行")
    private Boolean async = false;

    @ApiModelProperty(value = "是否在失败时回滚")
    private Boolean rollbackOnFailure = true;

    @ApiModelProperty(value = "是否跳过验证")
    private Boolean skipValidation = false;

    @ApiModelProperty(value = "是否强制执行（跳过业务规则检查）")
    private Boolean forceExecute = false;

    @ApiModelProperty(value = "预期执行时间")
    private Date scheduledTime;

    @ApiModelProperty(value = "通知收件人")
    private List<String> notificationRecipients;

    @ApiModelProperty(value = "操作原因")
    private String reason;

    // =============== 特定操作的参数 ===============

    // 价格更新参数
    @ApiModelProperty(value = "新价格")
    private BigDecimal newPrice;

    @ApiModelProperty(value = "新促销价格")
    private BigDecimal newPromotionPrice;

    @ApiModelProperty(value = "价格调整百分比")
    private BigDecimal priceAdjustmentPercentage;

    @ApiModelProperty(value = "价格调整类型：INCREASE->增加；DECREASE->减少")
    private String priceAdjustmentType;

    // 状态更新参数
    @ApiModelProperty(value = "发布状态")
    private Integer publishStatus;

    @ApiModelProperty(value = "审核状态")
    private Integer verifyStatus;

    @ApiModelProperty(value = "新品状态")
    private Integer newStatus;

    @ApiModelProperty(value = "推荐状态")
    private Integer recommendStatus;

    @ApiModelProperty(value = "生命周期状态")
    private String lifecycleStatus;

    // 分类迁移参数
    @ApiModelProperty(value = "目标分类ID")
    private Long targetCategoryId;

    @ApiModelProperty(value = "是否迁移属性")
    private Boolean migrateAttributes = true;

    @ApiModelProperty(value = "属性映射规则")
    private Map<Long, Long> attributeMapping;

    // 库存调整参数
    @ApiModelProperty(value = "库存调整数量")
    private Integer stockAdjustmentQuantity;

    @ApiModelProperty(value = "库存调整类型：ADD->增加；SUBTRACT->减少；SET->设置")
    private String stockAdjustmentType;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "调整原因")
    private String adjustmentReason;

    // 属性更新参数
    @ApiModelProperty(value = "属性更新映射")
    private Map<String, Object> attributeUpdates;

    // SEO更新参数
    @ApiModelProperty(value = "SEO标题")
    private String seoTitle;

    @ApiModelProperty(value = "SEO描述")
    private String seoDescription;

    @ApiModelProperty(value = "关键词")
    private String keywords;

    // 标签更新参数
    @ApiModelProperty(value = "标签操作类型：ADD->添加；REMOVE->移除；REPLACE->替换")
    private String tagOperationType;

    @ApiModelProperty(value = "标签列表")
    private List<String> tags;

    // 折扣更新参数
    @ApiModelProperty(value = "折扣百分比")
    private BigDecimal discountPercentage;

    @ApiModelProperty(value = "折扣开始时间")
    private Date discountStartTime;

    @ApiModelProperty(value = "折扣结束时间")
    private Date discountEndTime;

    @ApiModelProperty(value = "折扣类型")
    private String discountType;

    // Business methods
    public BatchOperationType getOperationTypeEnum() {
        return this.operationType != null ? BatchOperationType.fromCode(this.operationType) : null;
    }

    public void setOperationTypeEnum(BatchOperationType type) {
        this.operationType = type.getCode();
    }

    public boolean isHighRiskOperation() {
        BatchOperationType type = getOperationTypeEnum();
        return type != null && type.isHighRiskOperation();
    }

    public boolean requiresTransaction() {
        BatchOperationType type = getOperationTypeEnum();
        return type != null && type.requiresTransaction();
    }

    public int getMaxAllowedBatchSize() {
        BatchOperationType type = getOperationTypeEnum();
        return type != null ? type.getMaxBatchSize() : 1000;
    }

    public int getTimeoutSeconds() {
        BatchOperationType type = getOperationTypeEnum();
        return type != null ? type.getTimeoutSeconds() : 60;
    }

    public boolean isValidBatchSize() {
        return productIds != null && productIds.size() <= getMaxAllowedBatchSize();
    }

    public boolean needsApproval() {
        return isHighRiskOperation() || 
               (productIds != null && productIds.size() > 100) ||
               forceExecute;
    }

    public boolean shouldExecuteAsync() {
        return async || 
               (productIds != null && productIds.size() > 50) ||
               getTimeoutSeconds() > 60;
    }

    // 参数验证方法
    public boolean hasValidPriceParams() {
        if (getOperationTypeEnum() != BatchOperationType.PRICE_UPDATE) return true;
        return newPrice != null || newPromotionPrice != null || priceAdjustmentPercentage != null;
    }

    public boolean hasValidStatusParams() {
        if (getOperationTypeEnum() != BatchOperationType.STATUS_UPDATE) return true;
        return publishStatus != null || verifyStatus != null || 
               newStatus != null || recommendStatus != null || lifecycleStatus != null;
    }

    public boolean hasValidCategoryParams() {
        if (getOperationTypeEnum() != BatchOperationType.CATEGORY_MIGRATION) return true;
        return targetCategoryId != null;
    }

    public boolean hasValidInventoryParams() {
        if (getOperationTypeEnum() != BatchOperationType.INVENTORY_ADJUSTMENT) return true;
        return stockAdjustmentQuantity != null && stockAdjustmentType != null;
    }

    public boolean hasValidDiscountParams() {
        if (getOperationTypeEnum() != BatchOperationType.DISCOUNT_UPDATE) return true;
        return discountPercentage != null && discountStartTime != null && discountEndTime != null;
    }

    public boolean isValidOperation() {
        return hasValidPriceParams() && hasValidStatusParams() && 
               hasValidCategoryParams() && hasValidInventoryParams() && hasValidDiscountParams();
    }

    // 获取操作摘要
    public String getOperationSummary() {
        BatchOperationType type = getOperationTypeEnum();
        if (type == null) return "未知操作";

        StringBuilder summary = new StringBuilder();
        summary.append(type.getName());
        summary.append("，影响商品数量：").append(productIds != null ? productIds.size() : 0);

        switch (type) {
            case PRICE_UPDATE:
                if (newPrice != null) {
                    summary.append("，新价格：").append(newPrice);
                } else if (priceAdjustmentPercentage != null) {
                    summary.append("，价格调整：").append(priceAdjustmentPercentage).append("%");
                }
                break;
            case CATEGORY_MIGRATION:
                summary.append("，目标分类ID：").append(targetCategoryId);
                break;
            case INVENTORY_ADJUSTMENT:
                summary.append("，库存调整：").append(stockAdjustmentType)
                       .append(" ").append(stockAdjustmentQuantity);
                break;
            case DISCOUNT_UPDATE:
                summary.append("，折扣：").append(discountPercentage).append("%");
                break;
        }

        return summary.toString();
    }

    // 获取预估执行时间（秒）
    public int getEstimatedExecutionTime() {
        int baseTime = getTimeoutSeconds();
        int itemCount = productIds != null ? productIds.size() : 0;
        
        // 根据操作类型和数量估算时间
        double timePerItem = 0.1; // 默认每个商品0.1秒
        
        BatchOperationType type = getOperationTypeEnum();
        if (type != null) {
            switch (type) {
                case CATEGORY_MIGRATION:
                    timePerItem = 0.5; // 分类迁移较复杂
                    break;
                case INVENTORY_ADJUSTMENT:
                    timePerItem = 0.3; // 库存调整需要验证
                    break;
                case DELETE:
                    timePerItem = 0.2; // 删除需要级联处理
                    break;
                default:
                    timePerItem = 0.1;
            }
        }

        return Math.max(baseTime, (int) (itemCount * timePerItem));
    }

    // 验证操作参数完整性
    public String validateOperationParams() {
        if (!isValidBatchSize()) {
            return "批量操作商品数量超出限制，最大允许：" + getMaxAllowedBatchSize();
        }

        if (!isValidOperation()) {
            return "操作参数不完整或无效";
        }

        BatchOperationType type = getOperationTypeEnum();
        if (type == null) {
            return "未知的操作类型";
        }

        // 特定验证
        if (type == BatchOperationType.PRICE_UPDATE && newPrice != null && newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return "价格必须大于0";
        }

        if (type == BatchOperationType.INVENTORY_ADJUSTMENT && stockAdjustmentQuantity != null && stockAdjustmentQuantity == 0) {
            return "库存调整数量不能为0";
        }

        if (type == BatchOperationType.DISCOUNT_UPDATE) {
            if (discountPercentage != null && (discountPercentage.compareTo(BigDecimal.ZERO) <= 0 || discountPercentage.compareTo(new BigDecimal("100")) > 0)) {
                return "折扣百分比必须在0-100之间";
            }
            if (discountStartTime != null && discountEndTime != null && !discountEndTime.after(discountStartTime)) {
                return "折扣结束时间必须晚于开始时间";
            }
        }

        return null; // 验证通过
    }
}