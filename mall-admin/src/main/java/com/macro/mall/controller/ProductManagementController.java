package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.ProductManagementDashboard;
import com.macro.mall.dto.ProductManagementParam;
import com.macro.mall.dto.ProductManagementQueryParam;
import com.macro.mall.dto.ProductManagementResult;
import com.macro.mall.model.ProductManagement;
import com.macro.mall.service.ProductManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "ProductManagementController")
@Tag(name = "ProductManagementController", description = "增强商品管理")
@RequestMapping("/productManagement")
public class ProductManagementController {
    @Autowired
    private ProductManagementService productManagementService;

    @ApiOperation("创建商品管理任务")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody ProductManagementParam param) {
        int count = productManagementService.create(param);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("更新商品管理任务")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody ProductManagementParam param) {
        int count = productManagementService.update(id, param);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("获取商品管理任务详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ProductManagementResult> getById(@PathVariable Long id) {
        ProductManagementResult result = productManagementService.getById(id);
        return CommonResult.success(result);
    }

    @ApiOperation("分页查询商品管理任务")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<ProductManagement>> getList(ProductManagementQueryParam param,
                                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<ProductManagement> list = productManagementService.list(param, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取管理仪表板数据")
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ProductManagementDashboard> getDashboard(@RequestParam(value = "assignee", required = false) String assignee) {
        ProductManagementDashboard dashboard = productManagementService.getDashboard(assignee);
        return CommonResult.success(dashboard);
    }

    @ApiOperation("批量分配负责人")
    @RequestMapping(value = "/batch/assign", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult batchAssign(@RequestParam("ids") List<Long> ids,
                                    @RequestParam("assignee") String assignee) {
        int count = productManagementService.batchAssign(ids, assignee);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量更新优先级")
    @RequestMapping(value = "/batch/priority", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult batchUpdatePriority(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("priority") Integer priority) {
        int count = productManagementService.batchUpdatePriority(ids, priority);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量更新状态")
    @RequestMapping(value = "/batch/status", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult batchUpdateStatus(@RequestParam("ids") List<Long> ids,
                                          @RequestParam("status") Integer status) {
        int count = productManagementService.batchUpdateStatus(ids, status);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("获取逾期任务")
    @RequestMapping(value = "/overdue", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductManagement>> getOverdueItems() {
        List<ProductManagement> overdueItems = productManagementService.getOverdueItems();
        return CommonResult.success(overdueItems);
    }

    @ApiOperation("删除商品管理任务")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = productManagementService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
