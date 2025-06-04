package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class ProductManagementQueryParam {
    @ApiModelProperty("关联的商品ID")
    private Long productId;

    @ApiModelProperty("管理状态：0->待处理；1->处理中；2->已完成")
    private Integer managementStatus;

    @ApiModelProperty("优先级：1->低；2->中；3->高；4->紧急")
    private Integer priority;

    @ApiModelProperty("负责人")
    private String assignee;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("关键字搜索")
    private String keyword;
}
