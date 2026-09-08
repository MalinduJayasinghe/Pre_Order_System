package lk.ijse.preordersystem.service.impl;

import lk.ijse.preordersystem.dto.CategoryDTO;
import lk.ijse.preordersystem.entity.Category;
import lk.ijse.preordersystem.repository.CategoryRepository;
import lk.ijse.preordersystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {

        log.info("Execute method getAllCategories");

        try {

            List<CategoryDTO> responseList = new ArrayList<>();
            List<Category> categoryList = categoryRepository.findAll();

            for (Category category : categoryList) {

                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setCategoryId(category.getCategoryId());
                categoryDTO.setCategoryName(category.getCategoryName());
                categoryDTO.setDescription(category.getDescription());

                responseList.add(categoryDTO);
            }

            log.info("Categories retrieved successfully");
            return responseList;

        }catch (Exception e){
            log.error("Error in method getAllCategories" + e.getMessage());
            throw e;
        }
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        log.info("Execute method saveCategory");

        try {

            Category category = new Category();
            category.setCategoryName(categoryDTO.getCategoryName());
            category.setDescription(categoryDTO.getDescription());

            Category savedCategory = categoryRepository.save(category);
            categoryDTO.setCategoryId(savedCategory.getCategoryId());

            log.info("Category saved successfully");
            return categoryDTO;

        }catch (Exception e){
            log.error("Error in method saveCategory" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {

        log.info("Execute method deleteCategory");

        try {
            categoryRepository.deleteById(categoryId);
            log.info("Category deleted successfully");

        }catch (Exception e){
            log.error("Error in method deleteCategory" + e.getMessage());
            throw e;
        }
    }
}
