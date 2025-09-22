package com.macro.mall.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom health indicator for Product Management Module
 * Monitors the health of critical product management components
 */
@Component
public class ProductManagementHealthIndicator implements HealthIndicator {

    @Autowired(required = false)
    private DataSource dataSource;

    private static final String PRODUCT_COUNT_QUERY = "SELECT COUNT(*) FROM pms_product";
    private static final String LIFECYCLE_COUNT_QUERY = "SELECT COUNT(*) FROM pms_product_lifecycle";
    private static final String INVENTORY_ALERT_COUNT_QUERY = "SELECT COUNT(*) FROM pms_inventory_alert WHERE is_active = 1";
    private static final String LOW_STOCK_COUNT_QUERY = "SELECT COUNT(*) FROM pms_inventory_record WHERE available_quantity <= reorder_point AND available_quantity > 0";

    @Override
    public Health health() {
        try {
            Map<String, Object> details = new HashMap<>();
            
            // Check database connectivity
            boolean databaseHealthy = checkDatabaseHealth(details);
            
            // Check product management specific metrics
            boolean productMetricsHealthy = checkProductMetrics(details);
            
            // Check inventory management health
            boolean inventoryHealthy = checkInventoryHealth(details);
            
            // Check lifecycle management health
            boolean lifecycleHealthy = checkLifecycleHealth(details);
            
            // Add system information
            addSystemInfo(details);
            
            // Determine overall health status
            if (databaseHealthy && productMetricsHealthy && inventoryHealthy && lifecycleHealthy) {
                return Health.up()
                        .withDetails(details)
                        .build();
            } else {
                return Health.down()
                        .withDetails(details)
                        .build();
            }
            
        } catch (Exception e) {
            return Health.down()
                    .withException(e)
                    .withDetail("error", "Health check failed: " + e.getMessage())
                    .withDetail("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
        }
    }

    /**
     * Check database connectivity and basic queries
     */
    private boolean checkDatabaseHealth(Map<String, Object> details) {
        if (dataSource == null) {
            details.put("database", "DataSource not available");
            return false;
        }

        try (Connection connection = dataSource.getConnection()) {
            // Test basic connectivity
            boolean isValid = connection.isValid(5); // 5 second timeout
            details.put("database.connectivity", isValid ? "UP" : "DOWN");
            
            if (isValid) {
                // Test basic query execution
                try (PreparedStatement stmt = connection.prepareStatement("SELECT 1");
                     ResultSet rs = stmt.executeQuery()) {
                    boolean querySuccess = rs.next() && rs.getInt(1) == 1;
                    details.put("database.query_execution", querySuccess ? "UP" : "DOWN");
                    return querySuccess;
                }
            }
            return false;
            
        } catch (Exception e) {
            details.put("database.error", e.getMessage());
            details.put("database.status", "DOWN");
            return false;
        }
    }

    /**
     * Check product management specific metrics
     */
    private boolean checkProductMetrics(Map<String, Object> details) {
        try (Connection connection = dataSource.getConnection()) {
            // Check total product count
            try (PreparedStatement stmt = connection.prepareStatement(PRODUCT_COUNT_QUERY);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int productCount = rs.getInt(1);
                    details.put("products.total_count", productCount);
                    details.put("products.status", productCount >= 0 ? "UP" : "DOWN");
                }
            }

            // Check for recent product activity (products created in last 24 hours)
            String recentProductQuery = "SELECT COUNT(*) FROM pms_product WHERE create_time > DATE_SUB(NOW(), INTERVAL 1 DAY)";
            try (PreparedStatement stmt = connection.prepareStatement(recentProductQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int recentProducts = rs.getInt(1);
                    details.put("products.recent_activity", recentProducts);
                }
            }

            // Check for products with critical issues
            String criticalIssuesQuery = "SELECT COUNT(*) FROM pms_product WHERE delete_status = 1 OR publish_status = 0";
            try (PreparedStatement stmt = connection.prepareStatement(criticalIssuesQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int criticalIssues = rs.getInt(1);
                    details.put("products.critical_issues", criticalIssues);
                    if (criticalIssues > 1000) { // Threshold for concern
                        details.put("products.critical_status", "WARNING");
                    }
                }
            }

            return true;

        } catch (Exception e) {
            details.put("products.error", e.getMessage());
            details.put("products.status", "DOWN");
            return false;
        }
    }

    /**
     * Check inventory management health
     */
    private boolean checkInventoryHealth(Map<String, Object> details) {
        try (Connection connection = dataSource.getConnection()) {
            // Check active inventory alerts
            try (PreparedStatement stmt = connection.prepareStatement(INVENTORY_ALERT_COUNT_QUERY);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int activeAlerts = rs.getInt(1);
                    details.put("inventory.active_alerts", activeAlerts);
                    
                    // Set status based on alert count
                    if (activeAlerts > 100) {
                        details.put("inventory.alert_status", "CRITICAL");
                    } else if (activeAlerts > 50) {
                        details.put("inventory.alert_status", "WARNING");
                    } else {
                        details.put("inventory.alert_status", "UP");
                    }
                }
            }

            // Check low stock products
            try (PreparedStatement stmt = connection.prepareStatement(LOW_STOCK_COUNT_QUERY);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int lowStockCount = rs.getInt(1);
                    details.put("inventory.low_stock_count", lowStockCount);
                    
                    if (lowStockCount > 50) {
                        details.put("inventory.stock_status", "WARNING");
                    } else {
                        details.put("inventory.stock_status", "UP");
                    }
                }
            }

            // Check out of stock products
            String outOfStockQuery = "SELECT COUNT(*) FROM pms_inventory_record WHERE available_quantity = 0";
            try (PreparedStatement stmt = connection.prepareStatement(outOfStockQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int outOfStockCount = rs.getInt(1);
                    details.put("inventory.out_of_stock_count", outOfStockCount);
                }
            }

            return true;

        } catch (Exception e) {
            details.put("inventory.error", e.getMessage());
            details.put("inventory.status", "DOWN");
            return false;
        }
    }

    /**
     * Check lifecycle management health
     */
    private boolean checkLifecycleHealth(Map<String, Object> details) {
        try (Connection connection = dataSource.getConnection()) {
            // Check total lifecycle records
            try (PreparedStatement stmt = connection.prepareStatement(LIFECYCLE_COUNT_QUERY);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int lifecycleCount = rs.getInt(1);
                    details.put("lifecycle.total_records", lifecycleCount);
                    details.put("lifecycle.status", lifecycleCount >= 0 ? "UP" : "DOWN");
                }
            }

            // Check products pending review
            String pendingReviewQuery = "SELECT COUNT(*) FROM pms_product_lifecycle WHERE current_status = 'UNDER_REVIEW'";
            try (PreparedStatement stmt = connection.prepareStatement(pendingReviewQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int pendingReview = rs.getInt(1);
                    details.put("lifecycle.pending_review", pendingReview);
                    
                    if (pendingReview > 100) {
                        details.put("lifecycle.review_status", "WARNING");
                    } else {
                        details.put("lifecycle.review_status", "UP");
                    }
                }
            }

            // Check stale draft products (older than 30 days)
            String staleDraftsQuery = "SELECT COUNT(*) FROM pms_product_lifecycle WHERE current_status = 'DRAFT' AND created_at < DATE_SUB(NOW(), INTERVAL 30 DAY)";
            try (PreparedStatement stmt = connection.prepareStatement(staleDraftsQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int staleDrafts = rs.getInt(1);
                    details.put("lifecycle.stale_drafts", staleDrafts);
                }
            }

            return true;

        } catch (Exception e) {
            details.put("lifecycle.error", e.getMessage());
            details.put("lifecycle.status", "DOWN");
            return false;
        }
    }

    /**
     * Add system information
     */
    private void addSystemInfo(Map<String, Object> details) {
        Runtime runtime = Runtime.getRuntime();
        
        details.put("system.timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        details.put("system.free_memory_mb", runtime.freeMemory() / 1024 / 1024);
        details.put("system.total_memory_mb", runtime.totalMemory() / 1024 / 1024);
        details.put("system.max_memory_mb", runtime.maxMemory() / 1024 / 1024);
        details.put("system.available_processors", runtime.availableProcessors());
        
        // Memory usage calculation
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        double memoryUsagePercent = (double) usedMemory / runtime.totalMemory() * 100;
        details.put("system.memory_usage_percent", String.format("%.2f", memoryUsagePercent));
        
        if (memoryUsagePercent > 90) {
            details.put("system.memory_status", "CRITICAL");
        } else if (memoryUsagePercent > 75) {
            details.put("system.memory_status", "WARNING");
        } else {
            details.put("system.memory_status", "UP");
        }
    }
}