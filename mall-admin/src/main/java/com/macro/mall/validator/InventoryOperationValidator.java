package com.macro.mall.validator;

import com.macro.mall.model.StockMovement;
import com.macro.mall.enums.StockMovementType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 库存操作验证器
 * Created for Product Management Module Enhancement
 */
public class InventoryOperationValidator implements ConstraintValidator<ValidInventoryOperation, StockMovement> {

    private boolean allowNegativeStock;
    private int maxOperationQuantity;

    @Override
    public void initialize(ValidInventoryOperation annotation) {
        this.allowNegativeStock = annotation.allowNegativeStock();
        this.maxOperationQuantity = annotation.maxOperationQuantity();
    }

    @Override
    public boolean isValid(StockMovement stockMovement, ConstraintValidatorContext context) {
        if (stockMovement == null) {
            return true; // null值由其他注解处理
        }

        // 验证基本字段
        if (!stockMovement.isValid()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("库存移动记录缺少必要字段")
                   .addConstraintViolation();
            return false;
        }

        // 验证数量范围
        Integer quantity = stockMovement.getQuantity();
        if (quantity != null && Math.abs(quantity) > maxOperationQuantity) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("单次库存操作数量不能超过%d", maxOperationQuantity))
                   .addConstraintViolation();
            return false;
        }

        // 验证库存不能为负（除非明确允许）
        if (!allowNegativeStock && stockMovement.getQuantityAfter() != null && stockMovement.getQuantityAfter() < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("库存数量不能为负数")
                   .addConstraintViolation();
            return false;
        }

        // 验证移动类型和数量的一致性
        StockMovementType movementType = stockMovement.getMovementTypeEnum();
        if (movementType != null && quantity != null) {
            if (movementType.isInbound() && quantity < 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("入库操作的数量必须为正数")
                       .addConstraintViolation();
                return false;
            }
            
            if (movementType.isOutbound() && quantity > 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("出库操作的数量必须为负数")
                       .addConstraintViolation();
                return false;
            }
        }

        // 验证特定移动类型的必要字段
        if (!validateSpecificMovementType(stockMovement, context)) {
            return false;
        }

        return true;
    }

    /**
     * 验证特定移动类型的必要字段
     */
    private boolean validateSpecificMovementType(StockMovement stockMovement, ConstraintValidatorContext context) {
        StockMovementType movementType = stockMovement.getMovementTypeEnum();
        if (movementType == null) {
            return true;
        }

        switch (movementType) {
            case PURCHASE:
                if (stockMovement.getSupplierId() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("采购入库必须指定供应商")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case SALE:
                if (stockMovement.getCustomerId() == null && stockMovement.getReferenceNumber() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("销售出库必须指定客户或订单号")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case TRANSFER_IN:
            case TRANSFER_OUT:
                if (stockMovement.getSourceWarehouseId() == null || stockMovement.getTargetWarehouseId() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("调拨操作必须指定源仓库和目标仓库")
                           .addConstraintViolation();
                    return false;
                }
                if (stockMovement.getSourceWarehouseId().equals(stockMovement.getTargetWarehouseId())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("调拨的源仓库和目标仓库不能相同")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case ADJUSTMENT:
                if (stockMovement.getReason() == null || stockMovement.getReason().trim().isEmpty()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("库存调整必须填写调整原因")
                           .addConstraintViolation();
                    return false;
                }
                break;

            case DAMAGE:
            case EXPIRE:
                if (stockMovement.getReason() == null || stockMovement.getReason().trim().isEmpty()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("损坏或过期处理必须填写处理原因")
                           .addConstraintViolation();
                    return false;
                }
                break;
        }

        return true;
    }
}