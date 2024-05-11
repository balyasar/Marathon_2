package com.yasar.controller;

import com.yasar.entity.Category;
import com.yasar.service.CategoryService;
import com.yasar.utility.Response;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController() {
        this.categoryService = new CategoryService();
    }

    public void addCategory() {
        System.out.println("""
                ****   KATEGORİ EKLEME   ****""");
        System.out.print("Eklemek istediğiniz kategori adını giriniz ... : ");
        String name = new Scanner(System.in).nextLine();
        System.out.print("Üst Kategori ID'sini giriniz ... : ");
        Long categoryID = new Scanner(System.in).nextLong();
        Response<Boolean> response = categoryService.addCategory(name, categoryID);
        if (response.getStatusCode() == 400 || response.getStatusCode() == 500) {
            System.out.println(response.getMessage());
        }
        System.out.println(response.getMessage());

    }

    public void listCategory() {
        System.out.println("""
                ****   KATEGORİ LİSTESİ   ****""");
        List<Category> categoryList = categoryService.findAll();
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getCategoryId() == 0)
                System.out.println(" - " + categoryList.get(i).getName());
            for (int j = 0; j < categoryList.size(); j++) {
                if (categoryList.get(j).getCategoryId().equals(categoryList.get(i).getId())) {
                    System.out.println("   *   " + categoryList.get(j).getName());
                }
            }
        }
    }
}
