package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GmsGoods implements Serializable {
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品编码")
    private String goodsSn;

    @ApiModelProperty(value = "商品分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "商品分类名称")
    private String categoryName;

    @ApiModelProperty(value = "品牌ID")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "商品相册，多个图片用逗号分隔")
    private String albumPics;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "市场价")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "库存预警值")
    private Integer lowStock;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "商品重量，单位克")
    private BigDecimal weight;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    private Integer publishStatus;

    @ApiModelProperty(value = "新品状态：0->不是新品；1->新品")
    private Integer newStatus;

    @ApiModelProperty(value = "推荐状态：0->不推荐；1->推荐")
    private Integer recommendStatus;

    @ApiModelProperty(value = "审核状态：0->未审核；1->审核通过")
    private Integer verifyStatus;

    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    private Integer deleteStatus;

    @ApiModelProperty(value = "商品简介")
    private String subTitle;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "商品详情")
    private String detailHtml;

    @ApiModelProperty(value = "移动端商品详情")
    private String detailMobileHtml;

    @ApiModelProperty(value = "商品关键词")
    private String keywords;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

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

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAlbumPics() {
        return albumPics;
    }

    public void setAlbumPics(String albumPics) {
        this.albumPics = albumPics;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getLowStock() {
        return lowStock;
    }

    public void setLowStock(Integer lowStock) {
        this.lowStock = lowStock;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailHtml() {
        return detailHtml;
    }

    public void setDetailHtml(String detailHtml) {
        this.detailHtml = detailHtml;
    }

    public String getDetailMobileHtml() {
        return detailMobileHtml;
    }

    public void setDetailMobileHtml(String detailMobileHtml) {
        this.detailMobileHtml = detailMobileHtml;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", goodsSn=").append(goodsSn);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", categoryName=").append(categoryName);
        sb.append(", brandId=").append(brandId);
        sb.append(", brandName=").append(brandName);
        sb.append(", pic=").append(pic);
        sb.append(", albumPics=").append(albumPics);
        sb.append(", price=").append(price);
        sb.append(", marketPrice=").append(marketPrice);
        sb.append(", stock=").append(stock);
        sb.append(", lowStock=").append(lowStock);
        sb.append(", sale=").append(sale);
        sb.append(", unit=").append(unit);
        sb.append(", weight=").append(weight);
        sb.append(", sort=").append(sort);
        sb.append(", publishStatus=").append(publishStatus);
        sb.append(", newStatus=").append(newStatus);
        sb.append(", recommendStatus=").append(recommendStatus);
        sb.append(", verifyStatus=").append(verifyStatus);
        sb.append(", deleteStatus=").append(deleteStatus);
        sb.append(", subTitle=").append(subTitle);
        sb.append(", description=").append(description);
        sb.append(", detailHtml=").append(detailHtml);
        sb.append(", detailMobileHtml=").append(detailMobileHtml);
        sb.append(", keywords=").append(keywords);
        sb.append(", note=").append(note);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
