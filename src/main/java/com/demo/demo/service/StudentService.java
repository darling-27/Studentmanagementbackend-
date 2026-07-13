package com.demo.demo.service;

import com.demo.demo.dto.request.StudentCreateRequest;
import com.demo.demo.dto.request.StudentUpdateRequest;
import com.demo.demo.dto.response.StudentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {

    StudentResponse create(StudentCreateRequest request);
    StudentResponse update(Long id, StudentUpdateRequest request);
    void delete(Long id);
    List<StudentResponse> getAll();
    StudentResponse getById(Long id);

    // student-only profile access
    StudentResponse getMyProfile(String username);

    StudentResponse uploadImage(Long id, MultipartFile file);
    StudentResponse updateImage(Long id, MultipartFile file);
    void deleteImage(Long id);
}
