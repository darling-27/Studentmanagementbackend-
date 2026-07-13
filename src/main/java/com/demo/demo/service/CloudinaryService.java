package com.demo.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {

    /**
     * Upload a file and return a map containing "url" and "publicId".
     */
    Map<String, String> upload(MultipartFile file);

    /**
     * Delete an asset by its Cloudinary public id.
     */
    void delete(String publicId);
}
