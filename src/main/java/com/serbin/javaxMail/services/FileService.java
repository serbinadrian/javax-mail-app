package com.serbin.javaxMail.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    public void getFile(MultipartFile file, HttpServletResponse response) {
        response.setHeader("Content-disposition", "attachment;filename=" + file.getOriginalFilename());
        try {
            //file.transferTo((Path) response.getOutputStream());
            Files.copy((Path) file, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException exception) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}

