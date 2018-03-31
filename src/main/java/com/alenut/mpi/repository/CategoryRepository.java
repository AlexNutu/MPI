package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category getByBody(String body);
}
