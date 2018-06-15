package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Conversation;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Conversation> getUserConversationsFiltered(User user, String q) {
        List<Conversation> conversations = conversationRepository.getByUser(user);
        conversations.addAll(conversationRepository.getByUser2(user));
        List<Conversation> filteredConversations = new ArrayList<>();
        for (Conversation conversation : conversations) {
            if (conversation.getUser().equals(user)) {
                if (conversation.getUser2().getFull_name().trim().toLowerCase().matches(".*" + q.trim().toLowerCase() + ".*")) {
                    filteredConversations.add(conversation);
                }
            } else if (conversation.getUser2().equals(user)) {
                if (conversation.getUser().getFull_name().trim().toLowerCase().matches(".*" + q.trim().toLowerCase() + ".*")) {
                    filteredConversations.add(conversation);
                }
            }
        }

        return filteredConversations;
    }

    public Conversation getById(Long idConversation) {
        return conversationRepository.getById(idConversation);
    }

}
