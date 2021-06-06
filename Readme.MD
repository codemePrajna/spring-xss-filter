# Spring XSS Filter

This PoC is all about to apply XSS security checks to a Spring Boot RESTful service.

## Folder Structure
### xss-utils
Utility library having XSS filters and bean configurations
### customer-service
An example service where the library xss-utils is applied

## Running Application
### Prerequisites
* Docker or Postgres installed on your machine
* Java JDK 1.8 or Higher 
* Maven 3.x.x
* An IDE (Eclipse or IntelliJ IDEA) if you want to run it locally
### Steps
* Run postgres with `docker-compose -f docker/docker-compose.yml up -d`
* Go to `CustomerServiceApplication` and run the Spring Boot application on your IDE
### ExampleRequests
* You can use the requests from `requests.http`


