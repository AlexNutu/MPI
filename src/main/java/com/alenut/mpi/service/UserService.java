package com.alenut.mpi.service;

import com.alenut.mpi.models.User;
import com.alenut.mpi.models.info.UserInformation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

public interface UserService {

    @Transactional(readOnly = true)
    User getUserUP(String username, String password);

    @Transactional(readOnly = true)
    User getUserEP(String email, String password);

    @Transactional(readOnly = true)
    User getByUsername(String username);

    @Transactional(readOnly = true)
    User getByEmail(String username);

    @Transactional(readOnly = true)
    void createUser(UserInformation userInfo) throws NoSuchAlgorithmException;

    @Transactional(readOnly = true)
    void editUser(UserInformation userInfo) throws NoSuchAlgorithmException;
}