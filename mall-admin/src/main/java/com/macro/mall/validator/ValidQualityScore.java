package com.macro.mall.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 商品质量评分验证注解
 * Created for Product Management Module Enhancement
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = QualityScoreValidator.class)
@Documented
public @interface ValidQualityScore {
    
    String message() default "质量评分必须在0.00-5.00之间，且最多保留两位小数";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 最小值
     */
    double min() default 0.00;
    
    /**
     * 最大值
     */
    double max() default 5.00;
    
    /**
     * 是否允许null值
     */
    boolean nullable() default true;
}