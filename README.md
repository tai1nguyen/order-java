# Order-Java
A simple **Spring Boot** CRUD application for ordering coffee beans. A personal project to help with learning **Spring** technologies, **Hibernate**, and **SQL**.

## Prerequisites
- This service uses:
    1. Java 15
    2. Gradle 6.7.1

**NOTE:** If you do not have either, it is recommended, but not required, that you use [SDKMAN](https://sdkman.io/) to install and manage both development kits.

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
- Start the server with `./gradlew bootRun`.
    - The application will be hosted at `http://localhost:8080/order-java/`.
- Run all tests with `./gradlew test`.

## Tips
### Helpful Links
- API documentation can be found at `http://localhost:8080/order-java/swagger-ui`.
- The code coverage report can be found at `./build/reports/jacoco/test/html/index.html`.
- The test results can be found at `./build/reports/tests/test/index.html`.

**NOTE:** Both test and code coverage reports will be generated when `./gradlew test` or `./gradlew build` is executed.

### Setting up automated formatting in VSCode
- In order to proceed the **[Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)** must be installed.
- Navigate to `./.vscode/settings.json`.
    - Create a new json file named `settings.json` if one does not already exist.
- Within `settings.json` add the following properties:
    1. `"java.format.settings.url": "order-java-style.xml"`
        - Tells the Java Extension Pack to use the provided formatter file when formatting java files.
    2. `"editor.formatOnSave": true`
        - Enables automatic formatting of the active file when it is saved.
        - **NOTE:** if VSCode is already set up to automatically save files you may want to disable this property and only execute formatting manually.
- Manually formatting files can be done with `shift + option + f`.
