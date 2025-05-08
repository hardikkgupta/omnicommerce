package com.cloudscale.product.repository.mongo;

import com.cloudscale.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductMongoRepository extends MongoRepository<Product, Long> {
    List<Product> findByCategory(String category);
    
    @Query("{ 'price' : { $lte : ?0 } }")
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    
    @Query("{ 'stockQuantity' : { $gt : 0 } }")
    List<Product> findInStock();
    
    @Query("{ 'name' : { $regex : ?0, $options: 'i' } }")
    List<Product> searchByName(String name);
} 