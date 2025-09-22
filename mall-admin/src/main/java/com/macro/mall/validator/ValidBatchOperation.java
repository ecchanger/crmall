package com.macro.mall.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 批量操作验证注解
 * Created for Product Management Module Enhancement
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BatchOperationValidator.class)
@Documented
public @interface ValidBatchOperation {
    
    String message() default "无效的批量操作";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 是否需要管理员权限
     */
    boolean requireAdminRole() default false;
    
    /**
     * 最大批量操作数量
     */
    int maxBatchSize() default 1000;
}