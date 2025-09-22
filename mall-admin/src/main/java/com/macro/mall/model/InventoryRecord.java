package com.macro.mall.model;

import com.macro.mall.enums.InventoryAlertType;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 库存记录实体，用于实时库存跟踪
 * Created for Product Management Module Enhancement
 */
public class InventoryRecord implements Serializable {
    
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品编号")
    private String productSn;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "可用库存数量")
    private Integer availableQuantity;

    @ApiModelProperty(value = "预留库存数量")
    private Integer reservedQuantity;

    @ApiModelProperty(value = "损坏库存数量")
    private Integer damagedQuantity;

    @ApiModelProperty(value = "在途库存数量")
    private Integer inTransitQuantity;

    @ApiModelProperty(value = "补货点")
    private Integer reorderPoint;

    @ApiModelProperty(value = "最大库存水平")
    private Integer maxStockLevel;

    @ApiModelProperty(value = "供应商交货时间（天）")
    private Integer supplierLeadTime;

    @ApiModelProperty(value = "平均日销量")
    private Double averageDailySales;

    @ApiModelProperty(value = "最后补货时间")
    private Date lastRestockDate;

    @ApiModelProperty(value = "最后销售时间")
    private Date lastSaleDate;

    @ApiModelProperty(value = "库存周转率")
    private Double inventoryTurnoverRate;

    @ApiModelProperty(value = "库存价值")
    private Double inventoryValue;

    @ApiModelProperty(value = "成本单价")
    private Double unitCost;

    @ApiModelProperty(value = "是否启用自动补货")
    private Boolean autoReorderEnabled;

    @ApiModelProperty(value = "自动补货数量")
    private Integer autoReorderQuantity;

    @ApiModelProperty(value = "库存状态：0->正常；1->不足；2->缺货；3->过量")
    private Integer inventoryStatus;

    @ApiModelProperty(value = "过期日期")
    private Date expiryDate;

    @ApiModelProperty(value = "批次号")
    private String batchNumber;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "最后盘点时间")
    private Date lastStocktakeDate;

    @ApiModelProperty(value = "盘点差异数量")
    private Integer stocktakeDifference;

    @ApiModelProperty(value = "备注")
    private String notes;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "更新者ID")
    private Long updatedBy;

    private static final long serialVersionUID = 1L;

    // Constructors
    public InventoryRecord() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.availableQuantity = 0;
        this.reservedQuantity = 0;
        this.damagedQuantity = 0;
        this.inTransitQuantity = 0;
        this.inventoryStatus = 0;
        this.autoReorderEnabled = false;
    }

    // Business methods
    public Integer getTotalQuantity() {
        int available = this.availableQuantity != null ? this.availableQuantity : 0;
        int reserved = this.reservedQuantity != null ? this.reservedQuantity : 0;
        int damaged = this.damagedQuantity != null ? this.damagedQuantity : 0;
        int inTransit = this.inTransitQuantity != null ? this.inTransitQuantity : 0;
        return available + reserved + damaged + inTransit;
    }

    public Integer getUsableQuantity() {
        int available = this.availableQuantity != null ? this.availableQuantity : 0;
        int inTransit = this.inTransitQuantity != null ? this.inTransitQuantity : 0;
        return available + inTransit;
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

    public boolean needsReorder() {
        return isLowStock() && this.autoReorderEnabled != null && this.autoReorderEnabled;
    }

    public boolean isExpired() {
        return this.expiryDate != null && this.expiryDate.before(new Date());
    }

    public boolean isExpiringSoon(int days) {
        if (this.expiryDate == null) return false;
        long daysDiff = (this.expiryDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
        return daysDiff <= days && daysDiff >= 0;
    }

    public InventoryAlertType getAlertType() {
        if (isOutOfStock()) return InventoryAlertType.OUT_OF_STOCK;
        if (isExpired() || isExpiringSoon(7)) return InventoryAlertType.EXPIRED_PRODUCTS;
        if (isLowStock()) return InventoryAlertType.LOW_STOCK;
        if (isOverstock()) return InventoryAlertType.OVERSTOCK;
        if (needsReorder()) return InventoryAlertType.REORDER_POINT;
        return null;
    }

    public Double calculateInventoryValue() {
        if (this.unitCost == null || this.availableQuantity == null) return 0.0;
        return this.unitCost * this.availableQuantity;
    }

    public int getDaysSinceLastSale() {
        if (this.lastSaleDate == null) return Integer.MAX_VALUE;
        return (int) ((new Date().getTime() - this.lastSaleDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public boolean isSlowMoving(int threshold) {
        return getDaysSinceLastSale() > threshold;
    }

    public boolean isFastMoving() {
        return this.averageDailySales != null && this.averageDailySales > 0 && 
               this.availableQuantity != null && 
               (this.availableQuantity / this.averageDailySales) < 7; // 少于7天库存
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
        this.updatedAt = new Date();
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
        this.updatedAt = new Date();
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
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
        sb.append(", productId=").append(productId);
        sb.append(", productName=").append(productName);
        sb.append(", availableQuantity=").append(availableQuantity);
        sb.append(", reservedQuantity=").append(reservedQuantity);
        sb.append(", reorderPoint=").append(reorderPoint);
        sb.append(", inventoryStatus=").append(inventoryStatus);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}