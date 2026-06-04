package com.quickcart.controller;

import com.quickcart.model.Product;
import com.quickcart.service.ProductService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(
        ProductService productService
    ) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> products() {
        return productService.getProducts();
    }

    @GetMapping("/health")
    public String health() {
        return "Application Healthy";
    }
}