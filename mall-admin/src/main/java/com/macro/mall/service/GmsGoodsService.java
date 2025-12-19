package com.macro.mall.service;

import com.macro.mall.dto.GmsGoodsParam;
import com.macro.mall.dto.GmsGoodsQueryParam;
import com.macro.mall.model.GmsGoods;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品管理Service
 */
public interface GmsGoodsService {
    /**
     * 创建商品
     */
    @Transactional
    int create(GmsGoodsParam goodsParam);

    /**
     * 根据商品ID获取商品信息
     */
    GmsGoods getItem(Long id);

    /**
     * 更新商品
     */
    @Transactional
    int update(Long id, GmsGoodsParam goodsParam);

    /**
     * 删除商品
     */
    int delete(Long id);

    /**
     * 批量删除商品
     */
    int delete(List<Long> ids);

    /**
     * 分页查询商品
     */
    List<GmsGoods> list(GmsGoodsQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 根据商品名称或编码模糊查询
     */
    List<GmsGoods> list(String keyword);

    /**
     * 批量修改上架状态
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量修改新品状态
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量修改推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量修改审核状态
     */
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus);

    /**
     * 批量修改删除状态
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);
}
