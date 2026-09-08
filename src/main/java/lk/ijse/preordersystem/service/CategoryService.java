package lk.ijse.preordersystem.service;

import lk.ijse.preordersystem.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();
    CategoryDTO saveCategory(CategoryDTO categoryDTO);
    void deleteCategory(Long categoryId);
}
