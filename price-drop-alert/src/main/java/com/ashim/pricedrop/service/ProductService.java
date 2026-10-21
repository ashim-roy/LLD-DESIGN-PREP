package com.ashim.pricedrop.service;

import com.ashim.pricedrop.model.Money;
import com.ashim.pricedrop.model.Product;
import com.ashim.pricedrop.repository.ProductRepository;

import java.util.*;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository);
    }

    // create and save a product.
    public Product addProduct(String name, String url, Money price){
        Product newProduct = new Product(name, url, price);
//        productRepository.save(newProduct);
//        return newProduct;
        return productRepository.save(newProduct); // save and return
    }

    //return a product or throw an exception if it doesn't exist
    public Product getProduct(UUID id){
        Optional<Product> findProduct = productRepository.findById(id);
        if(findProduct.isPresent()){
            return findProduct.get();
        }
        throw new IllegalArgumentException("Product not found");

        // return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    // return all products.
    public List<Product> getAllProducts(){
       // List<Product> products = productRepository.findAll();
//        if(products.isEmpty()){
//            throw new IllegalArgumentException("Product not found");
//        }
//        return products;
        // An empty product catalog is a valid state, not necessarily an error.
        return productRepository.findAll();
    }

    // find the product, update its price, and return it.
    public Product updatePrice(UUID productId, Money newPrice){
        Product product = getProduct(productId);
        product.updatePrice(newPrice);
        return product;
    }

}
