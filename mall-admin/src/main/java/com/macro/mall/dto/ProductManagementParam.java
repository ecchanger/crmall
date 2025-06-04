package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class ProductManagementParam {
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
}
