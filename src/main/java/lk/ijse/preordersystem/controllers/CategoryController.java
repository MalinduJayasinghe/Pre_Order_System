package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CategoryDTO;
import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllCategories() {

        log.info("getAllCategories API was called");
        List<CategoryDTO> allCategories = categoryService.getAllCategories();

        log.info("getAllCategories API successful");
        return new CommonResponse(0, allCategories, "Categories called");
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addCategory(@RequestBody CategoryDTO categoryDTO) {

        log.info("addCategory API was called");
        CategoryDTO savedCategory = categoryService.saveCategory(categoryDTO);

        log.info("addCategory API successful");
        return new CommonResponse(0, savedCategory, "Category added successfully");
    }

    @DeleteMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteCategory(@PathVariable Long categoryId) {

        log.info("deleteCategory API was called");
        categoryService.deleteCategory(categoryId);

        log.info("deleteCategory API successful");
        return new CommonResponse(0, "Category Deleted", "Category deleted successfully");
    }
}
