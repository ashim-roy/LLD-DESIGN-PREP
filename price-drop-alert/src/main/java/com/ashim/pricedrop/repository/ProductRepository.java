package com.ashim.pricedrop.repository;

import com.ashim.pricedrop.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);
    Optional<Product> findById(UUID id); // need optional to handle the case when product is not found
    List<Product> findAll(); // dont need optional here because we can return an empty list if no products are found
}
