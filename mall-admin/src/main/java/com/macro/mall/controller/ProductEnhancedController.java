package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.ProductSearchDto;
import com.macro.mall.model.ProductEnhanced;
import com.macro.mall.service.ProductEnhancedService;
import com.macro.mall.validator.ValidQualityScore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

/**
 * 增强商品管理Controller
 * Created for Product Management Module Enhancement
 */
@Controller
@Api(tags = "ProductEnhancedController")
@Tag(name = "ProductEnhancedController", description = "增强商品管理")
@RequestMapping("/product/enhanced")
@Validated
public class ProductEnhancedController {

    @Autowired
    private ProductEnhancedService productEnhancedService;

    @ApiOperation("创建增强商品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('PRODUCT_CREATOR') or hasRole('ADMIN')")
    public CommonResult<ProductEnhanced> create(@Valid @RequestBody ProductEnhanced product) {
        try {
            int count = productEnhancedService.create(product);
            if (count > 0) {
                return CommonResult.success(product);
            } else {
                return CommonResult.failed("创建商品失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("创建商品失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新增强商品")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('PRODUCT_CREATOR') or hasRole('ADMIN')")
    public CommonResult<ProductEnhanced> update(
            @ApiParam("商品ID") @PathVariable Long id,
            @Valid @RequestBody ProductEnhanced product) {
        try {
            product.setId(id);
            int count = productEnhancedService.update(product);
            if (count > 0) {
                return CommonResult.success(product);
            } else {
                return CommonResult.failed("更新商品失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新商品失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取商品详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ProductEnhanced> getItem(@ApiParam("商品ID") @PathVariable Long id) {
        try {
            ProductEnhanced product = productEnhancedService.getItem(id);
            if (product != null) {
                return CommonResult.success(product);
            } else {
                return CommonResult.failed("商品不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取商品详情失败: " + e.getMessage());
        }
    }

    @ApiOperation("高级搜索商品")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<ProductEnhanced>> search(@Valid @RequestBody ProductSearchDto searchDto) {
        try {
            CommonPage<ProductEnhanced> result = productEnhancedService.search(searchDto);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("搜索商品失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据标签搜索商品")
    @RequestMapping(value = "/search-by-tags", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductEnhanced>> searchByTags(
            @ApiParam("标签列表，逗号分隔") @RequestParam String tags,
            @ApiParam("匹配模式：ALL-全部匹配，ANY-任意匹配") @RequestParam(defaultValue = "ANY") String matchMode,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            List<ProductEnhanced> products = productEnhancedService.searchByTags(tags, matchMode, pageNum, pageSize);
            return CommonResult.success(products);
        } catch (Exception e) {
            return CommonResult.failed("按标签搜索失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新商品质量评分")
    @RequestMapping(value = "/{id}/quality-score", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('QUALITY_MANAGER') or hasRole('ADMIN')")
    public CommonResult updateQualityScore(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("质量评分") @RequestParam @ValidQualityScore BigDecimal qualityScore) {
        try {
            int count = productEnhancedService.updateQualityScore(id, qualityScore);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新质量评分失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新质量评分失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新可持续性评分")
    @RequestMapping(value = "/{id}/sustainability-score", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('SUSTAINABILITY_MANAGER') or hasRole('ADMIN')")
    public CommonResult updateSustainabilityScore(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("可持续性评分") @RequestParam @Min(1) @Max(100) Integer sustainabilityScore) {
        try {
            int count = productEnhancedService.updateSustainabilityScore(id, sustainabilityScore);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新可持续性评分失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新可持续性评分失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新SEO信息")
    @RequestMapping(value = "/{id}/seo", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('SEO_MANAGER') or hasRole('ADMIN')")
    public CommonResult updateSeoInfo(
            @ApiParam("商品ID") @PathVariable Long id,
            @Valid @RequestBody SeoUpdateDto seoDto) {
        try {
            int count = productEnhancedService.updateSeoInfo(id, seoDto);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新SEO信息失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新SEO信息失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新商品标签")
    @RequestMapping(value = "/{id}/tags", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('ADMIN')")
    public CommonResult updateTags(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("标签列表") @RequestBody List<String> tags) {
        try {
            int count = productEnhancedService.updateTags(id, tags);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新商品标签失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("更新商品标签失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取低库存商品列表")
    @RequestMapping(value = "/low-stock", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductEnhanced>> getLowStockProducts(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            List<ProductEnhanced> products = productEnhancedService.getLowStockProducts(pageNum, pageSize);
            return CommonResult.success(products);
        } catch (Exception e) {
            return CommonResult.failed("获取低库存商品列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取缺货商品列表")
    @RequestMapping(value = "/out-of-stock", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductEnhanced>> getOutOfStockProducts(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            List<ProductEnhanced> products = productEnhancedService.getOutOfStockProducts(pageNum, pageSize);
            return CommonResult.success(products);
        } catch (Exception e) {
            return CommonResult.failed("获取缺货商品列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取需要补货的商品列表")
    @RequestMapping(value = "/need-reorder", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductEnhanced>> getNeedReorderProducts(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            List<ProductEnhanced> products = productEnhancedService.getNeedReorderProducts(pageNum, pageSize);
            return CommonResult.success(products);
        } catch (Exception e) {
            return CommonResult.failed("获取需要补货商品列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取商品统计信息")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ProductStatisticsDto> getStatistics() {
        try {
            ProductStatisticsDto statistics = productEnhancedService.getStatistics();
            return CommonResult.success(statistics);
        } catch (Exception e) {
            return CommonResult.failed("获取商品统计信息失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据可持续性评分筛选商品")
    @RequestMapping(value = "/filter-by-sustainability", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductEnhanced>> filterBySustainability(
            @ApiParam("最低评分") @RequestParam @Min(1) Integer minScore,
            @ApiParam("最高评分") @RequestParam(required = false) @Max(100) Integer maxScore,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            List<ProductEnhanced> products = productEnhancedService.filterBySustainability(
                    minScore, maxScore, pageNum, pageSize);
            return CommonResult.success(products);
        } catch (Exception e) {
            return CommonResult.failed("按可持续性评分筛选商品失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据质量评分筛选商品")
    @RequestMapping(value = "/filter-by-quality", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductEnhanced>> filterByQuality(
            @ApiParam("最低评分") @RequestParam @ValidQualityScore BigDecimal minScore,
            @ApiParam("最高评分") @RequestParam(required = false) @ValidQualityScore BigDecimal maxScore,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            List<ProductEnhanced> products = productEnhancedService.filterByQuality(
                    minScore, maxScore, pageNum, pageSize);
            return CommonResult.success(products);
        } catch (Exception e) {
            return CommonResult.failed("按质量评分筛选商品失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除商品")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult delete(@ApiParam("商品ID") @PathVariable Long id) {
        try {
            int count = productEnhancedService.delete(id);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("删除商品失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("删除商品失败: " + e.getMessage());
        }
    }

    /**
     * SEO更新DTO
     */
    public static class SeoUpdateDto {
        private String seoTitle;
        private String seoDescription;
        private String keywords;

        // Getters and setters
        public String getSeoTitle() { return seoTitle; }
        public void setSeoTitle(String seoTitle) { this.seoTitle = seoTitle; }
        public String getSeoDescription() { return seoDescription; }
        public void setSeoDescription(String seoDescription) { this.seoDescription = seoDescription; }
        public String getKeywords() { return keywords; }
        public void setKeywords(String keywords) { this.keywords = keywords; }
    }

    /**
     * 商品统计DTO
     */
    public static class ProductStatisticsDto {
        private Long totalProducts;
        private Long publishedProducts;
        private Long draftProducts;
        private Long lowStockProducts;
        private Long outOfStockProducts;
        private Long needReorderProducts;
        private Double averageQualityScore;
        private Double averageSustainabilityScore;

        // Getters and setters
        public Long getTotalProducts() { return totalProducts; }
        public void setTotalProducts(Long totalProducts) { this.totalProducts = totalProducts; }
        public Long getPublishedProducts() { return publishedProducts; }
        public void setPublishedProducts(Long publishedProducts) { this.publishedProducts = publishedProducts; }
        public Long getDraftProducts() { return draftProducts; }
        public void setDraftProducts(Long draftProducts) { this.draftProducts = draftProducts; }
        public Long getLowStockProducts() { return lowStockProducts; }
        public void setLowStockProducts(Long lowStockProducts) { this.lowStockProducts = lowStockProducts; }
        public Long getOutOfStockProducts() { return outOfStockProducts; }
        public void setOutOfStockProducts(Long outOfStockProducts) { this.outOfStockProducts = outOfStockProducts; }
        public Long getNeedReorderProducts() { return needReorderProducts; }
        public void setNeedReorderProducts(Long needReorderProducts) { this.needReorderProducts = needReorderProducts; }
        public Double getAverageQualityScore() { return averageQualityScore; }
        public void setAverageQualityScore(Double averageQualityScore) { this.averageQualityScore = averageQualityScore; }
        public Double getAverageSustainabilityScore() { return averageSustainabilityScore; }
        public void setAverageSustainabilityScore(Double averageSustainabilityScore) { this.averageSustainabilityScore = averageSustainabilityScore; }
    }
}