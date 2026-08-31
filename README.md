# TaskFlow API

REST API for team task management — organize teams, projects, tasks, and comments all in one place.

## About the Project

Personal project developed to apply hands-on concepts studied in the *Spring Boot Expert* course (by Dougllas Sousa), evolving in parallel with the course modules.

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Maven

## Domain Model

- **User** — system user
- **Team** — work team
- **TeamMembership** — association between User and Team, specifying the user's role in the team
- **Project** — project belonging to a team
- **Task** — task belonging to a project, assigned to a user
- **Comment** — comment on a task

## How to Run

*(Under construction — Docker Compose setup instructions will be added here)*

## Roadmap

- [ ] Database modeling
- [ ] CRUD operations for entities (User, Team, Project, Task, Comment)
- [ ] Bean Validation and exception handling
- [ ] Filtering with Specifications
- [ ] Authentication and authorization (Spring Security + JWT)
- [ ] OAuth2
- [ ] Documentation with Swagger/OpenAPI
- [ ] Logging and observability (Spring Boot Actuator)
- [ ] Containerization with Docker
- [ ] AWS Deployment
- [ ] Unit and integration tests
- [ ] Keycloak integration

## Author

Nícolas Lopes Martineli
