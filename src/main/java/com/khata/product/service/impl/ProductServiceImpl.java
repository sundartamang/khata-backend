package com.khata.product.service.impl;

import com.khata.exceptions.ResourceNotFoundException;
import com.khata.product.dto.ProductDTO;
import com.khata.product.entity.Category;
import com.khata.product.entity.Product;
import com.khata.product.repositories.ProductRepo;
import com.khata.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepo productRepo, ModelMapper modelMapper) {
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        String productId = generateProductId(productDTO.getName());
        Product product = this.modelMapper.map(productDTO, Product.class);
        product.setProductId(productId);
        Product savedProduct = this.productRepo.save(product);
        log.info("Product created with title: {}", product.getName());
        return this.modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Integer productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId));
        product.setName(product.getName());
        product.setQuantity(product.getQuantity());
        product.setSellingPrice(product.getSellingPrice());
        product.setPurchasePrice(product.getPurchasePrice());
        Product updatedProduct = this.productRepo.save(product);
        log.info("Product updated with ID: {}", productId);
        return this.modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO getProductById(Integer productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId));
        return null;
    }

    @Override
    public Page<ProductDTO> getProducts(Pageable pageable) {
        Page<Product> products = this.productRepo.findAll(pageable);
        return products.map(product -> this.modelMapper.map(product, ProductDTO.class));
    }

    @Override
    public void deleteProduct(Integer productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId));
        log.info("Product deleted with ID: {}", productId);
        this.productRepo.delete(product);
    }

    private String generateProductId(String productName) {
        String prefix = productName.substring(0, 2).toUpperCase();
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 7);
        return prefix + "_" + uniqueSuffix;
    }

}
