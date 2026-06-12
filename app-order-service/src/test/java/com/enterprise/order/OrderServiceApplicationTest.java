package com.enterprise.order;

import com.enterprise.order.controller.OrderController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderServiceApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.enterprise.order.service.OrderService orderService;

    @Test
    void healthEndpointReturnsUp() throws Exception {
        mockMvc.perform(get("/ecommerce-api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("order-service"));
    }
}
