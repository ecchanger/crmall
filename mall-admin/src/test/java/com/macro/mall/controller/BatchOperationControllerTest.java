package com.macro.mall.controller;

import com.macro.mall.dto.BatchOperationDto;
import com.macro.mall.dto.BatchOperationResultDto;
import com.macro.mall.enums.BatchOperationType;
import com.macro.mall.service.BatchOperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BatchOperationController.class)
@DisplayName("Batch Operation Controller Integration Tests")
class BatchOperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatchOperationService batchOperationService;

    @Autowired
    private ObjectMapper objectMapper;

    private BatchOperationDto batchOperationDto;
    private BatchOperationResultDto resultDto;

    @BeforeEach
    void setUp() {
        batchOperationDto = new BatchOperationDto();
        batchOperationDto.setOperationType(BatchOperationType.UPDATE);
        batchOperationDto.setProductIds(Arrays.asList(1L, 2L, 3L));
        batchOperationDto.setUpdateFields(Collections.singletonMap("name", "Updated Name"));

        resultDto = new BatchOperationResultDto();
        resultDto.setTotalRequested(3);
        resultDto.setSuccessfulCount(3);
        resultDto.setFailedCount(0);
        resultDto.setProcessedProductIds(Arrays.asList(1L, 2L, 3L));
    }

    @Test
    @DisplayName("Should execute batch update operation successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldExecuteBatchUpdateOperationSuccessfully() throws Exception {
        when(batchOperationService.executeBatchOperation(any(BatchOperationDto.class)))
                .thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.totalRequested").value(3))
                .andExpect(jsonPath("$.data.successfulCount").value(3))
                .andExpect(jsonPath("$.data.failedCount").value(0));

        verify(batchOperationService).executeBatchOperation(any(BatchOperationDto.class));
    }

    @Test
    @DisplayName("Should execute batch delete operation successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldExecuteBatchDeleteOperationSuccessfully() throws Exception {
        batchOperationDto.setOperationType(BatchOperationType.DELETE);
        batchOperationDto.setUpdateFields(null);

        when(batchOperationService.executeBatchOperation(any(BatchOperationDto.class)))
                .thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalRequested").value(3));

        verify(batchOperationService).executeBatchOperation(any(BatchOperationDto.class));
    }

    @Test
    @DisplayName("Should execute batch status update successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldExecuteBatchStatusUpdateSuccessfully() throws Exception {
        batchOperationDto.setOperationType(BatchOperationType.STATUS_UPDATE);
        batchOperationDto.setUpdateFields(Collections.singletonMap("publishStatus", "PUBLISHED"));

        when(batchOperationService.executeBatchOperation(any(BatchOperationDto.class)))
                .thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(batchOperationService).executeBatchOperation(any(BatchOperationDto.class));
    }

    @Test
    @DisplayName("Should execute batch price update successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldExecuteBatchPriceUpdateSuccessfully() throws Exception {
        batchOperationDto.setOperationType(BatchOperationType.PRICE_UPDATE);
        batchOperationDto.setUpdateFields(Collections.singletonMap("price", "29.99"));

        when(batchOperationService.executeBatchOperation(any(BatchOperationDto.class)))
                .thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(batchOperationService).executeBatchOperation(any(BatchOperationDto.class));
    }

    @Test
    @DisplayName("Should execute batch category assignment successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldExecuteBatchCategoryAssignmentSuccessfully() throws Exception {
        batchOperationDto.setOperationType(BatchOperationType.CATEGORY_ASSIGNMENT);
        batchOperationDto.setUpdateFields(Collections.singletonMap("productCategoryId", "5"));

        when(batchOperationService.executeBatchOperation(any(BatchOperationDto.class)))
                .thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(batchOperationService).executeBatchOperation(any(BatchOperationDto.class));
    }

    @Test
    @DisplayName("Should get operation history successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetOperationHistorySuccessfully() throws Exception {
        List<BatchOperationResultDto> history = Arrays.asList(resultDto);
        when(batchOperationService.getOperationHistory(0, 10)).thenReturn(history);

        mockMvc.perform(get("/admin/batch/history")
                .param("pageNum", "0")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].totalRequested").value(3));

        verify(batchOperationService).getOperationHistory(0, 10);
    }

    @Test
    @DisplayName("Should get operation result by ID successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetOperationResultByIdSuccessfully() throws Exception {
        when(batchOperationService.getOperationResult("batch-123")).thenReturn(resultDto);

        mockMvc.perform(get("/admin/batch/result/batch-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalRequested").value(3));

        verify(batchOperationService).getOperationResult("batch-123");
    }

    @Test
    @DisplayName("Should cancel batch operation successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldCancelBatchOperationSuccessfully() throws Exception {
        when(batchOperationService.cancelOperation("batch-123")).thenReturn(true);

        mockMvc.perform(post("/admin/batch/cancel/batch-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(batchOperationService).cancelOperation("batch-123");
    }

    @Test
    @DisplayName("Should get active operations successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetActiveOperationsSuccessfully() throws Exception {
        List<BatchOperationResultDto> activeOps = Arrays.asList(resultDto);
        when(batchOperationService.getActiveOperations()).thenReturn(activeOps);

        mockMvc.perform(get("/admin/batch/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());

        verify(batchOperationService).getActiveOperations();
    }

    @Test
    @DisplayName("Should validate batch operation successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldValidateBatchOperationSuccessfully() throws Exception {
        when(batchOperationService.validateOperation(any(BatchOperationDto.class)))
                .thenReturn(Collections.singletonList("Operation validated successfully"));

        mockMvc.perform(post("/admin/batch/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());

        verify(batchOperationService).validateOperation(any(BatchOperationDto.class));
    }

    @Test
    @DisplayName("Should preview batch operation successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldPreviewBatchOperationSuccessfully() throws Exception {
        when(batchOperationService.previewOperation(any(BatchOperationDto.class)))
                .thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/preview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalRequested").value(3));

        verify(batchOperationService).previewOperation(any(BatchOperationDto.class));
    }

    @Test
    @DisplayName("Should retry failed operations successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldRetryFailedOperationsSuccessfully() throws Exception {
        when(batchOperationService.retryFailedOperations("batch-123")).thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/retry/batch-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalRequested").value(3));

        verify(batchOperationService).retryFailedOperations("batch-123");
    }

    @Test
    @DisplayName("Should get batch operation statistics successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetBatchOperationStatisticsSuccessfully() throws Exception {
        mockMvc.perform(get("/admin/batch/statistics"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.code").value(200));

        verify(batchOperationService).getOperationStatistics();
    }

    @Test
    @DisplayName("Should reject unauthorized batch operation request")
    void shouldRejectUnauthorizedBatchOperationRequest() throws Exception {
        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isUnauthorized());

        verify(batchOperationService, never()).executeBatchOperation(any());
    }

    @Test
    @DisplayName("Should handle validation errors gracefully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldHandleValidationErrorsGracefully() throws Exception {
        batchOperationDto.setProductIds(null); // Invalid DTO

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpect(status().isBadRequest());

        verify(batchOperationService, never()).executeBatchOperation(any());
    }

    @Test
    @DisplayName("Should handle service exceptions gracefully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldHandleServiceExceptionsGracefully() throws Exception {
        when(batchOperationService.executeBatchOperation(any(BatchOperationDto.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpected(status().isInternalServerError());

        verify(batchOperationService).executeBatchOperation(any());
    }

    @Test
    @DisplayName("Should require proper role for batch operations")
    @WithMockUser(roles = {"USER"})
    void shouldRequireProperRoleForBatchOperations() throws Exception {
        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpected(status().isForbidden());

        verify(batchOperationService, never()).executeBatchOperation(any());
    }

    @Test
    @DisplayName("Should handle large batch operations gracefully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldHandleLargeBatchOperationsGracefully() throws Exception {
        // Create a large batch operation (1000 products)
        Long[] productIds = new Long[1000];
        for (int i = 0; i < 1000; i++) {
            productIds[i] = (long) (i + 1);
        }
        batchOperationDto.setProductIds(Arrays.asList(productIds));

        resultDto.setTotalRequested(1000);
        resultDto.setSuccessfulCount(1000);
        when(batchOperationService.executeBatchOperation(any(BatchOperationDto.class)))
                .thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.data.totalRequested").value(1000));

        verify(batchOperationService).executeBatchOperation(any());
    }

    @Test
    @DisplayName("Should handle partial success in batch operations")
    @WithMockUser(roles = {"ADMIN"})
    void shouldHandlePartialSuccessInBatchOperations() throws Exception {
        resultDto.setSuccessfulCount(2);
        resultDto.setFailedCount(1);
        resultDto.setFailedProductIds(Arrays.asList(3L));
        resultDto.setErrors(Collections.singletonMap(3L, "Product not found"));

        when(batchOperationService.executeBatchOperation(any(BatchOperationDto.class)))
                .thenReturn(resultDto);

        mockMvc.perform(post("/admin/batch/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchOperationDto)))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.data.successfulCount").value(2))
                .andExpected(jsonPath("$.data.failedCount").value(1));

        verify(batchOperationService).executeBatchOperation(any());
    }

    @Test
    @DisplayName("Should export operation results successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldExportOperationResultsSuccessfully() throws Exception {
        byte[] exportData = "CSV export data".getBytes();
        when(batchOperationService.exportResults("batch-123", "CSV")).thenReturn(exportData);

        mockMvc.perform(get("/admin/batch/export/batch-123")
                .param("format", "CSV"))
                .andExpected(status().isOk())
                .andExpected(header().string("Content-Type", "application/octet-stream"));

        verify(batchOperationService).exportResults("batch-123", "CSV");
    }
}