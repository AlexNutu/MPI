package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Category;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getUniqueCategoriesByUser(List<Idea> ideaList) {
        List<Category> categories = new ArrayList<>();
        for (Idea idea : ideaList) {
            if (!categories.contains(idea.getCategory())) {
                categories.add(idea.getCategory());
            }
        }
        return categories;
    }
}
