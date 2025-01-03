# fintech_app

# Setup Instructions for a Fintech App Project
- Set Up the Development Environment:

The following dependencies and tools set up:

Java Development Kit (JDK): Using JDK 17 

Build Tool: Used  Maven  for managing dependencies.

IDE: Used an IntelliJ IDEA.

Database: MySQL

- Set Up Project

The project is setup from the spring application (start.spring.io)

Added the necessary dependencies that will be used during the project

Generate the zip file of the project and extract the project.

Create a github repository and cloned the project.

Put the extracted spring boot application into the cloned repository.

Open the project with IntelliJ IDEA.

-Packages

Create different packages like config, controller, models, service,validations,
data transfer objects, repository that will be used in the project.

- Models

Users account model: was created to have information of the fintech app users.

Loan model: was created to keep track of the loan information

Transactions model : was created to keep track of user's transactions

Admin model : was created to perform some important task and to get information of the admin user's.

- Repositories

UserAccount Respository : It helps to persist users information into the database.

Loan Respository : It helps to persist loan information into the database.
Admin Respository: It helps to persist admin information into the database.
Transaction Respository : It helps to transaction users information into the database.

- Services

It is the package where we have all our implementations

- Controller
It is the place we have all our endpoints and we have Admin and User controller.

- Security (Authenticationa and Authorization)

JWT is the security that was implemented which also ensure 
role based access control.