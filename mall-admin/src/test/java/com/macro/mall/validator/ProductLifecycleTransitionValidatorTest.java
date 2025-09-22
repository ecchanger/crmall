package com.macro.mall.validator;

import com.macro.mall.dto.ProductLifecycleTransitionDto;
import com.macro.mall.enums.ProductLifecycleStatus;
import com.macro.mall.model.ProductLifecycle;
import com.macro.mall.service.ProductLifecycleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 商品生命周期状态转换验证器测试
 * Created for Product Management Module Enhancement
 */
@ExtendWith(MockitoExtension.class)
public class ProductLifecycleTransitionValidatorTest {

    @Mock
    private ProductLifecycleService productLifecycleService;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @InjectMocks
    private ProductLifecycleTransitionValidator validator;

    private ProductLifecycleTransitionDto validDto;
    private ProductLifecycle currentLifecycle;

    @BeforeEach
    void setUp() {
        // Initialize annotation
        ProductLifecycleTransition annotation = mock(ProductLifecycleTransition.class);
        when(annotation.allowForceTransition()).thenReturn(false);
        when(annotation.requiredRoles()).thenReturn(new String[]{});
        validator.initialize(annotation);

        // Setup valid DTO
        validDto = new ProductLifecycleTransitionDto();
        validDto.setProductId(1L);
        validDto.setTargetStatus(ProductLifecycleStatus.SUBMITTED.getCode());
        validDto.setNotes("Test transition");

        // Setup current lifecycle
        currentLifecycle = new ProductLifecycle(1L, ProductLifecycleStatus.DRAFT, 1L);

        // Setup context mock
        when(context.buildConstraintViolationWithTemplate(any(String.class))).thenReturn(violationBuilder);
    }

    @Test
    void testValidTransition() {
        // Given
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(currentLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertTrue(result);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    void testInvalidTransition() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.PUBLISHED.getCode()); // Invalid transition from DRAFT
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(currentLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(contains("不能从状态"));
    }

    @Test
    void testNullDto() {
        // When
        boolean result = validator.isValid(null, context);

        // Then
        assertTrue(result); // null values handled by @NotNull
    }

    @Test
    void testInvalidTargetStatus() {
        // Given
        validDto.setTargetStatus("INVALID_STATUS");

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("无效的目标状态代码");
    }

    @Test
    void testNewProductValidation() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.PUBLISHED.getCode()); // Invalid for new product
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(null); // No existing lifecycle

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("新产品只能设置为草稿状态");
    }

    @Test
    void testNewProductDraftStatus() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.DRAFT.getCode());
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(null);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertTrue(result);
    }

    @Test
    void testReviewTransitionRequiresReviewer() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.UNDER_REVIEW.getCode());
        validDto.setReviewerId(null); // Missing reviewer
        ProductLifecycle submittedLifecycle = new ProductLifecycle(1L, ProductLifecycleStatus.SUBMITTED, 1L);
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(submittedLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("转换到审核状态需要指定审核员");
    }

    @Test
    void testReviewTransitionWithReviewer() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.UNDER_REVIEW.getCode());
        validDto.setReviewerId(2L);
        ProductLifecycle submittedLifecycle = new ProductLifecycle(1L, ProductLifecycleStatus.SUBMITTED, 1L);
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(submittedLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertTrue(result);
    }

    @Test
    void testApprovalRequiresNotes() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.APPROVED.getCode());
        validDto.setNotes(null); // Missing notes
        ProductLifecycle reviewLifecycle = new ProductLifecycle(1L, ProductLifecycleStatus.UNDER_REVIEW, 1L);
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(reviewLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("审核操作需要填写审核备注");
    }

    @Test
    void testRejectionRequiresNotes() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.REJECTED.getCode());
        validDto.setNotes(""); // Empty notes
        ProductLifecycle reviewLifecycle = new ProductLifecycle(1L, ProductLifecycleStatus.UNDER_REVIEW, 1L);
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(reviewLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("审核操作需要填写审核备注");
    }

    @Test
    void testArchiveFromNonDiscontinued() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.ARCHIVED.getCode());
        ProductLifecycle publishedLifecycle = new ProductLifecycle(1L, ProductLifecycleStatus.PUBLISHED, 1L);
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(publishedLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("只有已停产的商品才能归档");
    }

    @Test
    void testArchiveFromDiscontinued() {
        // Given
        validDto.setTargetStatus(ProductLifecycleStatus.ARCHIVED.getCode());
        ProductLifecycle discontinuedLifecycle = new ProductLifecycle(1L, ProductLifecycleStatus.DISCONTINUED, 1L);
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(discontinuedLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertTrue(result);
    }

    @Test
    void testForceTransitionSkipsValidation() {
        // Given
        ProductLifecycleTransition annotation = mock(ProductLifecycleTransition.class);
        when(annotation.allowForceTransition()).thenReturn(true);
        when(annotation.requiredRoles()).thenReturn(new String[]{});
        validator.initialize(annotation);

        validDto.setTargetStatus(ProductLifecycleStatus.PUBLISHED.getCode()); // Invalid transition
        validDto.setForceTransition(true);
        when(productLifecycleService.getCurrentLifecycle(1L)).thenReturn(currentLifecycle);

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertTrue(result); // Should pass due to force transition
    }

    @Test
    void testServiceException() {
        // Given
        when(productLifecycleService.getCurrentLifecycle(1L)).thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = validator.isValid(validDto, context);

        // Then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate(contains("状态转换验证失败"));
    }
}