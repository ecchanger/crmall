package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.dto.PmsProductInventoryParam;
import com.macro.mall.dto.PmsProductInventoryResult;
import com.macro.mall.mapper.PmsProductInventoryLogMapper;
import com.macro.mall.mapper.PmsProductMapper;
import com.macro.mall.mapper.PmsSkuStockMapper;
import com.macro.mall.model.*;
import com.macro.mall.service.PmsProductInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品库存管理Service实现类
 */
@Service
public class PmsProductInventoryServiceImpl implements PmsProductInventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductInventoryServiceImpl.class);
    
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private PmsProductInventoryLogMapper inventoryLogMapper;

    @Override
    public PmsProductInventoryResult getInventoryInfo(Long productId) {
        PmsProductInventoryResult result = new PmsProductInventoryResult();
        // 获取商品信息
        PmsProduct product = productMapper.selectByPrimaryKey(productId);
        result.setProduct(product);
        
        // 获取SKU库存信息
        PmsSkuStockExample skuExample = new PmsSkuStockExample();
        skuExample.createCriteria().andProductIdEqualTo(productId);
        List<PmsSkuStock> skuStockList = skuStockMapper.selectByExample(skuExample);
        result.setSkuStockList(skuStockList);
        
        // 获取库存变动记录
        PmsProductInventoryLogExample logExample = new PmsProductInventoryLogExample();
        logExample.createCriteria().andProductIdEqualTo(productId);
        logExample.setOrderByClause("create_time desc");
        PageHelper.startPage(1, 10);
        List<PmsProductInventoryLog> logList = inventoryLogMapper.selectByExample(logExample);
        result.setInventoryLogList(logList);
        
        return result;
    }

    @Override
    @Transactional
    public int updateInventory(PmsProductInventoryParam inventoryParam) {
        int count = 0;
        Long productId = inventoryParam.getProductId();
        Long skuId = inventoryParam.getSkuId();
        Integer changeStock = inventoryParam.getChangeStock();
        
        // 如果指定了SKU ID，则更新SKU库存
        if (skuId != null) {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(skuId);
            if (skuStock != null) {
                // 记录变更前库存
                Integer beforeStock = skuStock.getStock();
                Integer afterStock;
                
                // 根据操作类型处理库存
                switch (inventoryParam.getOperateType()) {
                    case 1: // 入库
                        afterStock = beforeStock + changeStock;
                        break;
                    case 2: // 出库
                        afterStock = beforeStock - changeStock;
                        if (afterStock < 0) {
                            throw new IllegalArgumentException("库存不足");
                        }
                        break;
                    case 3: // 调整
                        afterStock = changeStock;
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的操作类型");
                }
                
                // 更新SKU库存
                skuStock.setStock(afterStock);
                count = skuStockMapper.updateByPrimaryKeySelective(skuStock);
                
                // 记录库存变动日志
                if (count > 0) {
                    PmsProductInventoryLog log = new PmsProductInventoryLog();
                    log.setProductId(productId);
                    log.setSkuId(skuId);
                    log.setBeforeStock(beforeStock);
                    log.setAfterStock(afterStock);
                    log.setChangeStock(changeStock);
                    log.setOperateType(inventoryParam.getOperateType());
                    log.setOperateMan(inventoryParam.getOperateMan());
                    log.setNote(inventoryParam.getNote());
                    log.setCreateTime(new Date());
                    inventoryLogMapper.insert(log);
                }
            }
        } else {
            // 如果没有指定SKU ID，则更新商品总库存
            PmsProduct product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                // 记录变更前库存
                Integer beforeStock = product.getStock();
                Integer afterStock;
                
                // 根据操作类型处理库存
                switch (inventoryParam.getOperateType()) {
                    case 1: // 入库
                        afterStock = beforeStock + changeStock;
                        break;
                    case 2: // 出库
                        afterStock = beforeStock - changeStock;
                        if (afterStock < 0) {
                            throw new IllegalArgumentException("库存不足");
                        }
                        break;
                    case 3: // 调整
                        afterStock = changeStock;
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的操作类型");
                }
                
                // 更新商品库存
                product.setStock(afterStock);
                count = productMapper.updateByPrimaryKeySelective(product);
                
                // 记录库存变动日志
                if (count > 0) {
                    PmsProductInventoryLog log = new PmsProductInventoryLog();
                    log.setProductId(productId);
                    log.setBeforeStock(beforeStock);
                    log.setAfterStock(afterStock);
                    log.setChangeStock(changeStock);
                    log.setOperateType(inventoryParam.getOperateType());
                    log.setOperateMan(inventoryParam.getOperateMan());
                    log.setNote(inventoryParam.getNote());
                    log.setCreateTime(new Date());
                    inventoryLogMapper.insert(log);
                }
            }
        }
        
        return count;
    }

    @Override
    @Transactional
    public int batchUpdateInventory(List<PmsProductInventoryParam> inventoryParamList) {
        int count = 0;
        if (CollUtil.isEmpty(inventoryParamList)) {
            return count;
        }
        
        for (PmsProductInventoryParam param : inventoryParamList) {
            count += updateInventory(param);
        }
        
        return count;
    }

    @Override
    public List<PmsProductInventoryLog> getInventoryLogs(Long productId, Long skuId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductInventoryLogExample example = new PmsProductInventoryLogExample();
        PmsProductInventoryLogExample.Criteria criteria = example.createCriteria();
        
        if (productId != null) {
            criteria.andProductIdEqualTo(productId);
        }
        
        if (skuId != null) {
            criteria.andSkuIdEqualTo(skuId);
        }
        
        example.setOrderByClause("create_time desc");
        return inventoryLogMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductInventoryResult> getLowStockProducts(Integer threshold, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<PmsProductInventoryResult> resultList = new ArrayList<>();
        
        // 查询库存低于阈值的商品
        PmsProductExample productExample = new PmsProductExample();
        productExample.createCriteria().andStockLessThanOrEqualTo(threshold).andDeleteStatusEqualTo(0);
        List<PmsProduct> productList = productMapper.selectByExample(productExample);
        
        // 查询库存低于阈值的SKU
        PmsSkuStockExample skuExample = new PmsSkuStockExample();
        skuExample.createCriteria().andStockLessThanOrEqualTo(threshold);
        List<PmsSkuStock> lowStockSkuList = skuStockMapper.selectByExample(skuExample);
        
        // 组装结果
        for (PmsProduct product : productList) {
            PmsProductInventoryResult result = new PmsProductInventoryResult();
            result.setProduct(product);
            
            // 获取该商品的所有SKU
            List<PmsSkuStock> productSkuList = new ArrayList<>();
            for (PmsSkuStock sku : lowStockSkuList) {
                if (sku.getProductId().equals(product.getId())) {
                    productSkuList.add(sku);
                }
            }
            result.setSkuStockList(productSkuList);
            
            resultList.add(result);
        }
        
        return resultList;
    }
}