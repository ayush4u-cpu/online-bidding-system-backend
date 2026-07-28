package com.bidding.orderservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bidding.orderservice.dto.OrderDto;
import com.bidding.orderservice.exception.ResourceNotFoundException;
import com.bidding.orderservice.service.OrderService;

@SpringBootTest
class OrderControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private OrderService orderService;

    private OrderDto testOrder;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testOrder = new OrderDto();
        testOrder.setId("ORD-1001");
        testOrder.setProductName("Laptop");
        testOrder.setSpecifications("16GB RAM, 512GB SSD");
        testOrder.setPrice(1200.0);
        testOrder.setStatus("PENDING");
        testOrder.setDeliveryPerson("John Rider");
        testOrder.setImage("laptop.jpg");
        testOrder.setBuyerEmail("buyer@example.com");
    }

    // --- CREATE ORDER ---

    @Test
    void createOrder_success() throws Exception {
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(testOrder);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"ORD-1001\",\"productName\":\"Laptop\",\"specifications\":\"16GB RAM, 512GB SSD\",\"price\":1200.0,\"status\":\"PENDING\",\"deliveryPerson\":\"John Rider\",\"image\":\"laptop.jpg\",\"buyerEmail\":\"buyer@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("ORD-1001"))
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }

    // --- GET ORDER BY ID ---

    @Test
    void getOrderById_success_numericId() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(testOrder);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("ORD-1001"));
    }

    @Test
    void getOrderById_success_formattedId() throws Exception {
        // ORD-2001 translates to (2001 - 1000) = 1001L in parseOrderId
        when(orderService.getOrderById(1001L)).thenReturn(testOrder);

        mockMvc.perform(get("/orders/ORD-2001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("ORD-1001"));
    }

    @Test
    void getOrderById_invalidFormat_returns400() throws Exception {
        mockMvc.perform(get("/orders/ORD-abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid order ID format: ORD-abc"));
    }

    @Test
    void getOrderById_notFound_returns404() throws Exception {
        when(orderService.getOrderById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Order not found with id: 999"));

        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found with id: 999"));
    }

    // --- GET ORDERS BY BUYER ---

    @Test
    void getOrdersByBuyer_success() throws Exception {
        when(orderService.getOrdersByBuyer("buyer@example.com")).thenReturn(Arrays.asList(testOrder));

        mockMvc.perform(get("/orders/buyer/buyer@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].buyerEmail").value("buyer@example.com"));
    }

    // --- GET ORDERS BY DELIVERY PERSON ---

    @Test
    void getOrdersByDeliveryPerson_success() throws Exception {
        when(orderService.getOrdersByDeliveryPerson("John Rider")).thenReturn(Arrays.asList(testOrder));

        mockMvc.perform(get("/orders/delivery/John Rider"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deliveryPerson").value("John Rider"));
    }

    // --- GET ALL ORDERS ---

    @Test
    void getAllOrders_success() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Arrays.asList(testOrder));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ORD-1001"));
    }

    // --- UPDATE ORDER STATUS ---

    @Test
    void updateOrderStatus_success() throws Exception {
        testOrder.setStatus("SHIPPED");
        when(orderService.updateOrderStatus(1L, "SHIPPED")).thenReturn(testOrder);

        Map<String, String> payload = new HashMap<>();
        payload.put("status", "SHIPPED");

        mockMvc.perform(put("/orders/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"SHIPPED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void updateOrderStatus_notFound_returns404() throws Exception {
        when(orderService.updateOrderStatus(anyLong(), anyString()))
                .thenThrow(new ResourceNotFoundException("Order not found with id: 999"));

        Map<String, String> payload = new HashMap<>();
        payload.put("status", "SHIPPED");

        mockMvc.perform(put("/orders/999/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"SHIPPED\"}"))
                .andExpect(status().isNotFound());
    }

    // --- ASSIGN DELIVERY PERSON ---

    @Test
    void assignDeliveryPerson_success() throws Exception {
        testOrder.setDeliveryPerson("David");
        when(orderService.assignDeliveryPerson(1L, "David")).thenReturn(testOrder);

        Map<String, String> payload = new HashMap<>();
        payload.put("deliveryPerson", "David");

        mockMvc.perform(put("/orders/1/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deliveryPerson\":\"David\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryPerson").value("David"));
    }

    @Test
    void assignDeliveryPerson_notFound_returns404() throws Exception {
        when(orderService.assignDeliveryPerson(anyLong(), anyString()))
                .thenThrow(new ResourceNotFoundException("Order not found with id: 999"));

        Map<String, String> payload = new HashMap<>();
        payload.put("deliveryPerson", "David");

        mockMvc.perform(put("/orders/999/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deliveryPerson\":\"David\"}"))
                .andExpect(status().isNotFound());
    }
}
