package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.*;
import com.alenut.mpi.repository.*;
import com.alenut.mpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private FollowingRepository followingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AppreciationRepository appreciationRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PictureLoaderService pictureLoaderService;

    private User usr = null;

    public User getUser(String email, String password) {

        User user = userRepository.getByEmail(email);/**/
        if (user == null) {
            return null;
        } else {

            if (BCrypt.checkpw(password, user.getPassword())) {
                return user;
            } else {
                throw new IllegalArgumentException();
            }
        }
//        return null;
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
    public User getById(Long id) {
        try {
            return userRepository.getById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public User getByToken(String token) {
        try {
            return userRepository.getByToken(token);
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

        try {
            usr = userRepository.save(user);
        } catch (Exception e) {
            System.out.println("The username or email already exists");
        }
    }

    @Override
    public Integer getNoOfIdeas(List<Idea> ideas) {
        return ideas.size();
    }

    @Override
    public Integer getNoOfMatchings(List<Idea> ideas) {
        int nr = 0;
        for (Idea idea : ideas) {
            nr += idea.getSimnumber();
        }
        return nr;
    }

    @Override
    public Integer getNoOfLikes(List<Idea> ideas) {
        int nr = 0;
        for (Idea idea : ideas) {
            nr += idea.getLikenumber();
        }
        return nr;
    }

    @Override
    public Integer getNoOfComments(List<Idea> ideas) {
        int nr = 0;
        for (Idea idea : ideas) {
            nr += idea.getComnumber();
        }
        return nr;
    }

    @Override
    public void editUser(User user, Long userId) throws NoSuchAlgorithmException {
        userRepository.editUserInfoById(user.getFull_name(), user.getUsername(), user.getEmail(), user.getPassword(),
                user.getPhone_number(), user.getOccupation(), user.getImage(), userId);
    }

    @Override
    public void editUser2(User user, Long userId) throws NoSuchAlgorithmException {
        userRepository.editUserInfoById2(user.getFull_name(), user.getUsername(),
                user.getPhone_number(), user.getOccupation(), userId);
    }

    public String savePhoto(MultipartFile image, User user) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String fileName = user.getUsername() + "_" + sdf.format(new Date()) + "_" + image.getOriginalFilename().replaceAll("\\s+", "");

        pictureLoaderService.savePictureToDisk(fileName, image.getBytes());

        return fileName;
    }

    @Override
    public String saveImage(MultipartFile image, User user) throws IOException {
        return this.savePhoto(image, user);
    }

    @Override
    public void updateAlertForUser(Integer val, User user) {
        userRepository.setAlert(val, user.getId());
    }

    @Override
    public void deleteUser(User user) throws IOException {
        // delete user's image
        if (!user.getImage().equals("av1.png") && !user.getImage().equals("user1.png")) {
            try {
                pictureLoaderService.deletePictureFromDisk(user.getImage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userRepository.delete(user);
    }

    @Override
    public void deleteUserConversations(User user) throws IOException {
        List<Conversation> conversationList = conversationRepository.getByUser(user);
        List<Conversation> conversationList2 = conversationRepository.getByUser2(user);

        for (Conversation conversation : conversationList) {
            List<Message> messageList = messageRepository.getByConversation(conversation);
            messageRepository.delete(messageList);
        }
        for (Conversation conversation : conversationList2) {
            List<Message> messageList = messageRepository.getByConversation(conversation);
            messageRepository.delete(messageList);
        }
        // delete the conversations
        conversationRepository.delete(conversationList);
        conversationRepository.delete(conversationList2);
    }

    @Override
    public void deleteUserFollowings(User user) throws IOException {
        List<Following> followingList = followingRepository.getByUser(user);
        List<Following> followingList2 = followingRepository.getByFollowingUser(user);

        followingRepository.delete(followingList);
        followingRepository.delete(followingList2);
    }


}
