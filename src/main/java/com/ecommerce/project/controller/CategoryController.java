package com.ecommerce.project.controller;
import com.ecommerce.project.configurations.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.payload.CategoryUpdateDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = {"/api"})
public class CategoryController {
    private final Logger logger= LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService=categoryService;
    }
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",required = false,defaultValue =AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name="pageSize",required = false,defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name="sortBy",required = false,defaultValue = AppConstants.SORT_BY)String sortBy,
            @RequestParam(name="sortOrder",required = false,defaultValue = AppConstants.SORT_ORDER)String sortOrder
    ) {
        logger.info("CALLED TO GET ALL CATEGORIES.....");
        return ResponseEntity.ok(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder));
    }
    @GetMapping("public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long categoryId){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategory(categoryId));
    }
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategories(@Valid @RequestBody CategoryDTO categoryDTO) {
            logger.info("CALLED TO CREATE A NEW CATEGORY");
            return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
            logger.info("CALLED TO DELETE A CATEGORY.....");
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));

    }
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid@RequestBody CategoryUpdateDTO categoryUpdateDTO, @PathVariable Long categoryId){
        logger.info("CALLED TO UPDATE A CATEGORY WITH ID{} .....", categoryId);
            return ResponseEntity.ok(categoryService.updateCategory(categoryUpdateDTO,categoryId));
    }

}

