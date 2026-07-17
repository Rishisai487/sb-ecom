package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.payload.CategoryUpdateDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import org.hibernate.query.SortDirection;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService{
    private final Logger logger= LoggerFactory.getLogger(CategoryServiceImp.class);
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sort= sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> categoryPage=categoryRepository.findAll(pageable);
        List<Category> categories=categoryPage.getContent();
        if(categories.isEmpty()){
            throw new APIException("No Category Present in the Database!!!");
        }
        List<CategoryDTO> categoryDTOS=categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        return new CategoryResponse(categoryDTOS,categoryPage.getNumber(),categoryPage.getSize(),categoryPage.getTotalElements(),categoryPage.getTotalPages(),categoryPage.isLast());
    }
    public CategoryDTO getCategory(Long categoryId){
        return modelMapper.map(categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId)),CategoryDTO.class);
    }
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<Category> existingCategory=categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if(existingCategory.isPresent()){
            throw new APIException("Category with name "+categoryDTO.getCategoryName()+" already exists!");
        }
        Category category=modelMapper.map(categoryDTO,Category.class);
        Category savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category existingCategory=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(existingCategory);
        return modelMapper.map(existingCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryUpdateDTO categoryUpdateDTO, Long categoryId) {
        Category existingCategory=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        Optional<Category> category=categoryRepository.findByCategoryName(categoryUpdateDTO.getCategoryName());
        if(category.isPresent() &&!(existingCategory.getCategoryId().equals(category.get().getCategoryId()))){
            throw new APIException("Category with name "+categoryUpdateDTO.getCategoryName()+" already exists!");
        }
        modelMapper.map(categoryUpdateDTO,existingCategory);
        return modelMapper.map(categoryRepository.save(existingCategory),CategoryDTO.class);
    }
}
