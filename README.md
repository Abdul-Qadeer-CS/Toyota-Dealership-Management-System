# Toyota-Dealership-Management-System



A Java-based Toyota Car Dealership Management System that allows customers to browse and purchase cars online, and employees to manage the full dealership database including cars, customers, sales, and maintenance records.



\## Group Members



| Full Name    | CMS/ID      | Section |

|--------------|-------------|---------|

| Abdul Qadeer | 023-25-0019 |    E    |

| Abdul Faheem | 023-25-0002 |    E    |



\## Purpose 



This system solves the problem of managing a Toyota car dealership efficiently. It provides two types of users:

\- \*\*Customers\*\* — can browse available cars, filter by preferences and purchase online

\- \*\*Employees/Admin\*\* — can manage all dealership data including cars, customers, sales and maintenance records



\## Main Modules



\- \*\*Cars\*\* — stores all car information including specifications and features

\- \*\*Customers\*\* — stores customer information

\- \*\*Employees\*\* — manages dealership staff

\- \*\*Sales\*\* — records all car sales transactions

\- \*\*Maintenance\*\* — tracks car servicing records



\## OOP Concepts Used



\- Classes and Objects

\- Encapsulation (private fields with getters and setters)

\- Interfaces (DatabaseOperations)

\- Inheritance and Polymorphism

\- Exception Handling



\## Database



\- MySQL database named `toyota\_dms`

\- Connected via JDBC (MySQL Connector/J 9.6.0)



\## How to Run



\### Requirements

\- JDK 8 or above

\- MySQL Server installed and running

\- MySQL Connector/J 9.6.0



\### Steps

1\. Clone or download this repository

2\. Import the SQL file into MySQL Workbench to create the database

3\. Open `DatabaseConnection.java` and update your MySQL password

4\. Compile using:javac -cp ".;lib/mysql-connector-j-9.6.0.jar" src/\*.java



5\. Run using:

java -cp ".;lib/mysql-connector-j-9.6.0.jar" src/Main



\## GitHub Repository



\[**Toyota Dealership Management System**]

(https://github.com/Abdul-Qadeer-CS/Toyota-Dealership-Management-System)



\## Demo Video Link



\---------------------------------------------------------























