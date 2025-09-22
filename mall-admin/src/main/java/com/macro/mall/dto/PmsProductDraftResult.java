package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import com.macro.mall.model.PmsProductDraft;
import java.util.Date;

/**
 * 商品草稿结果
 * Created by macro on 2023/12/22.
 */
public class PmsProductDraftResult extends PmsProductDraft {
    @ApiModelProperty(value = "创建人姓名")
    private String createdByName;

    @ApiModelProperty(value = "最后修改人姓名")
    private String lastModifiedByName;

    @ApiModelProperty(value = "是否可以编辑")
    private Boolean canEdit;

    @ApiModelProperty(value = "是否可以提交审核")
    private Boolean canSubmit;

    @ApiModelProperty(value = "是否可以删除")
    private Boolean canDelete;

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getLastModifiedByName() {
        return lastModifiedByName;
    }

    public void setLastModifiedByName(String lastModifiedByName) {
        this.lastModifiedByName = lastModifiedByName;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getCanSubmit() {
        return canSubmit;
    }

    public void setCanSubmit(Boolean canSubmit) {
        this.canSubmit = canSubmit;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }
}