package com.macro.mall.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品管理搜索文档
 * Created by macro on 2023/12/22.
 */
@Document(indexName = "pms_admin_products")
@Setting(shards = 1, replicas = 0)
public class AdminProductSearchDocument implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private Long id;
    
    @Field(type = FieldType.Keyword)
    private String productSn;
    
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String name;
    
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String subTitle;
    
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String keywords;
    
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String description;
    
    private Long brandId;
    
    @Field(type = FieldType.Keyword)
    private String brandName;
    
    private Long productCategoryId;
    
    @Field(type = FieldType.Keyword)
    private String productCategoryName;
    
    private BigDecimal price;
    
    private BigDecimal originalPrice;
    
    private Integer stock;
    
    @Field(type = FieldType.Integer)
    private Integer publishStatus;
    
    @Field(type = FieldType.Integer)
    private Integer verifyStatus;
    
    @Field(type = FieldType.Integer)
    private Integer newStatus;
    
    @Field(type = FieldType.Integer)
    private Integer recommendStatus;
    
    @Field(type = FieldType.Integer)
    private Integer deleteStatus;
    
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Date createTime;
    
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Date updateTime;
    
    @Field(type = FieldType.Keyword)
    private List<String> tags;
    
    @Field(type = FieldType.Nested)
    private List<AdminProductAttributeValue> attributes;
    
    private String pic;
    
    @Field(type = FieldType.Keyword)
    private List<String> albumPics;
    
    // 用于全文搜索的综合字段
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String searchText;
    
    // 用于搜索权重计算
    @Field(type = FieldType.Float)
    private Float searchScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
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

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<AdminProductAttributeValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AdminProductAttributeValue> attributes) {
        this.attributes = attributes;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<String> getAlbumPics() {
        return albumPics;
    }

    public void setAlbumPics(List<String> albumPics) {
        this.albumPics = albumPics;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Float getSearchScore() {
        return searchScore;
    }

    public void setSearchScore(Float searchScore) {
        this.searchScore = searchScore;
    }
}