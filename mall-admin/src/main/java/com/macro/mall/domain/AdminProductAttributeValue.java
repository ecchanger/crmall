package com.macro.mall.domain;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * 管理端商品属性值搜索文档
 * Created by macro on 2023/12/22.
 */
public class AdminProductAttributeValue implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private Long productAttributeId;
    
    @Field(type = FieldType.Keyword)
    private String name;
    
    @Field(type = FieldType.Keyword)
    private String value;
    
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(Long productAttributeId) {
        this.productAttributeId = productAttributeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}