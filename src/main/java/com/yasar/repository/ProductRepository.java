package com.yasar.repository;

import com.yasar.entity.Product;
import com.yasar.utility.Repository;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductRepository extends Repository<Product, Long> {
    public ProductRepository() {
        super(new Product());
    }

    public List<Product> searchTopTenProducts(String name) {
        openEntityManager();
        TypedQuery<Product> typedQuery = getEm().createNamedQuery("Product.searchTopTenProducts", Product.class);
        typedQuery.setParameter("name", "%"+name+"%");
        return typedQuery.getResultList();
    }
}
