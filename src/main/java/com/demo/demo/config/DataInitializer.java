package com.demo.demo.config;

import com.demo.demo.entity.Role;
import com.demo.demo.entity.RoleName;
import com.demo.demo.entity.User;
import com.demo.demo.entity.Student;
import com.demo.demo.repository.RoleRepository;
import com.demo.demo.repository.UserRepository;
import com.demo.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.username}")
    private String adminUsername;
    @Value("${app.default-admin.email}")
    private String adminEmail;
    @Value("${app.default-admin.password}")
    private String adminPassword;
    @Value("${app.default-student.username:student}")
    private String studentUsername;
    @Value("${app.default-student.password:Student@123}")
    private String studentPassword;
    @Value("${app.default-student.full-name:Leela Sankar}")
    private String studentFullName;
    @Value("${app.default-student.roll-no:STU001}")
    private String studentRollNo;
    @Value("${app.default-student.course:ECE}")
    private String studentCourse;
    @Value("${app.default-student.phone:9999999999}")
    private String studentPhone;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           StudentRepository studentRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build()));
        Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_STUDENT).build()));

        // Seed admin user
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .enabled(true)
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
        }

        // Seed student user + linked student record
        if (userRepository.findByUsername(studentUsername).isEmpty()) {
            User studentUser = User.builder()
                    .username(studentUsername)
                    .email(studentUsername + "@school.com")
                    .password(passwordEncoder.encode(studentPassword))
                    .enabled(true)
                    .roles(Set.of(studentRole))
                    .build();
            User savedUser = userRepository.save(studentUser);

            if (studentRepository.findByUser_Username(studentUsername).isEmpty()) {
                Student student = Student.builder()
                        .fullName(studentFullName)
                        .rollNo(studentRollNo)
                        .course(studentCourse)
                        .phone(studentPhone)
                        .user(savedUser)
                        .build();
                studentRepository.save(student);
            }
        }
    }
}
