package com.macro.mall.validator;

import com.macro.mall.dto.BatchOperationDto;
import com.macro.mall.enums.BatchOperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Batch Operation Validator Tests")
class BatchOperationValidatorTest {

    private BatchOperationValidator validator;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        validator = new BatchOperationValidator();
        context = mock(ConstraintValidatorContext.class);
        violationBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
    }

    @Test
    @DisplayName("Should validate valid batch update operation")
    void shouldValidateValidBatchUpdateOperation() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        
        boolean result = validator.isValid(dto, context);
        
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    @DisplayName("Should validate valid batch delete operation")
    void shouldValidateValidBatchDeleteOperation() {
        BatchOperationDto dto = createValidBatchDeleteDto();
        
        boolean result = validator.isValid(dto, context);
        
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    @DisplayName("Should reject null batch operation")
    void shouldRejectNullBatchOperation() {
        boolean result = validator.isValid(null, context);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Should reject empty product IDs list")
    void shouldRejectEmptyProductIdsList() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setProductIds(Collections.emptyList());
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Product IDs list cannot be empty");
    }

    @Test
    @DisplayName("Should reject null product IDs list")
    void shouldRejectNullProductIdsList() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setProductIds(null);
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Product IDs list cannot be empty");
    }

    @Test
    @DisplayName("Should reject batch operation exceeding maximum size")
    void shouldRejectBatchOperationExceedingMaximumSize() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        // Create a list with more than 1000 items (assuming max batch size is 1000)
        Long[] productIds = new Long[1001];
        for (int i = 0; i < 1001; i++) {
            productIds[i] = (long) (i + 1);
        }
        dto.setProductIds(Arrays.asList(productIds));
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Batch size cannot exceed 1000 items");
    }

    @Test
    @DisplayName("Should reject batch update without update fields")
    void shouldRejectBatchUpdateWithoutUpdateFields() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setUpdateFields(Collections.emptyMap());
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Update fields cannot be empty for UPDATE operation");
    }

    @Test
    @DisplayName("Should reject batch update with null update fields")
    void shouldRejectBatchUpdateWithNullUpdateFields() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setUpdateFields(null);
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Update fields cannot be empty for UPDATE operation");
    }

    @Test
    @DisplayName("Should accept batch delete without update fields")
    void shouldAcceptBatchDeleteWithoutUpdateFields() {
        BatchOperationDto dto = createValidBatchDeleteDto();
        dto.setUpdateFields(null);
        
        boolean result = validator.isValid(dto, context);
        
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    @DisplayName("Should reject batch operation with duplicate product IDs")
    void shouldRejectBatchOperationWithDuplicateProductIds() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setProductIds(Arrays.asList(1L, 2L, 3L, 2L, 4L)); // Duplicate ID: 2L
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Product IDs list contains duplicates");
    }

    @Test
    @DisplayName("Should reject batch operation with null product ID")
    void shouldRejectBatchOperationWithNullProductId() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setProductIds(Arrays.asList(1L, 2L, null, 4L));
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Product IDs cannot contain null values");
    }

    @Test
    @DisplayName("Should reject batch operation with invalid product ID")
    void shouldRejectBatchOperationWithInvalidProductId() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setProductIds(Arrays.asList(1L, 2L, 0L, 4L)); // Invalid ID: 0L
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Product IDs must be positive numbers");
    }

    @Test
    @DisplayName("Should reject batch operation with negative product ID")
    void shouldRejectBatchOperationWithNegativeProductId() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setProductIds(Arrays.asList(1L, 2L, -3L, 4L)); // Negative ID: -3L
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Product IDs must be positive numbers");
    }

    @Test
    @DisplayName("Should validate batch status update operation")
    void shouldValidateBatchStatusUpdateOperation() {
        BatchOperationDto dto = new BatchOperationDto();
        dto.setOperationType(BatchOperationType.STATUS_UPDATE);
        dto.setProductIds(Arrays.asList(1L, 2L, 3L));
        dto.setUpdateFields(Collections.singletonMap("publishStatus", "PUBLISHED"));
        
        boolean result = validator.isValid(dto, context);
        
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    @DisplayName("Should validate batch price update operation")
    void shouldValidateBatchPriceUpdateOperation() {
        BatchOperationDto dto = new BatchOperationDto();
        dto.setOperationType(BatchOperationType.PRICE_UPDATE);
        dto.setProductIds(Arrays.asList(1L, 2L, 3L));
        dto.setUpdateFields(Collections.singletonMap("price", "29.99"));
        
        boolean result = validator.isValid(dto, context);
        
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    @DisplayName("Should validate batch category assignment operation")
    void shouldValidateBatchCategoryAssignmentOperation() {
        BatchOperationDto dto = new BatchOperationDto();
        dto.setOperationType(BatchOperationType.CATEGORY_ASSIGNMENT);
        dto.setProductIds(Arrays.asList(1L, 2L, 3L));
        dto.setUpdateFields(Collections.singletonMap("productCategoryId", "5"));
        
        boolean result = validator.isValid(dto, context);
        
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    @DisplayName("Should handle unknown operation type gracefully")
    void shouldHandleUnknownOperationTypeGracefully() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.setOperationType(null);
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Operation type cannot be null");
    }

    @Test
    @DisplayName("Should reject batch operation with invalid update field values")
    void shouldRejectBatchOperationWithInvalidUpdateFieldValues() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.getUpdateFields().put("price", "invalid_price");
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate(contains("Invalid value for field"));
    }

    @Test
    @DisplayName("Should reject batch operation with empty update field value")
    void shouldRejectBatchOperationWithEmptyUpdateFieldValue() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.getUpdateFields().put("name", "");
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Update field values cannot be empty");
    }

    @Test
    @DisplayName("Should reject batch operation with null update field value")
    void shouldRejectBatchOperationWithNullUpdateFieldValue() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.getUpdateFields().put("description", null);
        
        boolean result = validator.isValid(dto, context);
        
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Update field values cannot be null");
    }

    @Test
    @DisplayName("Should validate batch operation with maximum allowed products")
    void shouldValidateBatchOperationWithMaximumAllowedProducts() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        // Create exactly 1000 product IDs (assuming max is 1000)
        Long[] productIds = new Long[1000];
        for (int i = 0; i < 1000; i++) {
            productIds[i] = (long) (i + 1);
        }
        dto.setProductIds(Arrays.asList(productIds));
        
        boolean result = validator.isValid(dto, context);
        
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    @DisplayName("Should validate complex batch update with multiple fields")
    void shouldValidateComplexBatchUpdateWithMultipleFields() {
        BatchOperationDto dto = createValidBatchUpdateDto();
        dto.getUpdateFields().put("price", "29.99");
        dto.getUpdateFields().put("publishStatus", "PUBLISHED");
        dto.getUpdateFields().put("description", "Updated description");
        
        boolean result = validator.isValid(dto, context);
        
        assertTrue(result);
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }

    // Helper methods

    private BatchOperationDto createValidBatchUpdateDto() {
        BatchOperationDto dto = new BatchOperationDto();
        dto.setOperationType(BatchOperationType.UPDATE);
        dto.setProductIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        dto.setUpdateFields(Collections.singletonMap("name", "Updated Product Name"));
        return dto;
    }

    private BatchOperationDto createValidBatchDeleteDto() {
        BatchOperationDto dto = new BatchOperationDto();
        dto.setOperationType(BatchOperationType.DELETE);
        dto.setProductIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));
        return dto;
    }
}