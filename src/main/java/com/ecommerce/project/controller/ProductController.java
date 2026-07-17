package com.ecommerce.project.controller;

import com.ecommerce.project.configurations.AppConstants;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId){
        ProductDTO savedProductDTO =productService.addProduct(categoryId,productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(required = false,defaultValue = AppConstants.PAGE_NUMBER)int pageNumber,
                                                          @RequestParam(required = false,defaultValue = AppConstants.PAGE_SIZE)int pageSize,
                                                          @RequestParam(required = false,defaultValue = AppConstants.SORT_BY_PRODUCT) String sortBy,
                                                          @RequestParam(required = false,defaultValue = AppConstants.SORT_ORDER) String sortOrder){
        return ResponseEntity.ok(productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder));
    }
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,@RequestParam(required = false,defaultValue = AppConstants.PAGE_NUMBER)int pageNumber,
                                                                 @RequestParam(required = false,defaultValue = AppConstants.PAGE_SIZE)int pageSize,
                                                                 @RequestParam(required = false,defaultValue = AppConstants.SORT_BY_PRODUCT) String sortBy,
                                                                 @RequestParam(required = false,defaultValue = AppConstants.SORT_ORDER) String sortOrder){
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder));
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable(required = false) String keyword,@RequestParam(required = false,defaultValue = AppConstants.PAGE_NUMBER)int pageNumber,
                                                                @RequestParam(required = false,defaultValue = AppConstants.PAGE_SIZE)int pageSize,
                                                                @RequestParam(required = false,defaultValue = AppConstants.SORT_BY_PRODUCT) String sortBy,
                                                                @RequestParam(required = false,defaultValue = AppConstants.SORT_ORDER) String sortOrder){
        return ResponseEntity.status(HttpStatus.FOUND).body(productService.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder));
    }
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productDTO,productId));
    }
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(productId));
    }
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("Image")MultipartFile image) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProductImage(productId,image));
    }
}
