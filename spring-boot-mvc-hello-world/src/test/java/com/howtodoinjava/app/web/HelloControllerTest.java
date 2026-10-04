package com.howtodoinjava.app.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests the MVC controller with MockMvc: view name, model attribute and rendered HTML.
 */
@WebMvcTest(HelloController.class)
class HelloControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void helloRendersTheHelloView() throws Exception {
    mockMvc.perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(view().name("hello"))
        .andExpect(model().attribute("message", "Hello, World!"))
        .andExpect(content().string(containsString("<h1>Hello, World!</h1>")));
  }

  @Test
  void requestParamChangesTheName() throws Exception {
    mockMvc.perform(get("/hello").param("name", "Lokesh"))
        .andExpect(model().attribute("message", "Hello, Lokesh!"));
  }

  @Test
  void pathVariableChangesTheName() throws Exception {
    mockMvc.perform(get("/hello/Lokesh"))
        .andExpect(view().name("hello"))
        .andExpect(model().attribute("message", "Hello, Lokesh!"));
  }
}
