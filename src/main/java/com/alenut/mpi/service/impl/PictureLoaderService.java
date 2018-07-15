package com.alenut.mpi.service.impl;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PictureLoaderService {

    private static String IMAGE_FOLDER = "./src/main/resources/static/img";

    public byte[] getPicture(String fileName) throws IOException {
        Path p = FileSystems.getDefault().getPath(IMAGE_FOLDER, fileName);
        return Files.readAllBytes(p);
    }

    public void savePictureToDisk(String fileName, byte[] content) throws IOException {
        Path p = FileSystems.getDefault().getPath(IMAGE_FOLDER, fileName);
        Files.write(p, content);
    }

    public void deletePictureFromDisk(String fileName) throws IOException {
        Path p = FileSystems.getDefault().getPath(IMAGE_FOLDER, fileName);
        if (p.toFile().exists()) {
            Files.delete(p);
        }
    }
}