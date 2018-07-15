package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Comment;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentsService {

    @Autowired
    CommentRepository commentRepository;

    public void deleteCommentsByIdea(Idea idea) {
        List<Comment> comments = commentRepository.getByIdea(idea);
        commentRepository.delete(comments);
    }
}
