package com.ecommerce.project.model;

import com.ecommerce.project.payload.ProductDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @NotBlank(message = "Category Name Cannot be Empty!!")
    @Size(min = 5,message = "Category Name must contain atleast 5 characters")
    private String categoryName;
//    @OneToMany(mappedBy = "category")
//    private List<Product> products=new ArrayList<>();
}
