package com.yasar.repository;

import com.yasar.entity.Category;
import com.yasar.utility.Repository;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class CategoryRepository extends Repository<Category, Long> {
    public CategoryRepository() {
        super(new Category());
    }

    public boolean isExistByName(String name) {
        openEntityManager();
        TypedQuery<Boolean> typedQuery = getEm().createNamedQuery("Category.isExistByName", Boolean.class);
        typedQuery.setParameter("name", name);
        return typedQuery.getSingleResult();
    }
}
