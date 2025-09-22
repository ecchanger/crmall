package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 商品草稿参数
 * Created by macro on 2023/12/22.
 */
public class PmsProductDraftParam {
    @ApiModelProperty(value = "关联商品ID（新商品为null）")
    private Long productId;

    @ApiModelProperty(value = "草稿标题")
    private String title;

    @ApiModelProperty(value = "商品基本信息")
    private PmsProductParam productParam;

    @ApiModelProperty(value = "是否自动保存")
    private Boolean autoSave = false;

    @ApiModelProperty(value = "备注")
    private String notes;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PmsProductParam getProductParam() {
        return productParam;
    }

    public void setProductParam(PmsProductParam productParam) {
        this.productParam = productParam;
    }

    public Boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}