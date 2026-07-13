package com.demo.demo.controller;

import com.demo.demo.dto.response.ApiResponse;
import com.demo.demo.dto.response.StudentResponse;
import com.demo.demo.security.CustomUserDetails;
import com.demo.demo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@PreAuthorize("hasRole('STUDENT')")
@Tag(name = "Student (Self)", description = "Endpoints accessible by a logged-in student for their own profile.")
public class StudentController {

    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Get my profile",
            description = "Returns the logged-in student's own record using the username inside the JWT."
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> myProfile(@AuthenticationPrincipal CustomUserDetails principal) {
        StudentResponse me = studentService.getMyProfile(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Your profile", me));
    }
}
