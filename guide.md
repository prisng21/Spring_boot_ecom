# Ecommerce Store — Project Guide

## Overview

**Ecommerce Store** is an online shopping web application built with **Java Spring Boot MVC** and **Thymeleaf**. It provides a clean, structured e-commerce platform with basic storefront browsing, user accounts, admin management, shopping cart, and password reset via email.

The frontend uses **HTML, CSS, JavaScript, jQuery,** and **jQuery DataTables**. The backend follows Spring Boot **Service / Repository / Controller** layering, uses **Spring Data JPA** with **MySQL**, and secures paths with **Spring Security** role-based authorization.

---

## Key features

- Spring Boot MVC architecture (Controller → Service → Repository → Entity)
- Server-side rendering with Thymeleaf + HTML5/CSS3
- Interactive UI with JavaScript, jQuery, and jQuery DataTables
- User registration, login, logout, and protected user pages
- Admin module for managing categories and products
- Product listing with category filtering and discount support
- Shopping cart functionality
- Password reset via email (JavaMail)
- Packaged as a **WAR** for deployment on servlet containers such as Apache Tomcat

---

## Technology stack

### Frontend
- Thymeleaf
- HTML5 / CSS3
- JavaScript / jQuery
- jQuery DataTables

### Backend
- Java 17 (project is set to Java 17; it can be adjusted for Java 8 if needed)
- Spring Boot 3.3.2
- Spring MVC
- Spring Data JPA
- Spring Security
- Lombok
- MySQL Connector
- Spring Boot Starter Mail
- Commons FileUpload
- Maven
- Spring Boot DevTools

### Deployment
- WAR packaging
- Servlet container (e.g., Apache Tomcat)

---

## Project structure

- `src/main/java/com/mdtalalwasim/ecommerce/`
  - `config/` — Spring Security and auth handlers
  - `controller/` — MVC controllers and REST endpoints
  - `entity/` — JPA entities
  - `repository/` — Spring Data JPA repositories
  - `service/` and `service/impl/` — business logic
  - `utils/` — application constants and helpers
- `src/main/resources/`
  - `templates/` — Thymeleaf templates
  - `static/` — CSS, JS, and images
- `src/test/` — basic Spring Boot application tests

---

## Requirements

- Java 17
- MySQL
- Maven
- (Optional) Apache Tomcat for WAR deployment

---

## Required keys and configuration

This project expects database and email settings to be configured in `src/main/resources/application.properties`.

Since the `.gitignore` marks `application.properties` as ignored, you should create your own local copy rather than committing secrets.

### Minimal required properties

You will likely need to set values similar to these:

```properties
# MySQL database connection
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_store
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# Optional: initialize schema on start if desired
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Mail settings for password reset
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your_email_username
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# App / frontend behavior
spring.thymeleaf.cache=false
```

Notes:
- The database name should match the one you create in MySQL.
- Email settings are required if you want the password reset flow to work.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/mdtalalwasim/Ecommerce_Store.git
   ```
2. Enter the project directory:
   ```bash
   cd Ecommerce_Store
   ```
3. Create the MySQL database:
   ```bash
   CREATE DATABASE ecommerce_store;
   ```
4. Configure `src/main/resources/application.properties` with your database and mail settings.
5. Build and run the application:
   - With Maven:
     ```bash
     mvn spring-boot:run
     ```
   - Or build a WAR and deploy it to a servlet container.
6. Open the app at:
   ```
   http://localhost:8080
   ```

---

## Security and access rules

- `/user/**` — requires `USER` role
- `/admin/**` — requires `ADMIN` role
- Everything else under `/**` — publicly accessible
- Login page: `/signin`
- Login processing URL: `/login`

---

## Development notes

- This project uses BCrypt password encoding.
- User details are loaded through a custom `UserDetailsService`.
- The README notes that the project is not fully completed and additional modules are still being developed.

---

## Deployment

- Build a WAR file with Maven.
- Deploy it to a servlet container such as Apache Tomcat.
- Ensure MySQL is accessible from the deployment environment.
- Keep `application.properties` in sync with the target environment.

---

## Notes for running locally

- Make sure MySQL is running.
- Confirm the database exists before starting the app.
- If you use email features, verify SMTP access and any app-specific password requirements from your email provider.
- For development, disabling Thymeleaf template caching can make iteration faster.
