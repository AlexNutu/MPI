package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IdeaService {


    @Autowired
    private IdeaRepository ideaRepository;


    public List<Idea> getAllIdeas() {
        return ideaRepository.findAll(); // gaseste toate ideile salvate in baza de date
    }

//    private Sort sortByPostedDateAsc() {
//        return new Sort(Sort.Direction.ASC, "id_idea");
//    }

//    public List<Idea> getIdeasByUser(User user) {
//        return ideaRepository.getIdeasObjectsByUser(user);
//    }

    public void insert(Idea idea) {
        if (idea.getUser() == null) { // nu ar trebui sa se intample
            idea.setUser(new User());
        }
        if (idea.getPosted_date() == null) {
            idea.setPosted_date(new Date());
        }
        ideaRepository.save(idea);
    }
}
