package com.alenut.mpi.repository;

import com.alenut.mpi.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User getByEmail(String email);
    User getByUsername(String username);
}