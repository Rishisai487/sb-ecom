package com.ecommerce.project.payload;

import com.ecommerce.project.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    @NotBlank(message = "Category Name cannot be Blank")
    @Size(min=5,message = "CategoryName should atleast be 5 characters!!")
    private String categoryName;
//    private List<Product> products;
}
