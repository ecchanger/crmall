package com.macro.mall.service;

import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.model.PmsProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品管理服务测试类
 * Created by macro on 2024/1/1.
 */
@SpringBootTest
@ActiveProfiles("test")
public class PmsProductServiceTest {

    @Autowired
    private PmsProductService productService;

    @Test
    public void testGetProduct() {
        // 测试获取商品详情
        Long productId = 1L;
        PmsProduct product = productService.getProduct(productId);
        
        if (product != null) {
            assertNotNull(product.getId());
            assertNotNull(product.getName());
            System.out.println("商品名称: " + product.getName());
        } else {
            System.out.println("商品不存在，ID: " + productId);
        }
    }

    @Test
    public void testGetProductStatistics() {
        // 测试获取商品统计信息
        Map<String, Object> statistics = productService.getProductStatistics();
        
        assertNotNull(statistics);
        assertTrue(statistics.containsKey("totalCount"));
        assertTrue(statistics.containsKey("publishedCount"));
        assertTrue(statistics.containsKey("pendingCount"));
        assertTrue(statistics.containsKey("lowStockCount"));
        
        System.out.println("商品统计信息:");
        System.out.println("总商品数: " + statistics.get("totalCount"));
        System.out.println("已上架商品数: " + statistics.get("publishedCount"));
        System.out.println("待审核商品数: " + statistics.get("pendingCount"));
        System.out.println("库存预警商品数: " + statistics.get("lowStockCount"));
    }

    @Test
    public void testGetStockWarningList() {
        // 测试获取库存预警列表
        List<PmsProduct> warningList = productService.getStockWarningList(10, 5, 1);
        
        assertNotNull(warningList);
        System.out.println("库存预警商品数量: " + warningList.size());
        
        for (PmsProduct product : warningList) {
            System.out.println("商品: " + product.getName() + ", 库存: " + product.getStock());
        }
    }

    @Test
    public void testAdvancedSearch() {
        // 测试高级搜索
        PmsProductQueryParam queryParam = new PmsProductQueryParam();
        queryParam.setPublishStatus(1); // 只查询已上架商品
        
        List<PmsProduct> searchResult = productService.advancedSearch(
                queryParam, 
                new BigDecimal("100"), // 最低价格
                new BigDecimal("1000"), // 最高价格
                1, // 推荐状态
                null, // 新品状态
                10, // 页面大小
                1 // 页码
        );
        
        assertNotNull(searchResult);
        System.out.println("高级搜索结果数量: " + searchResult.size());
        
        for (PmsProduct product : searchResult) {
            System.out.println("商品: " + product.getName() + 
                             ", 价格: " + product.getPrice() + 
                             ", 推荐状态: " + product.getRecommandStatus());
        }
    }
}
