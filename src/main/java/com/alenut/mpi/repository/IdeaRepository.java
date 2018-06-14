package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Category;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "UPDATE Idea i SET i.image_path = :val WHERE i.id_idea = :id", nativeQuery = true)
    void setNewPathForImage(@Param("val") String val, @Param("id") Long id);


    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "UPDATE Idea i SET i.likenumber = :val WHERE i.id_idea = :id", nativeQuery = true)
    int setNewLikenumberFor(@Param("val") int val, @Param("id") Long id);

    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "UPDATE Idea i SET i.comnumber = :val WHERE i.id_idea = :id", nativeQuery = true)
    void setNewComnumberFor(@Param("val") int val, @Param("id") Long id);

    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "UPDATE Idea i SET i.simnumber = :val WHERE i.id_idea = :id", nativeQuery = true)
    void setNewSimnumberFor(@Param("val") int val, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Idea i set i.title =?1,  i.body = ?2, i.category = ?3 where i.id = ?4")
    void editIdeaInfoById(String title, String body, Category category, Long ideaId);

    Idea getById(Long id);

    Page<Idea> findAllByOrderByIdDesc(Pageable p);

    Page<Idea> findAllByOrderByLikenumberDesc(Pageable p);

    Page<Idea> findAllByOrderByComnumberDesc(Pageable p);

    Page<Idea> findAllByOrderBySimnumberDesc(Pageable p);

    Page<Idea> findByCategoryOrderByComnumberDesc(Category c, Pageable p);

    Page<Idea> findByCategoryOrderBySimnumberDesc(Category c, Pageable p);

    Page<Idea> findByCategoryOrderByLikenumberDesc(Category c, Pageable p);

    Page<Idea> findByCategoryOrderByIdDesc(Category c, Pageable p);

    List<Idea> getIdeasObjectsByUser(User user);

    Page<Idea> findByUserOrderByIdDesc(User user, Pageable p);

    Page<Idea> findByUserAndCategoryOrderByIdDesc(User user, Category category, Pageable p);

    List<Idea> getIdeaByTitle(String title);

    List<Idea> getIdeasByCategory(Category category);

    Page<Idea> findByTitleLikeOrderByIdDesc(String title, Pageable p);

    Page<Idea> findByTitleLikeOrderByLikenumberDesc(String title, Pageable p);

    Page<Idea> findByTitleLikeOrderByComnumberDesc(String title, Pageable p);

    Page<Idea> findByTitleLikeOrderBySimnumberDesc(String title, Pageable p);

}
