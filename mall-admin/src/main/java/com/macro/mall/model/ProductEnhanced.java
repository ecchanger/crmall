package com.macro.mall.model;

import com.macro.mall.enums.ProductLifecycleStatus;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 增强的商品实体，包含生命周期管理和新功能字段
 * Created for Product Management Module Enhancement
 */
public class ProductEnhanced implements Serializable {
    
    private Long id;

    private Long brandId;

    private Long productCategoryId;

    private Long feightTemplateId;

    private Long productAttributeCategoryId;

    @ApiModelProperty(value = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称不能超过200个字符")
    private String name;

    private String pic;

    @ApiModelProperty(value = "货号")
    @NotBlank(message = "货号不能为空")
    @Size(max = 64, message = "货号不能超过64个字符")
    private String productSn;

    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    private Integer deleteStatus;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    private Integer publishStatus;

    @ApiModelProperty(value = "新品状态:0->不是新品；1->新品")
    private Integer newStatus;

    @ApiModelProperty(value = "推荐状态；0->不推荐；1->推荐")
    private Integer recommandStatus;

    @ApiModelProperty(value = "审核状态：0->未审核；1->审核通过")
    private Integer verifyStatus;

    // =============== 新增的生命周期管理字段 ===============
    
    @ApiModelProperty(value = "生命周期状态")
    private String lifecycleStatus;

    @ApiModelProperty(value = "商品质量评分 (0-5)")
    @DecimalMin(value = "0.00", message = "质量评分不能小于0")
    @DecimalMax(value = "5.00", message = "质量评分不能大于5")
    private BigDecimal qualityScore;

    @ApiModelProperty(value = "SEO优化标题")
    @Size(max = 100, message = "SEO标题不能超过100个字符")
    private String seoTitle;

    @ApiModelProperty(value = "SEO元描述")
    @Size(max = 160, message = "SEO描述不能超过160个字符")
    private String seoDescription;

    @ApiModelProperty(value = "商品标签，JSON格式存储")
    private String tags;

    @ApiModelProperty(value = "季节性可用性规则，JSON格式")
    private String seasonalAvailability;

    @ApiModelProperty(value = "可持续性评分 (1-100)")
    @Min(value = 1, message = "可持续性评分不能小于1")
    @Max(value = 100, message = "可持续性评分不能大于100")
    private Integer sustainabilityScore;

    @ApiModelProperty(value = "最后审核时间")
    private Date lastReviewedDate;

    @ApiModelProperty(value = "下次审核时间")
    private Date nextReviewDate;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "创建者ID")
    private Long createdBy;

    @ApiModelProperty(value = "最后更新者ID")
    private Long updatedBy;

    // =============== 增强的库存管理字段 ===============
    
    @ApiModelProperty(value = "可用库存数量")
    @Min(value = 0, message = "可用库存不能小于0")
    private Integer availableQuantity;

    @ApiModelProperty(value = "预留库存数量")
    @Min(value = 0, message = "预留库存不能小于0")
    private Integer reservedQuantity;

    @ApiModelProperty(value = "自动补货点")
    @Min(value = 0, message = "补货点不能小于0")
    private Integer reorderPoint;

    @ApiModelProperty(value = "最大库存水平")
    private Integer maxStockLevel;

    @ApiModelProperty(value = "供应商交货时间（天）")
    @Min(value = 0, message = "交货时间不能小于0")
    private Integer supplierLeadTime;

    @ApiModelProperty(value = "最后补货时间")
    private Date lastRestockDate;

    @ApiModelProperty(value = "库存移动历史，JSON格式")
    private String stockMovementHistory;

    // =============== 原有字段 ===============
    
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.00", message = "商品价格不能小于0")
    private BigDecimal price;

    @ApiModelProperty(value = "促销价格")
    @DecimalMin(value = "0.00", message = "促销价格不能小于0")
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "赠送的成长值")
    private Integer giftGrowth;

    @ApiModelProperty(value = "赠送的积分")
    private Integer giftPoint;

    @ApiModelProperty(value = "限制使用的积分数")
    private Integer usePointLimit;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "市场价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "库存")
    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;

    @ApiModelProperty(value = "库存预警值")
    @Min(value = 0, message = "库存预警值不能小于0")
    private Integer lowStock;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "商品重量，默认为克")
    private BigDecimal weight;

    @ApiModelProperty(value = "是否为预告商品：0->不是；1->是")
    private Integer previewStatus;

    @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
    private String serviceIds;

    private String keywords;

    private String note;

    @ApiModelProperty(value = "画册图片，连产品图片限制为5张，以逗号分割")
    private String albumPics;

    private String detailTitle;

    @ApiModelProperty(value = "促销开始时间")
    private Date promotionStartTime;

    @ApiModelProperty(value = "促销结束时间")
    private Date promotionEndTime;

    @ApiModelProperty(value = "活动限购数量")
    private Integer promotionPerLimit;

    @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购")
    private Integer promotionType;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品分类名称")
    private String productCategoryName;

    @ApiModelProperty(value = "商品描述")
    private String description;

    private String detailDesc;

    @ApiModelProperty(value = "产品详情网页内容")
    private String detailHtml;

    @ApiModelProperty(value = "移动端网页详情")
    private String detailMobileHtml;

    private static final long serialVersionUID = 1L;

    // Constructors
    public ProductEnhanced() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.lifecycleStatus = ProductLifecycleStatus.DRAFT.getCode();
        this.qualityScore = BigDecimal.ZERO;
        this.sustainabilityScore = 50; // 默认中等评分
        this.availableQuantity = 0;
        this.reservedQuantity = 0;
        this.reorderPoint = 10; // 默认补货点
        this.supplierLeadTime = 7; // 默认7天交货
    }

    // Business methods
    public ProductLifecycleStatus getLifecycleStatusEnum() {
        return this.lifecycleStatus != null ? ProductLifecycleStatus.fromCode(this.lifecycleStatus) : ProductLifecycleStatus.DRAFT;
    }

    public void setLifecycleStatusEnum(ProductLifecycleStatus status) {
        this.lifecycleStatus = status.getCode();
    }

    public boolean isLowStock() {
        return this.availableQuantity != null && this.reorderPoint != null && 
               this.availableQuantity <= this.reorderPoint;
    }

    public boolean isOutOfStock() {
        return this.availableQuantity == null || this.availableQuantity <= 0;
    }

    public boolean isOverstock() {
        return this.maxStockLevel != null && this.availableQuantity != null && 
               this.availableQuantity > this.maxStockLevel;
    }

    public Integer getTotalStock() {
        int available = this.availableQuantity != null ? this.availableQuantity : 0;
        int reserved = this.reservedQuantity != null ? this.reservedQuantity : 0;
        return available + reserved;
    }

    public boolean canEdit() {
        return getLifecycleStatusEnum().allowsEditing();
    }

    public boolean isVisibleToUsers() {
        return getLifecycleStatusEnum().isVisibleToUsers();
    }

    // 所有的getter和setter方法
    // ... (为了简洁，这里省略了所有getter/setter，实际实现中需要包含所有字段的getter/setter)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public String getLifecycleStatus() {
        return lifecycleStatus;
    }

    public void setLifecycleStatus(String lifecycleStatus) {
        this.lifecycleStatus = lifecycleStatus;
    }

    public BigDecimal getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(BigDecimal qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getSustainabilityScore() {
        return sustainabilityScore;
    }

    public void setSustainabilityScore(Integer sustainabilityScore) {
        this.sustainabilityScore = sustainabilityScore;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ... 其他getter/setter方法

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", productSn=").append(productSn);
        sb.append(", lifecycleStatus=").append(lifecycleStatus);
        sb.append(", qualityScore=").append(qualityScore);
        sb.append(", price=").append(price);
        sb.append(", availableQuantity=").append(availableQuantity);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}