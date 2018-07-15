package com.alenut.mpi.auxiliary;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.service.impl.IdeaServiceImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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
        List<Idea> ideaList = ideaService.getIdeasByUser(idea.getUser());
        if (ideaList.size() > 0) {
            if (idea.getId() != null && idea.getId() != 0) {
                for (Idea idea1 : ideaList) {
                    if (idea1.getBody().equals(idea.getBody()) && !idea.getId().equals(idea1.getId())) {
                        errors.reject("duplicate");
                    }
                }
            } else {
                for (Idea idea1 : ideaList) {
                    if (idea1.getBody().equals(idea.getBody())) {
                        errors.reject("duplicate");
                    }
                }
            }

        }

        // Validarea descriereii(cea dupa care se face matching-ul)
        // Caut similiraritatea ideii cu ea insasi, daca avem response corect, atunci descrierea este scrisa corect

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.post("https://api.dandelion.eu/datatxt/sim/v1")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .field("token", "a7252c0613ae465fa0fefbcd7a5894d6")
                    .field("text1", idea.getBody())
                    .field("text2", idea.getBody())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (!response.getBody().getObject().has("similarity")) {
            errors.reject("format");
        }


    }
}
