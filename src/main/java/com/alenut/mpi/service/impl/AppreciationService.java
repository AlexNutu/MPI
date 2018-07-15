package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Appreciation;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.repository.AppreciationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppreciationService {

    @Autowired
    AppreciationRepository appreciationRepository;

    public void deleteAppreciationsByIdea(Idea idea) {

        List<Appreciation> appreciations = appreciationRepository.getByIdea(idea);
        appreciationRepository.delete(appreciations);
    }
}
