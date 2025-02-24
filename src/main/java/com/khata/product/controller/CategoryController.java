package com.khata.product.controller;

import com.khata.payload.ApiResponse;
import com.khata.product.dto.CategoryDTO;
import com.khata.product.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/category/")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO category = this.categoryService.createCategory(categoryDTO);
        return new ResponseEntity<CategoryDTO>(category, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<Page<CategoryDTO>> getCategories(Pageable pageable){
        Page<CategoryDTO> categoryDTOSPage = this.categoryService.getCategories(pageable);
        return ResponseEntity.ok(categoryDTOSPage);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            @PathVariable Integer categoryId){
        CategoryDTO updatedCategory = this.categoryService.updateCategory(categoryDTO, categoryId);
        return ResponseEntity.ok(updatedCategory);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryDetails(@PathVariable Integer categoryId){
        CategoryDTO categoryDTO = this.categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(categoryDTO);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        this.categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse("Category deleted successfully", true));
    }
}
