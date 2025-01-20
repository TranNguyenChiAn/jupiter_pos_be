package com.jupiter.store.web.rest;

import com.jupiter.store.domain.Product;
import com.jupiter.store.dto.product.CreateProductDTO;
import com.jupiter.store.dto.product.ProductDTO;
import com.jupiter.store.dto.product.UpdateProductDTO;
import com.jupiter.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductResource {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public void addProduct(@RequestBody CreateProductDTO createProductDTO) {
        productService.addProduct(createProductDTO);
    }

    @GetMapping("/search")
    public List<Product> search() {
        return productService.search();
    }

    @GetMapping("/search/{productId}")
    public ResponseEntity<ProductDTO> searchById(@RequestParam Long productId) {
        return productService.searchById(productId);
    }

    @PutMapping("/update/{id}")
    public void updateProduct(@RequestParam Long productId, @RequestBody UpdateProductDTO updateProductDTO) {
        productService.updateProduct(productId, updateProductDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }
}
