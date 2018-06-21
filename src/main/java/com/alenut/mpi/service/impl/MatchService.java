package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.Matching;
import com.alenut.mpi.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    IdeaServiceImpl ideaService;

    public void deleteMatchingsByIdea(Idea idea) {
        List<Matching> matchings1 = matchRepository.getByIdea(idea);
        List<Matching> matchings2 = matchRepository.getByIdeaMatch(idea);

        if (matchings1 != null) {
            for (Matching matching : matchings1) {
                Idea ideaToUpdate = matching.getIdeaMatch();
                ideaService.updateSimilarities(ideaToUpdate, -1);
            }
            matchRepository.delete(matchings1);
        }
        if (matchings2.size() > 0) {
            for (Matching matching : matchings2) {
                Idea ideaToUpdate = matching.getIdea();
                ideaService.updateSimilarities(ideaToUpdate, -1);
            }
            matchRepository.delete(matchings2);
        }
        ideaService.updateSimilarities(idea, (-1) * idea.getSimnumber());
    }
}
