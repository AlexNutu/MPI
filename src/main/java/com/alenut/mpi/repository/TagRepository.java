package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> getTagsByIdea(Idea idea);

}
