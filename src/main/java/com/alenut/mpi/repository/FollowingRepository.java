package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Following;
import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    List<Following> getByUser(User user);

}
