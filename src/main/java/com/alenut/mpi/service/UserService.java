package com.alenut.mpi.service;

import com.alenut.mpi.entities.User;
import com.alenut.mpi.entities.info.UserInformation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

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

}
