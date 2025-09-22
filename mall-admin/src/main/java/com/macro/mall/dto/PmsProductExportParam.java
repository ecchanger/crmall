package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 商品导出参数
 * Created by macro on 2023/12/22.
 */
public class PmsProductExportParam {
    @ApiModelProperty(value = "导出商品ID列表（为空则导出所有）")
    private List<Long> productIds;

    @ApiModelProperty(value = "品牌ID列表")
    private List<Long> brandIds;

    @ApiModelProperty(value = "分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "发布状态")
    private Integer publishStatus;

    @ApiModelProperty(value = "审核状态")
    private Integer verifyStatus;

    @ApiModelProperty(value = "创建时间范围-开始")
    private java.util.Date createTimeStart;

    @ApiModelProperty(value = "创建时间范围-结束")
    private java.util.Date createTimeEnd;

    @ApiModelProperty(value = "导出字段列表")
    private List<String> exportFields;

    @ApiModelProperty(value = "是否包含SKU信息")
    private Boolean includeSku = false;

    @ApiModelProperty(value = "是否包含属性信息")
    private Boolean includeAttributes = false;

    @ApiModelProperty(value = "是否包含图片信息")
    private Boolean includeImages = false;

    @ApiModelProperty(value = "导出格式：excel/csv")
    private String format = "excel";

    @ApiModelProperty(value = "最大导出数量")
    private Integer maxCount = 10000;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
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

    public java.util.Date getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(java.util.Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public java.util.Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(java.util.Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public List<String> getExportFields() {
        return exportFields;
    }

    public void setExportFields(List<String> exportFields) {
        this.exportFields = exportFields;
    }

    public Boolean getIncludeSku() {
        return includeSku;
    }

    public void setIncludeSku(Boolean includeSku) {
        this.includeSku = includeSku;
    }

    public Boolean getIncludeAttributes() {
        return includeAttributes;
    }

    public void setIncludeAttributes(Boolean includeAttributes) {
        this.includeAttributes = includeAttributes;
    }

    public Boolean getIncludeImages() {
        return includeImages;
    }

    public void setIncludeImages(Boolean includeImages) {
        this.includeImages = includeImages;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }
}