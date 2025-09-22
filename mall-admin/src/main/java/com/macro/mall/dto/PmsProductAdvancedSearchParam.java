package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;

/**
 * 高级搜索参数
 * Created by macro on 2023/12/22.
 */
public class PmsProductAdvancedSearchParam {
    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "品牌ID列表")
    private List<Long> brandIds;

    @ApiModelProperty(value = "分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "最低价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最高价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "发布状态列表")
    private List<Integer> publishStatus;

    @ApiModelProperty(value = "审核状态列表")
    private List<Integer> verifyStatus;

    @ApiModelProperty(value = "新品状态")
    private Integer newStatus;

    @ApiModelProperty(value = "推荐状态")
    private Integer recommendStatus;

    @ApiModelProperty(value = "标签列表")
    private List<String> tags;

    @ApiModelProperty(value = "属性筛选：Map<属性ID, 属性值列表>")
    private java.util.Map<Long, List<String>> attributes;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序方向：asc/desc")
    private String sortDirection = "desc";

    @ApiModelProperty(value = "是否启用模糊搜索")
    private Boolean fuzzySearch = true;

    @ApiModelProperty(value = "搜索相似度阈值（0.0-1.0）")
    private Float similarityThreshold = 0.7f;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(List<Long> brandIds) {
        this.brandIds = brandIds;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<Integer> getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(List<Integer> publishStatus) {
        this.publishStatus = publishStatus;
    }

    public List<Integer> getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(List<Integer> verifyStatus) {
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public java.util.Map<Long, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(java.util.Map<Long, List<String>> attributes) {
        this.attributes = attributes;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Boolean getFuzzySearch() {
        return fuzzySearch;
    }

    public void setFuzzySearch(Boolean fuzzySearch) {
        this.fuzzySearch = fuzzySearch;
    }

    public Float getSimilarityThreshold() {
        return similarityThreshold;
    }

    public void setSimilarityThreshold(Float similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }
}