package com.macro.mall.enums;

/**
 * 批量操作类型枚举
 * Created for Product Management Module Enhancement
 */
public enum BatchOperationType {
    PRICE_UPDATE("PRICE_UPDATE", "批量价格更新", "批量更新商品价格"),
    STATUS_UPDATE("STATUS_UPDATE", "批量状态更新", "批量更新商品状态"),
    CATEGORY_MIGRATION("CATEGORY_MIGRATION", "类目迁移", "批量迁移商品类目"),
    INVENTORY_ADJUSTMENT("INVENTORY_ADJUSTMENT", "库存调整", "批量调整库存数量"),
    ATTRIBUTE_UPDATE("ATTRIBUTE_UPDATE", "属性更新", "批量更新商品属性"),
    SEO_UPDATE("SEO_UPDATE", "SEO更新", "批量更新SEO信息"),
    TAG_UPDATE("TAG_UPDATE", "标签更新", "批量更新商品标签"),
    DISCOUNT_UPDATE("DISCOUNT_UPDATE", "折扣更新", "批量更新促销折扣"),
    DELETE("DELETE", "批量删除", "批量删除商品"),
    PUBLISH("PUBLISH", "批量发布", "批量发布商品"),
    UNPUBLISH("UNPUBLISH", "批量下架", "批量下架商品");

    private final String code;
    private final String name;
    private final String description;

    BatchOperationType(String code, String name, String description) {
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
    public static BatchOperationType fromCode(String code) {
        for (BatchOperationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的批量操作类型代码: " + code);
    }

    /**
     * 检查操作是否需要事务支持
     */
    public boolean requiresTransaction() {
        return this == PRICE_UPDATE || this == STATUS_UPDATE || 
               this == CATEGORY_MIGRATION || this == INVENTORY_ADJUSTMENT ||
               this == DELETE || this == PUBLISH || this == UNPUBLISH;
    }

    /**
     * 检查操作是否为高风险操作
     */
    public boolean isHighRiskOperation() {
        return this == DELETE || this == CATEGORY_MIGRATION || this == INVENTORY_ADJUSTMENT;
    }

    /**
     * 获取操作的最大批量大小
     */
    public int getMaxBatchSize() {
        switch (this) {
            case DELETE:
                return 50; // 删除操作限制较小批量
            case CATEGORY_MIGRATION:
                return 100;
            case INVENTORY_ADJUSTMENT:
                return 200;
            case PRICE_UPDATE:
            case STATUS_UPDATE:
                return 500;
            default:
                return 1000;
        }
    }

    /**
     * 获取操作的超时时间（秒）
     */
    public int getTimeoutSeconds() {
        switch (this) {
            case DELETE:
            case CATEGORY_MIGRATION:
                return 300; // 5分钟
            case INVENTORY_ADJUSTMENT:
                return 180; // 3分钟
            case PRICE_UPDATE:
            case STATUS_UPDATE:
                return 120; // 2分钟
            default:
                return 60; // 1分钟
        }
    }
}