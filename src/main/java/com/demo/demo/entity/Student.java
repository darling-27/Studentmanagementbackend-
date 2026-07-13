package com.demo.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students",
       uniqueConstraints = @UniqueConstraint(columnNames = "roll_no"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Student extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(name = "roll_no", nullable = false, length = 50)
    private String rollNo;

    @Column(length = 100)
    private String course;

    @Column(length = 20)
    private String phone;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_public_id")
    private String imagePublicId;     // needed for Cloudinary update/delete

    // Link to the login account of this student.
    // user_id may be null temporarily during student deletion so the auth account is preserved.
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
