package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.GmsGoodsParam;
import com.macro.mall.dto.GmsGoodsQueryParam;
import com.macro.mall.model.GmsGoods;
import com.macro.mall.service.GmsGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理Controller
 */
@Controller
@Api(tags = "GmsGoodsController")
@Tag(name = "GmsGoodsController", description = "商品管理")
@RequestMapping("/goods")
public class GmsGoodsController {
    @Autowired
    private GmsGoodsService goodsService;

    @ApiOperation("创建商品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@Validated @RequestBody GmsGoodsParam goodsParam) {
        int count = goodsService.create(goodsParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据ID获取商品详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<GmsGoods> getItem(@PathVariable Long id) {
        GmsGoods goods = goodsService.getItem(id);
        return CommonResult.success(goods);
    }

    @ApiOperation("更新商品")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @Validated @RequestBody GmsGoodsParam goodsParam) {
        int count = goodsService.update(id, goodsParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除商品")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = goodsService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量删除商品")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteBatch(@RequestParam("ids") List<Long> ids) {
        int count = goodsService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页查询商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<GmsGoods>> list(GmsGoodsQueryParam queryParam,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<GmsGoods> goodsList = goodsService.list(queryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(goodsList));
    }

    @ApiOperation("根据商品名称或编码模糊查询")
    @RequestMapping(value = "/simpleList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<GmsGoods>> simpleList(@RequestParam(value = "keyword", required = false) String keyword) {
        List<GmsGoods> goodsList = goodsService.list(keyword);
        return CommonResult.success(goodsList);
    }

    @ApiOperation("批量修改上架状态")
    @RequestMapping(value = "/update/publishStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("publishStatus") Integer publishStatus) {
        int count = goodsService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量修改新品状态")
    @RequestMapping(value = "/update/newStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateNewStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("newStatus") Integer newStatus) {
        int count = goodsService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量修改推荐状态")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                              @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = goodsService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量修改审核状态")
    @RequestMapping(value = "/update/verifyStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateVerifyStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("verifyStatus") Integer verifyStatus) {
        int count = goodsService.updateVerifyStatus(ids, verifyStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量修改删除状态")
    @RequestMapping(value = "/update/deleteStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = goodsService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
