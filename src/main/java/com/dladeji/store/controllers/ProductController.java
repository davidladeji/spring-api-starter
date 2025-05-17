package com.dladeji.store.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.dladeji.store.dtos.ProductDto;
import com.dladeji.store.entities.Product;
import com.dladeji.store.mappers.ProductMapper;
import com.dladeji.store.repositories.CategoryRepository;
import com.dladeji.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {
    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;
    
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

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
        @RequestBody ProductDto request,
        UriComponentsBuilder uriBuilder
        ) {
        if (request.getCategoryId() == null)
            return ResponseEntity.badRequest().build();
        // Assumes categoryId always being passed
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null)
            return ResponseEntity.badRequest().build();

        var product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);
        request.setId(product.getId());

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductDto request
        ) {
        
        var product = productRepository.findById(id).orElse(null);
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();
        
        if (category == null)
            return ResponseEntity.badRequest().build();

        productMapper.update(request, product);
        product.setCategory(category);
        productRepository.save(product);
        request.setId(id);   
        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable Long id
        ) {
        
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();

        productRepository.delete(product);   
        return ResponseEntity.noContent().build();
    }
    
}
