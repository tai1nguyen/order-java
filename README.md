# Order-Java
A simple **Spring Boot** CRUD application for ordering coffee beans. A personal project to help with learning **Spring** technologies, **Hibernate**, and **SQL**.

## Todo
- [X] create initial application
    - [X] create database
    - [X] create api document
    - [X] create CRUD rest APIs
    - [X] add unit tests
    - [X] add integration tests
- [ ] improve tests
    - [ ] update tests to use separate database data
- [ ] improve database
    - [ ] convert in-memory database to read and store data to a static file.
- [X] add automated API documentation

## Running the App
- Start the server with `./gradlew bootRun`. The application will be hosted at `http://localhost:8080/order-java/`
- Run all tests with `./gradlew test`

## API Docs
- API documentation can be found at `http://localhost:8080/order-java/swagger-ui`