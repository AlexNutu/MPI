package com.alenut.mpi.auxiliary;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;


@Component
public class IdeaValidator implements Validator {

    @Autowired
    private IdeaServiceImpl ideaService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Idea.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        // Daca un user incearca sa insereze o idee identica cu una din cele deja postate de el
        Idea idea = (Idea) o;
        List<Idea> ideaList = ideaService.getIdeaByTitle(idea.getTitle());
        if (ideaList.size() > 0) {
            for (Idea idea1 : ideaList) {
                if (idea1.getBody().equals(idea.getBody())
                        && idea1.getCategory().getId().equals(idea.getCategory().getId())
                        && idea1.getUser().getId().equals(idea.getUser().getId())) {
                    errors.reject("Duplicate ideas");
                }
            }
        }
    }
}
