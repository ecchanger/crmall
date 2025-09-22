package com.macro.mall.model;

import com.macro.mall.enums.InventoryAlertType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InventoryAlert Entity Tests")
class InventoryAlertTest {

    private InventoryAlert inventoryAlert;
    private Long productId;

    @BeforeEach
    void setUp() {
        productId = 1L;
        inventoryAlert = new InventoryAlert();
        inventoryAlert.setId(1L);
        inventoryAlert.setProductId(productId);
        inventoryAlert.setAlertType(InventoryAlertType.LOW_STOCK);
        inventoryAlert.setMessage("Product stock is running low");
        inventoryAlert.setSeverity(InventoryAlert.Severity.MEDIUM);
        inventoryAlert.setIsActive(true);
        inventoryAlert.setCreatedAt(LocalDateTime.now());
        inventoryAlert.setTriggeredBy("SYSTEM");
    }

    @Test
    @DisplayName("Should validate alert is active")
    void shouldValidateAlertIsActive() {
        assertTrue(inventoryAlert.isActive());
        
        inventoryAlert.setIsActive(false);
        assertFalse(inventoryAlert.isActive());
    }

    @Test
    @DisplayName("Should acknowledge alert correctly")
    void shouldAcknowledgeAlertCorrectly() {
        String adminId = "admin123";
        
        assertFalse(inventoryAlert.isAcknowledged());
        assertNull(inventoryAlert.getAcknowledgedBy());
        assertNull(inventoryAlert.getAcknowledgedAt());
        
        inventoryAlert.acknowledge(adminId);
        
        assertTrue(inventoryAlert.isAcknowledged());
        assertEquals(adminId, inventoryAlert.getAcknowledgedBy());
        assertNotNull(inventoryAlert.getAcknowledgedAt());
    }

    @Test
    @DisplayName("Should resolve alert correctly")
    void shouldResolveAlertCorrectly() {
        String adminId = "admin123";
        String resolution = "Stock replenished";
        
        assertFalse(inventoryAlert.isResolved());
        assertNull(inventoryAlert.getResolvedBy());
        assertNull(inventoryAlert.getResolvedAt());
        assertNull(inventoryAlert.getResolutionNotes());
        
        inventoryAlert.resolve(adminId, resolution);
        
        assertTrue(inventoryAlert.isResolved());
        assertEquals(adminId, inventoryAlert.getResolvedBy());
        assertEquals(resolution, inventoryAlert.getResolutionNotes());
        assertNotNull(inventoryAlert.getResolvedAt());
        assertFalse(inventoryAlert.isActive()); // Should be deactivated when resolved
    }

    @Test
    @DisplayName("Should validate different alert types")
    void shouldValidateDifferentAlertTypes() {
        // Test low stock alert
        assertTrue(inventoryAlert.isLowStockAlert());
        assertFalse(inventoryAlert.isOutOfStockAlert());
        assertFalse(inventoryAlert.isExpiredAlert());
        assertFalse(inventoryAlert.isOverstockAlert());
        
        // Test out of stock alert
        inventoryAlert.setAlertType(InventoryAlertType.OUT_OF_STOCK);
        assertFalse(inventoryAlert.isLowStockAlert());
        assertTrue(inventoryAlert.isOutOfStockAlert());
        assertFalse(inventoryAlert.isExpiredAlert());
        assertFalse(inventoryAlert.isOverstockAlert());
        
        // Test expired products alert
        inventoryAlert.setAlertType(InventoryAlertType.EXPIRED_PRODUCTS);
        assertFalse(inventoryAlert.isLowStockAlert());
        assertFalse(inventoryAlert.isOutOfStockAlert());
        assertTrue(inventoryAlert.isExpiredAlert());
        assertFalse(inventoryAlert.isOverstockAlert());
        
        // Test overstock alert
        inventoryAlert.setAlertType(InventoryAlertType.OVERSTOCK);
        assertFalse(inventoryAlert.isLowStockAlert());
        assertFalse(inventoryAlert.isOutOfStockAlert());
        assertFalse(inventoryAlert.isExpiredAlert());
        assertTrue(inventoryAlert.isOverstockAlert());
    }

    @Test
    @DisplayName("Should validate alert severity levels")
    void shouldValidateAlertSeverityLevels() {
        // Test medium severity
        assertTrue(inventoryAlert.isMediumSeverity());
        assertFalse(inventoryAlert.isLowSeverity());
        assertFalse(inventoryAlert.isHighSeverity());
        assertFalse(inventoryAlert.isCriticalSeverity());
        
        // Test high severity
        inventoryAlert.setSeverity(InventoryAlert.Severity.HIGH);
        assertFalse(inventoryAlert.isMediumSeverity());
        assertFalse(inventoryAlert.isLowSeverity());
        assertTrue(inventoryAlert.isHighSeverity());
        assertFalse(inventoryAlert.isCriticalSeverity());
        
        // Test critical severity
        inventoryAlert.setSeverity(InventoryAlert.Severity.CRITICAL);
        assertFalse(inventoryAlert.isMediumSeverity());
        assertFalse(inventoryAlert.isLowSeverity());
        assertFalse(inventoryAlert.isHighSeverity());
        assertTrue(inventoryAlert.isCriticalSeverity());
        
        // Test low severity
        inventoryAlert.setSeverity(InventoryAlert.Severity.LOW);
        assertFalse(inventoryAlert.isMediumSeverity());
        assertTrue(inventoryAlert.isLowSeverity());
        assertFalse(inventoryAlert.isHighSeverity());
        assertFalse(inventoryAlert.isCriticalSeverity());
    }

    @Test
    @DisplayName("Should calculate alert age correctly")
    void shouldCalculateAlertAgeCorrectly() {
        inventoryAlert.setCreatedAt(LocalDateTime.now().minusHours(2));
        
        long ageInHours = inventoryAlert.getAgeInHours();
        assertTrue(ageInHours >= 1 && ageInHours <= 3); // Allow some margin for test execution time
        
        inventoryAlert.setCreatedAt(LocalDateTime.now().minusDays(1));
        long ageInDays = inventoryAlert.getAgeInDays();
        assertEquals(1, ageInDays);
    }

    @Test
    @DisplayName("Should validate alert is stale")
    void shouldValidateAlertIsStale() {
        // Fresh alert (less than 24 hours)
        inventoryAlert.setCreatedAt(LocalDateTime.now().minusHours(12));
        assertFalse(inventoryAlert.isStale());
        
        // Stale alert (more than 24 hours)
        inventoryAlert.setCreatedAt(LocalDateTime.now().minusHours(48));
        assertTrue(inventoryAlert.isStale());
        
        // Custom staleness threshold
        inventoryAlert.setCreatedAt(LocalDateTime.now().minusHours(8));
        assertTrue(inventoryAlert.isStale(6)); // 6 hours threshold
        assertFalse(inventoryAlert.isStale(10)); // 10 hours threshold
    }

    @Test
    @DisplayName("Should validate alert requires immediate attention")
    void shouldValidateAlertRequiresImmediateAttention() {
        // Critical severity always requires immediate attention
        inventoryAlert.setSeverity(InventoryAlert.Severity.CRITICAL);
        assertTrue(inventoryAlert.requiresImmediateAttention());
        
        // Out of stock alerts require immediate attention
        inventoryAlert.setSeverity(InventoryAlert.Severity.MEDIUM);
        inventoryAlert.setAlertType(InventoryAlertType.OUT_OF_STOCK);
        assertTrue(inventoryAlert.requiresImmediateAttention());
        
        // Low severity low stock alerts don't require immediate attention
        inventoryAlert.setSeverity(InventoryAlert.Severity.LOW);
        inventoryAlert.setAlertType(InventoryAlertType.LOW_STOCK);
        assertFalse(inventoryAlert.requiresImmediateAttention());
    }

    @Test
    @DisplayName("Should validate alert can be auto-resolved")
    void shouldValidateAlertCanBeAutoResolved() {
        // Low stock and overstock alerts can be auto-resolved
        inventoryAlert.setAlertType(InventoryAlertType.LOW_STOCK);
        assertTrue(inventoryAlert.canBeAutoResolved());
        
        inventoryAlert.setAlertType(InventoryAlertType.OVERSTOCK);
        assertTrue(inventoryAlert.canBeAutoResolved());
        
        // Expired products alerts cannot be auto-resolved
        inventoryAlert.setAlertType(InventoryAlertType.EXPIRED_PRODUCTS);
        assertFalse(inventoryAlert.canBeAutoResolved());
        
        // Quality issues cannot be auto-resolved
        inventoryAlert.setAlertType(InventoryAlertType.QUALITY_ISSUE);
        assertFalse(inventoryAlert.canBeAutoResolved());
    }

    @Test
    @DisplayName("Should create alert summary correctly")
    void shouldCreateAlertSummaryCorrectly() {
        String summary = inventoryAlert.getSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("LOW_STOCK"));
        assertTrue(summary.contains("MEDIUM"));
        assertTrue(summary.contains(productId.toString()));
    }

    @Test
    @DisplayName("Should validate alert escalation")
    void shouldValidateAlertEscalation() {
        String escalatedBy = "system";
        String escalationReason = "Alert not resolved within SLA";
        
        assertFalse(inventoryAlert.isEscalated());
        assertNull(inventoryAlert.getEscalatedBy());
        assertNull(inventoryAlert.getEscalatedAt());
        assertNull(inventoryAlert.getEscalationReason());
        
        inventoryAlert.escalate(escalatedBy, escalationReason);
        
        assertTrue(inventoryAlert.isEscalated());
        assertEquals(escalatedBy, inventoryAlert.getEscalatedBy());
        assertEquals(escalationReason, inventoryAlert.getEscalationReason());
        assertNotNull(inventoryAlert.getEscalatedAt());
    }

    @Test
    @DisplayName("Should validate alert deactivation")
    void shouldValidateAlertDeactivation() {
        String deactivatedBy = "admin123";
        String reason = "False positive";
        
        assertTrue(inventoryAlert.isActive());
        
        inventoryAlert.deactivate(deactivatedBy, reason);
        
        assertFalse(inventoryAlert.isActive());
        assertEquals(deactivatedBy, inventoryAlert.getDeactivatedBy());
        assertEquals(reason, inventoryAlert.getDeactivationReason());
        assertNotNull(inventoryAlert.getDeactivatedAt());
    }

    @Test
    @DisplayName("Should validate alert notification count")
    void shouldValidateAlertNotificationCount() {
        assertEquals(0, inventoryAlert.getNotificationCount());
        
        inventoryAlert.incrementNotificationCount();
        assertEquals(1, inventoryAlert.getNotificationCount());
        
        inventoryAlert.incrementNotificationCount();
        assertEquals(2, inventoryAlert.getNotificationCount());
        
        inventoryAlert.resetNotificationCount();
        assertEquals(0, inventoryAlert.getNotificationCount());
    }

    @Test
    @DisplayName("Should validate alert update timestamp")
    void shouldValidateAlertUpdateTimestamp() {
        LocalDateTime originalTimestamp = inventoryAlert.getUpdatedAt();
        
        inventoryAlert.updateTimestamp();
        
        assertNotNull(inventoryAlert.getUpdatedAt());
        if (originalTimestamp != null) {
            assertTrue(inventoryAlert.getUpdatedAt().isAfter(originalTimestamp) || 
                      inventoryAlert.getUpdatedAt().equals(originalTimestamp));
        }
    }

    @Test
    @DisplayName("Should compare alerts by priority")
    void shouldCompareAlertsByPriority() {
        InventoryAlert criticalAlert = new InventoryAlert();
        criticalAlert.setSeverity(InventoryAlert.Severity.CRITICAL);
        criticalAlert.setAlertType(InventoryAlertType.OUT_OF_STOCK);
        
        InventoryAlert lowAlert = new InventoryAlert();
        lowAlert.setSeverity(InventoryAlert.Severity.LOW);
        lowAlert.setAlertType(InventoryAlertType.LOW_STOCK);
        
        assertTrue(criticalAlert.getPriority() > lowAlert.getPriority());
        assertTrue(criticalAlert.isHigherPriorityThan(lowAlert));
        assertFalse(lowAlert.isHigherPriorityThan(criticalAlert));
    }

    @Test
    @DisplayName("Should validate equals and hashCode")
    void shouldValidateEqualsAndHashCode() {
        InventoryAlert alert1 = new InventoryAlert();
        alert1.setId(1L);
        
        InventoryAlert alert2 = new InventoryAlert();
        alert2.setId(1L);
        
        InventoryAlert alert3 = new InventoryAlert();
        alert3.setId(2L);
        
        assertEquals(alert1, alert2);
        assertNotEquals(alert1, alert3);
        assertEquals(alert1.hashCode(), alert2.hashCode());
        assertNotEquals(alert1.hashCode(), alert3.hashCode());
    }

    @Test
    @DisplayName("Should handle null values gracefully")
    void shouldHandleNullValuesGracefully() {
        InventoryAlert nullAlert = new InventoryAlert();
        
        assertFalse(nullAlert.isActive());
        assertFalse(nullAlert.isAcknowledged());
        assertFalse(nullAlert.isResolved());
        assertFalse(nullAlert.isEscalated());
        assertEquals(0, nullAlert.getNotificationCount());
        assertEquals(0, nullAlert.getPriority());
    }
}