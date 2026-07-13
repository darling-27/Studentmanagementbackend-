package com.demo.demo.mapper;

import com.demo.demo.dto.response.StudentResponse;
import com.demo.demo.entity.Student;
import com.demo.demo.entity.User;

public final class StudentMapper {

    private StudentMapper() {}

    public static StudentResponse toResponse(Student s) {
        User user = s.getUser();
        return new StudentResponse(
                s.getId(),
                s.getFullName(),
                s.getRollNo(),
                s.getCourse(),
                s.getPhone(),
                s.getImageUrl(),
                user != null ? user.getUsername() : null,
                user != null ? user.getEmail() : null,
                s.getCreatedAt(),
                s.getUpdatedAt(),
                s.getCreatedBy(),
                s.getUpdatedBy()
        );
    }
}
