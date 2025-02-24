package com.khata.product.service;

import com.khata.product.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId);
    CategoryDTO getCategoryById(Integer categoryId);
    Page<CategoryDTO> getCategories(Pageable pageable);
    void deleteCategory(Integer categoryId);
}
