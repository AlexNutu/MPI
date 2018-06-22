package com.alenut.mpi.repository;

import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User repository for CRUD operations.
 */

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional(readOnly = true)
    User getByUsername(String username);

    @Transactional(readOnly = true)
    User getByEmail(String email);

    @Transactional(readOnly = true)
    User getById(Long id);

    @Transactional(readOnly = true)
    User getByToken(String token);

    @Transactional(readOnly = true)
    List<User> findAllByOrderByIdDesc();

    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "UPDATE User u SET u.role = :val WHERE u.id_user = :id", nativeQuery = true)
    void setNewRole(@Param("val") Integer val, @Param("id") Long id);

    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "UPDATE User u SET u.alert = :val WHERE u.id_user = :id", nativeQuery = true)
    void setAlert(@Param("val") Integer val, @Param("id") Long id);

//    @Transactional(readOnly = true)
//    User findOne(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.full_name =?1,  u.username = ?2, u.email = ?3, u.password = ?4, u.phone_number = ?5, u.occupation = ?6, u.image = ?7 where u.id = ?8")
    void editUserInfoById(String fullname, String username, String email, String password, String phoneNumber,
                          String occupation, String image, Long userId);

    @Transactional
    @Modifying
    @Query("update User u set u.full_name =?1,  u.username = ?2, u.phone_number = ?3, u.occupation = ?4 where u.id = ?5")
    void editUserInfoById2(String fullname, String username, String phoneNumber,
                          String occupation, Long userId);
}
