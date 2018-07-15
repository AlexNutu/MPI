package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Appreciation;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppreciationRepository extends JpaRepository<Appreciation, Long> {
    List<Appreciation> getByIdea(Idea idea);
    List<Appreciation> getByUser(User user);

}
