package com.macro.mall.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * 商品质量评分验证器
 * Created for Product Management Module Enhancement
 */
public class QualityScoreValidator implements ConstraintValidator<ValidQualityScore, BigDecimal> {

    private double min;
    private double max;
    private boolean nullable;

    @Override
    public void initialize(ValidQualityScore annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
        this.nullable = annotation.nullable();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        // 如果允许null值且值为null，则验证通过
        if (value == null) {
            return nullable;
        }

        // 检查数值范围
        double doubleValue = value.doubleValue();
        if (doubleValue < min || doubleValue > max) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("质量评分必须在%.2f-%.2f之间", min, max))
                   .addConstraintViolation();
            return false;
        }

        // 检查小数位数（最多两位）
        int scale = value.scale();
        if (scale > 2) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("质量评分最多保留两位小数")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}