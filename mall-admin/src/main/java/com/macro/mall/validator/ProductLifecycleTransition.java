package com.macro.mall.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 商品生命周期状态转换验证注解
 * Created for Product Management Module Enhancement
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductLifecycleTransitionValidator.class)
@Documented
public @interface ProductLifecycleTransition {
    
    String message() default "无效的生命周期状态转换";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 是否允许强制转换（跳过业务规则验证）
     */
    boolean allowForceTransition() default false;
    
    /**
     * 需要特定角色才能执行的转换
     */
    String[] requiredRoles() default {};
}