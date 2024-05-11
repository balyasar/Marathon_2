package com.yasar.controller;

import com.yasar.entity.Category;
import com.yasar.entity.Product;
import com.yasar.service.ProductService;
import com.yasar.utility.Response;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class ProductController {
    private final ProductService productService;

    public ProductController() {
        this.productService = new ProductService();
    }

    public void addProduct() {
        System.out.println("""
                ****   ÜRÜN EKLEME   ****""");
        System.out.print("Lütfen ürün adı giriniz ....... : ");
        String name = new Scanner(System.in).nextLine();
        System.out.print("Lütfen kategori adı giriniz ... : ");
        String categoryName = new Scanner(System.in).nextLine();
        System.out.print("Lütfen açıklama giriniz ....... : ");
        String description = new Scanner(System.in).nextLine();
        System.out.print("Lütfen fiyat giriniz .......... : ");
        Double price = new Scanner(System.in).nextDouble();
        System.out.print("Lütfen stok giriniz ........... : ");
        int stock = new Scanner(System.in).nextInt();
        Response<Boolean> response = productService.addProduct(name, categoryName, description, price, stock);
        if (response.getStatusCode() == 400 || response.getStatusCode() == 500) {
            System.out.println(response.getMessage());
            return;
        }
        System.out.println(response.getMessage());
    }

    public void searchProductFirstTen() {
        System.out.println("""
                ****   ÜRÜN ARAMA   ****""");
        System.out.print("Lütfen aramak istediğiniz ürün adını giriniz ... : ");
        String name = new Scanner(System.in).nextLine();
        List<Product> productList = productService.searchTopTenProducts(name);
        System.out.println("Kategori    |" + "     Ürün Adı     |" + "     Fiyat     |" + "     Kategori     ");
        productList.forEach(x -> {
            DecimalFormat df = new DecimalFormat("###,###.##");
            String price = df.format(x.getPrice());
            System.out.println(x.getCategory().getName() + "       " + x.getName() + "       " + price + "₺" + "             " + x.getStock());
        });
    }
}
