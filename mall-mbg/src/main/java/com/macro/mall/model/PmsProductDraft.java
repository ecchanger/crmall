package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品草稿实体类
 * Created by macro on 2023/12/22.
 */
public class PmsProductDraft implements Serializable {
    private Long id;

    @ApiModelProperty(value = "关联商品ID（新商品为null）")
    private Long productId;

    @ApiModelProperty(value = "草稿版本号")
    private Integer version;

    @ApiModelProperty(value = "草稿标题")
    private String title;

    @ApiModelProperty(value = "草稿状态：0->编辑中；1->已提交审核；2->审核通过；3->审核拒绝")
    private Integer status;

    @ApiModelProperty(value = "草稿内容，JSON格式存储完整商品信息")
    private String content;

    @ApiModelProperty(value = "创建人ID")
    private Long createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改人ID")
    private Long lastModifiedBy;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifiedTime;

    @ApiModelProperty(value = "备注")
    private String notes;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", version=").append(version);
        sb.append(", title=").append(title);
        sb.append(", status=").append(status);
        sb.append(", content=").append(content);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", lastModifiedBy=").append(lastModifiedBy);
        sb.append(", lastModifiedTime=").append(lastModifiedTime);
        sb.append(", notes=").append(notes);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}