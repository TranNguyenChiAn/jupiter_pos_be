package com.jupiter.store.web.rest;

import com.jupiter.store.dto.CreateProductDTO;
import com.jupiter.store.dto.UpdateProductDTO;
import com.jupiter.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void search() {
        productService.search();
    }

    @GetMapping("/search/{id}")
    public void searchById(Long id) {
        productService.searchById(id);
    }

    @PutMapping("/update")
    public void updateProduct(@PathVariable Long id, @RequestBody UpdateProductDTO updateProductDTO) {
        productService.updateProduct(id, updateProductDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }


}
