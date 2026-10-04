package com.howtodoinjava.app.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests the REST controller: JSON body, no view.
 */
@WebMvcTest(HelloRestController.class)
class HelloRestControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void returnsJson() throws Exception {
    mockMvc.perform(get("/api/hello").param("name", "Lokesh"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(content().json("{\"message\":\"Hello, Lokesh!\"}"));
  }
}
