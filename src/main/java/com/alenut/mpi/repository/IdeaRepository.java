package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Category;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    Idea getById(Long id);

    List<Idea> getIdeasObjectsByUser(User user);

    List<Idea> getIdeaByTitle(String title);

    List<Idea> getIdeasByCategory(Category category);

    Page<Idea> findByTitleLike(String title, Pageable p);

    List<Idea> findAllByOrderByIdDesc();

}
