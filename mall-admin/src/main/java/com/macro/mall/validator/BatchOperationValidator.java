package com.macro.mall.validator;

import com.macro.mall.dto.BatchOperationDto;
import com.macro.mall.enums.BatchOperationType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 批量操作验证器
 * Created for Product Management Module Enhancement
 */
public class BatchOperationValidator implements ConstraintValidator<ValidBatchOperation, BatchOperationDto> {

    private boolean requireAdminRole;
    private int maxBatchSize;

    @Override
    public void initialize(ValidBatchOperation annotation) {
        this.requireAdminRole = annotation.requireAdminRole();
        this.maxBatchSize = annotation.maxBatchSize();
    }

    @Override
    public boolean isValid(BatchOperationDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true; // null值由其他注解处理
        }

        // 验证操作类型
        BatchOperationType operationType = dto.getOperationTypeEnum();
        if (operationType == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("无效的操作类型")
                   .addConstraintViolation();
            return false;
        }

        // 验证批量大小
        if (dto.getProductIds() == null || dto.getProductIds().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("商品ID列表不能为空")
                   .addConstraintViolation();
            return false;
        }

        int actualBatchSize = dto.getProductIds().size();
        int allowedBatchSize = Math.min(maxBatchSize, operationType.getMaxBatchSize());
        
        if (actualBatchSize > allowedBatchSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("批量操作数量超出限制，当前：%d，最大允许：%d", actualBatchSize, allowedBatchSize))
                   .addConstraintViolation();
            return false;
        }

        // 验证高风险操作
        if (operationType.isHighRiskOperation() && !Boolean.TRUE.equals(dto.getForceExecute())) {
            if (dto.getNotes() == null || dto.getNotes().trim().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("高风险操作必须填写操作备注")
                       .addConstraintViolation();
                return false;
            }
        }

        // 验证操作参数完整性
        String paramValidationError = dto.validateOperationParams();
        if (paramValidationError != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(paramValidationError)
                   .addConstraintViolation();
            return false;
        }

        // 验证特定操作类型的业务规则
        if (!validateSpecificOperationType(dto, operationType, context)) {
            return false;
        }

        return true;
    }

    /**
     * 验证特定操作类型的业务规则
     */
    private boolean validateSpecificOperationType(BatchOperationDto dto, BatchOperationType operationType, 
                                                ConstraintValidatorContext context) {
        
        switch (operationType) {
            case DELETE:
                // 删除操作需要确认
                if (!Boolean.TRUE.equals(dto.getForceExecute())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("删除操作需要设置强制执行标识")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case PRICE_UPDATE:
                if (dto.getNewPrice() == null && dto.getNewPromotionPrice() == null && 
                    dto.getPriceAdjustmentPercentage() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("价格更新操作必须指定新价格或调整百分比")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case CATEGORY_MIGRATION:
                if (dto.getTargetCategoryId() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("分类迁移操作必须指定目标分类")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case INVENTORY_ADJUSTMENT:
                if (dto.getStockAdjustmentQuantity() == null || dto.getStockAdjustmentType() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("库存调整操作必须指定调整数量和类型")
                           .addConstraintViolation();
                    return false;
                }
                if (dto.getAdjustmentReason() == null || dto.getAdjustmentReason().trim().isEmpty()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("库存调整操作必须填写调整原因")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case STATUS_UPDATE:
                if (dto.getPublishStatus() == null && dto.getVerifyStatus() == null && 
                    dto.getNewStatus() == null && dto.getRecommendStatus() == null && 
                    dto.getLifecycleStatus() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("状态更新操作必须指定至少一个状态字段")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case DISCOUNT_UPDATE:
                if (dto.getDiscountPercentage() == null || dto.getDiscountStartTime() == null || 
                    dto.getDiscountEndTime() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("折扣更新操作必须指定折扣百分比和有效期")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case TAG_UPDATE:
                if (dto.getTags() == null || dto.getTags().isEmpty()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("标签更新操作必须指定标签列表")
                           .addConstraintViolation();
                    return false;
                }
                if (dto.getTagOperationType() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("标签更新操作必须指定操作类型")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case SEO_UPDATE:
                if ((dto.getSeoTitle() == null || dto.getSeoTitle().trim().isEmpty()) && 
                    (dto.getSeoDescription() == null || dto.getSeoDescription().trim().isEmpty()) &&
                    (dto.getKeywords() == null || dto.getKeywords().trim().isEmpty())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("SEO更新操作必须指定至少一个SEO字段")
                           .addConstraintViolation();
                    return false;
                }
                break;
        }

        return true;
    }
}