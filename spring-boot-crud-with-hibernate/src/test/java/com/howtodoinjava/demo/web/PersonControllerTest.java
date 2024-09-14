package com.howtodoinjava.demo.web;

import com.howtodoinjava.demo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PersonService personService;

  @Test
  void testAddPersonWithInvalidData() throws Exception {
    String invalidPersonJson = """
        {
            "firstName": "",
            "lastName": "D",
            "email": "invalidemail"
        }
        """;

    mockMvc.perform(post("/api/persons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidPersonJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Validation Errors"))
        .andExpect(jsonPath("$.fieldErrors.firstName", containsInAnyOrder(
            "First name is required",
            "First name should be between 3 and 20 characters"
        )))
        .andExpect(jsonPath("$.fieldErrors.lastName[0]").value("Last name should be between 3 and 20 characters"))
        .andExpect(jsonPath("$.fieldErrors.email[0]").value("Email is invalid"));
  }
}
