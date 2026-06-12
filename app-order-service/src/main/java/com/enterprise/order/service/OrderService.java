package com.enterprise.order.service;

import com.enterprise.order.client.ProductServiceClient;
import com.enterprise.order.model.Order;
import com.enterprise.order.model.ProductDto;
import com.enterprise.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long productId, Integer quantity, String customerEmail) {
        ProductDto product = productServiceClient.getProduct(productId);
        if (product == null) {
            throw new RuntimeException("Product not found via product-service: " + productId);
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock()
                    + ", Requested: " + quantity);
        }

        boolean stockDecremented = productServiceClient.decrementStock(productId, quantity);
        if (!stockDecremented) {
            throw new RuntimeException("Failed to decrement stock for product: " + productId);
        }

        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice() * quantity);
        order.setCreatedAt(LocalDateTime.now());
        order.setCustomerEmail(customerEmail);
        order.setStatus("CONFIRMED");

        return orderRepository.save(order);
    }
}
