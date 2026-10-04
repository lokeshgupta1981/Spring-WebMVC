# Related Tutorials

- [Spring MVC Hello World Example](https://howtodoinjava.com/spring-mvc/spring-mvc-hello-world-example/)

Source code for the article [Spring MVC Hello World Example](https://howtodoinjava.com/spring-mvc/spring-mvc-hello-world-example/).

Versions: Spring Boot 4.1.1 (Spring Framework 7), Java 25, Thymeleaf 3.1.

```bash
mvn spring-boot:run          # http://localhost:8080/hello
curl "http://localhost:8080/hello?name=Lokesh"
curl http://localhost:8080/api/hello
mvn test                     # MockMvc and HTTP tests
```
