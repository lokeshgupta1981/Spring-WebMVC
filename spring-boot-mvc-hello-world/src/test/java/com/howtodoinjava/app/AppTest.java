package com.howtodoinjava.app;

import com.howtodoinjava.app.config.CompressingClientHttpRequestInterceptor;
import com.howtodoinjava.app.model.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @Autowired
  TestRestTemplate testRestTemplate;

  @Autowired
  WebClient webClient;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  WebTestClient webTestClient;

  @LocalServerPort
  int randomServerPort;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();
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

  @Test
  void testGzipRequestWithTestRestTemplate() throws Exception {

    final String baseUrl = "http://localhost:" + randomServerPort + "/v1/items";

    Item item = new Item();
    item.setName("SampleItem");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Item> requestEntity = new HttpEntity<>(item, headers);

    testRestTemplate.getRestTemplate().getInterceptors().addFirst(new CompressingClientHttpRequestInterceptor());
    testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

    ResponseEntity<Item> response = testRestTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, Item.class);
    System.out.println("Response: " + response.getBody());
  }

  @Test
  void testGzipRequestWithRestTemplate() throws Exception {

    final String baseUrl = "http://localhost:" + randomServerPort + "/v1/items";

    Item item = new Item();
    item.setName("SampleItem");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Item> requestEntity = new HttpEntity<>(item, headers);

    ResponseEntity<Item> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, Item.class);
    System.out.println("Response: " + response.getBody());
  }

  @Test
  void testGzipRequestWithWebTestClient() throws Exception {

    final String baseUrl = "http://localhost:"+randomServerPort+"/v1/items";

    Item item = new Item();
    item.setName("SampleItem");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Item> requestEntity = new HttpEntity<>(item, headers);

    EntityExchangeResult<Item> result = webTestClient.post()
        .uri(baseUrl)
        .bodyValue(item)
        .exchange()
        .expectBody(Item.class)
        .returnResult();

    System.out.println("Response: " + result.getResponseBody());
  }

  @Test
  void testGzipRequestWithWebClient() throws Exception {

    final String baseUrl = "http://localhost:"+randomServerPort+"/v1/items";

    Item item = new Item();
    item.setName("SampleItem");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Item> requestEntity = new HttpEntity<>(item, headers);

    Mono<Item> itemMono = webClient.post()
        .uri(baseUrl)
        .bodyValue(item)
        .retrieve()
        .bodyToMono(Item.class);

    itemMono.subscribe(System.out::println);
  }

  public static String encodeFileToBase64(File file) throws IOException {
    byte[] fileContent = Files.readAllBytes(file.toPath());
    return Base64.getEncoder().encodeToString(fileContent);
  }
}
