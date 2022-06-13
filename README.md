# Employee Management Platform (EMP) for WorkMotion

## Overview
The following repo contains a springboot app responsible for building emp platform for employees management system.

## Guidelines
Please follow the below steps to get the application up and running

1. Clone this repository
2. You can either use the jar already built in the project, or feel free to build it again using :
    ```sh
    foo@bar:~$ mvn clean package
    ```
3. Go to the root folder :
   ```sh
    foo@bar:~$ cd emp
    ```
4. Use docker-compose command line to start the containers :
    ```sh
    foo@bar:~$ docker-compose up
    ```
    
## Diagrams
### Class diagram
![Class Diagram](emp-class-diagram.png)


## Documentation
### Main endpoints
1. Create an Employee
> POST localhost:8080/workmotion/employee
2. Retrieve an Employee by ID 
> GET localhost:8080/workmotion/employee/{Id}
3. Retrieve all Employees
> GET localhost:8080/workmotion/employees
4. Update an Employee
> PUT localhost:8080/workmotion/employee

### Swagger detailed documentation
Swagger is automatically embedded to the API, the detailed documentation can be accessed from the link below :
>[Swagger UI](http://localhost:8080/swagger-ui/index.html)