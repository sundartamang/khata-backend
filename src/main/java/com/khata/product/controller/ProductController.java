package com.khata.product.controller;

import com.khata.payload.ApiResponse;
import com.khata.product.dto.ProductDTO;
import com.khata.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO){
        ProductDTO product = this.productService.createProduct(productDTO);
        return new ResponseEntity<ProductDTO>(product, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<Page<ProductDTO>> getProducts(Pageable pageable){
        Page<ProductDTO> product = this.productService.getProducts(pageable);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Integer productId){
        ProductDTO updatedProduct = this.productService.updateProduct(productDTO, productId);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductDetails(@PathVariable Integer productId){
        ProductDTO productDTO = this.productService.getProductById(productId);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Integer productId){
        this.productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully", true));
    }
}
