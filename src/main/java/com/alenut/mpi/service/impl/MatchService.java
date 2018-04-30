package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.Matching;
import com.alenut.mpi.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    MatchRepository matchRepository;

    public void deleteMatchingsByIdea(Idea idea) {
        List<Matching> matchings1 = matchRepository.getByIdea(idea);
        List<Matching> matchings2 = matchRepository.getByIdeaMatch(idea);

        if (matchings1 != null) {
            matchRepository.delete(matchings1);
        }
        if (matchings2 != null) {
            matchRepository.delete(matchings2);
        }
    }
}
