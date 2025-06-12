package com.macro.mall.service;

import com.macro.mall.dto.PmsProductParam;
import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.model.PmsProduct;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品管理Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductService {
    /**
     * 创建商品
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    int create(PmsProductParam productParam);

    /**
     * 根据商品ID获取商品信息（用于更新商品）
     */
    PmsProductResult getUpdateInfo(Long id);

    /**
     * 更新商品
     */
    @Transactional
    int update(Long id, PmsProductParam productParam);

    /**
     * 分页查询商品
     */
    List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量修改审核状态
     * @param ids 商品ID列表
     * @param verifyStatus 审核状态
     * @param detail 审核详情
     */
    @Transactional
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 批量修改商品上架状态
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量修改商品推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量修改新品状态
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量删除商品
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 根据商品名称或者货号模糊查询
     */
    List<PmsProduct> list(String keyword);

    /**
     * 根据商品ID获取商品详情
     */
    PmsProduct getProduct(Long id);

    /**
     * 批量复制商品
     */
    @Transactional
    int copyProducts(List<Long> ids);

    /**
     * 获取库存预警商品列表
     */
    List<PmsProduct> getStockWarningList(Integer lowStock, Integer pageSize, Integer pageNum);

    /**
     * 批量更新商品价格
     * @param ids 商品ID列表
     * @param price 价格
     * @param priceType 价格类型：0->销售价格；1->市场价格
     */
    @Transactional
    int updatePrice(List<Long> ids, BigDecimal price, Integer priceType);

    /**
     * 获取商品统计信息
     */
    Map<String, Object> getProductStatistics();

    /**
     * 根据分类ID获取商品列表
     */
    List<PmsProduct> getProductsByCategory(Long categoryId, Integer pageSize, Integer pageNum);

    /**
     * 根据品牌ID获取商品列表
     */
    List<PmsProduct> getProductsByBrand(Long brandId, Integer pageSize, Integer pageNum);

    /**
     * 高级搜索商品
     */
    List<PmsProduct> advancedSearch(PmsProductQueryParam queryParam, BigDecimal minPrice, BigDecimal maxPrice,
                                   Integer recommendStatus, Integer newStatus, Integer pageSize, Integer pageNum);
}
