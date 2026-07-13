# API Documentation

**Base URL:** `http://localhost:8080/api`  
**Auth:** JWT Bearer token in `Authorization: Bearer <token>`  
**Content-Type:** `application/json` (except image upload: `multipart/form-data`)

---

## Authentication

### `POST /auth/login`
Public endpoint — no auth required.

**Request body:**
```json
{
  "username": "admin",
  "password": "Admin@123"
}
```

**Success response (200):**
```json
{
  "success": true,
  "status": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbG...",
    "username": "admin",
    "roles": ["ROLE_ADMIN"]
  },
  "timestamp": "2026-07-12T..."
}
```

**Errors:**
- `400 Bad Request` — validation failure (blank username/password)
- `401 Unauthorized` — wrong credentials

> **Default admin credentials:** `admin` / `Admin@123` (auto-created on startup)

---

## Student Endpoints (Admin)

All endpoints below require a JWT with **ROLE_ADMIN**.

---

### `POST /admin/students`
Create a new student (also creates the linked login User with ROLE_STUDENT).

**Request body:**
```json
{
  "username": "john",
  "email": "john@school.com",
  "password": "Pass@123",
  "fullName": "John Doe",
  "rollNo": "CS2024-01",
  "course": "B.Tech CSE",
  "phone": "9876543210"
}
```

**Success response (201 Created):**
```json
{
  "success": true,
  "status": 201,
  "message": "Student created",
  "data": {
    "id": 1,
    "fullName": "John Doe",
    "rollNo": "CS2024-01",
    "course": "B.Tech CSE",
    "phone": "9876543210",
    "imageUrl": null,
    "username": "john",
    "email": "john@school.com",
    "createdAt": "2026-07-12T11:00:00",
    "updatedAt": "2026-07-12T11:00:00",
    "createdBy": "admin",
    "updatedBy": "admin"
  }
}
```

**Errors:**
- `409 Conflict` — username, email, or roll number already exists
- `404` — internal role not found (should not happen in normal flow)

---

### `GET /admin/students`
List all students.

**Success response (200):**
```json
{
  "success": true,
  "status": 200,
  "message": "All students",
  "data": [ ...StudentResponse objects... ]
}
```

---

### `GET /admin/students/{id}`
Get a student by ID.

**Success response (200):** single `StudentResponse` (same shape as above)

**Errors:**
- `404 Not Found` — student with given ID does not exist

---

### `PUT /admin/students/{id}`
Update a student.

**Request body:**
```json
{
  "fullName": "John Doe",
  "rollNo": "CS2024-02",
  "course": "B.Tech CSE",
  "phone": "9876543210",
  "email": "john.new@school.com"
}
```
> `email` is optional; omit it to leave the existing email unchanged.

**Success response (200):** updated `StudentResponse`

**Errors:**
- `404` — student not found
- `409` — new roll number already taken by another student

---

### `DELETE /admin/students/{id}`
Delete a student. Also deletes the linked `users` login account and any Cloudinary image.

**Success response (204 No Content):** empty body

**Errors:**
- `404` — student not found

---

## Image Endpoints (Admin)

All image endpoints require a multipart/form-data request with a `file` field.

---

### `POST /admin/students/{id}/image`
Upload a profile image for a student.

**Request:** `multipart/form-data`
- `file` — image file (max 5 MB)

**Success response (200):** `StudentResponse` with `imageUrl` populated

**Errors:**
- `404` — student not found
- `500` — upload failed (Cloudinary or file error)

---

### `PUT /admin/students/{id}/image`
Replace an existing image (old asset on Cloudinary is deleted first).

**Request:** same as POST

**Success response (200):** `StudentResponse` with new `imageUrl`

**Errors:** same as POST

---

### `DELETE /admin/students/{id}/image`
Remove the profile image (deletes from Cloudinary and nulls the URL in DB).

**Success response (204 No Content):** empty body

**Errors:**
- `404` — student not found

---

## Student Endpoints (Self)

Require a JWT with **ROLE_STUDENT**.

---

### `GET /students/me`
Get the logged-in student's own profile.

**Success response (200):** single `StudentResponse`

**Errors:**
- `404 Not Found` — no student profile linked to the logged-in account

---

## Student Response Object

```json
{
  "id": 1,
  "fullName": "John Doe",
  "rollNo": "CS2024-01",
  "course": "B.Tech CSE",
  "phone": "9876543210",
  "imageUrl": "https://res.cloudinary.com/...",
  "username": "john",
  "email": "john@school.com",
  "createdAt": "2026-07-12T11:00:00",
  "updatedAt": "2026-07-12T11:00:00",
  "createdBy": "admin",
  "updatedBy": "admin"
}
```

---

## Error Response

All errors follow this shape:

```json
{
  "success": false,
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "fieldErrors": {
    "username": "must not be blank",
    "email": "must be a valid email"
  },
  "timestamp": "2026-07-12T..."
}
```

`fieldErrors` is `null` for non-validation errors.

---

## Security Summary

| Path pattern | Access |
|---|---|
| `POST /api/auth/**` | Public |
| `GET /swagger-ui/**`, `/v3/api-docs/**` | Public |
| `/api/admin/**` | `ROLE_ADMIN` |
| `/api/students/me` | `ROLE_STUDENT` |
| Everything else | Authenticated (any role) |

Swagger UI: `http://localhost:8080/swagger-ui.html`
