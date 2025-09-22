package com.macro.mall.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 库存操作验证注解
 * Created for Product Management Module Enhancement
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InventoryOperationValidator.class)
@Documented
public @interface ValidInventoryOperation {
    
    String message() default "无效的库存操作";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 是否允许负库存
     */
    boolean allowNegativeStock() default false;
    
    /**
     * 最大单次操作数量
     */
    int maxOperationQuantity() default 10000;
}