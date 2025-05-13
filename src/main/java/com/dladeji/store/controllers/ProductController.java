package com.dladeji.store.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dladeji.store.dtos.ProductDto;
import com.dladeji.store.entities.Product;
import com.dladeji.store.mappers.ProductMapper;
import com.dladeji.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {
    private ProductRepository productRepository;
    private ProductMapper productMapper;
    
    @GetMapping
    public Iterable<ProductDto> getProducts(
        @RequestParam(required = false, name = "categoryId") Byte categoryId
    ) {
        List<Product> products;
        if (categoryId != null){
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAll();
        }

        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();
        
        
        return ResponseEntity.ok(productMapper.toDto(product));
    }
}
