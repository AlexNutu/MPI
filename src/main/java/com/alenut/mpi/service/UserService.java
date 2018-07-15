package com.alenut.mpi.service;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    @Transactional(readOnly = true)
    User getUser(String email, String password);

    @Transactional(readOnly = true)
    User getByEmail(String email);

    @Transactional(readOnly = true)
    User getById(Long id);

    @Transactional(readOnly = true)
    User getByToken(String token);

    @Transactional(readOnly = true)
    User getByForgotToken(String token);

    @Transactional(readOnly = true)
    void updateForgotToken(User user, String token);

    @Transactional(readOnly = true)
    void updatePassword(User user, String password);

    @Transactional(readOnly = true)
    void createUser(User user) throws NoSuchAlgorithmException;

    @Transactional(readOnly = true)
    void editUser(User user, Long userId) throws NoSuchAlgorithmException;

    @Transactional(readOnly = true)
    void editUser2(User user, Long userId) throws NoSuchAlgorithmException;

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

    @Transactional(readOnly = true)
    String saveImage(MultipartFile multipartFile, User user) throws IOException;

    @Transactional(readOnly = true)
    void updateAlertForUser(Integer val, User user);

    @Transactional(readOnly = true)
    void deleteUser(User user) throws IOException;

    @Transactional(readOnly = true)
    void deleteUserConversations(User user) throws IOException;

    @Transactional(readOnly = true)
    void deleteUserFollowings(User user) throws IOException;

}
