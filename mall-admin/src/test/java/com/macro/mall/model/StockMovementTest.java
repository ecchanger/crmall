package com.macro.mall.model;

import com.macro.mall.enums.StockMovementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockMovement Entity Tests")
class StockMovementTest {

    private StockMovement stockMovement;
    private Long productId;
    private String operatorId;

    @BeforeEach
    void setUp() {
        productId = 1L;
        operatorId = "admin123";
        stockMovement = new StockMovement();
        stockMovement.setId(1L);
        stockMovement.setProductId(productId);
        stockMovement.setMovementType(StockMovementType.INBOUND);
        stockMovement.setQuantity(100);
        stockMovement.setPreviousQuantity(50);
        stockMovement.setNewQuantity(150);
        stockMovement.setUnitCost(new BigDecimal("29.99"));
        stockMovement.setTotalCost(new BigDecimal("2999.00"));
        stockMovement.setReason("Initial stock");
        stockMovement.setReferenceNumber("PO-001");
        stockMovement.setOperatorId(operatorId);
        stockMovement.setMovementDate(LocalDateTime.now());
        stockMovement.setCreateTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should validate stock movement calculation")
    void shouldValidateStockMovementCalculation() {
        // Test inbound movement calculation
        assertTrue(stockMovement.isQuantityChangeValid());
        assertEquals(100, stockMovement.getQuantityChange());
        
        // Test outbound movement
        stockMovement.setMovementType(StockMovementType.OUTBOUND);
        stockMovement.setQuantity(30);
        stockMovement.setPreviousQuantity(150);
        stockMovement.setNewQuantity(120);
        
        assertTrue(stockMovement.isQuantityChangeValid());
        assertEquals(-30, stockMovement.getQuantityChange());
    }

    @Test
    @DisplayName("Should detect invalid quantity changes")
    void shouldDetectInvalidQuantityChanges() {
        // Invalid calculation: previous + quantity != new
        stockMovement.setPreviousQuantity(50);
        stockMovement.setQuantity(100);
        stockMovement.setNewQuantity(200); // Should be 150
        
        assertFalse(stockMovement.isQuantityChangeValid());
    }

    @Test
    @DisplayName("Should validate different movement types")
    void shouldValidateDifferentMovementTypes() {
        // Test adjustment movement
        stockMovement.setMovementType(StockMovementType.ADJUSTMENT);
        stockMovement.setQuantity(20);
        stockMovement.setPreviousQuantity(100);
        stockMovement.setNewQuantity(120);
        
        assertTrue(stockMovement.isQuantityChangeValid());
        assertTrue(stockMovement.isPositiveAdjustment());
        assertFalse(stockMovement.isNegativeAdjustment());
        
        // Test negative adjustment
        stockMovement.setQuantity(-30);
        stockMovement.setNewQuantity(70);
        
        assertTrue(stockMovement.isQuantityChangeValid());
        assertFalse(stockMovement.isPositiveAdjustment());
        assertTrue(stockMovement.isNegativeAdjustment());
    }

    @Test
    @DisplayName("Should calculate total cost correctly")
    void shouldCalculateTotalCostCorrectly() {
        assertTrue(stockMovement.isTotalCostValid());
        
        // Test invalid total cost
        stockMovement.setTotalCost(new BigDecimal("1000.00"));
        assertFalse(stockMovement.isTotalCostValid());
    }

    @Test
    @DisplayName("Should validate return movements")
    void shouldValidateReturnMovements() {
        stockMovement.setMovementType(StockMovementType.RETURN);
        stockMovement.setQuantity(20);
        stockMovement.setPreviousQuantity(100);
        stockMovement.setNewQuantity(120);
        
        assertTrue(stockMovement.isReturn());
        assertTrue(stockMovement.isQuantityChangeValid());
    }

    @Test
    @DisplayName("Should validate damaged stock movements")
    void shouldValidateDamagedStockMovements() {
        stockMovement.setMovementType(StockMovementType.DAMAGED);
        stockMovement.setQuantity(15);
        stockMovement.setPreviousQuantity(100);
        stockMovement.setNewQuantity(85);
        
        assertTrue(stockMovement.isDamaged());
        assertTrue(stockMovement.isQuantityChangeValid());
    }

    @Test
    @DisplayName("Should validate reserved stock movements")
    void shouldValidateReservedStockMovements() {
        stockMovement.setMovementType(StockMovementType.RESERVED);
        stockMovement.setQuantity(25);
        stockMovement.setPreviousQuantity(100);
        stockMovement.setNewQuantity(75);
        
        assertTrue(stockMovement.isReserved());
        assertTrue(stockMovement.isQuantityChangeValid());
    }

    @Test
    @DisplayName("Should generate unique batch numbers")
    void shouldGenerateUniqueBatchNumbers() {
        String batchNumber1 = stockMovement.generateBatchNumber();
        String batchNumber2 = stockMovement.generateBatchNumber();
        
        assertNotNull(batchNumber1);
        assertNotNull(batchNumber2);
        assertNotEquals(batchNumber1, batchNumber2);
        assertTrue(batchNumber1.startsWith("BATCH-"));
        assertTrue(batchNumber2.startsWith("BATCH-"));
    }

    @Test
    @DisplayName("Should validate movement with reason")
    void shouldValidateMovementWithReason() {
        assertTrue(stockMovement.hasReason());
        
        stockMovement.setReason("");
        assertFalse(stockMovement.hasReason());
        
        stockMovement.setReason(null);
        assertFalse(stockMovement.hasReason());
    }

    @Test
    @DisplayName("Should validate movement with reference number")
    void shouldValidateMovementWithReferenceNumber() {
        assertTrue(stockMovement.hasReferenceNumber());
        
        stockMovement.setReferenceNumber("");
        assertFalse(stockMovement.hasReferenceNumber());
        
        stockMovement.setReferenceNumber(null);
        assertFalse(stockMovement.hasReferenceNumber());
    }

    @Test
    @DisplayName("Should validate movement audit trail")
    void shouldValidateMovementAuditTrail() {
        assertTrue(stockMovement.hasAuditTrail());
        
        stockMovement.setOperatorId(null);
        stockMovement.setMovementDate(null);
        assertFalse(stockMovement.hasAuditTrail());
    }

    @Test
    @DisplayName("Should format movement summary correctly")
    void shouldFormatMovementSummaryCorrectly() {
        String summary = stockMovement.getMovementSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("INBOUND"));
        assertTrue(summary.contains("100"));
        assertTrue(summary.contains(productId.toString()));
    }

    @Test
    @DisplayName("Should compare movements by date")
    void shouldCompareMovementsByDate() {
        StockMovement olderMovement = new StockMovement();
        olderMovement.setMovementDate(LocalDateTime.now().minusDays(1));
        
        StockMovement newerMovement = new StockMovement();
        newerMovement.setMovementDate(LocalDateTime.now());
        
        assertTrue(olderMovement.getMovementDate().isBefore(newerMovement.getMovementDate()));
    }

    @Test
    @DisplayName("Should validate movement is within date range")
    void shouldValidateMovementIsWithinDateRange() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(1);
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        
        assertTrue(stockMovement.isWithinDateRange(startDate, endDate));
        
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        assertFalse(stockMovement.isWithinDateRange(startDate, futureDate.minusHours(2)));
    }

    @Test
    @DisplayName("Should validate movement affects product availability")
    void shouldValidateMovementAffectsProductAvailability() {
        // Inbound and return movements increase availability
        assertTrue(stockMovement.increasesAvailability());
        assertFalse(stockMovement.decreasesAvailability());
        
        // Outbound and reserved movements decrease availability
        stockMovement.setMovementType(StockMovementType.OUTBOUND);
        assertFalse(stockMovement.increasesAvailability());
        assertTrue(stockMovement.decreasesAvailability());
        
        // Damaged movements decrease availability
        stockMovement.setMovementType(StockMovementType.DAMAGED);
        assertFalse(stockMovement.increasesAvailability());
        assertTrue(stockMovement.decreasesAvailability());
    }

    @Test
    @DisplayName("Should handle edge cases for zero quantities")
    void shouldHandleEdgeCasesForZeroQuantities() {
        stockMovement.setQuantity(0);
        stockMovement.setPreviousQuantity(100);
        stockMovement.setNewQuantity(100);
        
        assertTrue(stockMovement.isQuantityChangeValid());
        assertEquals(0, stockMovement.getQuantityChange());
        assertFalse(stockMovement.isPositiveAdjustment());
        assertFalse(stockMovement.isNegativeAdjustment());
    }

    @Test
    @DisplayName("Should validate equals and hashCode")
    void shouldValidateEqualsAndHashCode() {
        StockMovement movement1 = new StockMovement();
        movement1.setId(1L);
        
        StockMovement movement2 = new StockMovement();
        movement2.setId(1L);
        
        StockMovement movement3 = new StockMovement();
        movement3.setId(2L);
        
        assertEquals(movement1, movement2);
        assertNotEquals(movement1, movement3);
        assertEquals(movement1.hashCode(), movement2.hashCode());
        assertNotEquals(movement1.hashCode(), movement3.hashCode());
    }
}