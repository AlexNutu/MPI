package com.alenut.mpi.service.impl;

import com.alenut.mpi.auxiliary.MD5Encryption;
import com.alenut.mpi.entities.Comment;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.*;
import com.alenut.mpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AppreciationRepository appreciationRepository;

    @Autowired
    private MatchRepository matchRepository;

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
            return userRepository.getByEmail(email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public User getByUsername(String username) {
        try {
            return userRepository.getByUsername(username);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void createUser(User user) throws NoSuchAlgorithmException {

        user.setPassword(MD5Encryption.computeMD5(user.getPassword()));
        try {
            usr = userRepository.save(user);
        } catch (Exception e) {
            System.out.println("The username or email already exists");
        }
    }

    @Override
    public void editUser(User user1) throws NoSuchAlgorithmException {

//        user1.setPassword(MD5Encryption.computeMD5(user1.getPassword()));
//       // User user = userRepository.getByEmail(user1.getEmail());
//
//        //TODO: de adaugat setere pentru celelalte campuri
//        user.setPassword(userInfo.getPassword());
//        user.setRole(userInfo.getRole());
//
//        try {
//            usr = userRepository.save(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public Integer getNoOfIdeas(List<Idea> ideas) {
        return ideas.size();
    }

    @Override
    public Integer getNoOfMatchings(List<Idea> ideas) {
        int nr = 0;
        for (Idea idea : ideas) {
            nr += matchRepository.getByIdea(idea).size();
        }
        return nr;
    }

    @Override
    public Integer getNoOfLikes(List<Idea> ideas) {
        int nr = 0;
        for (Idea idea : ideas) {
            nr += appreciationRepository.getByIdea(idea).size();
        }
        return nr;
    }

    @Override
    public Integer getNoOfComments(List<Idea> ideas) {
        int nr = 0;
        for (Idea idea : ideas) {
            nr += commentRepository.getByIdea(idea).size();
        }
        return nr;
    }
}
