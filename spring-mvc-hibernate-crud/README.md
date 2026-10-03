# Spring MVC and Hibernate CRUD Example

Source code for the article [Spring MVC and Hibernate CRUD Example](https://howtodoinjava.com/spring-mvc/spring-mvc-hibernate-crud-example/).

A contacts address book: list, create, edit and delete contacts with Thymeleaf pages, Bean Validation and Hibernate.

## Versions

- Java 25
- Spring Boot 4.1.1 (Spring Framework 7.0.9)
- Hibernate ORM 7.4.5.Final (managed by Spring Boot, Jakarta Persistence 3.2)
- Hibernate Validator 9.1.3.Final
- Thymeleaf 3.1.5
- H2 2.4.240 (in-memory database)
- JUnit 6.0.3
- Maven 3.9 or newer

## Run

```bash
mvn spring-boot:run   # then open http://localhost:8080/contacts
mvn test              # 27 tests: every page and action, the DAO and the Spring Data repository
```

The console prints every SQL statement Hibernate runs (`spring.jpa.show-sql=true`).

## Files

| File | What it shows |
|---|---|
| Contact.java | JPA entity with Bean Validation constraints |
| ContactDao.java | Plain JPA/Hibernate DAO built on `EntityManager` |
| ContactRepository.java | Spring Data JPA alternative to the DAO |
| ContactService.java | `@Service` with `@Transactional` boundaries |
| ContactController.java | `@Controller` with GET/POST mappings, validation and Post/Redirect/Get |
| ContactNotFoundException.java | Mapped to HTTP 404 with `@ResponseStatus` |
| DuplicateEmailException.java | Shown as a field error on the form |
| templates/contacts/list.html | Contact list with Edit and Delete buttons |
| templates/contacts/form.html | Shared create and edit form with error messages |
| application.properties | H2 datasource, SQL logging, open-in-view off |
| data.sql | Two seed contacts |
| ContactsApplication.java | `@SpringBootApplication` main class |
| HomeController.java | Redirects `/` to `/contacts` |
| ContactControllerTest.java | `@SpringBootTest` + MockMvc tests for every page and action |
| ContactDaoTest.java | `@DataJpaTest` tests for the DAO and the service transactions |
| ContactRepositoryTest.java | `@DataJpaTest` tests for the Spring Data repository |
| NoBindingResultController.java | Test-only handler without `BindingResult`, to show the 400 response |
