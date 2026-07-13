package com.demo.demo.controller;

import com.demo.demo.dto.request.StudentCreateRequest;
import com.demo.demo.dto.request.StudentUpdateRequest;
import com.demo.demo.dto.response.ApiResponse;
import com.demo.demo.dto.response.StudentResponse;
import com.demo.demo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/students")
@PreAuthorize("hasRole('ADMIN')")
@Tag(
        name = "Admin Student Management",
        description = "CRUD and Image Operations"
)
public class AdminStudentController {

    private final StudentService studentService;

    public AdminStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE STUDENT
    @Operation(summary = "Create Student", description = "Creates a new student record along with a linked login user account.")
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(
            @Valid @RequestBody StudentCreateRequest request) {

        StudentResponse response = studentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Student created successfully", response));
    }

    // UPDATE STUDENT
    @Operation(summary = "Update Student", description = "Updates student details and optionally the linked user email.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequest request) {

        StudentResponse response = studentService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.ok("Student updated successfully", response)
        );
    }

    // DELETE STUDENT
    @Operation(summary = "Delete Student", description = "Deletes a student record and associated Cloudinary image. The linked user account is retained.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        studentService.delete(id);

        return ResponseEntity.noContent().build();
    }

    // GET ALL STUDENTS
    @Operation(summary = "Get All Students", description = "Returns a list of all students.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Students fetched successfully",
                        studentService.getAll()
                )
        );
    }

    // GET STUDENT BY ID
    @Operation(summary = "Get Student By ID", description = "Returns a single student by their database ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Student found",
                        studentService.getById(id)
                )
        );
    }

    // UPLOAD IMAGE
    @Operation(summary = "Upload Student Profile Image")
    @PostMapping(
            value = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<StudentResponse>> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        StudentResponse response =
                studentService.uploadImage(id, file);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Profile image uploaded successfully",
                        response
                )
        );
    }

    // REPLACE IMAGE
    @Operation(summary = "Replace Student Profile Image")
    @PutMapping(
            value = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<StudentResponse>> updateImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        StudentResponse response =
                studentService.updateImage(id, file);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Profile image updated successfully",
                        response
                )
        );
    }

    // DELETE IMAGE
    @Operation(summary = "Delete Student Profile Image")
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long id) {

        studentService.deleteImage(id);

        return ResponseEntity.noContent().build();
    }
}