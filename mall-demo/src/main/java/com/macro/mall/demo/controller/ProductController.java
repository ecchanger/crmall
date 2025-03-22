package com.macro.mall.demo.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.demo.model.Product;
import com.macro.mall.demo.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "ProductController", description = "商品管理")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @ApiOperation("创建商品")
    @PostMapping("/create")
    public CommonResult<Product> create(@Validated @RequestBody Product product) {
        Product created = productService.create(product);
        return CommonResult.success(created);
    }

    @ApiOperation("更新商品")
    @PostMapping("/update/{id}")
    public CommonResult<Product> update(@PathVariable Long id, @Validated @RequestBody Product product) {
        Product updated = productService.update(id, product);
        return CommonResult.success(updated);
    }

    @ApiOperation("删除商品")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        productService.delete(id);
        return CommonResult.success(null);
    }

    @ApiOperation("获取商品详情")
    @GetMapping("/{id}")
    public CommonResult<Product> getItem(@PathVariable Long id) {
        Product product = productService.getItem(id);
        return CommonResult.success(product);
    }

    @ApiOperation("获取商品列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<Product>> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<Product> productList = productService.list(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @ApiOperation("修改上架状态")
    @PostMapping("/update/publishStatus")
    public CommonResult updatePublishStatus(
            @RequestParam Long id,
            @RequestParam Integer status) {
        int count = productService.updatePublishStatus(id, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
