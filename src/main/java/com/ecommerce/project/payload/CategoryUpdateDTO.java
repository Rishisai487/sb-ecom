package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDTO {
    @NotBlank(message = "Category Name Cannot be Empty!!")
    @Size(min = 5,message = "Category Name must contain atleast 5 characters")
    private String categoryName;
}
