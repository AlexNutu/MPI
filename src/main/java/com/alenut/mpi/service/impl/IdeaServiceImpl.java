package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.*;
import com.alenut.mpi.repository.*;
import com.alenut.mpi.service.EmailServiceImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import paralleldots.ParallelDots;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FollowingRepository followingRepository;

    public void updateImagePath(String imagePath, Idea idea) {
        ideaRepository.setNewPathForImage(imagePath, idea.getId());
    }

    public void updateLikesPlus(Idea idea) {
        ideaRepository.setNewLikenumberFor(idea.getLikenumber() + 1, idea.getId());
    }

    public void updateLikesMinus(Idea idea) {
        ideaRepository.setNewLikenumberFor(idea.getLikenumber() - 1, idea.getId());
    }

    public void updateComments(Idea idea) {
        ideaRepository.setNewComnumberFor(idea.getComnumber() + 1, idea.getId());
    }

    public void updateSimilarities(Idea idea, int val) {
        Idea updatedWithSimNumberIdea = ideaRepository.getById(idea.getId());
        ideaRepository.setNewSimnumberFor(updatedWithSimNumberIdea.getSimnumber() + val, idea.getId());
    }

    public Page<Idea> getAllIdeas(int pageNumber) {
        return ideaRepository.findAllByOrderByIdDesc(new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getAllIdeasPopular(int pageNumber) {
        return ideaRepository.findAllByOrderByLikenumberDesc(new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getAllIdeasSimilarities(int pageNumber) {
        return ideaRepository.findAllByOrderBySimnumberDesc(new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getAllIdeasComments(int pageNumber) {
        return ideaRepository.findAllByOrderByComnumberDesc(new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getByTitleLike(int pageNumber, String title) {
        return ideaRepository.findByTitleLikeOrderByIdDesc(title, new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getByTitleLikePopular(int pageNumber, String title) {
        return ideaRepository.findByTitleLikeOrderByLikenumberDesc(title, new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getByTitleLikeSimilarities(int pageNumber, String title) {
        return ideaRepository.findByTitleLikeOrderBySimnumberDesc(title, new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getByTitleLikeComments(int pageNumber, String title) {
        return ideaRepository.findByTitleLikeOrderByComnumberDesc(title, new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getByCategoryPopular(int pageNumber, Category category) {
        return ideaRepository.findByCategoryOrderByLikenumberDesc(category, new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getByCategorySimilarities(int pageNumber, Category category) {
        return ideaRepository.findByCategoryOrderBySimnumberDesc(category, new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getByCategoryComments(int pageNumber, Category category) {
        return ideaRepository.findByCategoryOrderByComnumberDesc(category, new PageRequest(pageNumber, 5));
    }

    public Page<Idea> getByCategory(int pageNumber, Category category) {
        return ideaRepository.findByCategoryOrderByIdDesc(category, new PageRequest(pageNumber, 5));
    }

    public List<Idea> getIdeasByUser(User user) {
        return ideaRepository.getIdeasObjectsByUser(user);
    }

    public Page<Idea> getIdeasByUser(int pageNumber, User user) {
        return ideaRepository.findByUser(user, new PageRequest(pageNumber, 5));
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
        idea.setLikenumber(0);
        idea.setComnumber(0);
        idea.setSimnumber(0);
        ideaRepository.save(idea);
    }

    public void sendEmails(User currentUser, Idea idea) {
        List<User> users = new ArrayList<>();
        List<Following> followings = followingRepository.getByFollowingUser(currentUser);
        for (Following following : followings) {
            users.add(following.getUser());
        }
        if (users.size() > 0) {
            for (User user : users) {
                emailService.sendSimpleMessage(
                        user.getEmail(),
                        "New Idea",
                        "Dear " + user.getFull_name() + ", \n\n The user " + idea.getUser().getFull_name() + " just posted a new idea! \n\n " +
                                "The idea: http://localhost:8090/user/viewIdea/" + idea.getId() + "\n\n" +
                                " Check out his/hers ideas here: http://localhost:8090/user/viewIdeas/userId=" + idea.getUser().getId() + "\n" +
                                " Check out his/hers profile here: http://localhost:8090/user/userIdeas/?page=0&userId=" + idea.getUser().getId() + "\n\n" +
                                " Best Regards, \n MPI Service");
            }
        }
    }

    public String savePhoto(MultipartFile image, Idea idea) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = idea.getBody().substring(0, 4) + "_" + sdf.format(new Date()) + "_" + image.getOriginalFilename();
        fileName = fileName.replaceAll("\\s+", "");

        pictureLoaderService.savePictureToDisk(fileName, image.getBytes());

        return fileName;
    }

    public String saveIdeaImage(MultipartFile image, Idea idea) throws IOException {
        return this.savePhoto(image, idea);
    }

    public void addMatchings(Idea idea) throws UnirestException {
        List<Idea> ideasByCategory = ideaRepository.getIdeasByCategory(idea.getCategory());
        int matchingsNr = 0;
        for (Idea idea1 : ideasByCategory) {

            if (!idea1.getId().equals(idea.getId())) { // sa nu existe matching cu propria idee

                HttpResponse<JsonNode> responseSemantic = Unirest.post("https://twinword-text-similarity-v1.p.mashape.com/similarity/")
                        .header("X-Mashape-Key", "xQAgEeWKfhmshNOMu3BRfpS2gHCHp1R1gTBjsnyy76dxDTDwaf")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "application/json")
                        .field("text1", idea.getBody())
                        .field("text2", idea1.getBody())
                        .asJson();

                String scoreSemantic = responseSemantic.getBody().getObject().get("similarity").toString();
                if (scoreSemantic.contains("-")) {
                    scoreSemantic = "0.0";
                }
                if (scoreSemantic.length() > 5) {
                    scoreSemantic = scoreSemantic.substring(0, 6);
                }

                Double doubleSemantic = Double.parseDouble(scoreSemantic) * 100;

                if (doubleSemantic >= 45.0) {
                    Matching matching = new Matching();
                    Matching matching2 = new Matching();
                    matching.setIdea(idea);
                    matching.setIdeaMatch(idea1);
                    matching2.setIdea(idea1);
                    matching2.setIdeaMatch(idea);
                    String semantic = doubleSemantic.toString();

                    matching.setSemantic(semantic);
                    matching2.setSemantic(semantic);

                    matchRepository.save(matching);
                    matchRepository.save(matching2);
                    // update the other ideas simnumber
                    updateSimilarities(matching.getIdeaMatch(), 1);
                    matchingsNr++;
                }
            }
        }
        // update simnumber for the new idea
        updateSimilarities(idea, matchingsNr);
    }

    public void addTags(Idea idea) throws Exception {
        // Keywords generation using Parallel DOCS API that uses Neural Language Processing
        ParallelDots pd = new ParallelDots("9EiarD6fAQkDPHZpPUnFLDnRySwOtKMEH3NUATr851s");
        String text = idea.getBody();
        String keywords = pd.keywords(text);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(keywords);
        // daca vectorul de keywords nu este gol
        if (!json.get("keywords").toString().contains("keywords\":[]")) {
            JSONArray jsonArray = (JSONArray) json.get("keywords");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String keyword = jsonObject.get("keyword").toString();
                Tag tag = new Tag();
                tag.setIdea(idea);
                tag.setBody(keyword);
                tagRepository.save(tag);
            }
        }else{
            Tag tag = new Tag();
            tag.setIdea(idea);
            tag.setBody(" No tags found");
            tagRepository.save(tag);
        }

    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteIdea(Idea idea) {
        ideaRepository.delete(idea);
    }
}
