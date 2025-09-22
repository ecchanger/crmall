package com.macro.mall.dto;

import com.macro.mall.enums.ProductLifecycleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 增强的商品搜索和过滤DTO
 * Created for Product Management Module Enhancement
 */
@Data
public class ProductSearchDto {

    // =============== 基础搜索字段 ===============
    
    @ApiModelProperty(value = "关键词搜索（商品名称、描述、SKU）")
    private String keyword;

    @ApiModelProperty(value = "商品ID列表")
    private List<Long> productIds;

    @ApiModelProperty(value = "商品编号")
    private String productSn;

    @ApiModelProperty(value = "商品名称（精确匹配）")
    private String name;

    @ApiModelProperty(value = "商品名称（模糊匹配）")
    private String nameLike;

    // =============== 分类和品牌过滤 ===============
    
    @ApiModelProperty(value = "品牌ID列表")
    private List<Long> brandIds;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "是否包含子分类")
    private Boolean includeSubCategories = true;

    @ApiModelProperty(value = "属性分类ID")
    private Long attributeCategoryId;

    // =============== 状态过滤 ===============
    
    @ApiModelProperty(value = "发布状态列表：0->下架；1->上架")
    private List<Integer> publishStatuses;

    @ApiModelProperty(value = "审核状态列表：0->未审核；1->审核通过")
    private List<Integer> verifyStatuses;

    @ApiModelProperty(value = "新品状态列表：0->不是新品；1->新品")
    private List<Integer> newStatuses;

    @ApiModelProperty(value = "推荐状态列表：0->不推荐；1->推荐")
    private List<Integer> recommendStatuses;

    @ApiModelProperty(value = "删除状态列表：0->未删除；1->已删除")
    private List<Integer> deleteStatuses;

    @ApiModelProperty(value = "生命周期状态列表")
    private List<String> lifecycleStatuses;

    // =============== 价格区间过滤 ===============
    
    @ApiModelProperty(value = "最低价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最高价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "最低促销价格")
    private BigDecimal minPromotionPrice;

    @ApiModelProperty(value = "最高促销价格")
    private BigDecimal maxPromotionPrice;

    @ApiModelProperty(value = "最低原价")
    private BigDecimal minOriginalPrice;

    @ApiModelProperty(value = "最高原价")
    private BigDecimal maxOriginalPrice;

    @ApiModelProperty(value = "是否有促销价")
    private Boolean hasPromotionPrice;

    // =============== 库存过滤 ===============
    
    @ApiModelProperty(value = "最低库存")
    private Integer minStock;

    @ApiModelProperty(value = "最高库存")
    private Integer maxStock;

    @ApiModelProperty(value = "是否有库存")
    private Boolean hasStock;

    @ApiModelProperty(value = "是否低库存")
    private Boolean isLowStock;

    @ApiModelProperty(value = "是否缺货")
    private Boolean isOutOfStock;

    @ApiModelProperty(value = "最低可用库存")
    private Integer minAvailableQuantity;

    @ApiModelProperty(value = "最高可用库存")
    private Integer maxAvailableQuantity;

    // =============== 销量和评分过滤 ===============
    
    @ApiModelProperty(value = "最低销量")
    private Integer minSale;

    @ApiModelProperty(value = "最高销量")
    private Integer maxSale;

    @ApiModelProperty(value = "最低质量评分")
    private BigDecimal minQualityScore;

    @ApiModelProperty(value = "最高质量评分")
    private BigDecimal maxQualityScore;

    @ApiModelProperty(value = "最低可持续性评分")
    private Integer minSustainabilityScore;

    @ApiModelProperty(value = "最高可持续性评分")
    private Integer maxSustainabilityScore;

    // =============== 时间范围过滤 ===============
    
    @ApiModelProperty(value = "创建时间开始")
    private Date createdAtStart;

    @ApiModelProperty(value = "创建时间结束")
    private Date createdAtEnd;

    @ApiModelProperty(value = "更新时间开始")
    private Date updatedAtStart;

    @ApiModelProperty(value = "更新时间结束")
    private Date updatedAtEnd;

    @ApiModelProperty(value = "最后审核时间开始")
    private Date lastReviewedStart;

    @ApiModelProperty(value = "最后审核时间结束")
    private Date lastReviewedEnd;

    @ApiModelProperty(value = "促销开始时间")
    private Date promotionStartTime;

    @ApiModelProperty(value = "促销结束时间")
    private Date promotionEndTime;

    @ApiModelProperty(value = "是否在促销期内")
    private Boolean inPromotionPeriod;

    // =============== 标签和SEO过滤 ===============
    
    @ApiModelProperty(value = "商品标签列表")
    private List<String> tags;

    @ApiModelProperty(value = "标签匹配模式：ALL->所有标签都匹配；ANY->任意标签匹配")
    private String tagMatchMode = "ANY";

    @ApiModelProperty(value = "关键词（原字段）")
    private String keywords;

    @ApiModelProperty(value = "SEO标题")
    private String seoTitle;

    @ApiModelProperty(value = "是否有SEO优化")
    private Boolean hasSeoOptimization;

    // =============== 供应商和仓库过滤 ===============
    
    @ApiModelProperty(value = "供应商ID列表")
    private List<Long> supplierIds;

    @ApiModelProperty(value = "仓库ID列表")
    private List<Long> warehouseIds;

    @ApiModelProperty(value = "最低供应商交货时间")
    private Integer minSupplierLeadTime;

    @ApiModelProperty(value = "最高供应商交货时间")
    private Integer maxSupplierLeadTime;

    // =============== 高级过滤选项 ===============
    
    @ApiModelProperty(value = "是否需要补货")
    private Boolean needsReorder;

    @ApiModelProperty(value = "是否即将过期")
    private Boolean expiringSoon;

    @ApiModelProperty(value = "过期天数阈值")
    private Integer expiryDaysThreshold = 30;

    @ApiModelProperty(value = "是否滞销商品")
    private Boolean isSlowMoving;

    @ApiModelProperty(value = "滞销天数阈值")
    private Integer slowMovingDaysThreshold = 90;

    @ApiModelProperty(value = "是否快销商品")
    private Boolean isFastMoving;

    @ApiModelProperty(value = "是否有质量问题")
    private Boolean hasQualityIssues;

    @ApiModelProperty(value = "是否有库存警报")
    private Boolean hasInventoryAlerts;

    @ApiModelProperty(value = "创建者ID")
    private Long createdBy;

    @ApiModelProperty(value = "最后更新者ID")
    private Long updatedBy;

    // =============== 排序和分页 ===============
    
    @ApiModelProperty(value = "页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize = 20;

    @ApiModelProperty(value = "排序字段")
    private String sortBy = "updatedAt";

    @ApiModelProperty(value = "排序方向：ASC->升序；DESC->降序")
    private String sortDirection = "DESC";

    @ApiModelProperty(value = "多字段排序")
    private List<SortField> sortFields;

    // =============== 查询选项 ===============
    
    @ApiModelProperty(value = "是否包含商品详情")
    private Boolean includeDetails = false;

    @ApiModelProperty(value = "是否包含库存信息")
    private Boolean includeInventory = false;

    @ApiModelProperty(value = "是否包含价格历史")
    private Boolean includePriceHistory = false;

    @ApiModelProperty(value = "是否包含销量统计")
    private Boolean includeSalesStats = false;

    @ApiModelProperty(value = "是否包含图片信息")
    private Boolean includeImages = false;

    @ApiModelProperty(value = "是否只返回必要字段")
    private Boolean briefMode = false;

    // =============== 统计选项 ===============
    
    @ApiModelProperty(value = "是否需要统计信息")
    private Boolean needStatistics = false;

    @ApiModelProperty(value = "是否按分类统计")
    private Boolean statsByCategory = false;

    @ApiModelProperty(value = "是否按品牌统计")
    private Boolean statsByBrand = false;

    @ApiModelProperty(value = "是否按价格区间统计")
    private Boolean statsByPriceRange = false;

    // Business methods
    public List<ProductLifecycleStatus> getLifecycleStatusEnums() {
        if (lifecycleStatuses == null || lifecycleStatuses.isEmpty()) {
            return null;
        }
        return lifecycleStatuses.stream()
                .map(ProductLifecycleStatus::fromCode)
                .collect(java.util.stream.Collectors.toList());
    }

    public void setLifecycleStatusEnums(List<ProductLifecycleStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            this.lifecycleStatuses = null;
            return;
        }
        this.lifecycleStatuses = statuses.stream()
                .map(ProductLifecycleStatus::getCode)
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean hasKeywordSearch() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    public boolean hasPriceFilter() {
        return minPrice != null || maxPrice != null || 
               minPromotionPrice != null || maxPromotionPrice != null ||
               minOriginalPrice != null || maxOriginalPrice != null;
    }

    public boolean hasStockFilter() {
        return minStock != null || maxStock != null || 
               hasStock != null || isLowStock != null || isOutOfStock != null ||
               minAvailableQuantity != null || maxAvailableQuantity != null;
    }

    public boolean hasTimeFilter() {
        return createdAtStart != null || createdAtEnd != null ||
               updatedAtStart != null || updatedAtEnd != null ||
               lastReviewedStart != null || lastReviewedEnd != null ||
               promotionStartTime != null || promotionEndTime != null;
    }

    public boolean hasAdvancedFilter() {
        return needsReorder != null || expiringSoon != null || 
               isSlowMoving != null || isFastMoving != null ||
               hasQualityIssues != null || hasInventoryAlerts != null;
    }

    public boolean isComplexQuery() {
        return hasKeywordSearch() || hasPriceFilter() || hasStockFilter() || 
               hasTimeFilter() || hasAdvancedFilter() ||
               (tags != null && !tags.isEmpty()) ||
               (categoryIds != null && !categoryIds.isEmpty()) ||
               (brandIds != null && !brandIds.isEmpty());
    }

    /**
     * 排序字段DTO
     */
    @Data
    public static class SortField {
        @ApiModelProperty(value = "排序字段名")
        private String field;

        @ApiModelProperty(value = "排序方向")
        private String direction = "ASC";

        public SortField() {}

        public SortField(String field, String direction) {
            this.field = field;
            this.direction = direction;
        }
    }
}