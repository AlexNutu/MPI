package com.alenut.mpi.repository;

import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Modifying
    @Query("update User u set u.full_name =?1,  u.username = ?2, u.email = ?3, u.password = ?4, u.phone_number = ?5, u.occupation = ?6, u.image = ?7 where u.id = ?8")
    void editUserInfoById(String fullname, String username, String email, String password, String phoneNumber,
                          String occupation, String image, Long userId);

}
