package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductManagementDashboard {
    @ApiModelProperty("待处理任务数")
    private Integer pendingCount;

    @ApiModelProperty("处理中任务数")
    private Integer processingCount;

    @ApiModelProperty("已完成任务数")
    private Integer completedCount;

    @ApiModelProperty("逾期任务数")
    private Integer overdueCount;

    @ApiModelProperty("高优先级任务数")
    private Integer highPriorityCount;

    @ApiModelProperty("紧急任务数")
    private Integer urgentCount;

    @ApiModelProperty("本周完成任务数")
    private Integer weeklyCompletedCount;

    @ApiModelProperty("本月完成任务数")
    private Integer monthlyCompletedCount;
}
