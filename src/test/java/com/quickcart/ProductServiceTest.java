package com.quickcart;

import com.quickcart.service.ProductService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    ProductService service =
            new ProductService();

    @Test
    void productListShouldNotBeEmpty() {

        assertFalse(
            service.getProducts().isEmpty()
        );
    }
}