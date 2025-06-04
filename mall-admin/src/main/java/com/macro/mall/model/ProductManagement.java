package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class ProductManagement implements Serializable {
    @ApiModelProperty("商品管理ID")
    private Long id;

    @ApiModelProperty("关联的商品ID")
    private Long productId;

    @ApiModelProperty("管理状态：0->待处理；1->处理中；2->已完成")
    private Integer managementStatus;

    @ApiModelProperty("优先级：1->低；2->中；3->高；4->紧急")
    private Integer priority;

    @ApiModelProperty("负责人")
    private String assignee;

    @ApiModelProperty("预计完成时间")
    private Date expectedCompletionTime;

    @ApiModelProperty("实际完成时间")
    private Date actualCompletionTime;

    @ApiModelProperty("管理备注")
    private String managementNotes;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

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

    public Integer getManagementStatus() {
        return managementStatus;
    }

    public void setManagementStatus(Integer managementStatus) {
        this.managementStatus = managementStatus;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getExpectedCompletionTime() {
        return expectedCompletionTime;
    }

    public void setExpectedCompletionTime(Date expectedCompletionTime) {
        this.expectedCompletionTime = expectedCompletionTime;
    }

    public Date getActualCompletionTime() {
        return actualCompletionTime;
    }

    public void setActualCompletionTime(Date actualCompletionTime) {
        this.actualCompletionTime = actualCompletionTime;
    }

    public String getManagementNotes() {
        return managementNotes;
    }

    public void setManagementNotes(String managementNotes) {
        this.managementNotes = managementNotes;
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
        sb.append(", productId=").append(productId);
        sb.append(", managementStatus=").append(managementStatus);
        sb.append(", priority=").append(priority);
        sb.append(", assignee=").append(assignee);
        sb.append(", expectedCompletionTime=").append(expectedCompletionTime);
        sb.append(", actualCompletionTime=").append(actualCompletionTime);
        sb.append(", managementNotes=").append(managementNotes);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
