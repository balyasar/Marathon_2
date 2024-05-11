package com.yasar.service;

import com.yasar.entity.Category;
import com.yasar.entity.Product;
import com.yasar.repository.ProductRepository;
import com.yasar.utility.Response;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService() {
        this.productRepository = new ProductRepository();
        this.categoryService = new CategoryService();
    }

    public Response<Boolean> addProduct(String name, String categoryName, String description, Double price, int stock) {
        Response<Boolean> response = new Response<>();
        Optional<Category> category = categoryService.findByName(categoryName);
        if (category.isEmpty()) {
            response.setMessage("Kategori adı bulunamadı. Lütfen tekrar giriniz.");
            response.setData(false);
            response.setStatusCode(400);
            return response;
        }

        productRepository.save(Product.builder()
                .name(name)
                .categoryId(category.get().getId())
                .description(description)
                .price(price)
                .stock(stock)
                .build());
        response.setData(true);
        response.setStatusCode(200);
        response.setMessage("Ürün başarıyla eklendi.");
        return response;
    }

    public List<Product> searchTopTenProducts(String name) {
        return productRepository.searchTopTenProducts(name);
    }
}
