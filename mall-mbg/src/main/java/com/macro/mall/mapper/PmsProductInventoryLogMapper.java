package com.macro.mall.mapper;

import com.macro.mall.model.PmsProductInventoryLog;
import com.macro.mall.model.PmsProductInventoryLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsProductInventoryLogMapper {
    long countByExample(PmsProductInventoryLogExample example);

    int deleteByExample(PmsProductInventoryLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsProductInventoryLog record);

    int insertSelective(PmsProductInventoryLog record);

    List<PmsProductInventoryLog> selectByExample(PmsProductInventoryLogExample example);

    PmsProductInventoryLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsProductInventoryLog record, @Param("example") PmsProductInventoryLogExample example);

    int updateByExample(@Param("record") PmsProductInventoryLog record, @Param("example") PmsProductInventoryLogExample example);

    int updateByPrimaryKeySelective(PmsProductInventoryLog record);

    int updateByPrimaryKey(PmsProductInventoryLog record);
}