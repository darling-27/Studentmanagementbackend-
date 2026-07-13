# Studentmanagementbackend-
Student Management System built with Spring Boot, MySQL, JWT Authentication, Role-Based Access Control (RBAC), Cloudinary Image Upload, Swagger/OpenAPI Documentation, and secure REST APIs.
The application provides secure role-based access control (RBAC), allowing administrators to manage student records while students can securely access their own profile information.
---
##  Features
### Authentication & Security
- JWT Authentication
- Role-Based Access Control (ADMIN, STUDENT)
- BCrypt Password Encryption
- Stateless Session Management
- Secure REST APIs
- Custom Authentication & Authorization Handling

### Student Management
- Create Student
- View All Students
- View Student By ID
- Update Student Information
- Delete Student Records

### Image Management
- Upload Student Profile Images
- Delete Student Profile Images
- Cloudinary Cloud Storage Integration

### API Documentation
- Swagger UI Integration
- OpenAPI 3 Documentation
- Interactive API Testing

### Error Handling
- Global Exception Handling
- Standardized API Responses
- Validation Error Management
- Secure Error Messages
---
## Technology Stack

| Technology | Version |
|------------|----------|
| Java | 21 |
| Spring Boot | 4.x |
| Spring Security | Latest |
| JWT | 0.12.x |
| MySQL | 8.x |
| Hibernate / JPA | Latest |
| Maven | Latest |
| Cloudinary | Latest |
| Swagger / OpenAPI | 3.x |
---
##  Project Structure
```text
src
├── main
│   ├── java
│   │   └── com.demo.demo
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── repository
│   │       ├── security
│   │       ├── service
│   │       └── util
│   └── resources
│       └── application.properties
```
---
## Roles & Permissions
### ADMIN
- Create Students
- View All Students
- Update Student Information
- Delete Students
- Upload Student Images
- Delete Student Images

### STUDENT

- Login
- View Own Profile
- Access Authorized Resources Only

---
##  API Endpoints
### Authentication

```http
POST /api/auth/login
GET  /api/auth/health
```

### Admin APIs

```http
GET    /api/admin/students
GET    /api/admin/students/{id}
POST   /api/admin/students
PUT    /api/admin/students/{id}
DELETE /api/admin/students/{id}
```

### Image APIs

```http
POST   /api/admin/students/{id}/image
DELETE /api/admin/students/{id}/image
```

### Student APIs

```http
GET /api/students/me
```

---

##  Configuration
### Database

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/studentdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Cloudinary

```properties
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

### JWT

```properties
jwt.secret=your_secret_key
jwt.expiration=86400000
```
---
## Running the Application

### Clone Repository

```bash
git clone https://github.com/yourusername/student-management-system-backend.git
```

### Navigate to Project

```bash
cd student-management-system-backend
```

### Build Project

```bash
mvn clean install
```

### Run Application

```bash
mvn spring-boot:run
```

Application runs at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---
## Testing Status
### Verified APIs
- Authentication APIs
- JWT Token Generation
- Role-Based Authorization
- Student CRUD Operations
- Image Upload/Delete APIs
- Global Exception Handling
- Swagger Documentation
- MySQL Integration
---
## Future Improvements
- Refresh Token Support
- Email Verification
- Password Reset Module
- Audit Logging
- Docker Support
- CI/CD Pipeline
- Unit & Integration Tests
- Deployment on AWS / Railway / Render
---
## Author
**Sankar Setti**
Final Year B.Tech (ECE)
Backend Developer | Spring Boot | Java | MySQL | REST APIs | JWT Security
GitHub: https://github.com/darling-27
---

## ⭐ If you found this project useful, please give it a star.
