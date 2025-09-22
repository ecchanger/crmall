package com.macro.mall.validator;

import com.macro.mall.dto.ProductLifecycleTransitionDto;
import com.macro.mall.enums.ProductLifecycleStatus;
import com.macro.mall.model.ProductLifecycle;
import com.macro.mall.service.ProductLifecycleService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 商品生命周期状态转换验证器
 * Created for Product Management Module Enhancement
 */
public class ProductLifecycleTransitionValidator implements ConstraintValidator<ProductLifecycleTransition, ProductLifecycleTransitionDto> {

    @Autowired
    private ProductLifecycleService productLifecycleService;

    private boolean allowForceTransition;
    private String[] requiredRoles;

    @Override
    public void initialize(ProductLifecycleTransition annotation) {
        this.allowForceTransition = annotation.allowForceTransition();
        this.requiredRoles = annotation.requiredRoles();
    }

    @Override
    public boolean isValid(ProductLifecycleTransitionDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true; // null值由@NotNull注解处理
        }

        // 如果允许强制转换且设置了强制标识，跳过验证
        if (allowForceTransition && Boolean.TRUE.equals(dto.getForceTransition())) {
            return true;
        }

        try {
            // 获取目标状态
            ProductLifecycleStatus targetStatus = dto.getTargetStatusEnum();
            if (targetStatus == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("无效的目标状态代码")
                       .addConstraintViolation();
                return false;
            }

            // 获取当前产品的生命周期状态
            ProductLifecycle currentLifecycle = productLifecycleService.getCurrentLifecycle(dto.getProductId());
            if (currentLifecycle == null) {
                // 如果是新产品，只能设置为DRAFT状态
                if (targetStatus != ProductLifecycleStatus.DRAFT) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("新产品只能设置为草稿状态")
                           .addConstraintViolation();
                    return false;
                }
                return true;
            }

            ProductLifecycleStatus currentStatus = currentLifecycle.getCurrentStatus();

            // 检查状态转换是否合法
            if (!currentStatus.canTransitionTo(targetStatus)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    String.format("不能从状态 '%s' 转换到 '%s'", 
                                currentStatus.getName(), targetStatus.getName()))
                       .addConstraintViolation();
                return false;
            }

            // 特定状态转换的业务规则验证
            if (!validateSpecificTransition(dto, currentStatus, targetStatus, context)) {
                return false;
            }

            return true;

        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("状态转换验证失败: " + e.getMessage())
                   .addConstraintViolation();
            return false;
        }
    }

    /**
     * 验证特定状态转换的业务规则
     */
    private boolean validateSpecificTransition(ProductLifecycleTransitionDto dto, 
                                             ProductLifecycleStatus currentStatus, 
                                             ProductLifecycleStatus targetStatus, 
                                             ConstraintValidatorContext context) {
        
        // 转换到审核状态需要审核员
        if (targetStatus == ProductLifecycleStatus.UNDER_REVIEW) {
            if (dto.getReviewerId() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("转换到审核状态需要指定审核员")
                       .addConstraintViolation();
                return false;
            }
        }

        // 审核通过或拒绝需要审核备注
        if ((targetStatus == ProductLifecycleStatus.APPROVED || targetStatus == ProductLifecycleStatus.REJECTED) 
            && currentStatus == ProductLifecycleStatus.UNDER_REVIEW) {
            if (dto.getNotes() == null || dto.getNotes().trim().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("审核操作需要填写审核备注")
                       .addConstraintViolation();
                return false;
            }
        }

        // 发布状态的额外验证
        if (targetStatus == ProductLifecycleStatus.PUBLISHED) {
            // 这里可以添加发布前的必要字段检查
            // 例如：商品必须有价格、库存、描述等
        }

        // 归档状态的验证
        if (targetStatus == ProductLifecycleStatus.ARCHIVED) {
            if (currentStatus != ProductLifecycleStatus.DISCONTINUED) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("只有已停产的商品才能归档")
                       .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}