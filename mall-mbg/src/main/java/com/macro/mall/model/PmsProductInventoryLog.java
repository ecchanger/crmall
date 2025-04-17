package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品库存变动日志
 */
public class PmsProductInventoryLog implements Serializable {
    private Long id;

    private Long productId;

    private Long skuId;

    @ApiModelProperty(value = "库存变动前数量")
    private Integer beforeStock;

    @ApiModelProperty(value = "库存变动后数量")
    private Integer afterStock;

    @ApiModelProperty(value = "变动数量")
    private Integer changeStock;

    @ApiModelProperty(value = "操作类型：1->入库；2->出库；3->调整")
    private Integer operateType;

    @ApiModelProperty(value = "操作人")
    private String operateMan;

    @ApiModelProperty(value = "操作备注")
    private String note;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;

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

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getBeforeStock() {
        return beforeStock;
    }

    public void setBeforeStock(Integer beforeStock) {
        this.beforeStock = beforeStock;
    }

    public Integer getAfterStock() {
        return afterStock;
    }

    public void setAfterStock(Integer afterStock) {
        this.afterStock = afterStock;
    }

    public Integer getChangeStock() {
        return changeStock;
    }

    public void setChangeStock(Integer changeStock) {
        this.changeStock = changeStock;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getOperateMan() {
        return operateMan;
    }

    public void setOperateMan(String operateMan) {
        this.operateMan = operateMan;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", skuId=").append(skuId);
        sb.append(", beforeStock=").append(beforeStock);
        sb.append(", afterStock=").append(afterStock);
        sb.append(", changeStock=").append(changeStock);
        sb.append(", operateType=").append(operateType);
        sb.append(", operateMan=").append(operateMan);
        sb.append(", note=").append(note);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}