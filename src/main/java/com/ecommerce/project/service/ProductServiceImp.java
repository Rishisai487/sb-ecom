package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;
        @Override
        public ProductDTO addProduct(Long categoryId,ProductDTO productDTO) {
            Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
            Boolean productExists=productRepository.existsByProductNameIgnoreCaseAndCategoryCategoryId(productDTO.getProductName(),categoryId);
            if(productExists){
                throw new APIException("Product with name already exists!!");
            }
            Product product=modelMapper.map(productDTO,Product.class);
            product.setImage("default.png");
            Double specialPrice=product.getPrice()-(product.getDiscount()*0.01)*product.getPrice();
            product.setSpecialPrice(specialPrice);
            product.setCategory(category);
            product=productRepository.save(product);
            return modelMapper.map(product,ProductDTO.class);
    }
    @Override
    public ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {
            Sort sort=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
            Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products=productRepository.findAll(pageable);
        if(products.isEmpty()){
            throw new APIException("No Products found!!");
        }
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        return new ProductResponse(productDTOS,products.getNumber(),products.getSize(),products.getTotalElements(),products.getTotalPages(),products.isLast());
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId,int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        Sort sort=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products=productRepository.findByCategoryCategoryId(categoryId,pageable);
        if(products.isEmpty()){
            throw new ResourceNotFoundException("Products","CategoryId",categoryId);
        }
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        return new ProductResponse(productDTOS,products.getNumber(),products.getSize(),products.getTotalElements(),products.getTotalPages(),products.isLast());
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder) {
            Sort sort=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
            Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products=productRepository.findByProductNameContainsIgnoreCase(keyword,pageable);
        if(products.isEmpty()){
            throw new APIException("Search Results Not Found with the keyword "+keyword+" 😭🥀");
        }
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        return new ProductResponse(productDTOS,products.getNumber(),products.getSize(),products.getTotalElements(),products.getTotalPages(),products.isLast());
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product existingProduct=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","ProductId",productId));
        Product product=modelMapper.map(productDTO,Product.class);
        product.setProductId(existingProduct.getProductId());
        product.setImage("default.png");
        Double specialPrice=product.getPrice()-(product.getDiscount()*0.01)*product.getPrice();
        product.setSpecialPrice(specialPrice);
        modelMapper.map(product,existingProduct);
        return modelMapper.map(productRepository.save(existingProduct),ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","ProductId",productId));
        productRepository.delete(existingProduct);
        return modelMapper.map(existingProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));
        String fileName=fileService.uploadImage(path,image);
        productFromDb.setImage(fileName);
        Product product=productRepository.save(productFromDb);
        return modelMapper.map(product,ProductDTO.class);
    }
}
