package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Conversation;
import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Transactional(readOnly = true)
    List<Conversation> getByUser(User user);

    @Transactional(readOnly = true)
    List<Conversation> getByUser2(User user);

    @Transactional(readOnly = true)
    Conversation getById(Long conversationId);

}
