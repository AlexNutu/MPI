package com.alenut.mpi.service;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    @Transactional(readOnly = true)
    User getUser(String email, String password);

    @Transactional(readOnly = true)
    User getByEmail(String email);

    @Transactional(readOnly = true)
    void createUser(User user) throws NoSuchAlgorithmException;

    @Transactional(readOnly = true)
    void editUser(User user) throws NoSuchAlgorithmException;

    @Transactional(readOnly = true)
    User getByUsername(String username);

    @Transactional(readOnly = true)
    Integer getNoOfIdeas(List<Idea> ideas);

    @Transactional(readOnly = true)
    Integer getNoOfMatchings(List<Idea> ideas);

    @Transactional(readOnly = true)
    Integer getNoOfLikes(List<Idea> ideas);

    @Transactional(readOnly = true)
    Integer getNoOfComments(List<Idea> ideas);

}
