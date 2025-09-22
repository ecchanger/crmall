package com.macro.mall.service.impl;

import com.macro.mall.common.util.RequestUtil;
import com.macro.mall.dao.PmsProductAuditLogDao;
import com.macro.mall.model.PmsProductAuditLog;
import com.macro.mall.service.PmsProductAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品审计日志Service实现类
 * Created by macro on 2023/12/22.
 */
@Service
public class PmsProductAuditServiceImpl implements PmsProductAuditService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductAuditServiceImpl.class);
    
    @Autowired
    private PmsProductAuditLogDao auditLogDao;

    @Override
    public int logOperation(Long productId, Integer operationType, String operationDesc, 
                            String beforeData, String afterData) {
        try {
            PmsProductAuditLog auditLog = new PmsProductAuditLog();
            auditLog.setProductId(productId);
            auditLog.setOperationType(operationType);
            auditLog.setOperationDesc(operationDesc);
            auditLog.setBeforeData(beforeData);
            auditLog.setAfterData(afterData);
            auditLog.setOperatorId(getCurrentUserId());
            auditLog.setOperatorName(getCurrentUserName());
            auditLog.setOperationTime(new Date());
            
            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                auditLog.setClientIp(RequestUtil.getRequestIp(request));
                auditLog.setUserAgent(request.getHeader("User-Agent"));
                auditLog.setRequestUri(request.getRequestURI());
                auditLog.setRequestMethod(request.getMethod());
            }
            
            return auditLogDao.insert(auditLog);
            
        } catch (Exception e) {
            LOGGER.error("记录审计日志失败", e);
            // 审计日志失败不应该影响业务操作
            return 0;
        }
    }

    @Override
    public List<Map<String, Object>> getProductOperationHistory(Long productId) {
        try {
            List<PmsProductAuditLog> logs = auditLogDao.getProductOperationHistory(productId, null, 100, 1);
            return convertToMapList(logs);
        } catch (Exception e) {
            LOGGER.error("获取商品操作历史失败", e);
            throw new RuntimeException("获取商品操作历史失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmsProductAuditLog> getOperatorHistory(Long operatorId, Date startTime, Date endTime) {
        return auditLogDao.getOperatorHistory(operatorId, startTime, endTime, 100, 1);
    }

    @Override
    public List<Map<String, Object>> getOperationStatistics(Date startTime, Date endTime) {
        return auditLogDao.getOperationStatistics(startTime, endTime, "operation_type");
    }

    @Override
    public int cleanExpiredLogs(Integer expireDays) {
        if (expireDays == null || expireDays <= 0) {
            expireDays = 90; // 默认保留90天
        }
        return auditLogDao.deleteExpiredLogs(expireDays);
    }
    
    /**
     * 转换为Map列表
     */
    private List<Map<String, Object>> convertToMapList(List<PmsProductAuditLog> logs) {
        // TODO: 实现转换逻辑
        return new java.util.ArrayList<>();
    }
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        // TODO: 从Spring Security获取
        return 1L;
    }
    
    /**
     * 获取当前用户名
     */
    private String getCurrentUserName() {
        // TODO: 从Spring Security获取
        return "admin";
    }
}