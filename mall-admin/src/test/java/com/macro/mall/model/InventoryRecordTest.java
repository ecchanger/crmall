package com.macro.mall.model;

import com.macro.mall.enums.InventoryAlertType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 库存记录实体测试
 * Created for Product Management Module Enhancement
 */
public class InventoryRecordTest {

    private InventoryRecord inventoryRecord;

    @BeforeEach
    void setUp() {
        inventoryRecord = new InventoryRecord();
        inventoryRecord.setProductId(1L);
        inventoryRecord.setProductName("Test Product");
        inventoryRecord.setWarehouseId(1L);
        inventoryRecord.setAvailableQuantity(100);
        inventoryRecord.setReservedQuantity(10);
        inventoryRecord.setDamagedQuantity(5);
        inventoryRecord.setInTransitQuantity(20);
        inventoryRecord.setReorderPoint(30);
        inventoryRecord.setMaxStockLevel(200);
        inventoryRecord.setUnitCost(10.0);
    }

    @Test
    void testGetTotalQuantity() {
        // When
        Integer totalQuantity = inventoryRecord.getTotalQuantity();

        // Then
        assertEquals(135, totalQuantity); // 100 + 10 + 5 + 20
    }

    @Test
    void testGetUsableQuantity() {
        // When
        Integer usableQuantity = inventoryRecord.getUsableQuantity();

        // Then
        assertEquals(120, usableQuantity); // 100 + 20 (available + inTransit)
    }

    @Test
    void testIsLowStock() {
        // When - stock below reorder point
        inventoryRecord.setAvailableQuantity(25);
        
        // Then
        assertTrue(inventoryRecord.isLowStock());

        // When - stock above reorder point
        inventoryRecord.setAvailableQuantity(50);
        
        // Then
        assertFalse(inventoryRecord.isLowStock());
    }

    @Test
    void testIsOutOfStock() {
        // When - zero stock
        inventoryRecord.setAvailableQuantity(0);
        
        // Then
        assertTrue(inventoryRecord.isOutOfStock());

        // When - null stock
        inventoryRecord.setAvailableQuantity(null);
        
        // Then
        assertTrue(inventoryRecord.isOutOfStock());

        // When - positive stock
        inventoryRecord.setAvailableQuantity(10);
        
        // Then
        assertFalse(inventoryRecord.isOutOfStock());
    }

    @Test
    void testIsOverstock() {
        // When - stock above max level
        inventoryRecord.setAvailableQuantity(250);
        
        // Then
        assertTrue(inventoryRecord.isOverstock());

        // When - stock below max level
        inventoryRecord.setAvailableQuantity(150);
        
        // Then
        assertFalse(inventoryRecord.isOverstock());
    }

    @Test
    void testNeedsReorder() {
        // Given
        inventoryRecord.setAvailableQuantity(25); // Below reorder point
        inventoryRecord.setAutoReorderEnabled(true);
        
        // When
        boolean needsReorder = inventoryRecord.needsReorder();
        
        // Then
        assertTrue(needsReorder);

        // When auto reorder is disabled
        inventoryRecord.setAutoReorderEnabled(false);
        
        // Then
        assertFalse(inventoryRecord.needsReorder());
    }

    @Test
    void testIsExpired() {
        // Given - expired date
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // 1 day ago
        inventoryRecord.setExpiryDate(pastDate);
        
        // When
        boolean isExpired = inventoryRecord.isExpired();
        
        // Then
        assertTrue(isExpired);

        // Given - future date
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day from now
        inventoryRecord.setExpiryDate(futureDate);
        
        // When
        isExpired = inventoryRecord.isExpired();
        
        // Then
        assertFalse(isExpired);
    }

    @Test
    void testIsExpiringSoon() {
        // Given - expires in 3 days
        Date nearFutureDate = new Date(System.currentTimeMillis() + 3 * 86400000);
        inventoryRecord.setExpiryDate(nearFutureDate);
        
        // When - check for 7 days threshold
        boolean isExpiringSoon = inventoryRecord.isExpiringSoon(7);
        
        // Then
        assertTrue(isExpiringSoon);

        // When - check for 2 days threshold
        isExpiringSoon = inventoryRecord.isExpiringSoon(2);
        
        // Then
        assertFalse(isExpiringSoon);
    }

    @Test
    void testGetAlertType() {
        // Test out of stock
        inventoryRecord.setAvailableQuantity(0);
        assertEquals(InventoryAlertType.OUT_OF_STOCK, inventoryRecord.getAlertType());

        // Test low stock
        inventoryRecord.setAvailableQuantity(25); // Below reorder point of 30
        assertEquals(InventoryAlertType.LOW_STOCK, inventoryRecord.getAlertType());

        // Test overstock
        inventoryRecord.setAvailableQuantity(250); // Above max level of 200
        assertEquals(InventoryAlertType.OVERSTOCK, inventoryRecord.getAlertType());

        // Test expired
        inventoryRecord.setAvailableQuantity(100); // Reset to normal
        Date pastDate = new Date(System.currentTimeMillis() - 86400000);
        inventoryRecord.setExpiryDate(pastDate);
        assertEquals(InventoryAlertType.EXPIRED_PRODUCTS, inventoryRecord.getAlertType());

        // Test reorder point
        inventoryRecord.setExpiryDate(null); // Reset expiry
        inventoryRecord.setAvailableQuantity(25);
        inventoryRecord.setAutoReorderEnabled(true);
        assertEquals(InventoryAlertType.REORDER_POINT, inventoryRecord.getAlertType());

        // Test no alert
        inventoryRecord.setAvailableQuantity(100);
        inventoryRecord.setAutoReorderEnabled(false);
        assertNull(inventoryRecord.getAlertType());
    }

    @Test
    void testCalculateInventoryValue() {
        // When
        Double inventoryValue = inventoryRecord.calculateInventoryValue();
        
        // Then
        assertEquals(1000.0, inventoryValue); // 100 * 10.0

        // When unit cost is null
        inventoryRecord.setUnitCost(null);
        inventoryValue = inventoryRecord.calculateInventoryValue();
        
        // Then
        assertEquals(0.0, inventoryValue);
    }

    @Test
    void testGetDaysSinceLastSale() {
        // Given - last sale 5 days ago
        Date lastSaleDate = new Date(System.currentTimeMillis() - 5 * 86400000);
        inventoryRecord.setLastSaleDate(lastSaleDate);
        
        // When
        int daysSinceLastSale = inventoryRecord.getDaysSinceLastSale();
        
        // Then
        assertEquals(5, daysSinceLastSale);

        // When no last sale date
        inventoryRecord.setLastSaleDate(null);
        daysSinceLastSale = inventoryRecord.getDaysSinceLastSale();
        
        // Then
        assertEquals(Integer.MAX_VALUE, daysSinceLastSale);
    }

    @Test
    void testIsSlowMoving() {
        // Given - last sale 100 days ago
        Date lastSaleDate = new Date(System.currentTimeMillis() - 100 * 86400000L);
        inventoryRecord.setLastSaleDate(lastSaleDate);
        
        // When - check with 90 days threshold
        boolean isSlowMoving = inventoryRecord.isSlowMoving(90);
        
        // Then
        assertTrue(isSlowMoving);

        // When - check with 120 days threshold
        isSlowMoving = inventoryRecord.isSlowMoving(120);
        
        // Then
        assertFalse(isSlowMoving);
    }

    @Test
    void testIsFastMoving() {
        // Given - high daily sales rate
        inventoryRecord.setAverageDailySales(20.0); // 20 units per day
        inventoryRecord.setAvailableQuantity(100); // 100 units available
        
        // When - 100/20 = 5 days of stock (< 7 days threshold)
        boolean isFastMoving = inventoryRecord.isFastMoving();
        
        // Then
        assertTrue(isFastMoving);

        // Given - low daily sales rate
        inventoryRecord.setAverageDailySales(5.0); // 5 units per day
        
        // When - 100/5 = 20 days of stock (> 7 days threshold)
        isFastMoving = inventoryRecord.isFastMoving();
        
        // Then
        assertFalse(isFastMoving);
    }

    @Test
    void testConstructorDefaults() {
        // Given
        InventoryRecord newRecord = new InventoryRecord();
        
        // Then - verify default values
        assertEquals(0, newRecord.getAvailableQuantity());
        assertEquals(0, newRecord.getReservedQuantity());
        assertEquals(0, newRecord.getDamagedQuantity());
        assertEquals(0, newRecord.getInTransitQuantity());
        assertEquals(0, newRecord.getInventoryStatus());
        assertFalse(newRecord.getAutoReorderEnabled());
        assertNotNull(newRecord.getCreatedAt());
        assertNotNull(newRecord.getUpdatedAt());
    }

    @Test
    void testSetQuantityUpdatesTimestamp() {
        // Given
        Date originalUpdatedAt = inventoryRecord.getUpdatedAt();
        
        try {
            Thread.sleep(10); // Ensure time difference
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // When
        inventoryRecord.setAvailableQuantity(150);
        
        // Then
        assertTrue(inventoryRecord.getUpdatedAt().after(originalUpdatedAt));
    }

    @Test
    void testNullSafetyInBusinessMethods() {
        // Given
        InventoryRecord nullRecord = new InventoryRecord();
        nullRecord.setAvailableQuantity(null);
        nullRecord.setReservedQuantity(null);
        nullRecord.setReorderPoint(null);
        
        // When & Then - should not throw exceptions
        assertDoesNotThrow(() -> {
            nullRecord.getTotalQuantity();
            nullRecord.isLowStock();
            nullRecord.isOutOfStock();
            nullRecord.isOverstock();
            nullRecord.calculateInventoryValue();
        });
    }
}