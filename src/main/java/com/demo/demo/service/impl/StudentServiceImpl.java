package com.demo.demo.service.impl;

import com.demo.demo.dto.request.StudentCreateRequest;
import com.demo.demo.dto.request.StudentUpdateRequest;
import com.demo.demo.dto.response.StudentResponse;
import com.demo.demo.entity.Role;
import com.demo.demo.entity.RoleName;
import com.demo.demo.entity.Student;
import com.demo.demo.entity.User;
import com.demo.demo.exception.DuplicateResourceException;
import com.demo.demo.exception.ResourceNotFoundException;
import com.demo.demo.mapper.StudentMapper;
import com.demo.demo.repository.RoleRepository;
import com.demo.demo.repository.StudentRepository;
import com.demo.demo.repository.UserRepository;
import com.demo.demo.service.CloudinaryService;
import com.demo.demo.service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    public StudentServiceImpl(StudentRepository studentRepository,
                              UserRepository userRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder,
                              CloudinaryService cloudinaryService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    @Transactional
    public StudentResponse create(StudentCreateRequest r) {
        if (userRepository.existsByUsername(r.username())) {
            throw new DuplicateResourceException("Username already taken: " + r.username());
        }
        if (r.email() != null && !r.email().isBlank() && userRepository.existsByEmail(r.email())) {
            throw new DuplicateResourceException("Email already registered: " + r.email());
        }
        if (studentRepository.existsByRollNo(r.rollNo())) {
            throw new DuplicateResourceException("Roll number already exists: " + r.rollNo());
        }

        Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Role STUDENT not found"));

        // Persist the User first — the @OneToOne cascade on Student.user is MERGE+REFRESH
        // (not PERSIST), so a brand-new User must be saved explicitly before the Student
        // can reference it, otherwise Hibernate throws TransientPropertyValueException.
        User user = User.builder()
                .username(r.username())
                .email(r.email())
                .password(passwordEncoder.encode(r.password()))
                .enabled(true)
                .roles(Set.of(studentRole))
                .build();
        User savedUser = userRepository.save(user);

        Student student = Student.builder()
                .fullName(r.fullName())
                .rollNo(r.rollNo())
                .course(r.course())
                .phone(r.phone())
                .user(savedUser)
                .build();

        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponse update(Long id, StudentUpdateRequest r) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if (!student.getRollNo().equals(r.rollNo()) && studentRepository.existsByRollNo(r.rollNo())) {
            throw new DuplicateResourceException("Roll number already exists: " + r.rollNo());
        }

        student.setFullName(r.fullName());
        student.setRollNo(r.rollNo());
        student.setCourse(r.course());
        student.setPhone(r.phone());

        if (r.email() != null && !r.email().isBlank()) {
            String currentEmail = student.getUser().getEmail();
            if (!r.email().equals(currentEmail) && userRepository.existsByEmail(r.email())) {
                throw new DuplicateResourceException("Email already registered: " + r.email());
            }
            student.getUser().setEmail(r.email());
        }

        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // remove Cloudinary asset if present
        if (student.getImagePublicId() != null) {
            cloudinaryService.delete(student.getImagePublicId());
        }

        // Detach the user so deleting the student does NOT delete the auth account
        student.setUser(null);
        studentRepository.save(student);
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return studentRepository.findById(id)
                .map(StudentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getMyProfile(String username) {
        return studentRepository.findByUser_Username(username)
                .map(StudentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
    }

    @Override
    @Transactional
    public StudentResponse uploadImage(Long id, MultipartFile file) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if (student.getImagePublicId() != null) {
            // already has an image -> use update semantics
            return updateImage(id, file);
        }

        var result = cloudinaryService.upload(file);
        student.setImageUrl(result.get("url"));
        student.setImagePublicId(result.get("publicId"));
        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponse updateImage(Long id, MultipartFile file) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // delete old asset first
        if (student.getImagePublicId() != null) {
            cloudinaryService.delete(student.getImagePublicId());
        }

        var result = cloudinaryService.upload(file);
        student.setImageUrl(result.get("url"));
        student.setImagePublicId(result.get("publicId"));
        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void deleteImage(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if (student.getImagePublicId() != null) {
            cloudinaryService.delete(student.getImagePublicId());
        }
        student.setImageUrl(null);
        student.setImagePublicId(null);
        studentRepository.save(student);
    }
}
