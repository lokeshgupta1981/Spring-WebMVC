package com.howtodoinjava.app;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class AppTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(webApplicationContext)
      .build();
  }

  @Test
  void contextLoads() {
  }

  @Test
  void testMessagePage() throws Exception {

    MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.get("/message"))
      .andExpect(MockMvcResultMatchers.view().name("messageView"))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();

    Assertions.assertThat(result.getResponse().getContentAsString())
      .contains("Message from server is: Hello, World!");
  }
}
