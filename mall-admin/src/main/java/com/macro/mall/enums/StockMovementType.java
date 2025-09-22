package com.macro.mall.enums;

/**
 * 库存移动类型枚举
 * Created for Product Management Module Enhancement
 */
public enum StockMovementType {
    INITIAL("INITIAL", "初始库存", "初始库存设置"),
    PURCHASE("PURCHASE", "采购入库", "采购商品入库"),
    SALE("SALE", "销售出库", "销售商品出库"),
    RETURN("RETURN", "退货入库", "客户退货入库"),
    DAMAGE("DAMAGE", "损坏出库", "商品损坏出库"),
    TRANSFER_IN("TRANSFER_IN", "调拨入库", "从其他仓库调入"),
    TRANSFER_OUT("TRANSFER_OUT", "调拨出库", "调出到其他仓库"),
    ADJUSTMENT("ADJUSTMENT", "库存调整", "人工库存调整"),
    RESERVE("RESERVE", "库存预留", "订单预留库存"),
    RELEASE("RELEASE", "预留释放", "释放预留库存"),
    EXPIRE("EXPIRE", "过期处理", "过期商品处理");

    private final String code;
    private final String name;
    private final String description;

    StockMovementType(String code, String name, String description) {
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
    public static StockMovementType fromCode(String code) {
        for (StockMovementType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的库存移动类型代码: " + code);
    }

    /**
     * 检查是否为入库类型
     */
    public boolean isInbound() {
        return this == INITIAL || this == PURCHASE || this == RETURN || 
               this == TRANSFER_IN || this == RELEASE ||
               (this == ADJUSTMENT && false); // 调整类型需要根据数量判断
    }

    /**
     * 检查是否为出库类型
     */
    public boolean isOutbound() {
        return this == SALE || this == DAMAGE || this == TRANSFER_OUT || 
               this == RESERVE || this == EXPIRE ||
               (this == ADJUSTMENT && false); // 调整类型需要根据数量判断
    }
}