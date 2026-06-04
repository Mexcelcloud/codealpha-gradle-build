package com.quickcart.service;

import com.quickcart.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    public List<Product> getProducts() {

        return List.of(
            new Product(1L,"Rice",25.50),
            new Product(2L,"Beans",15.00),
            new Product(3L,"Milk",8.75)
        );
    }
}