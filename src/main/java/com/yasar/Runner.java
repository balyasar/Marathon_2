package com.yasar;

import com.yasar.controller.CategoryController;
import com.yasar.controller.ProductController;

import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        CategoryController categoryController = new CategoryController();
        ProductController productController = new ProductController();
        int secim;
        do {
            System.out.println("""
                    **********************************
                    **********   ANASAYFA   **********
                    **********************************
                    1 - Kategori Ekle
                    2 - Kategori Listele
                    3 - Ürün Ekle
                    4 - Ürün Ara
                    5 - <<< Ç I K I Ş >>>
                    """);
            System.out.print("Lütfen bir seçim yapınız .... : ");
            secim = new Scanner(System.in).nextInt();
            switch (secim) {
                case 1 -> categoryController.addCategory();
                case 2 -> categoryController.listCategory();
                case 3 -> productController.addProduct();
                case 4 -> productController.searchProductFirstTen();
                case 5 -> System.out.println("ÇIKIŞ YAPILIYOR !!!");
                default -> System.out.println("Hatalı giriş yaptınız. Lütfen tekrar deneyiniz.");
            }
        } while (secim != 5);

    }
}
