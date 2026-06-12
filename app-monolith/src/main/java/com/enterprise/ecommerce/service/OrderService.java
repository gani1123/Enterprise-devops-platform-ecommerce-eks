package com.enterprise.ecommerce.service;

import com.enterprise.ecommerce.model.Order;
import com.enterprise.ecommerce.model.Product;
import com.enterprise.ecommerce.repository.OrderRepository;
import com.enterprise.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long productId, Integer quantity, String customerEmail) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        int updated = productRepository.decrementStock(productId, quantity);
        if (updated == 0) {
            throw new RuntimeException("Insufficient stock for product: " + productId
                    + ". Available: " + product.getStock() + ", Requested: " + quantity);
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
