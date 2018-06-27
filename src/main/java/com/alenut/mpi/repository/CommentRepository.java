package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Comment;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getByIdea(Idea idea);

    Comment getById(Long idComment);

    List<Comment> getByUser(User user);

}
