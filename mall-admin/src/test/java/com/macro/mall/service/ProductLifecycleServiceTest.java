package com.macro.mall.service;

import com.macro.mall.enums.ProductLifecycleStatus;
import com.macro.mall.model.ProductLifecycle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * 商品生命周期服务测试
 * Created for Product Management Module Enhancement
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@SpringJUnitConfig
@Transactional
public class ProductLifecycleServiceTest {

    private ProductLifecycleService productLifecycleService;
    
    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_OPERATOR_ID = 1L;
    private static final Long TEST_REVIEWER_ID = 2L;

    @BeforeEach
    void setUp() {
        // Initialize test data
    }

    @Test
    void testCreateLifecycle() {
        // Given
        Long productId = TEST_PRODUCT_ID;

        // When
        ProductLifecycle lifecycle = productLifecycleService.createLifecycle(productId);

        // Then
        assertNotNull(lifecycle);
        assertEquals(productId, lifecycle.getProductId());
        assertEquals(ProductLifecycleStatus.DRAFT.getCode(), lifecycle.getCurrentState());
        assertNotNull(lifecycle.getCreatedAt());
    }

    @Test
    void testTransitionFromDraftToSubmitted() {
        // Given
        ProductLifecycle lifecycle = createTestLifecycle(ProductLifecycleStatus.DRAFT);

        // When
        ProductLifecycle result = productLifecycleService.transitionStatus(
                TEST_PRODUCT_ID, ProductLifecycleStatus.SUBMITTED, "Ready for review", TEST_OPERATOR_ID);

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.SUBMITTED.getCode(), result.getCurrentState());
        assertEquals(ProductLifecycleStatus.DRAFT.getCode(), result.getPreviousState());
        assertEquals("Ready for review", result.getOperationNotes());
    }

    @Test
    void testInvalidTransition() {
        // Given
        ProductLifecycle lifecycle = createTestLifecycle(ProductLifecycleStatus.DRAFT);

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            productLifecycleService.transitionStatus(
                    TEST_PRODUCT_ID, ProductLifecycleStatus.PUBLISHED, "Invalid transition", TEST_OPERATOR_ID);
        });
    }

    @Test
    void testSubmitForReview() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.DRAFT);

        // When
        ProductLifecycle result = productLifecycleService.submitForReview(TEST_PRODUCT_ID, TEST_OPERATOR_ID);

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.SUBMITTED.getCode(), result.getCurrentState());
    }

    @Test
    void testApprove() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.UNDER_REVIEW);

        // When
        ProductLifecycle result = productLifecycleService.approve(TEST_PRODUCT_ID, TEST_REVIEWER_ID, "Approved");

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.APPROVED.getCode(), result.getCurrentState());
        assertEquals(TEST_REVIEWER_ID, result.getReviewerId());
        assertEquals("Approved", result.getReviewNotes());
    }

    @Test
    void testReject() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.UNDER_REVIEW);

        // When
        ProductLifecycle result = productLifecycleService.reject(TEST_PRODUCT_ID, TEST_REVIEWER_ID, "Needs improvement");

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.REJECTED.getCode(), result.getCurrentState());
        assertEquals("Needs improvement", result.getReviewNotes());
    }

    @Test
    void testPublish() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.APPROVED);

        // When
        ProductLifecycle result = productLifecycleService.publish(TEST_PRODUCT_ID, TEST_OPERATOR_ID);

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.PUBLISHED.getCode(), result.getCurrentState());
        assertNotNull(result.getActualPublishDate());
    }

    @Test
    void testEnterMaintenance() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.PUBLISHED);

        // When
        ProductLifecycle result = productLifecycleService.enterMaintenance(
                TEST_PRODUCT_ID, TEST_OPERATOR_ID, "System maintenance");

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.UNDER_MAINTENANCE.getCode(), result.getCurrentState());
        assertEquals("System maintenance", result.getOperationNotes());
    }

    @Test
    void testExitMaintenance() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.UNDER_MAINTENANCE);

        // When
        ProductLifecycle result = productLifecycleService.exitMaintenance(TEST_PRODUCT_ID, TEST_OPERATOR_ID);

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.PUBLISHED.getCode(), result.getCurrentState());
    }

    @Test
    void testDiscontinue() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.PUBLISHED);

        // When
        ProductLifecycle result = productLifecycleService.discontinue(
                TEST_PRODUCT_ID, TEST_OPERATOR_ID, "End of life");

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.DISCONTINUED.getCode(), result.getCurrentState());
        assertEquals("End of life", result.getOperationNotes());
    }

    @Test
    void testArchive() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.DISCONTINUED);

        // When
        ProductLifecycle result = productLifecycleService.archive(TEST_PRODUCT_ID, TEST_OPERATOR_ID);

        // Then
        assertNotNull(result);
        assertEquals(ProductLifecycleStatus.ARCHIVED.getCode(), result.getCurrentState());
    }

    @Test
    void testBatchTransitionStatus() {
        // Given
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);
        // Setup multiple products in draft state

        // When
        List<ProductLifecycle> results = productLifecycleService.batchTransitionStatus(
                productIds, ProductLifecycleStatus.SUBMITTED, "Batch submission", TEST_OPERATOR_ID);

        // Then
        assertNotNull(results);
        assertEquals(productIds.size(), results.size());
        results.forEach(result -> {
            assertEquals(ProductLifecycleStatus.SUBMITTED.getCode(), result.getCurrentState());
        });
    }

    @Test
    void testCanTransition() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.DRAFT);

        // When & Then
        assertTrue(productLifecycleService.canTransition(TEST_PRODUCT_ID, ProductLifecycleStatus.SUBMITTED));
        assertFalse(productLifecycleService.canTransition(TEST_PRODUCT_ID, ProductLifecycleStatus.PUBLISHED));
    }

    @Test
    void testGetNextPossibleStates() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.DRAFT);

        // When
        List<ProductLifecycleStatus> nextStates = productLifecycleService.getNextPossibleStates(TEST_PRODUCT_ID);

        // Then
        assertNotNull(nextStates);
        assertEquals(1, nextStates.size());
        assertEquals(ProductLifecycleStatus.SUBMITTED, nextStates.get(0));
    }

    @Test
    void testGetLifecycleHistory() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.DRAFT);
        productLifecycleService.transitionStatus(
                TEST_PRODUCT_ID, ProductLifecycleStatus.SUBMITTED, "Test", TEST_OPERATOR_ID);

        // When
        List<ProductLifecycle> history = productLifecycleService.getLifecycleHistory(TEST_PRODUCT_ID);

        // Then
        assertNotNull(history);
        assertTrue(history.size() >= 2);
    }

    @Test
    void testAssignReviewer() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.SUBMITTED);

        // When
        ProductLifecycle result = productLifecycleService.assignReviewer(
                TEST_PRODUCT_ID, TEST_REVIEWER_ID, TEST_OPERATOR_ID);

        // Then
        assertNotNull(result);
        assertEquals(TEST_REVIEWER_ID, result.getReviewerId());
        assertEquals(ProductLifecycleStatus.UNDER_REVIEW.getCode(), result.getCurrentState());
    }

    @Test
    void testGetPendingReviewCount() {
        // Given
        createTestLifecycle(ProductLifecycleStatus.UNDER_REVIEW);

        // When
        long count = productLifecycleService.getPendingReviewCount();

        // Then
        assertTrue(count >= 1);
    }

    @Test
    void testProcessExpiredReviews() {
        // Given
        // Create expired review records

        // When
        assertDoesNotThrow(() -> {
            productLifecycleService.processExpiredReviews();
        });

        // Then
        // Verify expired reviews are handled
    }

    // Helper methods
    private ProductLifecycle createTestLifecycle(ProductLifecycleStatus status) {
        ProductLifecycle lifecycle = new ProductLifecycle(TEST_PRODUCT_ID, status, TEST_OPERATOR_ID);
        // In real implementation, this would be saved to database
        return lifecycle;
    }
}