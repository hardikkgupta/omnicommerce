package com.cloudscale.product.service;

import com.cloudscale.product.model.Product;
import com.cloudscale.product.repository.jpa.ProductJpaRepository;
import com.cloudscale.product.repository.mongo.ProductMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductJpaRepository jpaRepository;

    @Mock
    private ProductMongoRepository mongoRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStockQuantity(100);
        testProduct.setCategory("Test Category");
    }

    @Test
    void createProduct_Success() {
        when(jpaRepository.save(any(Product.class))).thenReturn(testProduct);
        when(mongoRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        verify(jpaRepository).save(any(Product.class));
        verify(mongoRepository).save(any(Product.class));
    }

    @Test
    void getProduct_Success() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> result = productService.getProduct(1L);

        assertTrue(result.isPresent());
        assertEquals(testProduct.getName(), result.get().getName());
    }

    @Test
    void getProduct_NotFound() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProduct(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void updateProduct_Success() {
        when(jpaRepository.existsById(1L)).thenReturn(true);
        when(jpaRepository.save(any(Product.class))).thenReturn(testProduct);
        when(mongoRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.updateProduct(1L, testProduct);

        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        verify(jpaRepository).save(any(Product.class));
        verify(mongoRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        when(jpaRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productService.updateProduct(1L, testProduct));
    }

    @Test
    void getProductsByCategory_Success() {
        List<Product> products = Arrays.asList(testProduct);
        when(jpaRepository.findByCategory("Test Category")).thenReturn(products);

        List<Product> result = productService.getProductsByCategory("Test Category");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
    }

    @Test
    void getProductsByPriceRange_Success() {
        List<Product> products = Arrays.asList(testProduct);
        when(jpaRepository.findByPriceLessThanEqual(any(BigDecimal.class))).thenReturn(products);

        List<Product> result = productService.getProductsByPriceRange(new BigDecimal("100.00"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
    }

    @Test
    void getInStockProducts_Success() {
        List<Product> products = Arrays.asList(testProduct);
        when(jpaRepository.findInStock()).thenReturn(products);

        List<Product> result = productService.getInStockProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
    }

    @Test
    void searchProducts_Success() {
        List<Product> products = Arrays.asList(testProduct);
        when(jpaRepository.searchByName("Test")).thenReturn(products);

        List<Product> result = productService.searchProducts("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
    }
} 