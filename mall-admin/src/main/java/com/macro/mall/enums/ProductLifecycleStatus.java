package com.macro.mall.enums;

/**
 * 商品生命周期状态枚举
 * Created for Product Management Module Enhancement
 */
public enum ProductLifecycleStatus {
    DRAFT("DRAFT", "草稿", "产品正在创建或编辑中"),
    SUBMITTED("SUBMITTED", "已提交", "等待审核员分配"),
    UNDER_REVIEW("UNDER_REVIEW", "审核中", "正在被管理员审核"),
    REJECTED("REJECTED", "审核拒绝", "审核失败，需要修订"),
    APPROVED("APPROVED", "审核通过", "已通过审核，准备发布"),
    PUBLISHED("PUBLISHED", "已发布", "在平台上线销售"),
    UNDER_MAINTENANCE("UNDER_MAINTENANCE", "维护中", "暂时不可用"),
    DISCONTINUED("DISCONTINUED", "已停产", "不再销售"),
    ARCHIVED("ARCHIVED", "已归档", "仅作历史记录");

    private final String code;
    private final String name;
    private final String description;

    ProductLifecycleStatus(String code, String name, String description) {
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
     * 根据代码获取状态
     */
    public static ProductLifecycleStatus fromCode(String code) {
        for (ProductLifecycleStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的生命周期状态代码: " + code);
    }

    /**
     * 检查是否可以转换到目标状态
     */
    public boolean canTransitionTo(ProductLifecycleStatus target) {
        switch (this) {
            case DRAFT:
                return target == SUBMITTED;
            case SUBMITTED:
                return target == UNDER_REVIEW;
            case UNDER_REVIEW:
                return target == APPROVED || target == REJECTED;
            case REJECTED:
                return target == DRAFT;
            case APPROVED:
                return target == PUBLISHED;
            case PUBLISHED:
                return target == UNDER_MAINTENANCE || target == DISCONTINUED;
            case UNDER_MAINTENANCE:
                return target == PUBLISHED;
            case DISCONTINUED:
                return target == ARCHIVED;
            case ARCHIVED:
                return false; // 归档状态无法转换
            default:
                return false;
        }
    }

    /**
     * 获取下一个可能的状态列表
     */
    public ProductLifecycleStatus[] getNextPossibleStates() {
        switch (this) {
            case DRAFT:
                return new ProductLifecycleStatus[]{SUBMITTED};
            case SUBMITTED:
                return new ProductLifecycleStatus[]{UNDER_REVIEW};
            case UNDER_REVIEW:
                return new ProductLifecycleStatus[]{APPROVED, REJECTED};
            case REJECTED:
                return new ProductLifecycleStatus[]{DRAFT};
            case APPROVED:
                return new ProductLifecycleStatus[]{PUBLISHED};
            case PUBLISHED:
                return new ProductLifecycleStatus[]{UNDER_MAINTENANCE, DISCONTINUED};
            case UNDER_MAINTENANCE:
                return new ProductLifecycleStatus[]{PUBLISHED};
            case DISCONTINUED:
                return new ProductLifecycleStatus[]{ARCHIVED};
            case ARCHIVED:
            default:
                return new ProductLifecycleStatus[0];
        }
    }

    /**
     * 检查状态是否为终端状态（无法再转换）
     */
    public boolean isTerminalState() {
        return this == ARCHIVED;
    }

    /**
     * 检查状态是否允许编辑产品信息
     */
    public boolean allowsEditing() {
        return this == DRAFT || this == REJECTED;
    }

    /**
     * 检查状态是否对用户可见
     */
    public boolean isVisibleToUsers() {
        return this == PUBLISHED;
    }
}