package com.macro.mall.service;

import com.macro.mall.dto.PmsProductImportResult;
import com.macro.mall.dto.PmsProductExportParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

/**
 * 商品导入导出Service接口
 * Created by macro on 2023/12/22.
 */
public interface PmsProductImportExportService {
    
    /**
     * 导入商品从Excel文件
     */
    @Transactional
    PmsProductImportResult importProductsFromExcel(MultipartFile file, 
                                                  Boolean validateOnly, 
                                                  Boolean skipErrors);
    
    /**
     * 导入商品从CSV文件
     */
    @Transactional
    PmsProductImportResult importProductsFromCsv(MultipartFile file, 
                                                Boolean validateOnly, 
                                                Boolean skipErrors);
    
    /**
     * 导出商品到Excel
     */
    byte[] exportProductsToExcel(PmsProductExportParam exportParam);
    
    /**
     * 导出商品到CSV
     */
    byte[] exportProductsToCsv(PmsProductExportParam exportParam);
    
    /**
     * 导出商品模板
     */
    byte[] downloadImportTemplate(String format);
    
    /**
     * 验证导入文件格式
     */
    Map<String, Object> validateImportFile(MultipartFile file);
    
    /**
     * 获取导入历史
     */
    List<PmsProductImportResult> getImportHistory(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取导入进度
     */
    PmsProductImportResult getImportProgress(Long importId);
    
    /**
     * 取消导入操作
     */
    int cancelImport(Long importId);
    
    /**
     * 清理导入临时文件
     */
    int cleanTempFiles(Integer expireDays);
    
    /**
     * 导出错误报告
     */
    byte[] exportErrorReport(Long importId);
    
    /**
     * 批量导出指定商品
     */
    byte[] exportSelectedProducts(List<Long> productIds, String format);
}