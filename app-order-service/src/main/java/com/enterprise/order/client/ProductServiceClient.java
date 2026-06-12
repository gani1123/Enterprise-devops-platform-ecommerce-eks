package com.enterprise.order.client;

import com.enterprise.order.model.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ProductServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.service.url:http://product-service.ecommerce.svc.cluster.local:8080/ecommerce-api}")
    private String productServiceUrl;

    public ProductDto getProduct(Long productId) {
        String url = productServiceUrl + "/products/" + productId;
        return restTemplate.getForObject(url, ProductDto.class);
    }

    public boolean decrementStock(Long productId, Integer quantity) {
        String url = productServiceUrl + "/products/" + productId + "/decrement-stock?quantity=" + quantity;
        try {
            Map response = restTemplate.postForObject(url, null, Map.class);
            return response != null && Boolean.TRUE.equals(response.get("success"));
        } catch (Exception e) {
            return false;
        }
    }
}
