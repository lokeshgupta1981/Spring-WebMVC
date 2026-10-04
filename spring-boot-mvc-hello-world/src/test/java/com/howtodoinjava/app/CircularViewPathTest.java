package com.howtodoinjava.app;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Without Thymeleaf, the view name "hello" goes to the default InternalResourceViewResolver,
 * which forwards to /hello, the same URL: Spring MVC stops with "Circular view path".
 */
@SpringBootTest(properties =
    "spring.autoconfigure.exclude=org.springframework.boot.thymeleaf.autoconfigure.ThymeleafAutoConfiguration")
@AutoConfigureMockMvc
class CircularViewPathTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void viewNameEqualToUrlWithoutTemplateEngine() {
    assertThatThrownBy(() -> mockMvc.perform(get("/hello")))
        .isInstanceOf(ServletException.class)
        .hasMessageContaining("Circular view path [hello]: would dispatch back to the current handler URL [/hello] again");
  }
}
