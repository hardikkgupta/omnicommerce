package com.cloudscale.product.service;

import com.cloudscale.product.model.Product;
import com.cloudscale.product.repository.jpa.ProductJpaRepository;
import com.cloudscale.product.repository.mongo.ProductMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductJpaRepository jpaRepository;
    private final ProductMongoRepository mongoRepository;

    @Autowired
    public ProductService(ProductJpaRepository jpaRepository, ProductMongoRepository mongoRepository) {
        this.jpaRepository = jpaRepository;
        this.mongoRepository = mongoRepository;
    }

    @Transactional
    public Product createProduct(Product product) {
        // Save to both databases
        Product savedProduct = jpaRepository.save(product);
        mongoRepository.save(savedProduct);
        return savedProduct;
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProduct(Long id) {
        // Try JPA first for ACID transactions
        return jpaRepository.findById(id);
    }

    @Transactional
    public Product updateProduct(Long id, Product product) {
        if (jpaRepository.existsById(id)) {
            product.setId(id);
            Product updatedProduct = jpaRepository.save(product);
            mongoRepository.save(updatedProduct);
            return updatedProduct;
        }
        throw new RuntimeException("Product not found with id: " + id);
    }

    @Transactional
    public void deleteProduct(Long id) {
        jpaRepository.deleteById(id);
        mongoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return jpaRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(BigDecimal maxPrice) {
        return jpaRepository.findByPriceLessThanEqual(maxPrice);
    }

    @Transactional(readOnly = true)
    public List<Product> getInStockProducts() {
        return jpaRepository.findInStock();
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String name) {
        return jpaRepository.searchByName(name);
    }

    // High-performance read operations using MongoDB
    public List<Product> getProductsByCategoryMongo(String category) {
        return mongoRepository.findByCategory(category);
    }

    public List<Product> getProductsByPriceRangeMongo(BigDecimal maxPrice) {
        return mongoRepository.findByPriceLessThanEqual(maxPrice);
    }

    public List<Product> getInStockProductsMongo() {
        return mongoRepository.findInStock();
    }

    public List<Product> searchProductsMongo(String name) {
        return mongoRepository.searchByName(name);
    }
} 