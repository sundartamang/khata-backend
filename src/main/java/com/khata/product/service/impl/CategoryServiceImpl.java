package com.khata.product.service.impl;

import com.khata.exceptions.ResourceNotFoundException;
import com.khata.product.dto.CategoryDTO;
import com.khata.product.entity.Category;
import com.khata.product.repositories.CategoryRepo;
import com.khata.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = this.modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = this.categoryRepo.save(category);
        log.info("Category created with title: {}", category.getTitle());
        return this.modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId));
        category.setTitle(categoryDTO.getTitle());
        Category updatedCategory = this.categoryRepo.save(category);
        log.info("Category updated with ID: {}", categoryId);
        return this.modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO getCategoryById(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId));
        return this.modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public Page<CategoryDTO> getCategories(Pageable pageable) {
        Page<Category> categories = this.categoryRepo.findAll(pageable);
        return categories.map(category -> this.modelMapper.map(category, CategoryDTO.class));
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId));
        log.info("Category deleted with ID: {}", categoryId);
        this.categoryRepo.delete(category);
    }
}
