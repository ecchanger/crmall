package com.macro.mall.service;

import com.macro.mall.dto.PmsProductInventoryParam;
import com.macro.mall.dto.PmsProductInventoryResult;
import com.macro.mall.model.PmsProductInventoryLog;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品库存管理Service
 */
public interface PmsProductInventoryService {
    /**
     * 获取商品库存信息
     */
    PmsProductInventoryResult getInventoryInfo(Long productId);

    /**
     * 更新商品库存
     */
    @Transactional
    int updateInventory(PmsProductInventoryParam inventoryParam);

    /**
     * 批量更新商品库存
     */
    @Transactional
    int batchUpdateInventory(List<PmsProductInventoryParam> inventoryParamList);

    /**
     * 获取库存变动记录
     */
    List<PmsProductInventoryLog> getInventoryLogs(Long productId, Long skuId, Integer pageSize, Integer pageNum);

    /**
     * 获取库存预警商品
     */
    List<PmsProductInventoryResult> getLowStockProducts(Integer threshold, Integer pageSize, Integer pageNum);
}