package com.macro.mall.demo.mbg.mapper;

import com.macro.mall.demo.mbg.model.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductMapper {
    long countByExample(PmsProduct example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsProduct record);

    int insertSelective(PmsProduct record);

    List<PmsProduct> selectByExample(PmsProduct example);

    PmsProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProduct record);

    int updateByPrimaryKey(PmsProduct record);

    List<PmsProduct> selectByKeyword(@Param("keyword") String keyword);

    int updatePublishStatus(@Param("id") Long id, @Param("publishStatus") Integer publishStatus);
}
