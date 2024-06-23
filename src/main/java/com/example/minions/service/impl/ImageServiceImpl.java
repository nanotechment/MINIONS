package com.example.minions.service.impl;

import com.example.minions.exception.MinionsException;
import com.example.minions.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public String saveImage(MultipartFile image, String path) throws IOException {
        String fileName = image.getOriginalFilename();
        //check if file is null
        if (fileName == null) {
            throw MinionsException.notFound("No image file found!");
        }
        //check if the file is picture or not
        if(!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
            throw MinionsException.badRequest("Image file is not supported!");
        }

        //find working dir
        String projectPath = System.getProperty("user.dir");
        //change file name

        // Save image file to directory
        Path filePath = Paths.get(projectPath + path + fileName);
        Files.write(filePath, image.getBytes());
        return path + fileName;
    }
}
