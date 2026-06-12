package com.enterprise.product.controller;

import com.enterprise.product.model.Product;
import com.enterprise.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ecommerce-api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "product-service"));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @PostMapping("/products/{id}/decrement-stock")
    @Transactional
    public ResponseEntity<Map<String, Object>> decrementStock(@PathVariable Long id,
                                                               @RequestParam Integer quantity) {
        int updated = productRepository.decrementStock(id, quantity);
        if (updated == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient stock", "productId", id));
        }
        return ResponseEntity.ok(Map.of("success", true, "productId", id, "decremented", quantity));
    }
}
