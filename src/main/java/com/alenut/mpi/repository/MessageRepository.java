package com.alenut.mpi.repository;

import com.alenut.mpi.entities.Conversation;
import com.alenut.mpi.entities.Message;
import com.alenut.mpi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional(readOnly = true)
    List<Message> getBySender(User user);

    @Transactional(readOnly = true)
    List<Message> getByConversation(Conversation conversation);

}
