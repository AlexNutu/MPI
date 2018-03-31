package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IdeaServiceImpl {


    @Autowired
    private IdeaRepository ideaRepository;


    public List<Idea> getAllIdeas() {
        return ideaRepository.findAll(); // gaseste toate ideile salvate in baza de date
    }

    public List<Idea> getIdeasByUser(User user) {
        return ideaRepository.getIdeasObjectsByUser(user);
    }

    public List<Idea> getIdeaByTitle(String title) {
        return ideaRepository.getIdeaByTitle(title);
    }

    public void insert(Idea idea, User user) {
        idea.setLikes_number(0);
        idea.setImage_path("idea.jpg");
        idea.setPosted_date(new Date());

        ideaRepository.save(idea);
    }
}
