package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Category;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    Idea getById(Long id);

    Page<Idea> findAllByOrderByLikenumberDesc(Pageable p);

    Page<Idea> findAllByOrderByComnumberDesc(Pageable p);

    Page<Idea> findAllByOrderBySimnumberDesc(Pageable p);

    Page<Idea> findByCategoryOrderByComnumberDesc(Category c, Pageable p);

    Page<Idea> findByCategoryOrderBySimnumberDesc(Category c, Pageable p);

    Page<Idea> findByCategoryOrderByLikenumberDesc(Category c, Pageable p);

    Page<Idea> findByCategory(Category c, Pageable p);

    List<Idea> getIdeasObjectsByUser(User user);

    Page<Idea> findByUser(User user, Pageable p);

    List<Idea> getIdeaByTitle(String title);

    List<Idea> getIdeasByCategory(Category category);

    Page<Idea> findByTitleLike(String title, Pageable p);

    Page<Idea> findByTitleLikeOrderByLikenumberDesc(String title, Pageable p);

    Page<Idea> findByTitleLikeOrderByComnumberDesc(String title, Pageable p);

    Page<Idea> findByTitleLikeOrderBySimnumberDesc(String title, Pageable p);

}
