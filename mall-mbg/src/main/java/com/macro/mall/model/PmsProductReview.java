package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品审核记录实体类
 * Created by macro on 2023/12/22.
 */
public class PmsProductReview implements Serializable {
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "草稿ID")
    private Long draftId;

    @ApiModelProperty(value = "审核人ID")
    private Long reviewerId;

    @ApiModelProperty(value = "审核人姓名")
    private String reviewerName;

    @ApiModelProperty(value = "审核状态：0->待审核；1->审核通过；2->审核拒绝；3->需要修改")
    private Integer status;

    @ApiModelProperty(value = "审核级别：1->一级审核；2->二级审核；3->三级审核")
    private Integer level;

    @ApiModelProperty(value = "审核意见")
    private String comments;

    @ApiModelProperty(value = "审核时间")
    private Date reviewTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "附件路径，多个以逗号分隔")
    private String attachments;

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

    public Long getDraftId() {
        return draftId;
    }

    public void setDraftId(Long draftId) {
        this.draftId = draftId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", draftId=").append(draftId);
        sb.append(", reviewerId=").append(reviewerId);
        sb.append(", reviewerName=").append(reviewerName);
        sb.append(", status=").append(status);
        sb.append(", level=").append(level);
        sb.append(", comments=").append(comments);
        sb.append(", reviewTime=").append(reviewTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", attachments=").append(attachments);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}