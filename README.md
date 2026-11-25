# TaskFlow API

REST API for team task management: organize teams, projects, tasks and comments in one place, with real business rules, JWT authentication, centralized role-based authorization (RBAC), interactive OpenAPI documentation, observability, dynamic filtering and standardized error responses.

## About the Project

TaskFlow is a backend project built to practice, in a realistic scenario, the design and implementation of a layered REST API with Spring Boot. It has its own domain and evolves in stages, adding one layer at a time: core CRUD, security, documentation, observability, containerization, deployment and tests.

Users create **teams**, invite other users as members with a per-team role (`ADMIN` or `MEMBER`), organize work in **projects**, track **tasks** with status, priority and assignee, and discuss them through **comments**. Access rules depend on team membership (for example, a user can be an `ADMIN` in one team and a `MEMBER` in another), enforcing robust domain-level authorization.

**Current status:** Core API operations, stateless JWT authentication, centralized RBAC authorization, exception handling, interactive API documentation (Swagger UI) and observability (structured logging and Spring Boot Actuator metrics) are fully implemented and verified.

## Tech Stack

- Java 17+
- Spring Boot (Web, Data JPA, Validation, Actuator)
- Spring Security (BCrypt, JWT with OAuth2 Resource Server)
- springdoc-openapi (Swagger UI)
- Micrometer (Actuator metrics)
- SLF4J / Logback (logging)
- PostgreSQL
- MapStruct and Lombok
- Docker / Docker Compose (database and pgAdmin)
- Maven

## Domain Model

- **User** — system user, with a global role (`ADMIN` or `MEMBER`)
- **Team** — work team
- **TeamMembership** — association between User and Team (N:N), specifying the user's role in the team (`ADMIN` or `MEMBER`)
- **Project** — project belonging to a team
- **Task** — task belonging to a project, with status, priority, due date and an optional assignee
- **Comment** — comment written by a user on a task

## Business Rules & Security Matrix

- User emails are unique; passwords are stored as BCrypt hashes and never returned by the API.
- Team names are unique.
- The user who creates a team automatically becomes its first `ADMIN`.
- A user cannot be added twice to the same team.
- The last `ADMIN` of a team cannot be removed.
- A team that still has projects cannot be deleted.
- Project names are unique **within the same team**.
- A task's assignee must be an existing member of the project's team.
- A new task defaults to the `TODO` status when none is provided.
- Only team members can view, create, or update team resources.

### Security & Permission Matrix

| Action / Endpoint | Permission Required |
|---|---|
| `POST /users`, `POST /auth/login` | **Public** |
| `GET /users`, `GET /users/{id}` | Any Authenticated User |
| `PUT /users/{id}`, `DELETE /users/{id}` | Account Owner Only |
| `POST /teams` | Any Authenticated User (Creator becomes `ADMIN`) |
| `GET /teams/{id}`, `GET /teams/{id}/projects` | Team Member |
| `PUT /teams/{id}`, `DELETE /teams/{id}` | Team `ADMIN` |
| `POST /teams/{id}/members`, `DELETE /teams/{id}/members/{userId}` | Team `ADMIN` |
| `POST /projects`, `GET /projects/{id}`, `PUT /projects/{id}` | Team Member |
| `DELETE /projects/{id}` | Team `ADMIN` |
| `POST /tasks`, `GET /tasks/{id}`, `PUT /tasks/{id}` | Team Member (of owning team) |
| `GET /tasks` (listing) | Team Member (results filtered to user's teams) |
| `DELETE /tasks/{id}` | Team `ADMIN` |
| `POST /tasks/{id}/comments`, `GET /tasks/{taskId}/comments` | Team Member |
| `DELETE /comments/{id}` | Comment Author **OR** Team `ADMIN` |
| `GET /actuator/health` | **Public** |
| Other `/actuator/**` endpoints (`info`, `metrics`) | Global `ADMIN` |

> **Security Note (404 Policy):** To prevent resource enumeration attacks, unauthorized access attempts to non-owned domain resources throw a `ResourceNotFoundException` (HTTP `404 Not Found`) instead of revealing resource existence via HTTP `403 Forbidden`.

## Features

- **Stateless Authentication**: Login endpoint issuing JWT tokens signed with HS256 and BCrypt password hashing.
- **Centralized Service-Layer Authorization**: Enforced via `TeamAccessService` with a strict find-then-check pattern across all domain operations.
- **Interactive API Documentation**: OpenAPI specification and Swagger UI with Bearer token authentication, grouped endpoints and documented response codes.
- **Observability**: Rolling log files, per-package log levels, a colored console and Spring Boot Actuator endpoints (`health`, `info`, `metrics`) with restricted access, plus custom business metrics.
- **Dynamic Filtering & Pagination**: Powered by Spring Data JPA `Specification` (users by name/email; tasks by project, status, priority, and assignee).
- **Data Encapsulation**: DTOs implemented as Java records; domain entities are mapped using MapStruct and never exposed directly.
- **Auto-Association**: Team creation automatically binds the logged-in user as team `ADMIN`; comment authors are extracted automatically from the security context.
- **Robust Exception Handling**: Global exception handler and `SecurityErrorHandler` providing uniform error responses.

## Authentication

Only two API endpoints are public: `POST /users` (sign up) and `POST /auth/login`. Every other endpoint requires a valid JWT Bearer token. The documentation pages (Swagger UI and the OpenAPI specification) and the Actuator health endpoint are also publicly accessible.

### Login Request

```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "your-password"
}
```

### Login Response (`200 OK`)

```json
{
  "accessToken": "eyJhbGciOiJIUzI1Ni...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

Include the token in all subsequent requests:

```http
Authorization: Bearer <accessToken>
```

- Tokens expire after 60 minutes by default (`expiresIn` is in seconds).
- Invalid credentials return `401 Unauthorized` with a generic `Invalid credentials` message to prevent account enumeration.

## API Documentation

The API is documented with OpenAPI 3 and exposed through Swagger UI:

| Resource | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI specification (JSON) | `http://localhost:8080/v3/api-docs` |

To try the protected endpoints from Swagger UI:

1. Call `POST /auth/login` and copy the `accessToken` from the response.
2. Click **Authorize**, paste **only the token** (without the `Bearer ` prefix) and confirm.
3. Every request sent from the page now includes the `Authorization` header.

## Observability

### Logging

Logging uses SLF4J with Logback and is configured in `application.yml`.

- **Log file:** `logs/taskflow-api.log`, rotated at 10 MB per file, keeping 7 days of history and at most 100 MB in total. The `logs/` folder is ignored by Git.
- **Levels:** `INFO` by default, `DEBUG` for the application package and for the SQL executed by Hibernate, and `INFO` for Spring Security.
- **Console:** colored output for readability. The log file has the same messages without color codes.
- **What is logged:** business events (such as user registration, logins, team creation and membership changes) at `INFO`, and suspicious events (such as failed logins and denied access) at `WARN`.
- **What is never logged:** passwords, password hashes and tokens. Users are identified by id, not by email.

### Actuator

| Endpoint | Access | Description |
|---|---|---|
| `GET /actuator/health` | Public | Application status (`UP` or `DOWN`), without internal details |
| `GET /actuator/info` | Global `ADMIN` | Application name, description and version |
| `GET /actuator/metrics` | Global `ADMIN` | List of available metrics |
| `GET /actuator/metrics/{name}` | Global `ADMIN` | Value of a metric, such as `http.server.requests` or `jvm.memory.used` |

Only `health`, `info` and `metrics` are exposed. Other Actuator endpoints are not available.

The `ADMIN` role used here is the user's **global** role. There is no endpoint to grant it, so an administrator is created directly in the database:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@example.com';
```

After changing the role, log in again to get a token that carries it.

### Custom metrics

Besides the metrics provided by Spring Boot, the application records business metrics with Micrometer:

| Metric | Tags | Description |
|---|---|---|
| `taskflow.login.attempts` | `result`: `success` or `failure` | Login attempts |
| `taskflow.tasks.created` | none | Tasks created |

Example: `GET /actuator/metrics/taskflow.login.attempts?tag=result:failure`.

## API Overview

| Resource | Method | Endpoint |
|---|---|---|
| **Auth** | `POST` | `/auth/login` |
| **Users** | `POST` | `/users` |
| | `GET` | `/users`, `/users/{id}` |
| | `PUT` | `/users/{id}` |
| | `DELETE` | `/users/{id}` |
| **Teams** | `POST` | `/teams` |
| | `GET` | `/teams/{id}`, `/teams/{teamId}/projects` |
| | `PUT` | `/teams/{id}` |
| | `DELETE` | `/teams/{id}` |
| **Team Members** | `POST` | `/teams/{teamId}/members` |
| | `DELETE` | `/teams/{teamId}/members/{userId}` |
| **Projects** | `POST` | `/projects` |
| | `GET` | `/projects/{id}` |
| | `PUT` | `/projects/{id}` |
| | `DELETE` | `/projects/{id}` |
| **Tasks** | `POST` | `/tasks` |
| | `GET` | `/tasks`, `/tasks/{id}` |
| | `PUT` | `/tasks/{id}` |
| | `DELETE` | `/tasks/{id}` |
| **Comments** | `POST` | `/tasks/{taskId}/comments` |
| | `GET` | `/tasks/{taskId}/comments` |
| | `DELETE` | `/comments/{id}` |

### Error Format

All errors return a standardized JSON structure:

```json
{
  "status": 422,
  "message": "Validation error",
  "errors": [
    { "field": "password", "error": "Password must be between 8 and 255 characters" }
  ]
}
```

| HTTP Status | Trigger Condition |
|---|---|
| **401 Unauthorized** | Missing/invalid authentication token or incorrect login credentials |
| **403 Forbidden** | Authenticated user lacks the required global role (for example, Actuator endpoints) |
| **404 Not Found** | Resource does not exist OR unauthorized team-level access attempt |
| **409 Conflict** | Unique constraint or business rule violation (e.g., removing last ADMIN) |
| **422 Unprocessable Entity** | Request DTO validation failure |

## Project Structure

```
src/main/java/com/nicolasmartineli/taskflow_api
├── configs        # Spring configuration (Security, JWT, OpenAPI, Beans)
├── controllers    # REST Controllers
├── dtos           # Request/Response DTO records
├── exceptions     # Custom business exceptions & GlobalExceptionHandler
├── mappers        # MapStruct mapping interfaces
├── models         # JPA entities and Enums
├── repositories   # Spring Data Repositories & Specifications
├── security       # JWT, CurrentUserService & SecurityErrorHandler
└── services       # Business logic & authorization checks (TeamAccessService)
```

## How to Run

### Prerequisites
- JDK 17+
- Maven
- Docker & Docker Compose

### Execution Steps

1. **Clone the repository:**
   ```bash
   git clone https://github.com/nicolasmartineli/taskflow-api.git
   cd taskflow-api
   ```

2. **Start Database & pgAdmin:**
   ```bash
   docker compose up -d
   ```

3. **Configure Environment Variables:**
   Set the mandatory `JWT_SECRET` key (minimum 32 characters / 256 bits):
   ```bash
   # Linux / macOS / Git Bash
   export JWT_SECRET="your-at-least-32-character-long-secret-key"

   # PowerShell
   $env:JWT_SECRET="your-at-least-32-character-long-secret-key"
   ```
   Optional: `JWT_EXPIRATION_MINUTES` changes the token lifetime (default `60`).

4. **Run the Application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   The API will start at `http://localhost:8080`.

5. **Explore the API:**
    - Swagger UI: `http://localhost:8080/swagger-ui.html`
    - Health check: `http://localhost:8080/actuator/health`

## Roadmap

- [x] Database modeling & N:N associative tables
- [x] Full CRUD operations for core domain entities
- [x] Bean Validation and global exception handling (`ApiError`)
- [x] Dynamic filtering & pagination using Spring Data JPA `Specification`
- [x] PostgreSQL & pgAdmin setup via Docker Compose
- [x] Authentication & Authorization (Spring Security + JWT)
    - [x] BCrypt password hashing
    - [x] Custom `UserDetailsService` implementation
    - [x] Token generation & authentication endpoints
    - [x] Stateless route protection
    - [x] Per-team authorization layer (`TeamAccessService`)
    - [x] Standardized 401, 403, and 404 security responses
- [x] Documentation with Swagger / OpenAPI
- [x] Observability & Metrics (Logging, Spring Boot Actuator & Micrometer)
- [ ] Application containerization (Dockerfile)
- [ ] Cloud Deployment (AWS)
- [ ] Unit & Integration Test Suite
- [ ] OAuth2 / Keycloak integration

## Author

**Nícolas Lopes Martineli**