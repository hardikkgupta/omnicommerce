package com.cloudscale.product.controller;

import com.cloudscale.product.model.Product;
import com.cloudscale.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productService.getProduct(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/price")
    public ResponseEntity<List<Product>> getProductsByPriceRange(@RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.getProductsByPriceRange(maxPrice));
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        return ResponseEntity.ok(productService.getInStockProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProducts(name));
    }

    // High-performance MongoDB endpoints
    @GetMapping("/mongo/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategoryMongo(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategoryMongo(category));
    }

    @GetMapping("/mongo/price")
    public ResponseEntity<List<Product>> getProductsByPriceRangeMongo(@RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.getProductsByPriceRangeMongo(maxPrice));
    }

    @GetMapping("/mongo/in-stock")
    public ResponseEntity<List<Product>> getInStockProductsMongo() {
        return ResponseEntity.ok(productService.getInStockProductsMongo());
    }

    @GetMapping("/mongo/search")
    public ResponseEntity<List<Product>> searchProductsMongo(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProductsMongo(name));
    }
} 