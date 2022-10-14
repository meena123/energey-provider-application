# energy-provider
Energy Provider Application:

Follow the below given steps:
1) To compile the project use the command from terminal: mvn clean compile
2) Start the project with embedded tomcat using the command: mvn spring-boot:run 
3) Insert the roles in the database with the below given sql:

    INSERT INTO roles(name) VALUES('ROLE_USER');

    INSERT INTO roles(name) VALUES('ROLE_MODERATOR');

    INSERT INTO roles(name) VALUES('ROLE_ADMIN');

4) Create a new user using below given endpoint:

http://localhost:8080/api/auth/signup

Add new user with user information in body as JSON (using postman)

Request:

{
    "username" : "test",
    "email" : "test.np@gmail.com",
    "password": "xxxx",
    "role": ["admin","mod"]
}

5) Signin with the registered user using below given endpoint:

http://localhost:8080/api/auth/signin

Request:

{
    "username": "test",
    "password": "xxxx"
}

NOTE: Above request will generate a token, that you need to put in the header with Authorization as Key and value as Bearer <token> for accessing other end point.

6) Add list of batteries using endpoint:

http://localhost:8080/api/provider/addBatteries

Request: 

{
    "providerName": "Provider1",
    "batteryInfo":[
      {"batteryName":"Automatic","postCode":"21333","wattCapacity":100},
      {"batteryName":"Manual","postCode":"90809","wattCapacity":200},
      {"batteryName":"SemiManual","postCode":"9809","wattCapacity":300} 
    ]
}

7) To retrieve the list of batteries based on the provided range use the below given endpoint:

http://localhost:8080/api/provider/powerRange

Request:

{
    "startRange":100,
    "endRange": 100
}
