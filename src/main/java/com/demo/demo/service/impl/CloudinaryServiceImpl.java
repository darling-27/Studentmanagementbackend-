package com.demo.demo.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.demo.demo.exception.FileStorageException;
import com.demo.demo.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final String FOLDER = "student-management";

    private final Cloudinary cloudinary;
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<String, String> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File is empty", null);
        }
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", FOLDER, "resource_type", "image"));

            String url = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            if (url == null || publicId == null) {
                throw new FileStorageException("Cloudinary did not return a valid upload result", null);
            }

            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            result.put("publicId", publicId);
            return result;
        } catch (Exception ex) {
            // Cloudinary throws ApiException (RuntimeException) for API/Auth errors
            // and IOException for transport errors; wrap both consistently.
            throw new FileStorageException("Cloudinary upload failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception ex) {
            throw new FileStorageException("Cloudinary delete failed: " + ex.getMessage(), ex);
        }
    }
}
