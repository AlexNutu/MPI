package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Conversation;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {

    @Autowired
    ConversationRepository conversationRepository;

    public List<Conversation> getAllUserConversations(User user) {

        List<Conversation> conversations = conversationRepository.getByUser(user);
        conversations.addAll(conversationRepository.getByUser2(user));
        return conversations;
    }

    public Conversation getById(Long idConversation){
        return conversationRepository.getById(idConversation);
    }

}
