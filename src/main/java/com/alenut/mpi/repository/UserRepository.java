package com.alenut.mpi.repository;

import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User repository for CRUD operations.
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);
    User getByEmail(String email);


}
