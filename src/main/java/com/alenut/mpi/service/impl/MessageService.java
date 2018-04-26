package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Message;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public List<Message> getMessagesByUser(User user) {
        return messageRepository.getBySender(user);
    }

    public void addMessage(Message message){
        messageRepository.save(message);
    }

}
