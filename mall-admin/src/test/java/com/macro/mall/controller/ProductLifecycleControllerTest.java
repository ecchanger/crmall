package com.macro.mall.controller;

import com.macro.mall.dto.ProductLifecycleTransitionDto;
import com.macro.mall.dto.ProductLifecycleResponseDto;
import com.macro.mall.enums.ProductLifecycleStatus;
import com.macro.mall.model.ProductLifecycle;
import com.macro.mall.service.ProductLifecycleService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductLifecycleController.class)
@DisplayName("Product Lifecycle Controller Integration Tests")
class ProductLifecycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductLifecycleService productLifecycleService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductLifecycleResponseDto responseDto;
    private ProductLifecycleTransitionDto transitionDto;

    @BeforeEach
    void setUp() {
        responseDto = new ProductLifecycleResponseDto();
        responseDto.setProductId(1L);
        responseDto.setCurrentStatus(ProductLifecycleStatus.DRAFT);
        responseDto.setCreatedAt(LocalDateTime.now());
        responseDto.setUpdatedAt(LocalDateTime.now());

        transitionDto = new ProductLifecycleTransitionDto();
        transitionDto.setProductId(1L);
        transitionDto.setTargetStatus(ProductLifecycleStatus.SUBMITTED);
        transitionDto.setComment("Ready for review");
        transitionDto.setForceTransition(false);
    }

    @Test
    @DisplayName("Should transition product status successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldTransitionProductStatusSuccessfully() throws Exception {
        when(productLifecycleService.transitionStatus(any(ProductLifecycleTransitionDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/admin/lifecycle/transition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transitionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.productId").value(1L))
                .andExpect(jsonPath("$.data.currentStatus").value("DRAFT"));

        verify(productLifecycleService).transitionStatus(any(ProductLifecycleTransitionDto.class));
    }

    @Test
    @DisplayName("Should reject unauthorized transition request")
    void shouldRejectUnauthorizedTransitionRequest() throws Exception {
        mockMvc.perform(post("/admin/lifecycle/transition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transitionDto)))
                .andExpect(status().isUnauthorized());

        verify(productLifecycleService, never()).transitionStatus(any());
    }

    @Test
    @DisplayName("Should get product lifecycle successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetProductLifecycleSuccessfully() throws Exception {
        when(productLifecycleService.getProductLifecycle(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/admin/lifecycle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.productId").value(1L))
                .andExpect(jsonPath("$.data.currentStatus").value("DRAFT"));

        verify(productLifecycleService).getProductLifecycle(1L);
    }

    @Test
    @DisplayName("Should get lifecycle history successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetLifecycleHistorySuccessfully() throws Exception {
        List<ProductLifecycleResponseDto> history = Arrays.asList(responseDto);
        when(productLifecycleService.getLifecycleHistory(1L)).thenReturn(history);

        mockMvc.perform(get("/admin/lifecycle/1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].productId").value(1L));

        verify(productLifecycleService).getLifecycleHistory(1L);
    }

    @Test
    @DisplayName("Should get possible transitions successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetPossibleTransitionsSuccessfully() throws Exception {
        List<ProductLifecycleStatus> transitions = Arrays.asList(ProductLifecycleStatus.SUBMITTED);
        when(productLifecycleService.getPossibleTransitions(1L)).thenReturn(transitions);

        mockMvc.perform(get("/admin/lifecycle/1/transitions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpected(jsonPath("$.data[0]").value("SUBMITTED"));

        verify(productLifecycleService).getPossibleTransitions(1L);
    }

    @Test
    @DisplayName("Should bulk transition products successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldBulkTransitionProductsSuccessfully() throws Exception {
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);
        List<ProductLifecycleResponseDto> responses = Arrays.asList(responseDto);
        
        when(productLifecycleService.bulkTransition(eq(productIds), eq(ProductLifecycleStatus.PUBLISHED), anyString()))
                .thenReturn(responses);

        mockMvc.perform(post("/admin/lifecycle/bulk-transition")
                .param("productIds", "1,2,3")
                .param("targetStatus", "PUBLISHED")
                .param("comment", "Bulk publishing"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200))
                .andExpected(jsonPath("$.data").isArray());

        verify(productLifecycleService).bulkTransition(eq(productIds), eq(ProductLifecycleStatus.PUBLISHED), eq("Bulk publishing"));
    }

    @Test
    @DisplayName("Should get products by status successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetProductsByStatusSuccessfully() throws Exception {
        List<ProductLifecycleResponseDto> products = Arrays.asList(responseDto);
        when(productLifecycleService.getProductsByStatus(ProductLifecycleStatus.DRAFT)).thenReturn(products);

        mockMvc.perform(get("/admin/lifecycle/status/DRAFT"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200))
                .andExpected(jsonPath("$.data").isArray())
                .andExpected(jsonPath("$.data[0].currentStatus").value("DRAFT"));

        verify(productLifecycleService).getProductsByStatus(ProductLifecycleStatus.DRAFT);
    }

    @Test
    @DisplayName("Should approve product successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldApproveProductSuccessfully() throws Exception {
        when(productLifecycleService.approveProduct(1L, "admin123", "Quality checked"))
                .thenReturn(responseDto);

        mockMvc.perform(post("/admin/lifecycle/1/approve")
                .param("approverId", "admin123")
                .param("comment", "Quality checked"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200))
                .andExpected(jsonPath("$.data.productId").value(1L));

        verify(productLifecycleService).approveProduct(1L, "admin123", "Quality checked");
    }

    @Test
    @DisplayName("Should reject product successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldRejectProductSuccessfully() throws Exception {
        when(productLifecycleService.rejectProduct(1L, "admin123", "Needs improvement"))
                .thenReturn(responseDto);

        mockMvc.perform(post("/admin/lifecycle/1/reject")
                .param("reviewerId", "admin123")
                .param("reason", "Needs improvement"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200))
                .andExpected(jsonPath("$.data.productId").value(1L));

        verify(productLifecycleService).rejectProduct(1L, "admin123", "Needs improvement");
    }

    @Test
    @DisplayName("Should publish product successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldPublishProductSuccessfully() throws Exception {
        when(productLifecycleService.publishProduct(1L, "admin123"))
                .thenReturn(responseDto);

        mockMvc.perform(post("/admin/lifecycle/1/publish")
                .param("publisherId", "admin123"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200))
                .andExpected(jsonPath("$.data.productId").value(1L));

        verify(productLifecycleService).publishProduct(1L, "admin123");
    }

    @Test
    @DisplayName("Should archive product successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldArchiveProductSuccessfully() throws Exception {
        when(productLifecycleService.archiveProduct(1L, "admin123", "End of lifecycle"))
                .thenReturn(responseDto);

        mockMvc.perform(post("/admin/lifecycle/1/archive")
                .param("archiverId", "admin123")
                .param("reason", "End of lifecycle"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200))
                .andExpected(jsonPath("$.data.productId").value(1L));

        verify(productLifecycleService).archiveProduct(1L, "admin123", "End of lifecycle");
    }

    @Test
    @DisplayName("Should handle validation errors gracefully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldHandleValidationErrorsGracefully() throws Exception {
        transitionDto.setProductId(null); // Invalid DTO

        mockMvc.perform(post("/admin/lifecycle/transition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transitionDto)))
                .andExpected(status().isBadRequest());

        verify(productLifecycleService, never()).transitionStatus(any());
    }

    @Test
    @DisplayName("Should handle service exceptions gracefully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldHandleServiceExceptionsGracefully() throws Exception {
        when(productLifecycleService.transitionStatus(any(ProductLifecycleTransitionDto.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/admin/lifecycle/transition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transitionDto)))
                .andExpected(status().isInternalServerError());

        verify(productLifecycleService).transitionStatus(any());
    }

    @Test
    @DisplayName("Should require proper role for management operations")
    @WithMockUser(roles = {"USER"})
    void shouldRequireProperRoleForManagementOperations() throws Exception {
        mockMvc.perform(post("/admin/lifecycle/transition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transitionDto)))
                .andExpected(status().isForbidden());

        verify(productLifecycleService, never()).transitionStatus(any());
    }

    @Test
    @DisplayName("Should get lifecycle statistics successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetLifecycleStatisticsSuccessfully() throws Exception {
        mockMvc.perform(get("/admin/lifecycle/statistics"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200));

        verify(productLifecycleService).getLifecycleStatistics();
    }

    @Test
    @DisplayName("Should search lifecycle records successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldSearchLifecycleRecordsSuccessfully() throws Exception {
        List<ProductLifecycleResponseDto> results = Arrays.asList(responseDto);
        when(productLifecycleService.searchLifecycleRecords(anyString(), any(), any(), anyInt(), anyInt()))
                .thenReturn(results);

        mockMvc.perform(get("/admin/lifecycle/search")
                .param("keyword", "test")
                .param("status", "DRAFT")
                .param("pageSize", "10")
                .param("pageNum", "1"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200))
                .andExpected(jsonPath("$.data").isArray());

        verify(productLifecycleService).searchLifecycleRecords(eq("test"), any(), any(), eq(1), eq(10));
    }

    @Test
    @DisplayName("Should validate pending approvals successfully")
    @WithMockUser(roles = {"ADMIN"})
    void shouldValidatePendingApprovalsSuccessfully() throws Exception {
        List<ProductLifecycleResponseDto> pending = Arrays.asList(responseDto);
        when(productLifecycleService.getPendingApprovals()).thenReturn(pending);

        mockMvc.perform(get("/admin/lifecycle/pending"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.code").value(200))
                .andExpected(jsonPath("$.data").isArray());

        verify(productLifecycleService).getPendingApprovals();
    }
}