# Alura Test Case
This project is a test case for a Java Backend Developer position at [Alura](https://www.alura.com.br/).

## Table of Contents
* [Technologies](#Technologies)
* [Running](#Running)
* [API Endpoints](#API_Endpoints)
* [Users](#Users)
* [Courses](#Courses)
* [Authentication](#Authentication)
* [Tests](#Tests)
##

<a name="Technologies"/>

### Technologies
This project was built using the following technologies:
* Java 21;
* Maven;
* Spring Framework (Boot, Security and JPA);
* MySQL;
* H2 for integration tests;
* Flyway;
* Docker.
##

<a name="Running"/>

### Running
1. Open the terminal on the [/docker](https://github.com/LuizGCF/alura-test-case/tree/master/docker) folder
2. [Run](https://www.docker.com/):
```
docker-compose -f mysql-docker-compose.yml up -d
```
3. Start the project. The default port for this project is 8080.
##

<a name="API_Endpoints"/>

### API Endpoints

A [Postman](https://www.postman.com/) collection with all available endpoints is available at the [/collection](https://github.com/LuizGCF/alura-test-case/tree/master/collection) folder.
##

<a name="Users"/>

### Users
Since the user creation endpoint is public, no user is loaded within the database creation.  
Users can have one of the three available roles: STUDENT, INSTRUCTOR or ADMIN.
##

<a name="Courses"/>

### Courses
Courses can be created by ADMIN authenticated users.  
Courses can have one of the two available status: ACTIVE or INACTIVE.
##

<a name="Authentication"/>

### Authentication
This project uses JWT for user authentication and endpoints are also protected based on the user's role.  
For convenience, the collection available in this project already sets the generated token in the environment.  
All endpoints which require authentication also have the variable set with the token in this collection.
##

<a name="Tests"/>

### Tests
This project has 100% coverage on all Controllers, Services and Repositories.
##
Thank you for reading!