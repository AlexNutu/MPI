package com.alenut.mpi.service.impl;

import com.alenut.mpi.entities.Idea;
import com.alenut.mpi.entities.User;
import com.alenut.mpi.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class IdeaServiceImpl {


    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private PictureLoaderService pictureLoaderService;


    public List<Idea> getAllIdeas() {
        return ideaRepository.findAll(); // gaseste toate ideile salvate in baza de date
    }

    public List<Idea> getIdeasByUser(User user) {
        return ideaRepository.getIdeasObjectsByUser(user);
    }

    public List<Idea> getIdeaByTitle(String title) {
        return ideaRepository.getIdeaByTitle(title);
    }

    public void insert(Idea idea, User user) {
        if (idea.getImage_path() == null) {
            idea.setImage_path("idea.jpg");
        }
        idea.setPosted_date(new Date());

        ideaRepository.save(idea);
    }

    public String savePhoto(MultipartFile image, User user) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String fileName = user.getUsername() + "_" + sdf.format(new Date()) + "_" + image.getOriginalFilename();

        pictureLoaderService.savePictureToDisk(fileName, image.getBytes());

        return fileName;
    }

    public String saveIdeaImage(MultipartFile image, User user) throws IOException {
        return this.savePhoto(image, user);
    }
}
