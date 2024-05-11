package com.yasar.service;

import com.yasar.entity.Category;
import com.yasar.repository.CategoryRepository;
import com.yasar.utility.Response;

import java.util.List;
import java.util.Optional;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
    }

    public Response<Boolean> addCategory(String name, Long categoryID) {
        Response<Boolean> response = new Response<>();
        if (categoryRepository.isExistByName(name)) {
            response.setMessage("Böyle bir kategori adı zaten mevcut");
            response.setData(false);
            response.setStatusCode(400);
            return response;
        }
        categoryRepository.save(Category.builder()
                .name(name)
                .categoryId(categoryID)
                .build());
        response.setStatusCode(200);
        response.setMessage("Kategori başarıyla kayıt edildi.");
        response.setData(true);
        return response;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findByName(String categoryName) {
        List<Category> categoryList = categoryRepository.findAllByFromColumnAndValues("name", categoryName);
        if (categoryList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(categoryList.getFirst());
    }
}
