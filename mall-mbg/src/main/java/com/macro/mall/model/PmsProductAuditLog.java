package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品操作审计日志实体类
 * Created by macro on 2023/12/22.
 */
public class PmsProductAuditLog implements Serializable {
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "操作类型：1->创建；2->更新；3->删除；4->发布；5->下架；6->审核")
    private Integer operationType;

    @ApiModelProperty(value = "操作描述")
    private String operationDesc;

    @ApiModelProperty(value = "操作前数据JSON")
    private String beforeData;

    @ApiModelProperty(value = "操作后数据JSON")
    private String afterData;

    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    @ApiModelProperty(value = "操作时间")
    private Date operationTime;

    @ApiModelProperty(value = "客户端IP")
    private String clientIp;

    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    @ApiModelProperty(value = "请求URI")
    private String requestUri;

    @ApiModelProperty(value = "请求方法")
    private String requestMethod;

    @ApiModelProperty(value = "执行时长（毫秒）")
    private Long executionTime;

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

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public String getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(String beforeData) {
        this.beforeData = beforeData;
    }

    public String getAfterData() {
        return afterData;
    }

    public void setAfterData(String afterData) {
        this.afterData = afterData;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", operationType=").append(operationType);
        sb.append(", operationDesc=").append(operationDesc);
        sb.append(", beforeData=").append(beforeData);
        sb.append(", afterData=").append(afterData);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", userAgent=").append(userAgent);
        sb.append(", requestUri=").append(requestUri);
        sb.append(", requestMethod=").append(requestMethod);
        sb.append(", executionTime=").append(executionTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}