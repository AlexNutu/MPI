package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Matching, Long> {
    List<Matching> getByIdea(Idea idea);


}
