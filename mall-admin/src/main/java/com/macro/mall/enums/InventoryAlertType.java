package com.macro.mall.enums;

/**
 * 库存警报类型枚举
 * Created for Product Management Module Enhancement
 */
public enum InventoryAlertType {
    LOW_STOCK("LOW_STOCK", "库存不足", "库存数量低于补货点"),
    OUT_OF_STOCK("OUT_OF_STOCK", "缺货", "库存数量为零"),
    OVERSTOCK("OVERSTOCK", "库存过多", "库存数量超过最大库存水平"),
    EXPIRED_PRODUCTS("EXPIRED_PRODUCTS", "商品过期", "商品接近或已过期"),
    SLOW_MOVING("SLOW_MOVING", "滞销商品", "商品销售缓慢"),
    FAST_MOVING("FAST_MOVING", "快销商品", "商品销售快速，需要补货"),
    REORDER_POINT("REORDER_POINT", "补货提醒", "达到自动补货点"),
    QUALITY_ISSUE("QUALITY_ISSUE", "质量问题", "商品质量问题需要处理");

    private final String code;
    private final String name;
    private final String description;

    InventoryAlertType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取类型
     */
    public static InventoryAlertType fromCode(String code) {
        for (InventoryAlertType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的库存警报类型代码: " + code);
    }

    /**
     * 获取警报优先级
     */
    public int getPriority() {
        switch (this) {
            case OUT_OF_STOCK:
                return 1; // 最高优先级
            case LOW_STOCK:
                return 2;
            case EXPIRED_PRODUCTS:
                return 3;
            case QUALITY_ISSUE:
                return 4;
            case REORDER_POINT:
                return 5;
            case OVERSTOCK:
                return 6;
            case FAST_MOVING:
                return 7;
            case SLOW_MOVING:
                return 8; // 最低优先级
            default:
                return 9;
        }
    }

    /**
     * 检查是否需要立即处理
     */
    public boolean requiresImmediateAction() {
        return this == OUT_OF_STOCK || this == EXPIRED_PRODUCTS || this == QUALITY_ISSUE;
    }
}