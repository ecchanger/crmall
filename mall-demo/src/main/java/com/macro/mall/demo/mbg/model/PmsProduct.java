package com.macro.mall.demo.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PmsProduct implements Serializable {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Long categoryId;

    private String pic;

    private Integer publishStatus;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
