package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Comment;
import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.Matching;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.CommentRepository;
import com.alenut.mpi.repository.IdeaRepository;
import com.alenut.mpi.repository.MatchRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class IdeaServiceImpl {


    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private PictureLoaderService pictureLoaderService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private CommentRepository commentRepository;


    public List<Idea> getAllIdeas() {
        return ideaRepository.findAll(); // gaseste toate ideile salvate in baza de date
    }

    public List<Idea> getIdeasByUser(User user) {
        return ideaRepository.getIdeasObjectsByUser(user);
    }

    public Idea getIdeaById(Long id) {
        return ideaRepository.getById(id);
    }

    public List<Idea> getIdeaByTitle(String title) {
        return ideaRepository.getIdeaByTitle(title);
    }

    public void insert(Idea idea, User user) {
        if (idea.getImage_path() == null) {
            idea.setImage_path("idea.jpg");
        }
        idea.setPosted_date(new Date().toString());

        ideaRepository.save(idea);
    }

    public String savePhoto(MultipartFile image, Idea idea) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = idea.getBody().substring(0, 4) + "_" + sdf.format(new Date()) + "_" + image.getOriginalFilename().replaceAll("\\s+", "");

        pictureLoaderService.savePictureToDisk(fileName, image.getBytes());

        return fileName;
    }

    public String saveIdeaImage(MultipartFile image, Idea idea) throws IOException {
        return this.savePhoto(image, idea);
    }

    public void addMatchings(Idea idea) throws UnirestException {
        List<Idea> ideasByCategory = ideaRepository.getIdeasByCategory(idea.getCategory());

        for (Idea idea1 : ideasByCategory) {

            if (!idea1.getId().equals(idea.getId())) { // sa nu existe matching cu propria idee

                HttpResponse<JsonNode> responseSemantic = Unirest.post("https://api.dandelion.eu/datatxt/sim/v1")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                        .field("token", "a7252c0613ae465fa0fefbcd7a5894d6")
                        .field("text1", idea.getBody())
                        .field("text2", idea1.getBody())
                        .asJson();

                String scoreSemantic = responseSemantic.getBody().getObject().get("similarity").toString();

                HttpResponse<JsonNode> responseSintactic = Unirest.post("https://api.dandelion.eu/datatxt/sim/v1")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                        .field("token", "a7252c0613ae465fa0fefbcd7a5894d6")
                        .field("bow", "always")
                        .field("text1", idea.getBody())
                        .field("text2", idea1.getBody())
                        .asJson();

                String scoreSintactic = responseSintactic.getBody().getObject().get("similarity").toString();

                Double doubleSemantic = Double.parseDouble(scoreSemantic);
                Double doubleSintactic = Double.parseDouble(scoreSintactic);

                if (doubleSemantic >= 50.0) {
                    Matching matching = new Matching();
                    matching.setIdea(idea);
                    matching.setIdeaMatch(idea1);
                    matching.setSemantic(scoreSemantic);
                    matching.setSintactic(scoreSintactic);

                    matchRepository.save(matching);
                }
            }
        }
    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }
}
