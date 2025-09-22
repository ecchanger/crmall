package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;

/**
 * 批量操作参数
 * Created by macro on 2023/12/22.
 */
public class PmsBulkOperationParam {
    @ApiModelProperty(value = "要操作的商品ID列表")
    private List<Long> productIds;

    @ApiModelProperty(value = "操作类型：1->批量更新；2->批量状态变更；3->批量删除")
    private Integer operationType;

    @ApiModelProperty(value = "要更新的字段和值")
    private Map<String, Object> updateFields;

    @ApiModelProperty(value = "是否仅验证（不执行实际操作）")
    private Boolean validateOnly = false;

    @ApiModelProperty(value = "操作描述")
    private String description;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Map<String, Object> getUpdateFields() {
        return updateFields;
    }

    public void setUpdateFields(Map<String, Object> updateFields) {
        this.updateFields = updateFields;
    }

    public Boolean getValidateOnly() {
        return validateOnly;
    }

    public void setValidateOnly(Boolean validateOnly) {
        this.validateOnly = validateOnly;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}