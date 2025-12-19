package com.macro.mall.mapper;

import com.macro.mall.model.GmsGoods;
import com.macro.mall.model.GmsGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GmsGoodsMapper {
    long countByExample(GmsGoodsExample example);

    int deleteByExample(GmsGoodsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GmsGoods record);

    int insertSelective(GmsGoods record);

    List<GmsGoods> selectByExampleWithBLOBs(GmsGoodsExample example);

    List<GmsGoods> selectByExample(GmsGoodsExample example);

    GmsGoods selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GmsGoods record, @Param("example") GmsGoodsExample example);

    int updateByExampleWithBLOBs(@Param("record") GmsGoods record, @Param("example") GmsGoodsExample example);

    int updateByExample(@Param("record") GmsGoods record, @Param("example") GmsGoodsExample example);

    int updateByPrimaryKeySelective(GmsGoods record);

    int updateByPrimaryKeyWithBLOBs(GmsGoods record);

    int updateByPrimaryKey(GmsGoods record);
}
