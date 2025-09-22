package com.macro.mall.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Performance monitoring aspect for Product Management Module
 * Automatically tracks method execution times and performance metrics
 */
@Aspect
@Component
public class PerformanceMonitoringAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitoringAspect.class);
    
    private final MeterRegistry meterRegistry;
    private final Timer methodExecutionTimer;
    private final Counter methodExecutionCounter;
    private final Counter errorCounter;

    public PerformanceMonitoringAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.methodExecutionTimer = Timer.builder("method.execution.time")
                .description("Method execution time")
                .register(meterRegistry);
        this.methodExecutionCounter = Counter.builder("method.execution.count")
                .description("Method execution count")
                .register(meterRegistry);
        this.errorCounter = Counter.builder("method.execution.errors")
                .description("Method execution errors")
                .register(meterRegistry);
    }

    /**
     * Pointcut for all service layer methods
     */
    @Pointcut("execution(* com.macro.mall.service..*.*(..))")
    public void serviceLayer() {}

    /**
     * Pointcut for all controller layer methods
     */
    @Pointcut("execution(* com.macro.mall.controller..*.*(..))")
    public void controllerLayer() {}

    /**
     * Pointcut for all repository layer methods
     */
    @Pointcut("execution(* com.macro.mall.dao..*.*(..))")
    public void repositoryLayer() {}

    /**
     * Pointcut for all lifecycle operations
     */
    @Pointcut("execution(* com.macro.mall.service.ProductLifecycleService.*(..))")
    public void lifecycleOperations() {}

    /**
     * Pointcut for all batch operations
     */
    @Pointcut("execution(* com.macro.mall.service.BatchOperationService.*(..))")
    public void batchOperations() {}

    /**
     * Pointcut for all inventory operations
     */
    @Pointcut("execution(* com.macro.mall.service.InventoryService.*(..))")
    public void inventoryOperations() {}

    /**
     * Monitor all service layer method executions
     */
    @Around("serviceLayer()")
    public Object monitorServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethodExecution(joinPoint, "service");
    }

    /**
     * Monitor all controller layer method executions
     */
    @Around("controllerLayer()")
    public Object monitorControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethodExecution(joinPoint, "controller");
    }

    /**
     * Monitor all repository layer method executions
     */
    @Around("repositoryLayer()")
    public Object monitorRepositoryExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethodExecution(joinPoint, "repository");
    }

    /**
     * Monitor lifecycle operations with specific metrics
     */
    @Around("lifecycleOperations()")
    public Object monitorLifecycleOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        Instant startTime = Instant.now();
        
        try {
            logger.debug("Starting lifecycle operation: {}.{}", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName());
            
            Object result = joinPoint.proceed();
            
            Duration duration = Duration.between(startTime, Instant.now());
            logger.info("Lifecycle operation completed: {}.{} in {}ms", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName(),
                    duration.toMillis());
            
            // Record specific metrics for lifecycle operations
            meterRegistry.counter("product.lifecycle.operations", 
                    "method", joinPoint.getSignature().getName(),
                    "status", "success").increment();
            
            sample.stop(Timer.builder("product.lifecycle.execution.time")
                    .tag("method", joinPoint.getSignature().getName())
                    .register(meterRegistry));
            
            return result;
            
        } catch (Exception e) {
            Duration duration = Duration.between(startTime, Instant.now());
            logger.error("Lifecycle operation failed: {}.{} in {}ms - {}", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName(),
                    duration.toMillis(),
                    e.getMessage(), e);
            
            meterRegistry.counter("product.lifecycle.operations", 
                    "method", joinPoint.getSignature().getName(),
                    "status", "error").increment();
            
            sample.stop(Timer.builder("product.lifecycle.execution.time")
                    .tag("method", joinPoint.getSignature().getName())
                    .tag("status", "error")
                    .register(meterRegistry));
            
            throw e;
        }
    }

    /**
     * Monitor batch operations with specific metrics and thresholds
     */
    @Around("batchOperations()")
    public Object monitorBatchOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        Instant startTime = Instant.now();
        
        try {
            logger.info("Starting batch operation: {}.{}", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName());
            
            Object result = joinPoint.proceed();
            
            Duration duration = Duration.between(startTime, Instant.now());
            logger.info("Batch operation completed: {}.{} in {}ms", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName(),
                    duration.toMillis());
            
            // Check for performance degradation
            if (duration.toMillis() > 30000) { // 30 seconds threshold
                logger.warn("Batch operation exceeded performance threshold: {}ms", duration.toMillis());
                meterRegistry.counter("product.batch.slow.operations").increment();
            }
            
            meterRegistry.counter("product.batch.operations", 
                    "method", joinPoint.getSignature().getName(),
                    "status", "success").increment();
            
            sample.stop(Timer.builder("product.batch.execution.time")
                    .tag("method", joinPoint.getSignature().getName())
                    .register(meterRegistry));
            
            return result;
            
        } catch (Exception e) {
            Duration duration = Duration.between(startTime, Instant.now());
            logger.error("Batch operation failed: {}.{} in {}ms - {}", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName(),
                    duration.toMillis(),
                    e.getMessage(), e);
            
            meterRegistry.counter("product.batch.operations", 
                    "method", joinPoint.getSignature().getName(),
                    "status", "error").increment();
            
            sample.stop(Timer.builder("product.batch.execution.time")
                    .tag("method", joinPoint.getSignature().getName())
                    .tag("status", "error")
                    .register(meterRegistry));
            
            throw e;
        }
    }

    /**
     * Monitor inventory operations with real-time tracking
     */
    @Around("inventoryOperations()")
    public Object monitorInventoryOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        Instant startTime = Instant.now();
        
        try {
            logger.debug("Starting inventory operation: {}.{}", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName());
            
            Object result = joinPoint.proceed();
            
            Duration duration = Duration.between(startTime, Instant.now());
            logger.debug("Inventory operation completed: {}.{} in {}ms", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName(),
                    duration.toMillis());
            
            meterRegistry.counter("inventory.operations", 
                    "method", joinPoint.getSignature().getName(),
                    "status", "success").increment();
            
            sample.stop(Timer.builder("inventory.execution.time")
                    .tag("method", joinPoint.getSignature().getName())
                    .register(meterRegistry));
            
            return result;
            
        } catch (Exception e) {
            Duration duration = Duration.between(startTime, Instant.now());
            logger.error("Inventory operation failed: {}.{} in {}ms - {}", 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    joinPoint.getSignature().getName(),
                    duration.toMillis(),
                    e.getMessage(), e);
            
            meterRegistry.counter("inventory.operations", 
                    "method", joinPoint.getSignature().getName(),
                    "status", "error").increment();
            
            sample.stop(Timer.builder("inventory.execution.time")
                    .tag("method", joinPoint.getSignature().getName())
                    .tag("status", "error")
                    .register(meterRegistry));
            
            throw e;
        }
    }

    /**
     * Generic method execution monitoring
     */
    private Object monitorMethodExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        Instant startTime = Instant.now();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        try {
            methodExecutionCounter.increment(
                    "class", className,
                    "method", methodName,
                    "layer", layer);
            
            Object result = joinPoint.proceed();
            
            Duration duration = Duration.between(startTime, Instant.now());
            if (duration.toMillis() > 1000) { // Log slow operations
                logger.warn("Slow operation detected: {}.{} took {}ms", className, methodName, duration.toMillis());
            }
            
            sample.stop(Timer.builder("method.execution.time")
                    .tag("class", className)
                    .tag("method", methodName)
                    .tag("layer", layer)
                    .tag("status", "success")
                    .register(meterRegistry));
            
            return result;
            
        } catch (Exception e) {
            Duration duration = Duration.between(startTime, Instant.now());
            logger.error("Method execution failed: {}.{} in {}ms - {}", 
                    className, methodName, duration.toMillis(), e.getMessage());
            
            errorCounter.increment(
                    "class", className,
                    "method", methodName,
                    "layer", layer,
                    "exception", e.getClass().getSimpleName());
            
            sample.stop(Timer.builder("method.execution.time")
                    .tag("class", className)
                    .tag("method", methodName)
                    .tag("layer", layer)
                    .tag("status", "error")
                    .register(meterRegistry));
            
            throw e;
        }
    }
}