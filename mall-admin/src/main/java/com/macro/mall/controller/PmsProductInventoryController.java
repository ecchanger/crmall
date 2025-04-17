package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.PmsProductInventoryParam;
import com.macro.mall.dto.PmsProductInventoryResult;
import com.macro.mall.model.PmsProductInventoryLog;
import com.macro.mall.service.PmsProductInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品库存管理Controller
 */
@Controller
@Api(tags = "PmsProductInventoryController")
@Tag(name = "PmsProductInventoryController", description = "商品库存管理")
@RequestMapping("/inventory")
public class PmsProductInventoryController {
    @Autowired
    private PmsProductInventoryService inventoryService;

    @ApiOperation("获取商品库存信息")
    @RequestMapping(value = "/info/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsProductInventoryResult> getInventoryInfo(@PathVariable Long productId) {
        PmsProductInventoryResult result = inventoryService.getInventoryInfo(productId);
        return CommonResult.success(result);
    }

    @ApiOperation("更新商品库存")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateInventory(@Validated @RequestBody PmsProductInventoryParam inventoryParam) {
        int count = inventoryService.updateInventory(inventoryParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量更新商品库存")
    @RequestMapping(value = "/update/batch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult batchUpdateInventory(@Validated @RequestBody List<PmsProductInventoryParam> inventoryParamList) {
        int count = inventoryService.batchUpdateInventory(inventoryParamList);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("获取库存变动记录")
    @RequestMapping(value = "/logs", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProductInventoryLog>> getInventoryLogs(
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "skuId", required = false) Long skuId,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductInventoryLog> logList = inventoryService.getInventoryLogs(productId, skuId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(logList));
    }

    @ApiOperation("获取库存预警商品")
    @RequestMapping(value = "/lowStock", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProductInventoryResult>> getLowStockProducts(
            @RequestParam(value = "threshold", defaultValue = "10") Integer threshold,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductInventoryResult> resultList = inventoryService.getLowStockProducts(threshold, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resultList));
    }
}