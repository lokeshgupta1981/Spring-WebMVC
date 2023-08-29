package com.howtodoinjava;

import com.howtodoinjava.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@SpringBootTest
public class AppTest {

  @Autowired
  WebClient webClient;

  @Test
  public void testPost_WithOnlyStatus() {

    WebClient webClient = WebClient.create("https://api.example.com");

    Item item = new Item("test");

    webClient.post()
        .uri("/create")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(item)
        .retrieve()
        .toBodilessEntity()
        .subscribe(
            responseEntity -> {
              //Handle Success
              System.out.println("Status: " + responseEntity.getStatusCode().value());
              System.out.println("Location URI: " + responseEntity.getHeaders().getLocation());
            },
            throwable -> {
              //Handle Error
              System.err.println("Fatal Error: " + throwable.getMessage());
            }
        );
  }
}