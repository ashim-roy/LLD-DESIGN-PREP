package com.ashim.pricedrop.repository;

import com.ashim.pricedrop.model.Product;

import java.util.*;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> productMap = new HashMap<>();

    @Override
    public Product save(Product product ) {
//        if(product == null) {
//            throw new NullPointerException("Product cannot be null");
//        }
//        productMap.put(product.getId(), product);
//        return product;
        Objects.requireNonNull(product, "Product cannot be null");
        productMap.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(UUID id) {
//        if(productMap.containsKey(id)) {
//            return Optional.of(productMap.get(id));
//        }
//        return Optional.empty();
        return Optional.ofNullable(productMap.get(id));

    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productMap.values());
    }
}
