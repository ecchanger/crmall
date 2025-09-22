package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.*;
import com.macro.mall.enums.ProductLifecycleStatus;
import com.macro.mall.model.ProductLifecycle;
import com.macro.mall.service.ProductLifecycleService;
import com.macro.mall.validator.ProductLifecycleTransition;
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
import java.util.List;

/**
 * 商品生命周期管理Controller
 * Created for Product Management Module Enhancement
 */
@Controller
@Api(tags = "ProductLifecycleController")
@Tag(name = "ProductLifecycleController", description = "商品生命周期管理")
@RequestMapping("/product/lifecycle")
@Validated
public class ProductLifecycleController {

    @Autowired
    private ProductLifecycleService productLifecycleService;

    @ApiOperation("获取商品生命周期状态")
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ProductLifecycleResponseDto> getProductLifecycle(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            ProductLifecycle lifecycle = productLifecycleService.getCurrentLifecycle(productId);
            if (lifecycle == null) {
                return CommonResult.failed("商品生命周期记录不存在");
            }
            
            ProductLifecycleResponseDto response = convertToResponseDto(lifecycle);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("获取商品生命周期状态失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取商品生命周期历史")
    @RequestMapping(value = "/{productId}/history", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductLifecycleResponseDto>> getProductLifecycleHistory(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            List<ProductLifecycle> history = productLifecycleService.getLifecycleHistory(productId);
            List<ProductLifecycleResponseDto> response = history.stream()
                    .map(this::convertToResponseDto)
                    .collect(java.util.stream.Collectors.toList());
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("获取商品生命周期历史失败: " + e.getMessage());
        }
    }

    @ApiOperation("转换商品生命周期状态")
    @RequestMapping(value = "/transition", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public CommonResult<ProductLifecycleResponseDto> transitionStatus(
            @Valid @ProductLifecycleTransition @RequestBody ProductLifecycleTransitionDto transitionDto) {
        try {
            ProductLifecycle result = productLifecycleService.transitionStatus(
                    transitionDto.getProductId(),
                    transitionDto.getTargetStatusEnum(),
                    transitionDto.getNotes(),
                    getCurrentUserId()
            );
            
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (IllegalStateException e) {
            return CommonResult.failed("状态转换失败: " + e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("转换商品生命周期状态失败: " + e.getMessage());
        }
    }

    @ApiOperation("提交商品审核")
    @RequestMapping(value = "/{productId}/submit-review", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('PRODUCT_CREATOR') or hasRole('ADMIN')")
    public CommonResult<ProductLifecycleResponseDto> submitForReview(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            ProductLifecycle result = productLifecycleService.submitForReview(productId, getCurrentUserId());
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("提交审核失败: " + e.getMessage());
        }
    }

    @ApiOperation("审核通过")
    @RequestMapping(value = "/{productId}/approve", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('REVIEWER') or hasRole('ADMIN')")
    public CommonResult<ProductLifecycleResponseDto> approve(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("审核备注") @RequestParam(required = false) String notes) {
        try {
            ProductLifecycle result = productLifecycleService.approve(productId, getCurrentUserId(), notes);
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("审核通过失败: " + e.getMessage());
        }
    }

    @ApiOperation("审核拒绝")
    @RequestMapping(value = "/{productId}/reject", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('REVIEWER') or hasRole('ADMIN')")
    public CommonResult<ProductLifecycleResponseDto> reject(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("拒绝原因") @RequestParam String notes) {
        try {
            ProductLifecycle result = productLifecycleService.reject(productId, getCurrentUserId(), notes);
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("审核拒绝失败: " + e.getMessage());
        }
    }

    @ApiOperation("发布商品")
    @RequestMapping(value = "/{productId}/publish", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public CommonResult<ProductLifecycleResponseDto> publish(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            ProductLifecycle result = productLifecycleService.publish(productId, getCurrentUserId());
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("发布商品失败: " + e.getMessage());
        }
    }

    @ApiOperation("下架商品进入维护模式")
    @RequestMapping(value = "/{productId}/maintenance", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public CommonResult<ProductLifecycleResponseDto> enterMaintenance(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("维护原因") @RequestParam String reason) {
        try {
            ProductLifecycle result = productLifecycleService.enterMaintenance(productId, getCurrentUserId(), reason);
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("进入维护模式失败: " + e.getMessage());
        }
    }

    @ApiOperation("从维护模式恢复")
    @RequestMapping(value = "/{productId}/resume", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public CommonResult<ProductLifecycleResponseDto> exitMaintenance(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            ProductLifecycle result = productLifecycleService.exitMaintenance(productId, getCurrentUserId());
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("退出维护模式失败: " + e.getMessage());
        }
    }

    @ApiOperation("停产商品")
    @RequestMapping(value = "/{productId}/discontinue", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<ProductLifecycleResponseDto> discontinue(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("停产原因") @RequestParam String reason) {
        try {
            ProductLifecycle result = productLifecycleService.discontinue(productId, getCurrentUserId(), reason);
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("停产商品失败: " + e.getMessage());
        }
    }

    @ApiOperation("归档商品")
    @RequestMapping(value = "/{productId}/archive", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<ProductLifecycleResponseDto> archive(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            ProductLifecycle result = productLifecycleService.archive(productId, getCurrentUserId());
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("归档商品失败: " + e.getMessage());
        }
    }

    @ApiOperation("分配审核员")
    @RequestMapping(value = "/{productId}/assign-reviewer", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<ProductLifecycleResponseDto> assignReviewer(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("审核员ID") @RequestParam Long reviewerId) {
        try {
            ProductLifecycle result = productLifecycleService.assignReviewer(productId, reviewerId, getCurrentUserId());
            ProductLifecycleResponseDto response = convertToResponseDto(result);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("分配审核员失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询商品生命周期列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<ProductLifecycleResponseDto>> list(ProductLifecycleQueryDto queryDto) {
        try {
            // 这里需要实现分页查询逻辑
            // List<ProductLifecycle> lifecycles = productLifecycleService.list(queryDto);
            // CommonPage<ProductLifecycleResponseDto> result = ...
            
            // 示例返回
            return CommonResult.success(new CommonPage<>());
        } catch (Exception e) {
            return CommonResult.failed("查询生命周期列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量状态转换")
    @RequestMapping(value = "/batch-transition", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<List<ProductLifecycleResponseDto>> batchTransition(
            @Valid @RequestBody BatchLifecycleTransitionDto batchDto) {
        try {
            List<ProductLifecycle> results = productLifecycleService.batchTransitionStatus(
                    batchDto.getProductIds(),
                    batchDto.getTargetStatusEnum(),
                    batchDto.getNotes(),
                    getCurrentUserId()
            );
            
            List<ProductLifecycleResponseDto> response = results.stream()
                    .map(this::convertToResponseDto)
                    .collect(java.util.stream.Collectors.toList());
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("批量状态转换失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取下一个可能的状态")
    @RequestMapping(value = "/{productId}/next-states", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ProductLifecycleResponseDto.StatusOptionDto>> getNextPossibleStates(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            List<ProductLifecycleStatus> nextStates = productLifecycleService.getNextPossibleStates(productId);
            List<ProductLifecycleResponseDto.StatusOptionDto> response = nextStates.stream()
                    .map(ProductLifecycleResponseDto.StatusOptionDto::new)
                    .collect(java.util.stream.Collectors.toList());
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("获取下一个可能状态失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取待审核商品数量")
    @RequestMapping(value = "/pending-review-count", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Long> getPendingReviewCount() {
        try {
            long count = productLifecycleService.getPendingReviewCount();
            return CommonResult.success(count);
        } catch (Exception e) {
            return CommonResult.failed("获取待审核商品数量失败: " + e.getMessage());
        }
    }

    // 辅助方法
    private ProductLifecycleResponseDto convertToResponseDto(ProductLifecycle lifecycle) {
        ProductLifecycleResponseDto dto = new ProductLifecycleResponseDto();
        dto.setId(lifecycle.getId());
        dto.setProductId(lifecycle.getProductId());
        dto.setCurrentState(lifecycle.getCurrentState());
        dto.setCurrentStateName(lifecycle.getCurrentStatus().getName());
        dto.setCurrentStateDescription(lifecycle.getCurrentStatus().getDescription());
        dto.setPreviousState(lifecycle.getPreviousState());
        if (lifecycle.getPreviousStatus() != null) {
            dto.setPreviousStateName(lifecycle.getPreviousStatus().getName());
        }
        dto.setStateChangedAt(lifecycle.getStateChangedAt());
        dto.setStateChangedBy(lifecycle.getStateChangedBy());
        dto.setReviewerId(lifecycle.getReviewerId());
        dto.setReviewNotes(lifecycle.getReviewNotes());
        dto.setScheduledPublishDate(lifecycle.getScheduledPublishDate());
        dto.setActualPublishDate(lifecycle.getActualPublishDate());
        dto.setNextReviewDate(lifecycle.getNextReviewDate());
        dto.setLastReviewedDate(lifecycle.getLastReviewedDate());
        dto.setStateDurationMinutes(lifecycle.getStateDurationMinutes());
        dto.setOperationNotes(lifecycle.getOperationNotes());
        dto.setCreatedAt(lifecycle.getCreatedAt());
        dto.setUpdatedAt(lifecycle.getUpdatedAt());
        
        // 设置业务属性
        dto.setCanEdit(lifecycle.getCurrentStatus().allowsEditing());
        dto.setVisibleToUsers(lifecycle.getCurrentStatus().isVisibleToUsers());
        dto.setRequiresImmediateAction(lifecycle.getCurrentStatus().requiresImmediateAction());
        
        // 设置下一个可能的状态
        ProductLifecycleStatus[] nextStates = lifecycle.getCurrentStatus().getNextPossibleStates();
        List<ProductLifecycleResponseDto.StatusOptionDto> nextOptions = java.util.Arrays.stream(nextStates)
                .map(ProductLifecycleResponseDto.StatusOptionDto::new)
                .collect(java.util.stream.Collectors.toList());
        dto.setNextPossibleStates(nextOptions);
        
        return dto;
    }

    // 获取当前用户ID的方法（需要根据实际认证框架实现）
    private Long getCurrentUserId() {
        // 这里应该从SecurityContext或其他认证框架获取当前用户ID
        // 暂时返回示例值
        return 1L;
    }

    /**
     * 批量生命周期转换DTO
     */
    public static class BatchLifecycleTransitionDto {
        private List<Long> productIds;
        private String targetStatus;
        private String notes;

        // Getters and setters
        public List<Long> getProductIds() { return productIds; }
        public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
        public String getTargetStatus() { return targetStatus; }
        public void setTargetStatus(String targetStatus) { this.targetStatus = targetStatus; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        
        public ProductLifecycleStatus getTargetStatusEnum() {
            return ProductLifecycleStatus.fromCode(this.targetStatus);
        }
    }
}