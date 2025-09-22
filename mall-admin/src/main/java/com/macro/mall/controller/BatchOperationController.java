package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.BatchOperationDto;
import com.macro.mall.dto.BatchOperationResultDto;
import com.macro.mall.service.BatchOperationService;
import com.macro.mall.validator.ValidBatchOperation;
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
 * 批量操作Controller
 * Created for Product Management Module Enhancement
 */
@Controller
@Api(tags = "BatchOperationController")
@Tag(name = "BatchOperationController", description = "批量操作管理")
@RequestMapping("/product/batch")
@Validated
public class BatchOperationController {

    @Autowired
    private BatchOperationService batchOperationService;

    @ApiOperation("执行批量操作")
    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('BATCH_OPERATOR') or hasRole('ADMIN')")
    public CommonResult<BatchOperationResultDto> executeBatchOperation(
            @Valid @ValidBatchOperation @RequestBody BatchOperationDto operationDto) {
        try {
            // 设置操作人
            operationDto.setOperatorId(getCurrentUserId());
            
            BatchOperationResultDto result = batchOperationService.executeBatchOperation(operationDto);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("执行批量操作失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取批量操作结果")
    @RequestMapping(value = "/result/{operationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<BatchOperationResultDto> getBatchOperationResult(
            @ApiParam("操作ID") @PathVariable String operationId) {
        try {
            BatchOperationResultDto result = batchOperationService.getBatchOperationResult(operationId);
            if (result != null) {
                return CommonResult.success(result);
            } else {
                return CommonResult.failed("批量操作记录不存在");
            }
        } catch (Exception e) {
            return CommonResult.failed("获取批量操作结果失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取批量操作历史")
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<BatchOperationResultDto>> getBatchOperationHistory(
            @ApiParam("操作类型") @RequestParam(required = false) String operationType,
            @ApiParam("操作人ID") @RequestParam(required = false) Long operatorId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            List<BatchOperationResultDto> history = batchOperationService.getBatchOperationHistory(
                    operationType, operatorId, pageNum, pageSize);
            return CommonResult.success(history);
        } catch (Exception e) {
            return CommonResult.failed("获取批量操作历史失败: " + e.getMessage());
        }
    }

    @ApiOperation("取消批量操作")
    @RequestMapping(value = "/cancel/{operationId}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult cancelBatchOperation(
            @ApiParam("操作ID") @PathVariable String operationId) {
        try {
            boolean success = batchOperationService.cancelBatchOperation(operationId);
            if (success) {
                return CommonResult.success("操作已取消");
            } else {
                return CommonResult.failed("取消操作失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("取消批量操作失败: " + e.getMessage());
        }
    }

    @ApiOperation("重试失败的批量操作")
    @RequestMapping(value = "/retry/{operationId}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('BATCH_OPERATOR') or hasRole('ADMIN')")
    public CommonResult<BatchOperationResultDto> retryBatchOperation(
            @ApiParam("操作ID") @PathVariable String operationId) {
        try {
            BatchOperationResultDto result = batchOperationService.retryBatchOperation(operationId);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("重试批量操作失败: " + e.getMessage());
        }
    }

    @ApiOperation("回滚批量操作")
    @RequestMapping(value = "/rollback/{operationId}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<BatchOperationResultDto> rollbackBatchOperation(
            @ApiParam("操作ID") @PathVariable String operationId,
            @ApiParam("回滚原因") @RequestParam String reason) {
        try {
            BatchOperationResultDto result = batchOperationService.rollbackBatchOperation(operationId, reason);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("回滚批量操作失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量价格更新")
    @RequestMapping(value = "/price-update", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('PRICE_MANAGER') or hasRole('ADMIN')")
    public CommonResult<BatchOperationResultDto> batchPriceUpdate(
            @Valid @RequestBody BatchPriceUpdateDto priceUpdateDto) {
        try {
            BatchOperationDto operationDto = new BatchOperationDto();
            operationDto.setOperationType("PRICE_UPDATE");
            operationDto.setProductIds(priceUpdateDto.getProductIds());
            operationDto.setNewPrice(priceUpdateDto.getNewPrice());
            operationDto.setPriceAdjustmentPercentage(priceUpdateDto.getAdjustmentPercentage());
            operationDto.setPriceAdjustmentType(priceUpdateDto.getAdjustmentType());
            operationDto.setNotes(priceUpdateDto.getNotes());
            operationDto.setOperatorId(getCurrentUserId());
            
            BatchOperationResultDto result = batchOperationService.executeBatchOperation(operationDto);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("批量价格更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量状态更新")
    @RequestMapping(value = "/status-update", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('ADMIN')")
    public CommonResult<BatchOperationResultDto> batchStatusUpdate(
            @Valid @RequestBody BatchStatusUpdateDto statusUpdateDto) {
        try {
            BatchOperationDto operationDto = new BatchOperationDto();
            operationDto.setOperationType("STATUS_UPDATE");
            operationDto.setProductIds(statusUpdateDto.getProductIds());
            operationDto.setPublishStatus(statusUpdateDto.getPublishStatus());
            operationDto.setVerifyStatus(statusUpdateDto.getVerifyStatus());
            operationDto.setNewStatus(statusUpdateDto.getNewStatus());
            operationDto.setRecommendStatus(statusUpdateDto.getRecommendStatus());
            operationDto.setNotes(statusUpdateDto.getNotes());
            operationDto.setOperatorId(getCurrentUserId());
            
            BatchOperationResultDto result = batchOperationService.executeBatchOperation(operationDto);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("批量状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量分类迁移")
    @RequestMapping(value = "/category-migration", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('CATEGORY_MANAGER') or hasRole('ADMIN')")
    public CommonResult<BatchOperationResultDto> batchCategoryMigration(
            @Valid @RequestBody BatchCategoryMigrationDto migrationDto) {
        try {
            BatchOperationDto operationDto = new BatchOperationDto();
            operationDto.setOperationType("CATEGORY_MIGRATION");
            operationDto.setProductIds(migrationDto.getProductIds());
            operationDto.setTargetCategoryId(migrationDto.getTargetCategoryId());
            operationDto.setMigrateAttributes(migrationDto.getMigrateAttributes());
            operationDto.setNotes(migrationDto.getNotes());
            operationDto.setOperatorId(getCurrentUserId());
            
            BatchOperationResultDto result = batchOperationService.executeBatchOperation(operationDto);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("批量分类迁移失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量库存调整")
    @RequestMapping(value = "/inventory-adjustment", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasRole('INVENTORY_MANAGER') or hasRole('ADMIN')")
    public CommonResult<BatchOperationResultDto> batchInventoryAdjustment(
            @Valid @RequestBody BatchInventoryAdjustmentDto adjustmentDto) {
        try {
            BatchOperationDto operationDto = new BatchOperationDto();
            operationDto.setOperationType("INVENTORY_ADJUSTMENT");
            operationDto.setProductIds(adjustmentDto.getProductIds());
            operationDto.setStockAdjustmentQuantity(adjustmentDto.getAdjustmentQuantity());
            operationDto.setStockAdjustmentType(adjustmentDto.getAdjustmentType());
            operationDto.setWarehouseId(adjustmentDto.getWarehouseId());
            operationDto.setAdjustmentReason(adjustmentDto.getReason());
            operationDto.setNotes(adjustmentDto.getNotes());
            operationDto.setOperatorId(getCurrentUserId());
            
            BatchOperationResultDto result = batchOperationService.executeBatchOperation(operationDto);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("批量库存调整失败: " + e.getMessage());
        }
    }

    @ApiOperation("预览批量操作影响")
    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<BatchOperationPreviewDto> previewBatchOperation(
            @Valid @RequestBody BatchOperationDto operationDto) {
        try {
            BatchOperationPreviewDto preview = batchOperationService.previewBatchOperation(operationDto);
            return CommonResult.success(preview);
        } catch (Exception e) {
            return CommonResult.failed("预览批量操作失败: " + e.getMessage());
        }
    }

    // 获取当前用户ID的方法
    private Long getCurrentUserId() {
        // 这里应该从SecurityContext或其他认证框架获取当前用户ID
        return 1L;
    }

    // DTO类定义
    public static class BatchPriceUpdateDto {
        private List<Long> productIds;
        private java.math.BigDecimal newPrice;
        private java.math.BigDecimal adjustmentPercentage;
        private String adjustmentType;
        private String notes;

        // Getters and setters
        public List<Long> getProductIds() { return productIds; }
        public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
        public java.math.BigDecimal getNewPrice() { return newPrice; }
        public void setNewPrice(java.math.BigDecimal newPrice) { this.newPrice = newPrice; }
        public java.math.BigDecimal getAdjustmentPercentage() { return adjustmentPercentage; }
        public void setAdjustmentPercentage(java.math.BigDecimal adjustmentPercentage) { this.adjustmentPercentage = adjustmentPercentage; }
        public String getAdjustmentType() { return adjustmentType; }
        public void setAdjustmentType(String adjustmentType) { this.adjustmentType = adjustmentType; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class BatchStatusUpdateDto {
        private List<Long> productIds;
        private Integer publishStatus;
        private Integer verifyStatus;
        private Integer newStatus;
        private Integer recommendStatus;
        private String notes;

        // Getters and setters
        public List<Long> getProductIds() { return productIds; }
        public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
        public Integer getPublishStatus() { return publishStatus; }
        public void setPublishStatus(Integer publishStatus) { this.publishStatus = publishStatus; }
        public Integer getVerifyStatus() { return verifyStatus; }
        public void setVerifyStatus(Integer verifyStatus) { this.verifyStatus = verifyStatus; }
        public Integer getNewStatus() { return newStatus; }
        public void setNewStatus(Integer newStatus) { this.newStatus = newStatus; }
        public Integer getRecommendStatus() { return recommendStatus; }
        public void setRecommendStatus(Integer recommendStatus) { this.recommendStatus = recommendStatus; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class BatchCategoryMigrationDto {
        private List<Long> productIds;
        private Long targetCategoryId;
        private Boolean migrateAttributes;
        private String notes;

        // Getters and setters
        public List<Long> getProductIds() { return productIds; }
        public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
        public Long getTargetCategoryId() { return targetCategoryId; }
        public void setTargetCategoryId(Long targetCategoryId) { this.targetCategoryId = targetCategoryId; }
        public Boolean getMigrateAttributes() { return migrateAttributes; }
        public void setMigrateAttributes(Boolean migrateAttributes) { this.migrateAttributes = migrateAttributes; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class BatchInventoryAdjustmentDto {
        private List<Long> productIds;
        private Integer adjustmentQuantity;
        private String adjustmentType;
        private Long warehouseId;
        private String reason;
        private String notes;

        // Getters and setters
        public List<Long> getProductIds() { return productIds; }
        public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
        public Integer getAdjustmentQuantity() { return adjustmentQuantity; }
        public void setAdjustmentQuantity(Integer adjustmentQuantity) { this.adjustmentQuantity = adjustmentQuantity; }
        public String getAdjustmentType() { return adjustmentType; }
        public void setAdjustmentType(String adjustmentType) { this.adjustmentType = adjustmentType; }
        public Long getWarehouseId() { return warehouseId; }
        public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class BatchOperationPreviewDto {
        private Integer affectedProductCount;
        private Integer estimatedExecutionTimeSeconds;
        private Boolean requiresApproval;
        private Boolean highRiskOperation;
        private List<String> warnings;
        private List<String> errors;

        // Getters and setters
        public Integer getAffectedProductCount() { return affectedProductCount; }
        public void setAffectedProductCount(Integer affectedProductCount) { this.affectedProductCount = affectedProductCount; }
        public Integer getEstimatedExecutionTimeSeconds() { return estimatedExecutionTimeSeconds; }
        public void setEstimatedExecutionTimeSeconds(Integer estimatedExecutionTimeSeconds) { this.estimatedExecutionTimeSeconds = estimatedExecutionTimeSeconds; }
        public Boolean getRequiresApproval() { return requiresApproval; }
        public void setRequiresApproval(Boolean requiresApproval) { this.requiresApproval = requiresApproval; }
        public Boolean getHighRiskOperation() { return highRiskOperation; }
        public void setHighRiskOperation(Boolean highRiskOperation) { this.highRiskOperation = highRiskOperation; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
    }
}