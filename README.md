# TaskFlow API

REST API for team task management: organize teams, projects, tasks and comments in one place, with real business rules, dynamic filtering and standardized error responses.

## About the Project

TaskFlow is a backend project built to practice, in a realistic scenario, the design and implementation of a layered REST API with Spring Boot. It has its own domain and evolves in stages, adding one layer at a time: core CRUD, security, documentation, observability, containerization, deployment and tests.

Users create **teams**, invite other users as members with a per-team role, organize work in **projects**, track **tasks** with status, priority and assignee, and discuss them through **comments**. Access rules depend on the data (for example, being an `ADMIN` in one team and a `MEMBER` in another), which makes the domain a good fit for role-based authorization.

**Current status:** the core API (CRUD, validation, filtering and error handling) is implemented. Security is in progress: password hashing with BCrypt and the user loading layer are done, and JWT authentication is the next step. Until it is merged, the endpoints are open.

## Tech Stack

- Java
- Spring Boot (Web, Data JPA, Validation, Security)
- PostgreSQL
- MapStruct and Lombok
- Docker / Docker Compose (database and pgAdmin)
- Maven

## Domain Model

- **User** — system user, with a global role (`ADMIN` or `MEMBER`)
- **Team** — work team
- **TeamMembership** — association between User and Team (N:N), specifying the user's role in the team
- **Project** — project belonging to a team
- **Task** — task belonging to a project, with status, priority, due date and an optional assignee
- **Comment** — comment written by a user on a task

## Business Rules

- User emails are unique; passwords are stored as BCrypt hashes and never returned by the API.
- Team names are unique.
- A user cannot be added twice to the same team.
- The last `ADMIN` of a team cannot be removed.
- Project names are unique **within the same team**.
- A task's assignee must be a member of the team that owns the project.
- A new task defaults to the `TODO` status when none is provided.
- Only members of the team can comment on its tasks.

## Features

- Full CRUD for users, teams, projects and tasks; comments can be created, listed and deleted.
- Dynamic filtering and pagination using Spring Data JPA `Specification` (users by name and email; tasks by project, status, priority and assignee).
- DTOs implemented as Java records, with mapping handled by MapStruct, so entities are never exposed directly.
- Bean Validation on every request DTO.
- Global exception handling with a standardized error body.
- `Location` header on resource creation (`201 Created`).
- Transactional write operations in the service layer.

## API Overview

| Resource | Endpoints |
|---|---|
| Users | `POST /users`, `GET /users`, `GET /users/{id}`, `PUT /users/{id}`, `DELETE /users/{id}` |
| Teams | `POST /teams`, `GET /teams/{id}`, `PUT /teams/{id}`, `DELETE /teams/{id}`, `GET /teams/{teamId}/projects` |
| Team members | `POST /teams/{teamId}/members`, `DELETE /teams/{teamId}/members/{userId}` |
| Projects | `POST /projects`, `GET /projects/{id}`, `PUT /projects/{id}`, `DELETE /projects/{id}` |
| Tasks | `POST /tasks`, `GET /tasks`, `GET /tasks/{id}`, `PUT /tasks/{id}`, `DELETE /tasks/{id}` |
| Comments | `POST /tasks/{taskId}/comments`, `GET /tasks/{taskId}/comments`, `DELETE /comments/{id}` |

Listing endpoints accept standard pagination parameters (`page`, `size`, `sort`).

### Error format

Every error follows the same structure:

```json
{
  "status": 422,
  "message": "Validation error",
  "errors": [
    { "field": "password", "error": "Password must be between 8 and 255 characters" }
  ]
}
```

| Status | Meaning |
|---|---|
| 404 | Resource not found |
| 409 | Conflict (duplicate resource or business rule violation) |
| 422 | Validation error on the request body |

## Project Structure

```
src/main/java/com/nicolasmartineli/taskflow_api
├── configs        # Spring configuration (security, beans)
├── controllers    # REST layer
├── dtos           # Request/response records, grouped by entity
├── exceptions     # Custom exceptions and global handler
├── mappers        # MapStruct mappers
├── models         # JPA entities and enums
├── repositories   # Spring Data repositories and specifications
├── security       # Authentication components
└── services       # Business logic
```

## How to Run

**Requirements:** JDK (version defined in `pom.xml`), Maven and Docker.

1. Clone the repository.
2. Start the database (and pgAdmin) with Docker Compose:
```bash
   docker compose up -d
```
3. Run the application:
```bash
   ./mvnw spring-boot:run
```
4. The API is available at `http://localhost:8080`.

The PostgreSQL database and the pgAdmin credentials are defined in `docker-compose.yml`, and the datasource settings are in `src/main/resources/application.yml`.

## Roadmap

- [x] Database modeling
- [x] CRUD operations for entities (User, Team, Project, Task, Comment)
- [x] Bean Validation and exception handling
- [x] Filtering and pagination with Specifications
- [x] PostgreSQL running on Docker Compose
- [ ] Authentication and authorization (Spring Security + JWT)
    - [x] Password hashing with BCrypt
    - [x] User loading for authentication
    - [ ] Login and JWT generation
    - [ ] Route protection and per-team access rules
- [ ] OAuth2
- [ ] Documentation with Swagger/OpenAPI
- [ ] Logging and observability (Spring Boot Actuator)
- [ ] Application containerization with Docker
- [ ] AWS Deployment
- [ ] Unit and integration tests
- [ ] Keycloak integration

## Author

Nícolas Lopes Martineli