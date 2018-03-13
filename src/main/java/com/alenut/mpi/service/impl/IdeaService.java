package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void insert(Idea idea) {
        if (idea.getCreator() == null) {
            idea.setCreator(123L);
        }
        if (idea.getPostedDate() == null) {
            idea.setPostedDate(new Date());
        }
        ideaRepository.save(idea);
    }
}
