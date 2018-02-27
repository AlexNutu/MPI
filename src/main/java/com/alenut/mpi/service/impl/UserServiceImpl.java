package com.alenut.mpi.service.impl;

import com.alenut.mpi.auxiliary.MD5Encryption;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.entities.info.UserInformation;
import com.alenut.mpi.repository.UserRepository;
import com.alenut.mpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private User usr = null;

    public User getUser(String email, String password) {

        try {
            User user = userRepository.getByEmail(email);/**/
            if (user == null) {
                return null;
            } else {
                if (user.getPassword().equals(MD5Encryption.computeMD5(password))) {
                    return user;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getByEmail(String email) {
        try {
            User user = userRepository.getByEmail(email);
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void createUser(UserInformation userInfo) throws NoSuchAlgorithmException {

        userInfo.setPassword(MD5Encryption.computeMD5("parola"));
        User user = new User(userInfo);

        try {
            usr = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editUser(UserInformation userInfo) throws NoSuchAlgorithmException {

        userInfo.setPassword(MD5Encryption.computeMD5(userInfo.getPassword()));
        User user = userRepository.getByEmail(userInfo.getEmail());

        //TODO: de adaugat setere pentru celelalte campuri
        user.setPassword(userInfo.getPassword());
        user.setRole(userInfo.getRole());
        try {
            usr = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
