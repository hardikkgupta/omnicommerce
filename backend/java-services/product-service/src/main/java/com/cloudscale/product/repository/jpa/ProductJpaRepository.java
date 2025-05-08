package com.cloudscale.product.repository.jpa;

import com.cloudscale.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    
    @Query("SELECT p FROM Product p WHERE p.price <= ?1")
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0")
    List<Product> findInStock();
    
    @Query(value = "SELECT * FROM products WHERE name ILIKE %?1%", nativeQuery = true)
    List<Product> searchByName(String name);
} 