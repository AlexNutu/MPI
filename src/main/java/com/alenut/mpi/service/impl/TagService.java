package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.Tag;
import com.alenut.mpi.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    TagRepository tagRepository;

    public void deleteTagsByIdea(Idea idea) {
        List<Tag> tags = tagRepository.getTagsByIdea(idea);
        if (tags != null) {
            tagRepository.delete(tags);
        }
    }
}
