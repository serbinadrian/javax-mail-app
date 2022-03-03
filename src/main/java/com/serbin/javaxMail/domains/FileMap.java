package com.serbin.javaxMail.domains;

import org.springframework.web.multipart.MultipartFile;


public class FileMap {
    private String filename;
    private MultipartFile file;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFileStream(MultipartFile fileStream) {
        this.file = fileStream;
    }
}
