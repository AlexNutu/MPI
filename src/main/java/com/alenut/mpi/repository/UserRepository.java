package com.alenut.mpi.repository;

import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * User repository for CRUD operations.
 */

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional(readOnly = true)
    User getByUsername(String username);

    @Transactional(readOnly = true)
    User getByEmail(String email);

}
